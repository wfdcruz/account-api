package com.ebanx.accountapi.controller.response;

import com.ebanx.accountapi.entity.Account;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonIgnoreProperties
public class AccountResponse {
    private final Account origin;
    private final Account destination;
}
