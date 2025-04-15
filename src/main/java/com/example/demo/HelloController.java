package com.example.demo; // 실제 프로젝트의 패키지 이름 사용

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // RESTful 웹 서비스 컨트롤러임을 명시
public class HelloController {

    @GetMapping("/") // HTTP GET 요청을 루트 경로("/")에 매핑
    public String home() {
        // 루트 경로로 요청이 오면 이 문자열을 반환
        return "Hello from Spring Boot! (Accessed via /java/ path)";
    }

    // 다른 경로 테스트용 (예: /hello)
    @GetMapping("/hello")
    public String hello() {
        return "Hello endpoint response!";
    }
}