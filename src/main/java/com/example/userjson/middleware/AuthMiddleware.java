package com.example.userjson.middleware;

import com.example.userjson.soap.SoapAuthClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
 * AuthMiddleware нь User JSON Service-ийн authentication middleware юм.
 *
 * JSON service-ийн үндсэн үүрэг нь user profile CRUD хийх.
 * Харин authentication буюу token зөв эсэхийг өөрөө шийдэхгүй,
 * SOAP Authentication Service рүү ValidateToken request явуулж шалгана.
 *
 * Энэ middleware нь /users endpoint рүү ирсэн request бүр дээр:
 * 1. Authorization header байгаа эсэхийг шалгана
 * 2. Bearer token format зөв эсэхийг шалгана
 * 3. Token-ийг SOAP service-ээр validate хийлгэнэ
 * 4. Token valid бол request-ийг controller рүү үргэлжлүүлнэ
 * 5. Token invalid бол 401 Unauthorized буцаана
 */
@Component
public class AuthMiddleware extends OncePerRequestFilter {

    /*
     * SoapAuthClient нь SOAP service-тэй харилцах client class.
     *
     * Энэ middleware token-ийг өөрөө шалгахгүй,
     * soapAuthClient.validateToken(token) method-оор SOAP service рүү дамжуулж шалгуулна.
     */
    private final SoapAuthClient soapAuthClient;

    /*
     * Constructor injection.
     *
     * Spring Boot SoapAuthClient bean-ийг автоматаар inject хийж өгнө.
     */
    public AuthMiddleware(SoapAuthClient soapAuthClient) {
        this.soapAuthClient = soapAuthClient;
    }

    /*
     * doFilterInternal method нь request бүр дээр ажиллана.
     *
     * OncePerRequestFilter ашиглаж байгаа учраас нэг request дээр filter нэг удаа л ажиллана.
     * Энэ нь authentication logic давхар ажиллахаас сэргийлнэ.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        /*
         * CORS header-үүд.
         *
         * Frontend өөр domain дээр байрлаж байгаа тул browser cross-origin request илгээх үед
         * backend эдгээр header-ийг буцаах шаардлагатай.
         */
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");

        /*
         * Browser зарим request-ийн өмнө OPTIONS preflight request илгээдэг.
         *
         * OPTIONS request нь actual CRUD request биш, зөвхөн CORS зөвшөөрөл шалгах request.
         * Тиймээс token validate хийхгүйгээр OK response буцааж байна.
         */
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        /*
         * Request-ийн URI path-ийг авч байна.
         * Жишээ:
         * /users
         * /users/1
         */
        String path = request.getRequestURI();

        /*
         * Зөвхөн /users endpoint-уудыг хамгаалж байна.
         *
         * Profile create/read/update/delete бүгд /users path-аар явдаг тул
         * энэ хэсэгт authentication шалгалт хийгдэнэ.
         */
        if (path.startsWith("/users")) {

            /*
             * Frontend эсвэл API Gateway-ээс ирсэн Authorization header-ийг уншиж байна.
             *
             * Header format:
             * Authorization: Bearer <token>
             */
            String authHeader = request.getHeader("Authorization");

            /*
             * Authorization header байхгүй эсвэл "Bearer " гэж эхлэхгүй бол
             * request-ийг зөвшөөрөхгүй.
             */
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("Missing or invalid Authorization header");
                return;
            }

            /*
             * "Bearer " гэдэг эхний 7 тэмдэгтийг авч хасаад зөвхөн token хэсгийг салгаж байна.
             */
            String token = authHeader.substring(7);

            /*
             * Token-ийг SOAP Authentication Service рүү явуулж validate хийлгэнэ.
             *
             * Хэрэв SOAP service true буцаавал valid token гэж үзнэ.
             * false буцаавал invalid token гэж үзнэ.
             */
            boolean valid = soapAuthClient.validateToken(token);

            /*
             * Token хүчингүй бол controller рүү request дамжуулахгүй.
             * Шууд 401 Unauthorized response буцаана.
             */
            if (!valid) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("Invalid token");
                return;
            }
        }

        /*
         * Хэрэв token valid эсвэл хамгаалах шаардлагагүй path бол
         * request-ийг дараагийн filter/controller рүү үргэлжлүүлнэ.
         */
        filterChain.doFilter(request, response);
    }
}