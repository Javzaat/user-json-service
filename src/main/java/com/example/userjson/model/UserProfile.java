package com.example.userjson.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/*
 * UserProfile нь JSON Service-ийн profile data-г илэрхийлэх Entity class юм.
 *
 * Энэ class нь PostgreSQL database дээрх user_profile table-тэй map хийгдэнэ.
 * Өөрөөр хэлбэл UserProfile object бүр database дээр нэг profile row болж хадгалагдана.
 *
 * Энэ entity дээр хэрэглэгчийн profile мэдээлэл хадгалагдана:
 * - name
 * - email
 * - bio
 * - phone
 * - imageUrl
 */
@Data
@Entity
public class UserProfile {

    /*
     * id нь profile бүрийн unique primary key.
     *
     * GenerationType.IDENTITY ашиглаж байгаа тул PostgreSQL id-г автоматаар нэмэгдүүлж үүсгэнэ.
     * Жишээ нь эхний profile id=1, дараагийн profile id=2 гэх мэт.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Хэрэглэгчийн нэр.
     *
     * Frontend profile form дээрээс name талбараар ирнэ.
     */
    private String name;

    /*
     * Хэрэглэгчийн email.
     *
     * Profile мэдээлэлд хадгалагдах холбоо барих мэдээлэл.
     */
    private String email;

    /*
     * Хэрэглэгчийн bio буюу өөрийн тухай богино тайлбар.
     *
     * Facebook profile шиг хэрэглэгч өөрийн тухай мэдээлэл бичих хэсэг.
     */
    private String bio;

    /*
     * Хэрэглэгчийн утасны дугаар.
     */
    private String phone;

    /*
     * Хэрэглэгчийн profile image URL.
     *
     * Зураг File Manager Service-ээр дамжин DigitalOcean Spaces рүү upload хийгдсэний дараа
     * буцаж ирсэн public URL энд хадгалагдана.
     *
     * Database дээр энэ field ихэвчлэн image_url column болж хадгалагдана.
     */
    private String imageUrl;
}