package org.ojbc.util.rest.jackson;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.joda.time.DateTime;

public class JodaDateTimeDeserializer extends JsonDeserializer<DateTime> {

	    @Override
	    public DateTime deserialize(JsonParser arg0, DeserializationContext arg1)throws IOException, JsonProcessingException {
	    	return DateTime.parse(arg0.getText());
	    }
}
