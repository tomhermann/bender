package com.zombietank.bender.influx;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Serie;

import com.zombietank.bender.api.Repository;

public class InfluxDbRepo<T> implements Repository<T> {
	private final String databaseName;
	private final InfluxDB influxDb;
	private final SeriesTransformer<T> seriesTransformer;

	public InfluxDbRepo(String databaseName, InfluxDB influxDb, SeriesTransformer<T> seriesTransformer) {
		this.databaseName = databaseName;
		this.influxDb = influxDb;
		this.seriesTransformer = seriesTransformer;
	}

	@Override
	public void store(T instance, Class<T> clazz) { 
		Serie[] series = seriesTransformer.apply(instance);
		influxDb.write(databaseName, seriesTransformer.getPrecision(), series);
	}
}
