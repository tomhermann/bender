package com.zombietank.bender.sigar;

import org.hyperic.sigar.Sigar;

import com.zombietank.bender.api.Repository;

public class GatherSystemDataTask implements Runnable {
	private final Sigar sigar;
	private final Repository<Sigar> repository;

	public GatherSystemDataTask(Sigar sigar, Repository<Sigar> repository) {
		this.sigar = sigar;
		this.repository = repository;
	}

	@Override
	public void run() {
		repository.store(sigar, Sigar.class);
	}
}
