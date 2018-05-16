package com.pomkine.domain.account;

import com.pomkine.domain.account.command.OpenAccount;
import com.pomkine.domain.common.AggregateId;
import java.util.Optional;

public interface AccountRepository {

    Account save(Account account);

    Optional<Account> findById(AggregateId id);

    Account openAccount(OpenAccount openAccount);

}
