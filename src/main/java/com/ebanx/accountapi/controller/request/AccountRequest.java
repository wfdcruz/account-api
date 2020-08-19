package com.ebanx.accountapi.controller.request;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class AccountRequest {
    private EventType type;
    private String destination;
    private BigDecimal amount;
}
