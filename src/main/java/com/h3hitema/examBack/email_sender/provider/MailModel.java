package com.h3hitema.examBack.email_sender.provider;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailModel {

    private String from;

    @Builder.Default
    private List<String> to = new ArrayList<>();

    @Builder.Default
    private List<String> cc = new ArrayList<>();

    private String subject;

    private String message;

    private boolean isHtml;

}