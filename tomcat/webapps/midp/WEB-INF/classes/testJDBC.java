import java.sql.*;
import java.io.*;
import java.util.*;
import sqlTransfer.*;

public class testJDBC {
	
	
  public static void main(String args[]) {
	DataTransfer DB = new DataTransfer();
	SearchUtility SU = new SearchUtility();
	
	String imageName = "WIN_20190124_11_24_07_Pro.jpg";
	String caption = "Caption2";
	String date = "20000101";
	//String lat = "49";
	//String lon = "120";

try {
	ArrayList<String[]> photoDetails = DB.ReadDB();

	double[] searchLoc = {49,120};
	double searchDist = 10000000;
	ArrayList<String[]> photoGallery = SU.searchFunc("00010101_000001", "22220101_000001", null, searchDist, searchLoc, photoDetails);
	
	String[] photo1 = photoGallery.get(0);
	for (int i = 0; i<5;i++)
	{
		System.out.println("Picture Results:" + photo1[i]);
	}
	String[] photo2 = photoGallery.get(1);
	System.out.println("Photogallery Size = " + photoGallery.size());
	}
catch(Exception ex) {
System.out.println("Failed: " + ex);}

/*	  Connection con = null;
  try {
		con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "Austin");
		con.setAutoCommit(false);
		
		//using Transactions
        PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO photos (name, caption, imDate, latitude, longitude) VALUES (?,?,?,?,?)");

		String imageName = "WIN_20190124_11_24_07_Pro.jpg";
		String caption = "Caption2";
		String date = "20000101";
		String lat = "49";
		String lon = "120";
		

        preparedStatement.setString(1, imageName);
        preparedStatement.setString(2, caption);
		preparedStatement.setString(3, date);
		preparedStatement.setString(4, lat);
		preparedStatement.setString(5, lon);
        int row = preparedStatement.executeUpdate();
		
		con.commit();
		con.close();
		System.out.println("Run cleanly");
		}
		
		catch(SQLException ex) {
			try {
				con.rollback();
                                con.close();
			} catch (SQLException e) {
				System.out.println("\nError Rolling back\n");	
			} 
			System.out.println("\n--- SQLException caught ---\n"); 
			while (ex != null) { 
				System.out.println("Message: " + ex.getMessage ()); 
				System.out.println("SQLState: " + ex.getSQLState ()); 
				System.out.println("ErrorCode: " + ex.getErrorCode ()); 
				ex = ex.getNextException(); 
				System.out.println("");
			} 
		}
*/	}
}