package cn.cmos.postman;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWord {

    @RequestMapping("/say")
    public String sayHello(){
        System.out.println(System.currentTimeMillis());
        return "hello, wordl";
    }
}
