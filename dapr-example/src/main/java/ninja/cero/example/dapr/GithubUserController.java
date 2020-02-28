package ninja.cero.example.dapr;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/github")
public class GithubUserController {
    RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public Map index(@RequestHeader("x-oauth-token") String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        var entity = new HttpEntity<>(headers);

        Map profile = restTemplate.exchange("https://api.github.com/users/cero-t", HttpMethod.GET, entity, Map.class)
                .getBody();
        return profile;
    }
}
