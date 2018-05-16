package com.pomkine.resource.account;

import com.codahale.metrics.annotation.Timed;
import com.pomkine.boundary.Accounts;
import com.pomkine.read.accounts.AccountInfoResponse;
import com.pomkine.read.accounts.AccountsInfo;
import java.util.Optional;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

    private AccountsInfo accountsInfo;
    private Accounts accounts;

    @Inject
    public AccountResource(AccountsInfo accountsInfo, Accounts accounts) {
        this.accountsInfo = accountsInfo;
        this.accounts = accounts;
    }

    @Path("/{account_id}")
    @GET
    @Timed
    public AccountInfoResponse getAccountInfo(@PathParam("account_id") String accountId) {
        Optional<AccountInfoResponse> respOpt = accountsInfo.getAccountInfo(accountId);
        if (respOpt.isPresent()) {
            return respOpt.get();
        } else {
            String message = String.format("Account ID not found: %s", accountId);
            throw new WebApplicationException(message, Status.NOT_FOUND);
        }
    }

    @POST
    @Timed
    public OpenedAccountResponse openAccount(@NotNull OpenAccountRequest request) {
        return new OpenedAccountResponse(accounts.openAccount(request.getInitialBalance()));
    }

}
