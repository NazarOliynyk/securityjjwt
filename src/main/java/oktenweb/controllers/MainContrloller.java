package oktenweb.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainContrloller {

    @GetMapping("/admin/data")
    public String adminData(){
        return "Hello from adminData!";
    }
}
