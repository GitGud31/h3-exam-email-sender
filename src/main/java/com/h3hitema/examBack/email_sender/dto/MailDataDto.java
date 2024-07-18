package com.h3hitema.examBack.email_sender.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MailDataDto {
    String to;
    Map<String, Object> model = new HashMap<>();
}
