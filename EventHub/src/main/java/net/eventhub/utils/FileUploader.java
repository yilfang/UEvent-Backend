package net.eventhub.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import net.eventhub.service.EventBusiness;

import javax.servlet.annotation.*;
/* The Java file upload Servlet example */

@WebServlet(name = "FileUploader", urlPatterns = { "/fileuploader" })
@MultipartConfig(fileSizeThreshold=1024*1024,maxFileSize=1024*1024*5)
@Configurable
public class FileUploader extends HttpServlet {
	
	@Autowired
	private EventBusiness eventBuiness;  
	
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    /* Receive file uploaded to the Servlet from the HTML5 form */
    Part filePart = request.getPart("file");
    String eventId = request.getParameter("eventId");
    
	/*
	 * System.out.println("name " + filePart.getName()); FileInputStream is=
	 * (FileInputStream)filePart.getInputStream();
	 * 
	 * System.out.println("input stream" + );
	 */    
    StringBuilder dir = new StringBuilder();
    dir.append(EventHubConstants.FILE_UPLOAD_DIR)
    	.append(File.separator)
    	.append(eventId);
    	
    Path path = Paths.get(dir.toString());
    if ( Files.exists(path))
    {
    	FileUtils.cleanDirectory(new File(dir.toString()));
    }
    else
    {
    	Files.createDirectory(path);
    }
       
    String fileName = filePart.getSubmittedFileName();
    
    for (Part part : request.getParts()) {
      part.write(path + File.separator + fileName);
    }
    
    
    
    eventBuiness.insertImage(Integer.parseInt(eventId), fileName);
    response.getWriter().print("The file uploaded sucessfully.");
  }
  
  	@Override
	public void init(ServletConfig config) throws ServletException{
	  super.init(config);
	  SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	  
	}
  

  public EventBusiness getEventBuiness() {
	return eventBuiness;
  }

  public void setEventBuiness(EventBusiness eventBuiness) {
	this.eventBuiness = eventBuiness;
  }
  
  

}
