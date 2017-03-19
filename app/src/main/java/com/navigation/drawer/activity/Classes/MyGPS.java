package com.navigation.drawer.activity.Classes;


public class MyGPS {
	private double laltitude;
	private double longitude;

	public String toString(){
		return "[Laltitude = "+ laltitude +", Longitude = "+ longitude +"]";
	}

	public MyGPS(double laltitude, double y) {
		this.laltitude = laltitude;
		this.longitude = y;
	}

	public MyGPS() {
		this.laltitude = 0;
		this.longitude = 0;
	}

	public double getLaltitude() {
		return laltitude;
	}

	public void setLaltitude(double laltitude) {
		this.laltitude = laltitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
}
