package com.example.userjson.soap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/*
 * SoapAuthClient нь User JSON Service-ээс User SOAP Service рүү request илгээх client class юм.
 *
 * JSON service өөрөө token-ийн valid эсэхийг шийдэхгүй.
 * Харин энэ class-аар дамжуулж SOAP service-ийн ValidateToken operation-ийг дуудна.
 *
 * Энэ нь Lab 06 дээр шаардсан service-to-service communication болон
 * authentication delegation-ийн гол хэрэгжүүлэлт юм.
 */
@Component
public class SoapAuthClient {

    /*
     * SOAP service-ийн URL.
     *
     * Энэ утга application.properties дээрх soap.service.url property-оос уншигдана.
     * Cloud орчинд энэ нь API Gateway эсвэл SOAP service-ийн private/public URL байж болно.
     *
     * Жишээ:
     * soap.service.url=https://206.189.153.86.nip.io/api/soap
     * эсвэл VPC private network ашиглаж байгаа бол:
     * soap.service.url=http://10.104.0.6:8081/ws
     */
    @Value("${soap.service.url}")
    private String soapUrl;

    /*
     * validateToken method нь token зөв эсэхийг SOAP service-ээр шалгуулна.
     *
     * AuthMiddleware дээрээс энэ method дуудагдана.
     * Хэрэв SOAP service valid=true гэж буцаавал true буцаана.
     * Хэрэв token invalid бол false буцаана.
     */
    public boolean validateToken(String token) {

        /*
         * SOAP request нь XML envelope хэлбэртэй байх ёстой.
         *
         * Энд ValidateTokenRequest SOAP body үүсгэж байна.
         * token утгыг <auth:token> element дотор хийж SOAP service рүү илгээнэ.
         */
        String xmlRequest =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" "
                        + "xmlns:auth=\"http://example.com/authsoap\">"
                        + "<soapenv:Header/>"
                        + "<soapenv:Body>"
                        + "<auth:ValidateTokenRequest>"
                        + "<auth:token>" + token + "</auth:token>"
                        + "</auth:ValidateTokenRequest>"
                        + "</soapenv:Body>"
                        + "</soapenv:Envelope>";

        /*
         * SOAP request-ийн header үүсгэж байна.
         *
         * SOAP XML request тул Content-Type нь text/xml байна.
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);

        /*
         * XML body болон header-ийг нэг HttpEntity object болгож байна.
         * RestTemplate энэ object-ийг HTTP request body болгон илгээнэ.
         */
        HttpEntity<String> request = new HttpEntity<>(xmlRequest, headers);

        /*
         * RestTemplate нь Spring-ийн HTTP client.
         *
         * Энэ client-аар SOAP service рүү POST request илгээж байна.
         */
        RestTemplate restTemplate = new RestTemplate();

        /*
         * SOAP service-ийн ValidateToken operation руу XML request илгээж,
         * XML response-ийг String хэлбэрээр авна.
         */
        String response = restTemplate.postForObject(soapUrl, request, String.class);

        /*
         * SOAP response дотор <ns2:valid>true</ns2:valid> байвал token valid гэж үзэж байна.
         *
         * Энэ нь энгийн string contains шалгалт.
         * Lab-ийн хувьд ойлгомжтой, ажиллахад хангалттай.
         * Илүү production-like хувилбарт XML parser ашиглаж valid element-ийг parse хийж болно.
         */
        return response != null && response.contains("<ns2:valid>true</ns2:valid>");
    }
}