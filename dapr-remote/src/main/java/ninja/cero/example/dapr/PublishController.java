package ninja.cero.example.dapr;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class PublishController {
    @Value("http://localhost:${dapr.http.port:3500}/v1.0")
    private String baseUrl;

    private RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/pub/{topic}")
    public Map publish(@PathVariable String topic, @RequestBody Map data) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-correlation-id", MDC.get("x-correlation-id"));
        var entity = new HttpEntity<>(data, headers);

        return restTemplate.exchange(baseUrl + "/publish/" + topic,
                HttpMethod.POST, entity, Map.class).getBody();
    }
}
