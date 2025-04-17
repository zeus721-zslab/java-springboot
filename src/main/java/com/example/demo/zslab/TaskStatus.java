package com.example.demo.zslab; // 또는 다른 적절한 도메인 관련 패키지

/**
 * 비동기 작업의 상태를 나타내는 Enum (열거형)
 */
public enum TaskStatus {
    PENDING,    // 작업 대기 중
    PROCESSING, // 작업 처리 중
    COMPLETED,  // 작업 완료
    FAILED      // 작업 실패
}