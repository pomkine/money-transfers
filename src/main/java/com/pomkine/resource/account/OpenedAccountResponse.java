package com.pomkine.resource.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class OpenedAccountResponse {

    @JsonProperty("account_id")
    private String accountId;

}
