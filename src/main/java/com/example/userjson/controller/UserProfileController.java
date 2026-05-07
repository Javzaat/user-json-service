package com.example.userjson.controller;

import com.example.userjson.model.UserProfile;
import com.example.userjson.service.UserProfileService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * UserProfileController нь User JSON Service-ийн REST API controller юм.
 *
 * Энэ controller нь user profile-ийн CRUD operation-уудыг хариуцна:
 * - Create profile
 * - Read profile
 * - Update profile
 * - Delete profile
 * - Get all profiles
 *
 * Authentication шалгалтыг энэ controller дотор шууд хийхгүй.
 * /users endpoint рүү request ирэхээс өмнө AuthMiddleware ажиллаж,
 * Authorization token-ийг SOAP service-ээр validate хийдэг.
 *
 * Ийм байдлаар controller зөвхөн profile management logic дээр төвлөрч,
 * authentication logic тусдаа middleware болон SOAP service рүү салсан.
 */
@RestController
@RequestMapping("/users")
public class UserProfileController {

    /*
     * UserProfileService нь business logic болон database operation-ийг хариуцна.
     *
     * Controller шууд repository ашиглахгүй, service layer-ээр дамжуулж ажиллаж байна.
     * Ингэснээр controller нь request/response handle хийх үүрэгтэй,
     * service нь profile logic хийх үүрэгтэй болж салж байна.
     */
    private final UserProfileService userProfileService;

    /*
     * Constructor injection.
     *
     * Spring Boot UserProfileService bean-ийг автоматаар inject хийж өгнө.
     */
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    /*
     * POST /users
     *
     * Шинэ user profile үүсгэнэ.
     * Frontend-ээс name, email, bio, phone, imageUrl зэрэг мэдээлэл JSON body-оор ирнэ.
     *
     * Request энэ method дээр ирэхээс өмнө AuthMiddleware token шалгасан байна.
     */
    @PostMapping
    public UserProfile createUser(@RequestBody UserProfile user) {
        return userProfileService.createUser(user);
    }

    /*
     * GET /users/{id}
     *
     * id-аар нэг user profile уншина.
     * Жишээ:
     * GET /users/1
     *
     * @PathVariable нь URL дээрх id утгыг method parameter болгон авдаг.
     */
    @GetMapping("/{id}")
    public UserProfile getUser(@PathVariable Long id) {
        return userProfileService.getUser(id);
    }

    /*
     * PUT /users/{id}
     *
     * Тухайн id-тай user profile-ийн мэдээллийг шинэчилнэ.
     * Frontend-ээс шинэ profile data JSON body-оор ирнэ.
     */
    @PutMapping("/{id}")
    public UserProfile updateUser(@PathVariable Long id, @RequestBody UserProfile user) {
        return userProfileService.updateUser(id, user);
    }

    /*
     * DELETE /users/{id}
     *
     * Тухайн id-тай user profile-ийг database-аас устгана.
     * Устгал амжилттай бол text message буцаана.
     */
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userProfileService.deleteUser(id);
        return "User deleted successfully";
    }

    /*
     * GET /users
     *
     * Database дээр байгаа бүх user profile-ийг list хэлбэрээр буцаана.
     *
     * Энэ endpoint дээр Redis cache Gateway дээр ашиглагдаж байгаа.
     * Эхний GET /api/users request cache miss болж backend рүү ирнэ.
     * Дараагийн ижил request cache hit болж Redis-ээс шууд response буцна.
     */
    @GetMapping
    public List<UserProfile> getAllUsers() {
        return userProfileService.getAllUsers();
    }
}