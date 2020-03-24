import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.sql.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import sqlTransfer.*;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;


public class SearchServlet extends HttpServlet {
  
	int imageCount = 0;
	public static ArrayList<String[]> photoGallery = new ArrayList<String[]>();
	DataTransfer DB = new DataTransfer();
	SearchUtility SU = new SearchUtility();
	
// Method to handle initial GET method request.
  public void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
		imageCount = 0;
    
		String title = "Photo Gallery";
	
	// Set response content type
      response.setContentType("text/html");

      PrintWriter out = response.getWriter();
      out.println(
			"<html>\n" +			
			"<body bgcolor=\"#d9d9d9\">\n" +
			"<ul>\n" +
			"<div align=\"center\" >\n" +
			"<form action=\"/midp/search\" method=\"POST\">\n" +
			"<h1> " + title + " </h1>\n" + 
			"<br />\n" +
			"<br />\n" +	
			
			"<b> Caption </b>" +
			"<br />\n" +
			"<input type=\"text\" name=\"caption\" placeholder=\"Caption\">\n"   +
			"<br />\n" +
			"<br />\n" +

			"<b> Time </b>" +
			"<br />\n" +
			"<input type=\"text\" name=\"startDate\" placeholder=\"Start Date\" /> <input type=\"text\" name=\"endDate\" placeholder=\"End Date\" />\n"   +
			"<br />\n" +
			"Format of time, yyyyMMdd_HHmmss" + 
			"<br />\n" +
			"<br />\n" +

			"<b> Location </b>" +
			"<br />\n" +
			"Latitude: <input type=\"text\" name=\"lat\" value=\"49\" /> \n"  +
			"<br />\n" +
			"Longitude: <input type=\"text\" name=\"lon\" value=\"120\" /> \n" +
			"<br />\n" +
			"Radius: <input type=\"text\" name=\"radius\" value=\"100000\" />\n" +
			"<br />\n" +
			"<br />\n" +				
			
			"<input type=\"submit\" value=\"Search Now\" />\n" +
			
			"<input type=\"button\" value=\"Upload from desktop\" onclick=\"location.href='http://localhost:8081/midp/select.html';\" />\n" +
			"</div>\n</form>\n" +
			"</form>\n</body>\n</html>");

		request.setAttribute("Poster", "Search");

  }
  
  
// Method to handle POST method request.
  public void doPost(HttpServletRequest request,
                     HttpServletResponse response)
      throws ServletException, IOException {
	
	String action = request.getParameter("action");
	
	if (action == null) {
		
		String caption = request.getParameter("caption");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		
		
		double searchDist = 100000;
		double lat = 49;
		double lon = 120;
		double[] searchLoc = new double[2];
		
		String latString = request.getParameter("lat");
		String lonString = request.getParameter("lon");
		String searchDistString = request.getParameter("radius");
	
		//Checking for any empty search fields that will ruin the search
		if(caption.equals(""))
			caption = null;
		
		if(startDate.equals(""))
			startDate = "00010101_000001"; //Earliest date
		
		if(endDate.equals(""))
			endDate = "22220101_000001"; //Real far away date
	
		try{
		if(!latString.equals(null)){
			lat = Double.parseDouble(latString);
		}

		if(!lonString.equals(null)){
			lon = Double.parseDouble(lonString);
		}
		if(!searchDistString.equals(null)){
			searchDist = Double.parseDouble(searchDistString);
		}
		}
		catch (Exception e) {}
		
		searchLoc[0] = lat;
		searchLoc[1] = lon;
		
		ArrayList<String[]> photoDetails = DB.ReadDB();
		photoGallery = SU.searchFunc(startDate, endDate, caption, searchDist, searchLoc, photoDetails);
		imageCount = 0;
	}
	
/*
	int Count;
	File file = new File("C:/COMP7855Project/tomcat/webapps/midp/Images");
	String[] imageList = file.list();
*/		

	
	PrintWriter out = response.getWriter();
	response.setContentType("text/html");
	String title = "Searched Photo Gallery";

	if ("Left".equals(action) && imageCount > 0) {
		imageCount = imageCount - 1;
	} else if ("Right".equals(action) && imageCount < (photoGallery.size() - 1)) {
		imageCount = imageCount + 1;
	}

	if(photoGallery.size() > 0){
	try{
			String[] photoData = photoGallery.get(imageCount);
			String docType =
			"<!doctype html public \"-//w3c//dtd html 4.0 " +
			"transitional//en\">\n";
			out.println(
				"<html>\n" +			
				"<body bgcolor=\"#d9d9d9\">\n" +
				"<ul>\n" +
				"<div align=\"center\" >\n" +
				"<form action=\"/midp/search\" method=\"POST\">\n" +
				"<h1> " + title + " </h1>\n" + 
				"<h2 align=\"center\">" + (imageCount + 1)  + "/" + photoGallery.size() + " Photos" + "</h2>\n" + 
				"<br />\n" +		
				"<br />\n" +
				"<input type=\"submit\" name=\"action\" value=\"Left\" />\n" +   //DownCount
				"<input type=\"button\" value=\"Search Again\" onclick=\"location.href='http://localhost:8081/midp/search';\" />\n" +
				"<input type=\"submit\" name=\"action\" value=\"Right\" />\n" +   //UpCount
				"<br />\n" +
				"<br />\n" +
				"<img id=\"myImg\" src=\"Images/" + photoData[0] + "\"" + "width=\"640\" height=\"480\">\n\n" + //photoData[0]
				"<b> " +  " </b>\n" + 
				"<br />\n" +		
				"Photo Caption: " + photoData[1] + "<br />\n" +
				"Photo Date (yyyyMMdd_HHmmss): " + photoData[2] + "<br />\n" +
				"Latitude: " + photoData[3] + "<br />\n" +
				"Longitude: " + photoData[4] +
				"</div>\n</form>\n" +
				"</form>\n</body>\n</html>");		

		}
		catch(Exception e){
			//response.sendRedirect("http://localhost:8081/midp/search");
		}
	}
	else{
		String docType =
			"<!doctype html public \"-//w3c//dtd html 4.0 " +
			"transitional//en\">\n";
			out.println(
				"<html>\n" +			
				"<body bgcolor=\"#d9d9d9\">\n" +
				"<ul>\n" +
				"<div align=\"center\" >\n" +
				"<form action=\"/midp/hits\" method=\"POST\">\n" +
				"<h1> " + title + " </h1>\n" + 
				"<h2 align=\"center\">" + "0"  + "/" + "0" + " Photos" + "</h2>\n" +
				"<br />\n" +		
				"<br />\n" +
				"<input type=\"button\" value=\"Search Again\" onclick=\"location.href='http://localhost:8081/midp/search';\" />\n" +
				"<br />\n" +
				"<br />\n" +
				"<b> " +  " </b>\n" + //photoGallery.get(imageCount)
				
				"</div>\n</form>\n" +
				"</form>\n</body>\n</html>");
	}
	}
}