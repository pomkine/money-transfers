package com.pomkine.resource.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.joda.money.Money;

public class MoneySerializer extends StdSerializer<Money> {

    public MoneySerializer() {
        super(Money.class);
    }

    @Override
    public void serialize(Money value, JsonGenerator generator, SerializerProvider provider)
        throws IOException {
        generator.writeStartObject();
        {
            generator.writeNumberField("amount", value.getAmount());
            generator.writeStringField("currency", value.getCurrencyUnit().getCode());
        }
        generator.writeEndObject();
    }
}
