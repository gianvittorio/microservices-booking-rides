package com.gianvittorio.drivermatchingservice.config.web;

import com.gianvittorio.common.web.RestExceptionsHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RestExceptionsHandler.class)
public class RestExceptionsHandlerConfiguration {
}
