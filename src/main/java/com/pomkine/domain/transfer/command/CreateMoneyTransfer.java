package com.pomkine.domain.transfer.command;

import com.pomkine.domain.transfer.TransferDetails;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class CreateMoneyTransfer {

    private final TransferDetails details;

    public CreateMoneyTransfer(TransferDetails details) {
        this.details = details;
    }
}
