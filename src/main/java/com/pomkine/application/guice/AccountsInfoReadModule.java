package com.pomkine.application.guice;

import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.pomkine.read.accounts.AccountInfoResponse;
import com.pomkine.read.accounts.AccountsInfo;
import com.pomkine.read.accounts.AccountsUpdateEventListener;
import com.pomkine.read.accounts.InMemoryAccountsInfo;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class AccountsInfoReadModule extends AbstractModule {


    @Override
    // @formatter:off
    protected void configure() {
        ConcurrentMap<String,AccountInfoResponse> responses = Maps.newConcurrentMap();
        bind(new TypeLiteral<Map<String,AccountInfoResponse>>(){})
            .annotatedWith(Names.named("accountInfoResponses"))
            .toInstance(responses);
        bind(AccountsInfo.class).to(InMemoryAccountsInfo.class);
        requireBinding(AccountsUpdateEventListener.class);
    }
    // @formatter:on


}
