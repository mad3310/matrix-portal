package com.letv.common.session;

public interface Executable<T> {
	T execute() throws Throwable;
}
