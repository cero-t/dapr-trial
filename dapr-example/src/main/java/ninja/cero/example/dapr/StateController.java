package ninja.cero.example.dapr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/state/{storeName}/{key}")
public class StateController {
    @Value("http://localhost:${dapr.http.port}/v1.0")
    private String baseUrl;

    private RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    public Map store(@PathVariable String storeName, @PathVariable String key, @RequestBody Map data) {
        var map = new HashMap<>();
        map.put("key", key);
        map.put("value", data);

        var payload = new Map[]{map};
        return restTemplate.postForObject(baseUrl + "/state/" + storeName, payload, Map.class);
    }

    @GetMapping
    public Map read(@PathVariable String storeName, @PathVariable String key) {
        return restTemplate.getForObject(baseUrl + "/state/" + storeName + "/" + key,
                Map.class);
    }
}
