package com.ebanx.accountapi.controller.response;

import com.ebanx.accountapi.entity.Account;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponse {
    private final Account origin;
    private final Account destination;
}
