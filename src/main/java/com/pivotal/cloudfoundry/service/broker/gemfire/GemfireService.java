package com.pivotal.cloudfoundry.service.broker.gemfire;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.pivotal.cloudfoundry.service.broker.CloudFoundryService;

public class GemfireService extends CloudFoundryService {

	public static final String GEMFIRE_SERVICE_ID = "5531b971-5698-4816-a7ac-6abz2e347688";
	public static final String GEMFIRE_SERVICE_NAME = "p-gemfire";
	public static final String GEMFIRE_SERVICE_DESC = "Gemfire service for application development and testing";
	public static final List<String> _gfTags = Collections.unmodifiableList(Arrays.asList("nosql", "gemfire"));
	
	public GemfireService() {
		super(GEMFIRE_SERVICE_ID, GEMFIRE_SERVICE_NAME, GEMFIRE_SERVICE_DESC);
		addTags(_gfTags);
	}

}
