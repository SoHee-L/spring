package hello.typeconverter.controller;

import hello.typeconverter.type.IpPort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request){
        String data = request.getParameter("data"); // data 를 parameter 로 가지고 오면 문자 타입 조회
        Integer intValue = Integer.valueOf(data); //숫자 타입으로 변경.
        System.out.println("intValue = " + intValue); // 숫자 값이 출력 됨.
        return "ok";

    }

    @GetMapping("/hello-v2") //"10,000" MyNumberFormatter 가 숫자에 들어가는 , 를 다 포함해서 처리해줌.
    public String helloV2(@RequestParam Integer data){
        System.out.println("data = "+data);
        return "ok";
    }

    @GetMapping("/ip-port")
    public String ipPort(@RequestParam IpPort ipPort){
        System.out.println("ipPort = "+ ipPort.getIp());
        System.out.println("ipPort PORT = "+ ipPort.getPort());
        return "ok";
    }

}
