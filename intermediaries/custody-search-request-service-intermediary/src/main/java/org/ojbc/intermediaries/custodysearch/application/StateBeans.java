package org.ojbc.intermediaries.custodysearch.application;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ConditionalOnProperty(name = "spring.config.additional-location")
@ImportResource(value = {"file:${spring.config.additional-location}beans/*.xml"})
public class StateBeans{

}
