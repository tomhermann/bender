package com.zombietank.bender;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
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

	public static void main(String... args) throws IOException {
		new Driver().start();
	}
	
	void start() throws IOException {
		ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1);
		
		InfluxDB influxDb = createConnection(loadProperties());
		Repository<Sigar> repository = new InfluxDbRepo<>("sigar", influxDb, new SigarTransformer());
		
		Runnable task = new GatherSystemDataTask(new Sigar(), repository);
		newScheduledThreadPool.scheduleWithFixedDelay(task, 0, 1, TimeUnit.SECONDS);
	}

	private InfluxDB createConnection(Properties properties) {
		String hostname = properties.getProperty("hostname");
		String username = properties.getProperty("username");
		String password = properties.getProperty("password");
		return InfluxDBFactory.connect(hostname, username, password);
	}
	
	private Properties loadProperties() throws IOException {
		Properties properties = new Properties();
		try(InputStream inputStream = Driver.class.getResourceAsStream("/influxdb.properties")) {
			properties.load(inputStream);
		}
		return properties;
	}
}
