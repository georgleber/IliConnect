package com.android.iliConnect.models;

public class ClassAlias {

	public Class<?> classInfo;
	public String className;

	public ClassAlias(String name, Class<?> c) {
		this.classInfo = c;
		this.className = name;
	}
}
