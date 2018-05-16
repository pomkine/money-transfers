package com.pomkine.read.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.joda.money.Money;

@Value
public class AccountInfoResponse {

    @JsonProperty("account_id")
    private String accountId;
    @JsonProperty()
    private Money balance;


}
