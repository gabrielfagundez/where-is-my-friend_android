package com.whereismyfriend;

public class Amigo {

	private String name;
	private String mail;
	private String id;
	private double lat;
	private double lon;
	
	
	
	public Amigo(String name, String mail, String id) {
		super();
		this.name = name;
		this.mail = mail;
		this.id = id;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}


	public String getMail() {
		return mail;
	}


	public void setMail(String mail) {
		this.mail = mail;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}
	
	
}
