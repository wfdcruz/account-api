package com.ebanx.accountapi.controller.request;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
@SuppressWarnings("unused")
public class AccountRequest {
    private EventType type;
    private String destination;
    private String origin;
    private BigDecimal amount;
}
