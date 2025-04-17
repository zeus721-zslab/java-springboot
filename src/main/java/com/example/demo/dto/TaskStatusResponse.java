package com.example.demo.dto; // 또는 다른 적절한 DTO 관련 패키지

import com.example.demo.zslab.TaskStatus; // 위에서 정의한 TaskStatus Enum 임포트
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 작업 상태 조회 요청 시 클라이언트에게 반환되는 응답 DTO.
 * 작업 ID, 현재 상태, 그리고 선택적으로 결과 URL을 포함합니다.
 */
@Data // Lombok: @Getter, @Setter, @ToString, @EqualsAndHashCode 자동 생성
@NoArgsConstructor // Lombok: 파라미터 없는 기본 생성자 자동 생성
@AllArgsConstructor // Lombok: 모든 필드를 인자로 받는 생성자 자동 생성
public class TaskStatusResponse {

    /**
     * 조회 대상 작업의 고유 ID
     */
    private String taskId;

    /**
     * 작업의 현재 상태 (PENDING, PROCESSING, COMPLETED, FAILED)
     */
    private TaskStatus status;

    /**
     * 작업 완료 시 결과 파일 등에 접근할 수 있는 URL (선택 사항)
     */
    private String resultUrl;

    // 필요에 따라 다른 필드를 추가할 수 있습니다.
    // 예: private String errorMessage; // 실패 시 에러 메시지
    // 예: private int progressPercentage; // 진행률
}
