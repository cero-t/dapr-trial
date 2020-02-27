package ninja.cero.example.dapr;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/remote")
public class FrontController {
    @Value("http://localhost:${dapr.http.port:3500}/v1.0")
    private String baseUrl;

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public Map writeAndRead() {
        var res = restTemplate.postForEntity(baseUrl + "/invoke/hello-dapr/method/state/statestore/r1",
                Map.of("message", "Hello from front"), Map.class);

        if (res.getStatusCodeValue() / 100 != 2) {
            throw new RuntimeException(res.getStatusCodeValue() + " " + res.getStatusCode().getReasonPhrase());
        }

        return restTemplate.getForObject(baseUrl + "/invoke/hello-dapr/method/state/statestore/r1",
                Map.class);
    }

    @GetMapping("/trace")
    public Map withTrace() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-correlation-id", MDC.get("x-correlation-id"));
        var entity = new HttpEntity<>(Map.of("message", "Hello from front"), headers);

        var res = restTemplate.exchange(baseUrl + "/invoke/hello-dapr/method/state/statestore/r2",
                HttpMethod.POST, entity, Map.class);

        if (res.getStatusCodeValue() / 100 != 2) {
            throw new RuntimeException(res.getStatusCodeValue()
                    + " " + res.getStatusCode().getReasonPhrase());
        }

        return restTemplate.exchange(baseUrl + "/invoke/hello-dapr/method/state/statestore/r2",
                HttpMethod.GET, new HttpEntity<>(headers), Map.class).getBody();
    }
}
