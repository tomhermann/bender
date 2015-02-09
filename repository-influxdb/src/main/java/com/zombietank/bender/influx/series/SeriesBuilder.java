package com.zombietank.bender.influx.series;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.influxdb.dto.Serie;

import com.google.common.base.Preconditions;

public class SeriesBuilder {
	private String name;
	private Map<String, Object> entries = new LinkedHashMap<>();

	public SeriesBuilder(String name) {
		Preconditions.checkNotNull("Name cannot be null", name);
		this.name = name;
	}

	public SeriesBuilder addEntry(String key, Object value) {
		entries.put(key, value);
		return this;
	}

	public SeriesBuilder addAll(Map<String, Object> collection) {
		entries.putAll(collection);
		return this;
	}

	public SeriesColumnValueBuilder columns(String... columns) {
		return new SeriesColumnValueBuilder(name, entries).columns(columns);
	}

	public Serie build() {
		List<String> columns = new ArrayList<>();
		List<Object> values = new ArrayList<>();

		for (Entry<String, Object> entry : entries.entrySet()) {
			columns.add(entry.getKey());
			values.add(entry.getValue());
		}

		return new Serie.Builder(name).columns(columns.toArray(new String[columns.size()]))
				.values(values.toArray(new Object[values.size()])).build();
	}
}
