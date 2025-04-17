package com.example.demo.service; // 또는 별도 패키지

// --- ↓ 필요한 import 구문들 ---
import com.example.demo.zslab.TaskStatus; // TaskStatus Enum 사용을 위해
import com.example.demo.repository.InMemoryTaskRepository; // InMemoryTaskRepository 사용을 위해
import org.slf4j.Logger; // Logger 사용을 위해
import org.slf4j.LoggerFactory; // LoggerFactory 사용을 위해
import org.springframework.scheduling.annotation.Async; // @Async 어노테이션 사용을 위해
import org.springframework.stereotype.Component; // @Component 어노테이션 사용을 위해
import java.util.concurrent.TimeUnit; // TimeUnit 사용을 위해

@Component // Spring Bean으로 등록
public class AsyncTaskExecutor {
    private static final Logger log = LoggerFactory.getLogger(AsyncTaskExecutor.class);
    private final InMemoryTaskRepository taskRepository; // 주입 필요

    // 생성자 주입
    public AsyncTaskExecutor(InMemoryTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Async // 비동기 실행
    public void executeTask(String taskId) {
        log.info("[Thread: {}] Starting async execution for task: {}", Thread.currentThread().getName(), taskId);
        try {
            taskRepository.updateStatus(taskId, TaskStatus.PROCESSING);
            TimeUnit.SECONDS.sleep(5); // 작업 시뮬레이션
            String resultUrl = "/downloads/report-" + taskId + ".pdf";
            taskRepository.saveResult(taskId, resultUrl);
            taskRepository.updateStatus(taskId, TaskStatus.COMPLETED);
            log.info("[Thread: {}] Async execution completed successfully: {}", Thread.currentThread().getName(), taskId);
        } catch (InterruptedException e) {
            log.warn("[Thread: {}] Task interrupted: {}", Thread.currentThread().getName(), taskId, e);
            taskRepository.updateStatus(taskId, TaskStatus.FAILED);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("[Thread: {}] Task failed unexpectedly: {}", Thread.currentThread().getName(), taskId, e);
            taskRepository.updateStatus(taskId, TaskStatus.FAILED);
        }
    }
}