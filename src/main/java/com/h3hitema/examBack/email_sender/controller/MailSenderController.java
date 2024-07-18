package com.h3hitema.examBack.email_sender.controller;

import com.h3hitema.examBack.email_sender.config.ApplicationProperties;
import com.h3hitema.examBack.email_sender.dto.MailDataDto;
import com.h3hitema.examBack.email_sender.dto.Response;
import com.h3hitema.examBack.email_sender.service.MailSenderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public record MailSenderController(MailSenderService service, ApplicationProperties applicationProperties) {

    @PostMapping()
    public Response getProfileById(@RequestBody MailDataDto mailDataDto) {
        return new Response(service.sendMailForgetPwd(applicationProperties.getMailFrom(), mailDataDto).name());
    }
}
