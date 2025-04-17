package com.example.demo.repository; // 또는 다른 적절한 저장소 관련 패키지

import com.example.demo.zslab.TaskStatus; // 이전에 정의한 TaskStatus Enum 임포트
import org.springframework.stereotype.Repository; // 또는 @Component

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 비동기 작업의 상태와 결과를 메모리에 저장하고 관리하는 저장소 클래스입니다.
 * <p>
 * 참고: 이 구현은 애플리케이션이 재시작되면 모든 데이터가 사라지는 메모리 기반 저장소입니다.
 * 실제 프로덕션 환경에서는 영속성을 위해 데이터베이스(예: JPA Repository) 사용을 고려해야 합니다.
 * {@link ConcurrentHashMap}을 사용하여 멀티스레드 환경에서의 동시성 문제를 일부 완화합니다.
 * </p>
 */
@Repository // 데이터 저장소 역할을 나타내는 Spring 스테레오타입 어노테이션 (@Component도 가능)
public class InMemoryTaskRepository {

    // 작업 ID(String)를 Key로, 작업 상태(TaskStatus)를 Value로 저장하는 Map
    private final Map<String, TaskStatus> taskStatuses = new ConcurrentHashMap<>();
    // 작업 ID(String)를 Key로, 작업 결과(여기서는 결과 URL String으로 가정)를 Value로 저장하는 Map
    private final Map<String, String> taskResults = new ConcurrentHashMap<>();

    /**
     * 새로운 작업을 저장소에 추가하고 초기 상태(PENDING)를 설정합니다.
     *
     * @param taskId 생성된 고유 작업 ID
     */
    public void createTask(String taskId) {
        taskStatuses.put(taskId, TaskStatus.PENDING);
        taskResults.remove(taskId); // 혹시 이전 결과가 남아있다면 제거
        System.out.println("Task created: " + taskId + " with status PENDING"); // 로그 추가 (디버깅용)
    }

    /**
     * 지정된 작업 ID의 상태를 업데이트합니다.
     *
     * @param taskId 상태를 업데이트할 작업 ID
     * @param status 새로운 작업 상태
     */
    public void updateStatus(String taskId, TaskStatus status) {
        taskStatuses.put(taskId, status);
        System.out.println("Task updated: " + taskId + " to status " + status); // 로그 추가 (디버깅용)
    }

    /**
     * 지정된 작업 ID의 현재 상태를 조회합니다.
     * 작업 ID가 존재하지 않으면 Optional.empty()를 반환합니다.
     *
     * @param taskId 상태를 조회할 작업 ID
     * @return Optional<TaskStatus> 작업 상태
     */
    public Optional<TaskStatus> getStatus(String taskId) {
        return Optional.ofNullable(taskStatuses.get(taskId));
    }

    /**
     * 지정된 작업 ID에 대한 결과(여기서는 결과 URL 문자열)를 저장합니다.
     *
     * @param taskId   결과를 저장할 작업 ID
     * @param resultUrl 저장할 결과 URL 문자열
     */
    public void saveResult(String taskId, String resultUrl) {
        taskResults.put(taskId, resultUrl);
        System.out.println("Result saved for task: " + taskId + " -> " + resultUrl); // 로그 추가 (디버깅용)
    }

    /**
     * 지정된 작업 ID의 결과를 조회합니다.
     * 결과가 존재하지 않으면 Optional.empty()를 반환합니다.
     *
     * @param taskId 결과를 조회할 작업 ID
     * @return Optional<String> 작업 결과 URL
     */
    public Optional<String> getResult(String taskId) {
        return Optional.ofNullable(taskResults.get(taskId));
    }
}
