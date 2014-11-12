package com.zombietank.bender.api;

public interface Repository<T> {
	void store(T instance, Class<T> clazz);
}
