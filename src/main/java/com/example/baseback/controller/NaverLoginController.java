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
                return "Token retrieval failed!"; // 에러 처리
            }

            String accessToken = tokenResponse.get("access_token");

            // 네이버 사용자 정보 요청
            String profileUrl = "https://openapi.naver.com/v1/nid/me";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(profileUrl, HttpMethod.GET, entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {});

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                Map<String, Object> userProfile = (Map<String, Object>) responseBody.get("response");

                // 사용자 정보를 가지고 필요한 작업 수행 (예: 사용자 등록, 로그인 처리 등)
                System.out.println("User Profile: " + userProfile);
                
                return "로그인 성공"; // 적절한 페이지 또는 데이터를 반환
            } else {
                return "Profile retrieval failed!"; // 프로필 가져오기 실패
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage(); // 예외 처리
        }
    }

    // State 파라미터 생성 (CSRF 보호를 위해)
    private String generateState() {
        return UUID.randomUUID().toString();
    }
}
