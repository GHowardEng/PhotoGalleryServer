import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import sqlTransfer.DataTransfer;

//@WebServlet("/uploads")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ServletFileUpload uploader = null;
	private String imDir = "C:/COMP7855Project/tomcat/webapps/midp/Images";
	String date = "";
	String caption = "";
	String latitude = "";
	String longitude = "";
	String fileName = "";
	DataTransfer DB = new DataTransfer();;
	
	@Override
	public void init() throws ServletException{
		DiskFileItemFactory fileFactory = new DiskFileItemFactory();
		File filesDir = new File(imDir);
		fileFactory.setRepository(filesDir);
		this.uploader = new ServletFileUpload(fileFactory);
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileName = request.getParameter("fileName");
		if(fileName == null || fileName.equals("")){
			throw new ServletException("File Name can't be null or empty");
		}
		File file = new File(request.getServletContext().getAttribute("FILES_DIR")+File.separator+fileName);
		if(!file.exists()){
			throw new ServletException("File doesn't exists on server.");
		}
		System.out.println("File location on server::"+file.getAbsolutePath());
		ServletContext ctx = getServletContext();
		InputStream fis = new FileInputStream(file);
		String mimeType = ctx.getMimeType(file.getAbsolutePath());
		response.setContentType(mimeType != null? mimeType:"application/octet-stream");
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		
		ServletOutputStream os = response.getOutputStream();
		byte[] bufferData = new byte[1024];
		int read=0;
		while((read = fis.read(bufferData))!= -1){
			os.write(bufferData, 0, read);
		}
		os.flush();
		os.close();
		fis.close();
		System.out.println("File downloaded at client successfully");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*if(!ServletFileUpload.isMultipartContent(request)){
			throw new ServletException("Content type is not multipart/form-data");
		}*/
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.write("<html><head></head><body>");
		

		try {
			List<FileItem> fileItemsList = uploader.parseRequest(request);
			Iterator<FileItem> fileItemsIterator = fileItemsList.iterator();
			System.out.println(request.getParameter("source"));

			while(fileItemsIterator.hasNext()){
				FileItem fileItem = fileItemsIterator.next();
				
				System.out.println("FieldName="+fileItem.getFieldName());
				System.out.println("FileName="+fileItem.getName());
				System.out.println("ContentType="+fileItem.getContentType());
				System.out.println("Size in bytes="+fileItem.getSize());
				
				if( fileItem.isFormField()){
					System.out.println("FormField");
					
					String fieldname = fileItem.getFieldName();
					String fieldvalue = fileItem.getString();
					System.out.println(fieldname+"\n " +fieldvalue);
					if(fieldname.equals("date")){
						date = fieldvalue + "_000000";
					}
					else if(fieldname.equals("caption")){
						caption = fieldvalue;
					}
					else if(fieldname.equals("lat")){
						latitude = fieldvalue;
					}
					else if(fieldname.equals("long")){
						longitude = fieldvalue;
					}
				}

				else{
				File file = new File(imDir+File.separator+fileItem.getName());
				System.out.println("Absolute Path at server="+file.getAbsolutePath());
				fileItem.write(file);
				fileName = fileItem.getName();
				
				}
			}
			out.write("File "+fileName+ " uploaded successfully.");
			out.write(
			"<html>\n" +
			"<br />\n" +
			"<br />\n" +
			"  <li><b>Date</b>: "
			+ date + "\n" +
			"  <li><b>Caption</b>: "
			+ caption + "\n" +	
			"  <li><b>Latitude</b>: "
			+ latitude + "\n" +
			"  <li><b>Longitude</b>: "
			+ longitude+ "\n" +
			"<br />\n" +
			"<br />\n" +
			"<input type=\"button\" value=\"Back to Search\" onclick=\"location.href='http://localhost:8081/midp/search';\" />\n" +
			"</div>\n</form>\n" +
			"</form>\n</body>\n</html>");

		//Upload to database
		DB.WriteDB(fileName, caption, date, latitude, longitude);
			
		} catch (FileUploadException e) {
			out.write("Exception in uploading file." + e);
		} catch (Exception e) {
			out.write("Exception in uploading file." + e);
		}
		out.write("</body></html>");
	}

}