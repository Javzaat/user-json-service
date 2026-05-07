package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/*
 * UserJsonServiceApplication нь User JSON Service-ийн main class юм.
 *
 * Spring Boot application яг энэ class-аас эхэлж ажиллана.
 *
 * Энэ service-ийн үндсэн үүрэг:
 * - User profile CRUD хийх
 * - PostgreSQL database дээр user_profile table ашиглах
 * - /users endpoint-үүдийг REST API хэлбэрээр гаргах
 * - AuthMiddleware ашиглаж SOAP service-ээр token validate хийлгэх
 *
 * Lab 06 дээр энэ service нь JSON-based REST Service-ийн үүргийг гүйцэтгэж байгаа.
 * Lab 08 дээр API Gateway-ийн ард private service хэлбэрээр ажиллаж байна.
 */
@SpringBootApplication(scanBasePackages = {"com.example.demo", "com.example.userjson"})

/*
 * EnableJpaRepositories нь Spring Data JPA repository interface-үүдийг хаанаас хайхыг зааж өгнө.
 *
 * Манай UserProfileRepository нь:
 * com.example.userjson.repository
 *
 * package дотор байрлаж байгаа.
 *
 * Энэ annotation байхгүй бол Spring Boot repository-г bean болгож үүсгэхгүй байж болно.
 */
@EnableJpaRepositories(basePackages = "com.example.userjson.repository")

/*
 * EntityScan нь JPA entity class-уудыг хаанаас хайхыг зааж өгнө.
 *
 * Манай UserProfile entity нь:
 * com.example.userjson.model
 *
 * package дотор байрлаж байгаа.
 *
 * Main class com.example.demo package дотор байгаа тул entity package-ийг тодорхой зааж өгсөн.
 */
@EntityScan(basePackages = "com.example.userjson.model")
public class UserJsonServiceApplication {

    /*
     * main method нь Spring Boot application-ийг эхлүүлнэ.
     *
     * Энэ method ажиллах үед:
     * - Spring context үүснэ
     * - REST controller-ууд бүртгэгдэнэ
     * - AuthMiddleware filter бүртгэгдэнэ
     * - JPA entity болон repository scan хийгдэнэ
     * - PostgreSQL database connection үүснэ
     * - Tomcat server 8080 port дээр асна
     */
    public static void main(String[] args) {
        SpringApplication.run(UserJsonServiceApplication.class, args);
    }
}