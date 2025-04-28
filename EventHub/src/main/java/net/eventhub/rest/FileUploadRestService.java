package net.eventhub.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.springframework.stereotype.Component;

//@Component
//@Path("/upload")
//@Secured
public class FileUploadRestService {
	
	// http://localhost:8080/EventHub/rest/upload/images
	/*
	 * @POST
	 * 
	 * @Path("images")
	 * 
	 * @Consumes({MediaType.MULTIPART_FORM_DATA})
	 * 
	 * @Produces(MediaType.APPLICATION_JSON)
	 */
	public String uploadPdfFile(  @FormParam("file") InputStream fileInputStream,
	                                @FormParam("file") FormDataContentDisposition fileMetaData) throws Exception
	{
	    String UPLOAD_PATH = "c:/Test/";
	    try
	    {
	        int read = 0;
	        byte[] bytes = new byte[1024];
	 
	        OutputStream out = new FileOutputStream(new File(UPLOAD_PATH + fileMetaData.getFileName()));
	        while ((read = fileInputStream.read(bytes)) != -1) 
	        {
	            out.write(bytes, 0, read);
	        }
	        out.flush();
	        out.close();
	    } catch (IOException e) 
	    {
	        throw new WebApplicationException("Error while uploading file. Please try again !!");
	    }
	    return "File uploaded successfully !!";//Response.ok("File uploaded successfully !!").build();
	}

}
