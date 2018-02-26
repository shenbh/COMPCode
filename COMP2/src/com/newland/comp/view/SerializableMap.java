package com.newland.comp.view;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializableMap implements Serializable {
	private Map<String,Object> map = new HashMap<String, Object>();
	public Map<String,Object> getMap()
	{
		return map;
	}
	public void setMap(String key, Object value)
	{
		map.put(key, value);
	}
}
