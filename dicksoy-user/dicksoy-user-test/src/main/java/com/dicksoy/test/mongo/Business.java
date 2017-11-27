package com.dicksoy.test.mongo;

import java.io.Serializable;

public class Business implements Serializable {

	private static final long serialVersionUID = 1L;  
	//维度  
	private double lat;  
	//经度  
	private double lng;  
	//商家编号  
	private int openid;  

	//默认构造函数  
	public Business() {  
		super();  
	}  
	//set和get   
	public double getLat() {  
		return lat;  
	}  
	public void setLat(double lat) {  
		this.lat = lat;  
	}  
	public double getLng() {  
		return lng;  
	}  
	public void setLng(double lng) {  
		this.lng = lng;  
	}  
	public int getOpenid() {  
		return openid;  
	}  
	public void setOpenid(int openid) {  
		this.openid = openid;  
	}  
}
