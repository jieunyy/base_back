package com.example.baseback.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class NaverLoginController {

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Value("${naver.redirect-uri}")
    private String redirectUri;

    // Android 앱으로 리디렉션할 URI
    private static final String ANDROID_APP_URI = "myapp://naverlogin/success";

    @GetMapping("/auth/naver/login")
    public RedirectView naverLogin() {
        // 네이버 로그인 페이지로 리다이렉트
        String naverAuthUrl = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=" 
                                + clientId + "&redirect_uri=" + redirectUri + "&state=" + generateState();

        return new RedirectView(naverAuthUrl);
    }

    @GetMapping("/auth/naver/callback")
    public String naverCallback(@RequestParam("code") String code, @RequestParam("state") String state) {
        try {
            // 액세스 토큰 요청
            String tokenUrl = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id="
                                + clientId + "&client_secret=" + clientSecret + "&code=" + code + "&state=" + state;

            RestTemplate restTemplate = new RestTemplate();
            Map<String, String> tokenResponse = restTemplate.getForObject(tokenUrl, HashMap.class);

            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                // 에러 처리
                return "redirect:/error?message=Token retrieval failed!";
            }
            
            String refreshToken = tokenResponse.get("refresh_token");

            if (refreshToken != null) {
                // Android 앱으로 리디렉션하기 위해 필요한 데이터를 URL로 전달
                return "redirect:" + ANDROID_APP_URI + "&refresh_token=" + refreshToken;
            } else {
                // 리프레시 토큰이 없는 경우 에러 처리
                return "redirect:/error?message=Refresh token not found!";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error?message=" + e.getMessage(); // 예외 처리
        }
    }


    // State 파라미터 생성 (CSRF 보호를 위해)
    private String generateState() {
        return UUID.randomUUID().toString();
    }
}
