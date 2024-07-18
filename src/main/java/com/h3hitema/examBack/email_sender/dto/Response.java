package com.h3hitema.examBack.email_sender.dto;

import com.h3hitema.examBack.email_sender.service.SendMailStatus;

public record Response(SendMailStatus status) {

}
