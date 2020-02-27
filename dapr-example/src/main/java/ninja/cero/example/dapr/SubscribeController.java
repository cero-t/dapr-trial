package ninja.cero.example.dapr;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SubscribeController {
    @GetMapping("/dapr/subscribe")
    public String[] subscribe() {
        System.out.println("subscribe is called");
        return new String[]{"A", "B"};
    }

    @PostMapping("/A")
    public void whenA(@RequestBody Map data) {
        System.out.println("A is called");
        System.out.println(data);
    }

    @PostMapping("/B")
    public void whenB(@RequestBody Map data) {
        System.out.println("B is called");
        System.out.println(data);
    }
}
