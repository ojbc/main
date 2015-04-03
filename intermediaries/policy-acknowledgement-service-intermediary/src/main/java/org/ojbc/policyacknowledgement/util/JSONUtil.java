package org.ojbc.policyacknowledgement.util;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JSONUtil {
    private final static Log log = LogFactory.getLog(JSONUtil.class);

    private static ObjectWriter objectWriter = new ObjectMapper().writer()
            .withDefaultPrettyPrinter()
            .with(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));;

    private JSONUtil() {
    }

    public static String toJsonString(Object object) {
        String json = "";
        try {
            json = objectWriter.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage());
            json = e.getMessage();
        }
        return json;
    }

}
