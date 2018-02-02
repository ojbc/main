package org.ojbc.util.rest.jackson;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class JodaDateTimeSerializer extends JsonSerializer<DateTime> {

	    private static DateTimeFormatter formatter = 
	        DateTimeFormat.forPattern("yyyy-MM-dd");

	    @Override
	    public void serialize(DateTime value, JsonGenerator gen, 
	                          SerializerProvider arg2)
	        throws IOException, JsonProcessingException {

	        gen.writeString(formatter.print(value));
	    }
}
