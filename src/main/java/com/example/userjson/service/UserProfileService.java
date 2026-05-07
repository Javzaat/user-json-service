package com.example.userjson.service;

import com.example.userjson.model.UserProfile;
import com.example.userjson.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
 * UserProfileService нь User JSON Service-ийн business logic layer юм.
 *
 * Controller нь HTTP request/response хариуцдаг бол
 * Service layer нь profile-тэй холбоотой үндсэн logic-ийг хариуцна.
 *
 * Энэ class нь UserProfileRepository ашиглаж PostgreSQL database дээр:
 * - profile үүсгэх
 * - profile унших
 * - бүх profile авах
 * - profile шинэчлэх
 * - profile устгах
 *
 * үйлдлүүдийг хийдэг.
 */
@Service
public class UserProfileService {

    /*
     * UserProfileRepository нь user_profile table-тэй харилцана.
     *
     * Service class repository-г ашиглаж database operation хийдэг.
     * Ингэснээр controller database-тэй шууд харилцахгүй, layer separation хадгалагдана.
     */
    private final UserProfileRepository userProfileRepository;

    /*
     * Constructor injection.
     *
     * Spring Boot UserProfileRepository bean-ийг автоматаар inject хийж өгнө.
     */
    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    /*
     * Шинэ user profile үүсгэх method.
     *
     * Controller-оос ирсэн UserProfile object-ийг database-д хадгална.
     * save() method нь JPA repository-ийн built-in method.
     *
     * Хадгалагдсаны дараа id зэрэг database-аас үүссэн утгуудтай object буцаана.
     */
    public UserProfile createUser(UserProfile user) {
        return userProfileRepository.save(user);
    }

    /*
     * id-аар нэг user profile авах method.
     *
     * findById() нь Optional<UserProfile> буцаадаг.
     * Хэрэв тухайн id-тай profile байвал object буцаана.
     * Байхгүй бол null буцааж байна.
     */
    public UserProfile getUser(Long id) {
        return userProfileRepository.findById(id).orElse(null);
    }

    /*
     * Бүх user profile-ийг авах method.
     *
     * findAll() нь user_profile table дотор байгаа бүх record-ийг list хэлбэрээр буцаана.
     * Энэ method GET /users endpoint дээр ашиглагдана.
     */
    public List<UserProfile> getAllUsers() {
        return userProfileRepository.findAll();
    }

    /*
     * Profile update хийх method.
     *
     * Эхлээд id-аар database-аас хуучин profile байгаа эсэхийг шалгана.
     * Байвал хуучин object-ийн field-үүдийг шинэ request body-оос ирсэн утгаар сольж,
     * save() хийж database-д update хийнэ.
     */
    public UserProfile updateUser(Long id, UserProfile updatedUser) {

        /*
         * Тухайн id-тай profile database дээр байгаа эсэхийг хайж байна.
         */
        Optional<UserProfile> existingOpt = userProfileRepository.findById(id);

        /*
         * Profile олдсон тохиолдолд field-үүдийг update хийнэ.
         */
        if (existingOpt.isPresent()) {
            UserProfile existingUser = existingOpt.get();

            /*
             * Frontend-ээс ирсэн шинэ утгуудаар profile мэдээллийг сольж байна.
             */
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setBio(updatedUser.getBio());
            existingUser.setPhone(updatedUser.getPhone());

            /*
             * imageUrl нь DigitalOcean Spaces рүү upload хийсний дараа буцаж ирсэн
             * public image URL байж болно.
             *
             * Энэ URL-г user_profile table-ийн image_url талбарт хадгалдаг.
             */
            existingUser.setImageUrl(updatedUser.getImageUrl());

            /*
             * Өөрчлөгдсөн object-ийг database-д хадгалж update хийж байна.
             */
            return userProfileRepository.save(existingUser);
        }

        /*
         * Хэрэв тухайн id-тай profile олдоогүй бол null буцаана.
         */
        return null;
    }

    /*
     * Profile delete хийх method.
     *
     * id-аар user_profile table-ээс record устгана.
     */
    public void deleteUser(Long id) {
        userProfileRepository.deleteById(id);
    }
}