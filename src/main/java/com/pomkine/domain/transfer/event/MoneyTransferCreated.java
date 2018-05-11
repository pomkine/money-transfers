package com.pomkine.domain.transfer.event;


import com.pomkine.domain.transfer.TransferDetails;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MoneyTransferCreated extends MoneyTransferEvent {

    public MoneyTransferCreated(TransferDetails transferDetails) {
        super(transferDetails);
    }
}
