package com.pomkine.read.accounts;

import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;


public class InMemoryAccountsInfo implements AccountsInfo {

    private final Map<String, AccountInfoResponse> responses;

    @Inject
    public InMemoryAccountsInfo(
        @Named("accountInfoResponses") Map<String, AccountInfoResponse> responses) {
        this.responses = responses;
    }


    @Override
    public Optional<AccountInfoResponse> getAccountInfo(String accountId) {
        return Optional.ofNullable(responses.get(accountId));
    }
}
