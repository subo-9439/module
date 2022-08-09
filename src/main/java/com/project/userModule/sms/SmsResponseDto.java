package com.project.userModule.sms;

import lombok.*;

import java.time.LocalDateTime;
/*
{
    "requestId":"string",
    "requestTime":"string",
    "statusCode":"string",
    "statusName":"string"
}
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SmsResponseDto {
    private String requestId;
    private LocalDateTime requestTime;
    private String statusCode;
    private String statusName;

    @Builder
    public SmsResponseDto(String statusCode, String statusName, String requestId, LocalDateTime requestTime) {
        this.statusCode = statusCode;
        this.statusName = statusName;
        this.requestId = requestId;
        this.requestTime = requestTime;
    }
}
