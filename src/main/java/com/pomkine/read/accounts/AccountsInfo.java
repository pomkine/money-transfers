package com.pomkine.read.accounts;

import java.util.Optional;

public interface AccountsInfo {

    Optional<AccountInfoResponse> getAccountInfo(String accountId);
}
