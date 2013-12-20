package com.pivotal.cloudfoundry.service.broker.gemfire;

import org.springframework.data.gemfire.function.annotation.GemfireFunction;
import org.springframework.data.gemfire.mapping.Regions;
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.PartitionAttributesFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.RegionFactory;
import com.gemstone.gemfire.cache.RegionShortcut;

public class GemfireFunctions {

	@GemfireFunction(id = "provision")
	public void provision(String region) {
		System.out.println("Trying to create Region [" + region + "]");

		try {
			Cache cache = CacheFactory.getAnyInstance();
			RegionFactory factory = cache.createRegionFactory(RegionShortcut.PARTITION);
			PartitionAttributesFactory paf = new PartitionAttributesFactory();
			paf.setTotalNumBuckets(113);
			paf.setRedundantCopies(1);
			paf.setRecoveryDelay(0);
			paf.setStartupRecoveryDelay(0);
			factory.setPartitionAttributes(paf.create());
			factory.create(region);
			System.out.println("Region created...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@GemfireFunction(id = "deprovision")
	public void deprovision(String region) {
		System.out.println("Trying to destroy Region [" + region + "]");

		try {
			Cache cache = CacheFactory.getAnyInstance();
			Region r = cache.getRegion(region);
			r.destroyRegion();
			System.out.println("Region created...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
