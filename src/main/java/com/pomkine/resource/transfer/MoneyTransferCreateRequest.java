package com.pomkine.resource.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Value;
import org.joda.money.Money;

@Value
public class MoneyTransferCreateRequest {

    @JsonProperty("from_account")
    @NotNull
    private String fromAccountId;
    @JsonProperty("to_account")
    @NotNull
    private String toAccountId;
    @JsonProperty("amount")
    @NotNull
    private Money amount;

}
