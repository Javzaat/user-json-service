package com.example.userjson.soap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SoapAuthClient {

    @Value("${soap.service.url}")
    private String soapUrl;

    public boolean validateToken(String token) {
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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);

        HttpEntity<String> request = new HttpEntity<>(xmlRequest, headers);

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(soapUrl, request, String.class);

        return response != null && response.contains("<ns2:valid>true</ns2:valid>");
    }
}