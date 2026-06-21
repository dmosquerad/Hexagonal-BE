package com.architecture.hexagonal.boot.config.usecase.email;

import com.architecture.hexagonal.application.business.email.getblockedrules.usecase.impl.GetBlockedRulesUseCaseImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({GetBlockedRulesUseCaseImpl.class})
public class EmailUseCaseConfig {}
