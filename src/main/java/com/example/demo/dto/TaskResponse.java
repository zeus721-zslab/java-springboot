package com.example.demo.dto; // 또는 다른 적절한 DTO 관련 패키지

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 작업 생성 요청 시 클라이언트에게 반환되는 응답 DTO.
 * 생성된 작업의 ID를 포함합니다.
 */
@Data // Lombok: @Getter, @Setter, @ToString, @EqualsAndHashCode 자동 생성
@RequiredArgsConstructor // Lombok: final 또는 @NonNull 필드만 인자로 받는 생성자 자동 생성
public class TaskResponse {

    /**
     * 생성된 작업의 고유 ID (final로 선언하여 생성자 주입)
     */
    private final String taskId;
}