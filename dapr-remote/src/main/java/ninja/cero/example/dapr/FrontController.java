package ninja.cero.example.dapr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/remote")
public class FrontController {
    @Value("http://localhost:${dapr.http.port}/v1.0")
    private String baseUrl;

    private RestTemplate restTemplate = new RestTemplate();


    @GetMapping
    public Map read() {
        var data = Map.of("message", "Hello from remote");
        var res = restTemplate.postForEntity(baseUrl + "/invoke/hello-dapr/method/state/statestore/r1",
                data, Map.class);

        if (res.getStatusCodeValue() / 100 != 2) {
            throw new RuntimeException(res.getStatusCodeValue() + " " + res.getStatusCode().getReasonPhrase());
        }

        return restTemplate.getForObject(baseUrl + "/invoke/hello-dapr/method/state/statestore/r1",
                Map.class);
    }

    @GetMapping("/trace")
    public Map read(@RequestHeader("x-correlation-id") String corId) {
        System.out.println(corId);

        var data = Map.of("message", "Hello from front");

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-correlation-id", corId);
        var entity = new HttpEntity<>(data, headers);

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
