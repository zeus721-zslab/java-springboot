package com.example.demo.controller; // 또는 다른 적절한 컨트롤러 관련 패키지

import com.example.demo.dto.TaskResponse; // TaskResponse DTO 임포트
import com.example.demo.dto.TaskStatusResponse; // TaskStatusResponse DTO 임포트
import com.example.demo.service.TaskService; // 이전 단계에서 만든 Service 임포트
import lombok.RequiredArgsConstructor; // Lombok: final 필드 생성자 자동 생성
import org.slf4j.Logger; // 로깅
import org.slf4j.LoggerFactory; // 로깅
import org.springframework.http.HttpStatus; // HTTP 상태 코드 사용
import org.springframework.http.ResponseEntity; // HTTP 응답 객체 사용
import org.springframework.web.bind.annotation.GetMapping; // GET 요청 매핑
import org.springframework.web.bind.annotation.PathVariable; // 경로 변수 사용
import org.springframework.web.bind.annotation.PostMapping; // POST 요청 매핑
import org.springframework.web.bind.annotation.RequestMapping; // 기본 경로 매핑
import org.springframework.web.bind.annotation.RestController; // REST 컨트롤러 선언

import java.util.Optional;

/**
 * 비동기 작업 생성 및 상태 조회와 관련된 HTTP 요청을 처리하는 REST 컨트롤러입니다.
 */
@RestController // 이 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냅니다. (@Controller + @ResponseBody)
@RequestMapping("/tasks") // 이 컨트롤러 내의 모든 핸들러 메서드는 "/tasks" 라는 기본 경로를 가집니다.
@RequiredArgsConstructor // Lombok: final 필드(taskService)에 대한 생성자를 자동으로 만듭니다. (생성자 주입)
public class TaskController {

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    // 생성자 주입 방식으로 TaskService를 주입받습니다.
    private final TaskService taskService;

    /**
     * 새로운 비동기 작업을 생성(제출)하는 API 엔드포인트입니다.
     * HTTP POST 요청을 "/tasks" 경로로 받습니다.
     *
     * @return 생성된 작업 ID를 포함하는 응답 (HTTP 202 Accepted)
     */
    @PostMapping // HTTP POST 메서드와 매핑됩니다. 경로는 클래스 레벨의 @RequestMapping("/tasks")을 따릅니다.
    public ResponseEntity<TaskResponse> submitNewTask() {
        log.info("Received HTTP POST request on /tasks");
        // 서비스 계층의 메서드를 호출하여 작업 제출 로직 실행
        TaskResponse response = taskService.submitTask();

        // ResponseEntity를 사용하여 HTTP 상태 코드를 명시적으로 지정할 수 있습니다.
        // 202 Accepted는 요청이 성공적으로 접수되었고 처리가 시작되었음을 의미합니다.
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    /**
     * 지정된 작업 ID의 현재 상태를 조회하는 API 엔드포인트입니다.
     * HTTP GET 요청을 "/tasks/{taskId}/status" 경로로 받습니다.
     * {taskId} 부분은 경로 변수(Path Variable)로 처리됩니다.
     *
     * @param taskId 경로 변수({taskId})로부터 전달받은 조회할 작업의 ID
     * @return 작업 상태 정보를 포함하는 응답 (HTTP 200 OK)
     * 또는 작업 ID가 존재하지 않으면 (HTTP 404 Not Found) 응답
     */
    @GetMapping("/{taskId}/status") // HTTP GET 메서드와 매핑됩니다. "/tasks" + "/{taskId}/status" 경로가 됩니다.
    public ResponseEntity<TaskStatusResponse> getTaskStatus(@PathVariable String taskId) {
        // @PathVariable 어노테이션은 URL 경로의 일부({taskId})를 메서드 파라미터(String taskId)로 받습니다.
        log.info("Received HTTP GET request on /tasks/{}/status", taskId);

        // 서비스 계층의 메서드를 호출하여 상태 조회 로직 실행
        Optional<TaskStatusResponse> statusResponseOptional = taskService.getTaskStatus(taskId);

        // Optional 객체를 사용하여 결과가 있는지 확인하고 적절한 HTTP 응답 반환
        return statusResponseOptional
                .map(response -> ResponseEntity.ok(response)) // Optional에 값이 있으면(작업 ID 존재), 200 OK 상태와 함께 응답 본문(response) 반환
                .orElseGet(() -> ResponseEntity.notFound().build()); // Optional이 비어있으면(작업 ID 없음), 404 Not Found 상태 반환 (본문 없음)

        /* 위 map().orElseGet()과 동일한 로직 (if-else 사용 시)
        if (statusResponseOptional.isPresent()) {
            return ResponseEntity.ok(statusResponseOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
        */
    }
}
