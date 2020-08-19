package com.ebanx.accountapi.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class Account {
    private String id;
    private BigDecimal balance;
}
