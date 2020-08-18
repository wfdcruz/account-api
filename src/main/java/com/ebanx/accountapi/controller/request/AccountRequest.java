package com.ebanx.accountapi.controller.request;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class AccountRequest {
    private EventType type;
    private Long destination;
    private BigDecimal amount;
}
