package com.project.userModule.sms;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class SmsRequestDto {
    String type;
    String from;
    String content;
    List<SmsMessageDto> messages = new ArrayList<>();

    @Builder
    public SmsRequestDto(String type,  String from, String content, List<SmsMessageDto> messages) {
        this.type = type;
        this.from = from;
        this.content = content;
        this.messages = messages;
    }

    public void addMessages(SmsMessageDto smsMessageDto){
        messages.add(smsMessageDto);
    }

}
