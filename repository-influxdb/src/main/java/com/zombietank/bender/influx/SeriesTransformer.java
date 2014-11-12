package com.zombietank.bender.influx;

import java.util.concurrent.TimeUnit;

import org.influxdb.dto.Serie;

import com.google.common.base.Function;

public interface SeriesTransformer<T> extends Function<T, Serie[]> {
	TimeUnit getPrecision();
}
