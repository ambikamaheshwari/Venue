package venue;

import java.io.BufferedReader;
import java.util.regex.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;

public class VenueMain {

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws ParseException, IOException, NumberFormatException {
		VenueData vd = new VenueData();
		VenueMain vm = new VenueMain();
		
		try{
	/*  
	 * Connecting to the Mongoclient with localhost and default port 27017
	 * 
	 * */	
		@SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		@SuppressWarnings("deprecation")
		DB db = mongoClient.getDB("urbanft");
		System.out.println("Connected to database successfully");
		DBCollection coll = db.getCollection("venue_data");

		
		BasicDBObject query;
		DBCursor cursor;
		int zipcode;
		double uslat, uslng, dblat, dblng, radius, dist, rating;
		String city, name, rate;
		List<BasicDBObject> obj;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String con;
		
		
		String zip ;
		
	/* 
	 * For Input Validation zipPattern is for checking Areacode it should be of 5 optionally of length 4 #For US Only
	 * \\p{L} is a Unicode Character Property that matches any kind of letter from any language
	 * */	
	
		final String zipPattern = "^[0-9]{4,5}$";
		final String namePattern = "^[\\p{L} .'-]+$";
		final String cityPattern = "^[a-zA-Z ]*$";
/*
 * Cases for all the output options
 * 
 * */	
		
		do {
			//Display Menu with 10 option
			vd.mainmenu();

			@SuppressWarnings("resource")
			Scanner reader = new Scanner(System.in);
			System.out.println("Option:");
			int option = reader.nextInt();

			switch (option) {
			
			/* 
			 * Search venue by name
			 * */
			case 1:
				
				System.out.println("1 . Enter the Name to search Venue by Name: ");
				name = br.readLine();
				
				/*
				 * Input Validation
				 * 
				 * */
				if (!(Pattern.matches(namePattern, name))) 
				{
					System.out.println("Improper Name format.");
		 			break;
				}
				query = new BasicDBObject("foreign_venue_name", name);
			
				
				vd.searchdb(query,coll);
			
				break;
				
/* 
* Search venue by city
* */	
			case 2:
				
				System.out.println("2 . Enter the City to search Venue by City: ");
				city = br.readLine();
				
				/*
				 * Input Validation
				 * 
				 * */
				if (!(Pattern.matches(cityPattern, city))) 
				{
					System.out.println("Improper City format.");
		 			break;
				}
				
				query = new BasicDBObject("foreign_venue_city", city);
				
				vd.searchdb(query,coll);
				
				break;
				
/* 
 * Search venue by Areacode/Zipcode
* */
				
			case 3:
				
				System.out.println("3 . Enter the Areacode to search Venue by Areacode/Zipcode: ");
				zip = br.readLine();
				/*
				 * Input Validation
				 * 
				 * */	
				if (!(Pattern.matches(zipPattern, zip))) 
				{
					System.out.println("Improper Zipcode format.");
		 			break;
				}
				zipcode =Integer.parseInt(zip);
				
				
				query = new BasicDBObject("foreign_venue_zipcode", zipcode);
				vd.searchdb(query,coll);				
				break;
				
/* 
* Search venue by Latitude,longitute,Radius
* */
			case 4:
				System.out.println("Search Venue by Entering the Latitute, Longitute and Radius : ");
				System.out.println("Enter the Latitute: ");
				uslat = Double.parseDouble(br.readLine());
				System.out.println("Enter the Longitute: ");
				uslng = Double.parseDouble(br.readLine());
				System.out.println("Enter the Radius(in miles) to find venue: ");
				radius = Double.parseDouble(br.readLine());
				
				query = new BasicDBObject();
				db.getCollection("venue_data").find();
				cursor = coll.find(query);
				
				if (cursor.hasNext()) {
					while (cursor.hasNext()) {
						DBObject Obj = cursor.next();
						String Output = Obj.toString();
						JSONObject json = (JSONObject) new JSONParser().parse(Output);
						dblat = (double) json.get("foreign_venue_lat");
						dblng = (double) json.get("foreign_venue_lng");
						dist = vd.haversine(uslat, uslng, dblat, dblng);
						if (dist <= radius) {
							vd.Display(Output);
						}

					}
				} else {
					System.out.println("No Data Found");

				}
				break;
				
/* 
* Search venue by Name and City
* */
			case 5:
				
				System.out.println("Search Venue by Name and City: ");
				System.out.println("Enter the Name");
				name = br.readLine();
				System.out.println("Enter the City");
				city = br.readLine();
				
				if (!(Pattern.matches(namePattern, name))) 
				{
					System.out.println("Improper Name format.");
		 			break;
				}
				if (!(Pattern.matches(cityPattern, city))) 
				{
					System.out.println("Improper City format.");
		 			break;
				}
				query = new BasicDBObject();
				obj = new ArrayList<BasicDBObject>();
				obj.add(new BasicDBObject("foreign_venue_name", name));
				obj.add(new BasicDBObject("foreign_venue_city", city));
				query.put("$and", obj);

				vd.searchdb(query,coll);
				
				break;
/* 
* Search venue by Name and Areacode/Zipcode
* */	
			case 6:

				System.out.println("Search Venue by Name and Areacode/Zipcode: ");
				System.out.println("Enter the Name");
				name = br.readLine();
				System.out.println("Enter the Areacode/Zipcode");
				zip =br.readLine();
				if (!(Pattern.matches(namePattern, name))) 
				{
					System.out.println("Improper Name format.");
		 			break;
				}
				if (!(Pattern.matches(zipPattern, zip))) 
				{
					System.out.println("Improper Zipcode format.");
		 			break;
				}
				zipcode =Integer.parseInt(zip);

				query = new BasicDBObject();
				obj = new ArrayList<BasicDBObject>();
				obj.add(new BasicDBObject("foreign_venue_name", name));
				obj.add(new BasicDBObject("foreign_venue_zipcode", zipcode));
				query.put("$and", obj);

				vd.searchdb(query,coll);
				break;

/* 
* Search venue by Latitude, Longitude, Radius,Name
* */				
			case 7:
				System.out.println("Search Venue by Name within Latitude, Longitude, Radius: ");
				System.out.println("Enter the Latitute: ");
				uslat = Double.parseDouble(br.readLine());
				System.out.println("Enter the Longitute: ");
				uslng = Double.parseDouble(br.readLine());
				System.out.println("Enter the Radius(in miles) to find venue: ");
				radius = Double.parseDouble(br.readLine());
				System.out.println("Enter the Name");
				name = br.readLine();
				
				if (!(Pattern.matches(namePattern, name))) 
				{
					System.out.println("Improper Name format.");
		 			break;
				}
				
				query = new BasicDBObject();
				db.getCollection("venue_data").find();

				cursor = coll.find(query);
				if (cursor.hasNext()) {
					while (cursor.hasNext()) {
						DBObject Obj = cursor.next();
						String Output = Obj.toString();
						JSONObject json = (JSONObject) new JSONParser().parse(Output);
						dblat = (double) json.get("foreign_venue_lat");
						dblng = (double) json.get("foreign_venue_lng");
						String venuename = (String) json.get("foreign_venue_name");

						dist = vd.haversine(uslat, uslng, dblat, dblng);

						if (dist <= radius) {
							if (venuename.equals(name))
								vd.Display(Output);
						}

					}
				} else {
					System.out.println("No Data Found");

				}
				break;
				
/*
 * Search venue by Rating and City
 * 				
 */
			case 8:
				System.out.println("8. Search venue by Rating and City ");
				System.out.println("Enter the City: ");
				city = br.readLine();
				System.out.println("Enter rating : ");
				rate = br.readLine();
				
				if (!(Pattern.matches(cityPattern, city))) 
				{
					System.out.println("Improper City format.");
		 			break;
				}
				
				query = new BasicDBObject();
				obj = new ArrayList<BasicDBObject>();
				if (rate.contains("null")) {
					rate = "0";
				}
				rating = Double.parseDouble(rate);
				
				if(rating >= 1 && rating <2)
				{
					obj.add(new BasicDBObject("foreign_venue_rating",new BasicDBObject("$gte", rating).append("$lt", 2)));
					obj.add(new BasicDBObject("foreign_venue_city", city));
					query.put("$and", obj);

					db.getCollection("venue_data").find();
									
					vd.searchdb(query,coll);
					}
				
				else if(rating >= 2 && rating <3)
				{
					obj.add(new BasicDBObject("foreign_venue_rating",new BasicDBObject("$gte", rating).append("$lt", 3)));
					obj.add(new BasicDBObject("foreign_venue_city", city));
					query.put("$and", obj);

					db.getCollection("venue_data").find();
										
					vd.searchdb(query,coll);
					}
				
				else if (rating >= 3 && rating <5)
				{			
				obj.add(new BasicDBObject("foreign_venue_rating",new BasicDBObject("$gte", rating).append("$lt", 5)));
				obj.add(new BasicDBObject("foreign_venue_city", city));
				query.put("$and", obj);

				cursor = coll.find(query);
				vd.searchdb(query,coll);
				}
				break;
				
/* 
 * 	Search venue by rating and area code
 * 
 * */								 
				
			case 9:
				System.out.println("9. Search venue by rating and area code ");
				System.out.println("Enter rating : ");
				rate = br.readLine();
				System.out.println("Enter the Areacode/Zipcode");
				zip = br.readLine();
				if (!(Pattern.matches(zipPattern, zip))) 
				{
					System.out.println("Improper Name format.");
		 			break;
				}
				zipcode =Integer.parseInt(zip);
				
				query = new BasicDBObject();
				obj = new ArrayList<BasicDBObject>();
				if (rate.contains("null")) {
					rate = "0";
				}
				rating = Double.parseDouble(rate);
				
				if(rating >= 1 && rating <2)
				{
					obj.add(new BasicDBObject("foreign_venue_rating",new BasicDBObject("$gte", rating).append("$lt", 2)));
					obj.add(new BasicDBObject("foreign_venue_zipcode", zipcode));
					query.put("$and", obj);

					db.getCollection("venue_data").find();
									
					vd.searchdb(query,coll);
					}
				
				else if(rating >= 2 && rating <3)
				{
					obj.add(new BasicDBObject("foreign_venue_rating",new BasicDBObject("$gte", rating).append("$lt", 3)));
					obj.add(new BasicDBObject("foreign_venue_zipcode", zipcode));
					query.put("$and", obj);

					db.getCollection("venue_data").find();
										
					vd.searchdb(query,coll);
					
				}
				
				else if (rating >= 3 && rating <5)
				{			
				obj.add(new BasicDBObject("foreign_venue_rating",new BasicDBObject("$gte", rating).append("$lt", 5)));
				obj.add(new BasicDBObject("foreign_venue_zipcode", zipcode));
				query.put("$and", obj);

				
				vd.searchdb(query,coll);
				}
				
				break;
/*
 * 10.	Search venue by rating, latitude,longitude,Radius
 * */				
				
			case 10:
				System.out.println("Enter rating : ");
				rating = Double.parseDouble(br.readLine());
				System.out.println("Enter the Latitute: ");
				uslat = Double.parseDouble(br.readLine());
				System.out.println("Enter the Longitute: ");
				uslng = Double.parseDouble(br.readLine());
				System.out.println("Enter the Radius(in miles) to find venue: ");
				radius = Double.parseDouble(br.readLine());
				
				query = new BasicDBObject();
				db.getCollection("venue_data").find();
				
				obj = new ArrayList<BasicDBObject>();
				
				if(rating >= 1 && rating <2)
				{
				obj.add(new BasicDBObject("foreign_venue_rating",new BasicDBObject("$gte", rating).append("$lt", 5)));
				obj.add(new BasicDBObject());
				query.put("$and", obj);
					
				cursor = coll.find(query);
				
				
				while (cursor.hasNext()) {
					DBObject Obj = cursor.next();
					String Output = Obj.toString();
					JSONObject json = (JSONObject) new JSONParser().parse(Output);
					dblat = (double) json.get("foreign_venue_lat");
					dblng = (double) json.get("foreign_venue_lng");
					
					dist = vd.haversine(uslat, uslng, dblat, dblng);
					
					if (dist <= radius) {
						vd.Display(Output);
					}}
				}
				
				else if(rating >= 2 && rating <3)
				{
				obj.add(new BasicDBObject("foreign_venue_rating",new BasicDBObject("$gte",rating).append("$lt", 5)));
				obj.add(new BasicDBObject());
				query.put("$and", obj);
					
				cursor = coll.find(query);
				
				
				while (cursor.hasNext()) {
					DBObject Obj = cursor.next();
					String Output = Obj.toString();
					JSONObject json = (JSONObject) new JSONParser().parse(Output);
					dblat = (double) json.get("foreign_venue_lat");
					dblng = (double) json.get("foreign_venue_lng");
					
					dist = vd.haversine(uslat, uslng, dblat, dblng);
					
					if (dist <= radius) {
						vd.Display(Output);
					}}
				}
				if(rating >= 3 && rating <5)
				{
				obj.add(new BasicDBObject("foreign_venue_rating",new BasicDBObject("$gte", rating).append("$lt", 5)));
				obj.add(new BasicDBObject());
				query.put("$and", obj);
					
				cursor = coll.find(query);
				
				
				while (cursor.hasNext()) {
					DBObject Obj = cursor.next();
					String Output = Obj.toString();
					JSONObject json = (JSONObject) new JSONParser().parse(Output);
					dblat = (double) json.get("foreign_venue_lat");
					dblng = (double) json.get("foreign_venue_lng");
					
					dist = vd.haversine(uslat, uslng, dblat, dblng);
					
					if (dist <= radius) {
						vd.Display(Output);
					}}
				}
				break;
			
			case 11:
				System.out.println("Good Bye !");
				System.exit(0);
				
			default:
				break;
			}

			System.out.println("\nDo you want to continue (YES/NO)?");
			con = br.readLine().trim();
		}

		while (con.equalsIgnoreCase("YES"));
		System.out.println("Good Bye !");
		}
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
			e.getSuppressed();
			vm.main(args);
		}
}	
}