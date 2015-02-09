package com.zombietank.bender.influx.series;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.influxdb.dto.Serie;

import com.google.common.base.Preconditions;

public class SeriesColumnValueBuilder extends SeriesBuilder {
	private List<String> columns;
	private List<Object> values;

	public SeriesColumnValueBuilder(String name, Map<String, Object> entries) {
		super(name);
		addAll(entries);
	}

	@Override
	public SeriesColumnValueBuilder columns(String... columns) {
		this.columns = Arrays.asList(columns);
		return this;
	}

	public SeriesColumnValueBuilder values(Object... values) {
		this.values = Arrays.asList(values);
		return this;
	}

	@Override
	public Serie build() {
		Preconditions.checkNotNull("Set columns.", columns);
		Preconditions.checkNotNull("Set values.", values);
		Preconditions.checkState(columns.size() == values.size(), "Values and columns must be of same length.");
		
		for (int i = 0; i < columns.size(); i++) {
			addEntry(columns.get(i), values.get(i));
		}

		return super.build();
	}
}
