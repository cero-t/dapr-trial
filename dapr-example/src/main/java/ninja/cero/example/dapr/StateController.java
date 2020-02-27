package ninja.cero.example.dapr;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/state/{storeName}/{key}")
public class StateController {
    @Value("http://localhost:${dapr.http.port:3500}/v1.0")
    private String baseUrl;

    private RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    public void store(@PathVariable String storeName, @PathVariable String key, @RequestBody Map data) {
        var map = new HashMap<>();
        map.put("key", key);
        map.put("value", data);

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-correlation-id", MDC.get("x-correlation-id"));
        var entity = new HttpEntity<>(new Map[]{map}, headers);

        restTemplate.exchange(baseUrl + "/state/" + storeName, HttpMethod.POST, entity, Void.class);
    }

    @GetMapping
    public Map read(@PathVariable String storeName, @PathVariable String key) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-correlation-id", MDC.get("x-correlation-id"));
        var entity = new HttpEntity<>(headers);

        return restTemplate.exchange(baseUrl + "/state/" + storeName + "/" + key, HttpMethod.GET, entity, Map.class)
                .getBody();
    }
}
