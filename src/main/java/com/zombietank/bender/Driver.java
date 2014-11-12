package com.zombietank.bender;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.hyperic.sigar.Sigar;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

import com.zombietank.bender.api.Repository;
import com.zombietank.bender.influx.InfluxDbRepo;
import com.zombietank.bender.sigar.GatherSystemDataTask;
import com.zombietank.bender.sigar.influxdb.SigarTransformer;

public class Driver {

	public void run() {
		ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1);
		
		InfluxDB influxDb = InfluxDBFactory.connect("http://zombietank.com:8086", "tom", "testing");
		Repository<Sigar> repository = new InfluxDbRepo<>("sigar", influxDb, new SigarTransformer());
		
		Runnable task = new GatherSystemDataTask(new Sigar(), repository);
		newScheduledThreadPool.scheduleWithFixedDelay(task, 0, 1, TimeUnit.SECONDS);
	}
	
	public static void main(String[] args) {
		new Driver().run();
	}
	
}
