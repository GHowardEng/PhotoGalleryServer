import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.sql.*;
import java.io.*;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import sqlTransfer.DataTransfer;

public class HitServlet extends HttpServlet {
  private int mCount;
  private String imDir = "C:/COMP7855Project/tomcat/webapps/midp/Images";
  
  public void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
    // Set response content type


      response.setContentType("text/html");

      PrintWriter out = response.getWriter();
      out.println("<html>\n" +
                "<body>\n" + 
                "<form action=\"/midp/search\" method=\"GET\">\n" +
                "First Name: <input type=\"text\" name=\"first_name\" value=\"Grant\" />\n"   +
                "<br />\n" +
                "Last Name: <input type=\"text\" name=\"last_name\" value=\"Howard\" />\n"   +
                "<input type=\"submit\" value=\"Submit\" />\n"
                + 
                "</form>\n</body>\n</html\n");


  }
// Method to handle POST method request.
  public void doPost(HttpServletRequest request,
                     HttpServletResponse response)
      throws ServletException, IOException {

	DataTransfer DB = new DataTransfer();
	
    PrintWriter out = response.getWriter();
	/// HANDLE IMAGE HERE
    response.setContentType("text");

	String dataString = request.getParameter("data");
	String encodedImage = request.getParameter("image");
	String imName = request.getParameter("name");
	
	//BufferedImage = decodeToImage(encodedImage);
	
	String[] contents = dataString.split("_");
	String date = "";
	String caption = "";
	String latitude = "";
	String longitude = "";
	
	if (contents != null){
		date = (contents[0] + "_" + contents[1]);
		caption = contents[2];
		latitude = contents[3];
		longitude = contents[4];
	}
	File imageFile = new File((imDir+File.separator+imName));
	ByteArrayInputStream imStream = new ByteArrayInputStream(Base64.decodeBase64(encodedImage.getBytes()));
	
	BufferedImage image = null;
	
	image = ImageIO.read(imStream);
	imStream.close();

	// write the image to a file
	ImageIO.write(image, "jpeg", imageFile);
         
	DB.WriteDB(imName, caption, date, latitude, longitude);
		
    out.write("Received Post. File Name: " + imName + ", File Date: " + contents[0] + "_" + contents[1]);
  }
}



