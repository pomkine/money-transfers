package com.pomkine.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.pomkine.application.guice.AccountsInfoReadModule;
import com.pomkine.application.guice.EventStoreModule;
import com.pomkine.resource.serialize.MoneyDeserializer;
import com.pomkine.resource.serialize.MoneySerializer;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.joda.money.Money;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class MoneyTransferApplication extends Application<MoneyTransferConfiguration> {

    public static void main(String[] args) throws Exception {
        new MoneyTransferApplication().run(args);
    }

    @Override
    public String getName() {
        return "money-transfer";
    }

    @Override
    public void initialize(Bootstrap<MoneyTransferConfiguration> bootstrap) {
        bootstrap.addBundle(GuiceBundle.builder()
            .printDiagnosticInfo()
            .enableAutoConfig("com.pomkine")
            .modules(new EventStoreModule(), new AccountsInfoReadModule())
            .build());
        ObjectMapper mapper = bootstrap.getObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Money.class, new MoneyDeserializer());
        module.addSerializer(Money.class, new MoneySerializer());
        mapper.registerModule(module);
    }

    @Override
    public void run(MoneyTransferConfiguration configuration, Environment environment) {

    }

}
