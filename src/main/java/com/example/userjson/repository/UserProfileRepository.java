package com.example.userjson.repository;

import com.example.userjson.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * UserProfileRepository нь user_profile table-тэй харилцах Repository interface юм.
 *
 * Spring Data JPA ашиглаж байгаа тул CRUD query-үүдийг гараар бичих шаардлагагүй.
 * JpaRepository-г extend хийснээр дараах basic operation-ууд автоматаар бэлэн болно:
 *
 * - save()       -> profile үүсгэх эсвэл update хийх
 * - findById()   -> id-аар profile хайх
 * - findAll()    -> бүх profile авах
 * - deleteById() -> id-аар profile устгах
 *
 * Энэ repository-г UserProfileService ашиглаж database operation хийдэг.
 */
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}