package com.zombietank.bender.influx;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Serie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zombietank.bender.api.Repository;
import com.zombietank.bender.influx.series.SeriesTransformer;

public class InfluxDbRepo<T> implements Repository<T> {
	private static final Logger logger = LoggerFactory.getLogger(InfluxDbRepo.class);
	private final String databaseName;
	private final InfluxDB influxDb;
	private final SeriesTransformer<T> seriesTransformer;

	public InfluxDbRepo(String databaseName, InfluxDB influxDb, SeriesTransformer<T> seriesTransformer) {
		this.databaseName = databaseName;
		this.influxDb = influxDb;
		this.seriesTransformer = seriesTransformer;
	}

	@Override
	public void store(T instance) { 
		Serie[] series = seriesTransformer.apply(instance);
		influxDb.write(databaseName, seriesTransformer.getPrecision(), series);
		logger.info("Wrote instance");
	}
}
