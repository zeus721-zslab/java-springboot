package com.example.demo.service; // 또는 다른 적절한 서비스 관련 패키지

import com.example.demo.zslab.TaskStatus; // TaskStatus Enum 임포트
import com.example.demo.dto.TaskResponse; // TaskResponse DTO 임포트
import com.example.demo.dto.TaskStatusResponse; // TaskStatusResponse DTO 임포트
import com.example.demo.repository.InMemoryTaskRepository; // 이전 단계에서 만든 Repository 임포트
import lombok.RequiredArgsConstructor; // Lombok: final 필드 생성자 자동 생성
import org.slf4j.Logger; // 로깅을 위한 SLF4J Logger
import org.slf4j.LoggerFactory; // 로깅을 위한 SLF4J LoggerFactory
import org.springframework.scheduling.annotation.Async; // 비동기 메서드 표시
import org.springframework.stereotype.Service; // 서비스 빈(Bean)으로 등록

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit; // Thread.sleep 대신 사용 (가독성)

/**
 * 비동기 작업을 관리하고 처리하는 핵심 로직을 담당하는 서비스 클래스입니다.
 */
@Service // 이 클래스를 Spring의 서비스 컴포넌트로 등록합니다.
@RequiredArgsConstructor // Lombok: final 필드(taskRepository)에 대한 생성자를 자동으로 만듭니다. (생성자 주입)
public class TaskService {

    // 로거(Logger) 인스턴스 생성 (System.out.println 대신 사용 권장)
    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    // final 키워드와 @RequiredArgsConstructor를 통해 생성자 주입 방식으로 Repository를 주입받습니다.
    private final InMemoryTaskRepository taskRepository;
    private final AsyncTaskExecutor asyncTaskExecutor; // 새로운 Executor 주입

    public TaskResponse submitTask() {
        String taskId = UUID.randomUUID().toString();
        taskRepository.createTask(taskId);
        log.info("Task submitted: {}", taskId);
        // 별도 Bean의 @Async 메서드 호출
        asyncTaskExecutor.executeTask(taskId);
        return new TaskResponse(taskId);
    }


    /**
     * 지정된 작업 ID의 현재 상태와 결과(결과 URL)를 조회합니다.
     *
     * @param taskId 조회할 작업 ID
     * @return 작업 상태 정보를 담은 Optional<TaskStatusResponse> 객체. 작업 ID가 없으면 Optional.empty() 반환.
     */
    public Optional<TaskStatusResponse> getTaskStatus(String taskId) {
        // 저장소에서 상태 조회
        Optional<TaskStatus> statusOptional = taskRepository.getStatus(taskId);

        // 작업 ID 자체가 존재하지 않으면 빈 Optional 반환
        if (statusOptional.isEmpty()) {
            log.warn("Status requested for non-existent task: {}", taskId);
            return Optional.empty();
        }

        TaskStatus status = statusOptional.get();
        Optional<String> resultUrlOptional = Optional.empty();

        // 상태가 COMPLETED일 때만 결과 조회를 시도 (결과는 완료 시에만 의미가 있음)
        if (status == TaskStatus.COMPLETED) {
            resultUrlOptional = taskRepository.getResult(taskId);
        }

        // 조회된 정보로 응답 DTO 생성
        TaskStatusResponse response = new TaskStatusResponse(
                taskId,
                status,
                resultUrlOptional.orElse(null) // 결과 URL이 없으면 null로 설정
        );
        return Optional.of(response);
    }
}
