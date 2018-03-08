package model.sensors.structures;

import java.lang.Math;

public class GeographicCoordinates {
	
	private double latitude,longitude,altitude;
	//private LatitudeDirection latD;
	//private LongitudeDirection lonD;
	
	

	public static final double minLat=-90,maxLat=90,minLong=-180,maxLong=180;
	public static final double degreeToMeters=111319;
	public static final double meterToDegrees=1/degreeToMeters;
	
	private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}

		return (dist);
	}
	
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}
	
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
	
	public static boolean isLatitudeValid(double latitude)
	{
		return latitude>=minLat&&latitude<=maxLat;
	}
	
	public static boolean isLongitudeValid(double longitude)
	{
		return longitude>=minLong&&longitude<=maxLong;
	}
	
	public static GeographicCoordinates addToCoordinates(GeographicCoordinates coordinates, double latitude,double longitude) throws Exception
	{
		
		if (!isLatitudeValid(latitude)||!isLongitudeValid(longitude))
			throw new Exception("Parameters not valid");
		
		double sumLatitude,sumLongitude;
		sumLatitude=coordinates.getLatitude()+latitude;
		sumLongitude=coordinates.getLongitude()+longitude;
		
		if (sumLatitude>maxLat)
		{
			sumLatitude=90-sumLatitude%90;
			sumLongitude=sumLongitude+maxLong;
		}
		else if (sumLatitude<minLat)
		{
			sumLatitude=-90-sumLatitude%90;
			sumLongitude=sumLongitude+maxLong;
		}
		
		if (sumLongitude>maxLong)
		{
			sumLongitude=(-360+sumLongitude);
		}
		else if (sumLongitude<minLong)
		{
			sumLongitude=(360-sumLongitude);
		}
		
		return new GeographicCoordinates(sumLatitude, sumLongitude);
	}
	
	public GeographicCoordinates()
	{
		latitude=0;
		longitude=0;
	}
	
	public GeographicCoordinates(double latitude,double longitude) throws Exception
	{
		if(!isLatitudeValid(latitude)||!isLongitudeValid(longitude))
			throw new Exception(String.format("Parameters not valid: Lat:%f, Long:%f", latitude,longitude));
		this.latitude=latitude;
		this.longitude=longitude;
	}
	
	
	
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
}


