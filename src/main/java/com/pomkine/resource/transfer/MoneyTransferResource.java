package com.pomkine.resource.transfer;

import com.codahale.metrics.annotation.Timed;
import com.pomkine.boundary.MoneyTransfers;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/transfer")
@Produces(MediaType.APPLICATION_JSON)
public class MoneyTransferResource {

    private MoneyTransfers moneyTransfers;

    @Inject
    public MoneyTransferResource(MoneyTransfers moneyTransfers) {
        this.moneyTransfers = moneyTransfers;
    }

    @POST
    @Timed
    public MoneyTransferCreatedResponse transferMoney(@Valid MoneyTransferCreateRequest request) {
        String transferId = moneyTransfers
            .transferMoney(
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount());
        return new MoneyTransferCreatedResponse(transferId);
    }

}
