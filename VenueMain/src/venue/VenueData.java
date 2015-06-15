package venue;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class VenueData {

	
	/**
	 * 
	 * This method is called to display all the Venue 
	 * 
	 * **/
	
	public void Display(String Name) throws ParseException
	{
				
			    JSONObject json = (JSONObject)new JSONParser().parse(Name);
			   	System.out.println("Name :          " + json.get("foreign_venue_name"));
				System.out.println("Address :       " + json.get("foreign_venue_address1"));
				System.out.println("                " + json.get("foreign_venue_address2"));
				System.out.println("City :          " + json.get("foreign_venue_city"));
				System.out.println("State :         " + json.get("foreign_venue_state"));
				System.out.println("Country :       " + json.get("foreign_venue_country"));
				System.out.println("Zipcode :       " + json.get("foreign_venue_zipcode"));
				System.out.println("Phone Number :  " + json.get("foreign_venue_phonenumber"));
				System.out.println("Rating    :     " + json.get("foreign_venue_rating"));
				System.out.println("Latitude :      " + json.get("foreign_venue_lat"));
				System.out.println("Longitude :     " + json.get("foreign_venue_lng"));
				System.out.println("Source :        " + json.get("foreign_venue_source"));
				System.out.println("___________________________________________________");
	}
	
	/**
	 * 
	 * This method is called to find the difference between 2 longitude and 2 latitude
	 * 
	 * Source: http://rosettacode.org/wiki/Haversine_formula
	 * 
	 * **/
	  
	    public double haversine(double lat1, double lon1, double lat2, double lon2) {
	        double dLat = Math.toRadians(lat2 - lat1);
	        double dLon = Math.toRadians(lon2 - lon1);
	        lat1 = Math.toRadians(lat1);
	        lat2 = Math.toRadians(lat2);
	 
	        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
	        double c = 2 * Math.asin(Math.sqrt(a));
	        
	        return 6372.8 * (c / 1.609344);
	    }
	    
	    /**
		 * 
		 * This method is called to display all the options to user
		 * 
		 * **/    
	    
	public void mainmenu()
	{
		System.out.println("Please Enter the number to select search options");
		System.out.println("1 . Search Venue by Name      ");
		System.out.println("2 . Search Venue by City      ");
		System.out.println("3 . Search Venue by Zipcode      ");
		System.out.println("4 . Search Venue within Radius by Longitute,Latitude      ");
		System.out.println("5 . Search by Name,City      ");
		System.out.println("6 . Search by Name,Zipcode      ");
		System.out.println("7 . Search within Radius by Longitute,Latitude  by Name  ");
		System.out.println("8 . Search by Rating,City      ");
		System.out.println("9 . Search by Rating,Zipcode      ");
		System.out.println("10 . Search within Radius by Longitute,Latitude by Rating     ");
		System.out.println("11 . Exit     ");
	}
	
		/**
		 * 
		 * This method to extact data from the database 
		 * 
		 * **/  
	
	
	public void searchdb(BasicDBObject query, DBCollection coll) throws ParseException
	{
		

		DBCursor cursor;
	cursor = coll.find(query);
	
	if (cursor.hasNext()) {
		while (cursor.hasNext()) {
			DBObject Obj = cursor.next();
			String Output = Obj.toString();
			Display(Output);
		}
	} else {
		System.out.println("No Data Found");
	}
}

}
