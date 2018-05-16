package com.pomkine.resource.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.joda.money.Money;

@Value
public class OpenAccountRequest {

    @JsonProperty("initial_balance")
    private Money initialBalance;

}
