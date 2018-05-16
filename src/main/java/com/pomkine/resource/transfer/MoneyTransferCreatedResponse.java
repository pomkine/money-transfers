package com.pomkine.resource.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class MoneyTransferCreatedResponse {

    @JsonProperty("transfer_id")
    private String transferId;
}
