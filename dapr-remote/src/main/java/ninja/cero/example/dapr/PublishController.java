package ninja.cero.example.dapr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class PublishController {
    @Value("http://localhost:${dapr.http.port}/v1.0")
    private String baseUrl;

    private RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/pub/{topic}")
    public Map publish(@PathVariable String topic, @RequestBody Map data) {
        return restTemplate.postForObject(baseUrl + "/publish/" + topic, data, Map.class);
    }

    @PostMapping("/pub/{topic}/trace")
    public Map publish(@RequestHeader("x-correlation-id") String corId, @PathVariable String topic, @RequestBody Map data) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-correlation-id", corId);
        var entity = new HttpEntity<>(data, headers);

        return restTemplate.exchange(baseUrl + "/publish/" + topic,
                HttpMethod.POST, entity, Map.class).getBody();
    }
}
