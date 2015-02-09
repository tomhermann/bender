package com.zombietank.bender.sigar;

import org.hyperic.sigar.Sigar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zombietank.bender.api.Repository;

public class GatherSystemDataTask implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(GatherSystemDataTask.class);
	private final Sigar sigar;
	private final Repository<Sigar> repository;

	public GatherSystemDataTask(Sigar sigar, Repository<Sigar> repository) {
		this.sigar = sigar;
		this.repository = repository;
	}

	@Override
	public void run() {
		logger.info("Gathering system information");
		repository.store(sigar);
	}
}
