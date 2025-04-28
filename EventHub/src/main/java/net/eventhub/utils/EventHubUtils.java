package net.eventhub.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.Part;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;

public class EventHubUtils {
	
	private static final SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public static String getCurrentTimeString(SimpleDateFormat format) 
	{
		Timestamp now = new Timestamp(System.currentTimeMillis());
		String current = format.format(now);
		
		return current;
	}
	
	public static String getCurrentTimeString() 
	{
		return getCurrentTimeString(EventHubUtils.defaultFormat);
	}

	public static String getDateString(Date date, SimpleDateFormat format) 
	{
		String dateStr = format.format(date);
		
		return dateStr;
	}
	
	public static String getDateString(Date date) 
	{
		String dateStr = getDateString(date, EventHubUtils.defaultFormat);
		
		return dateStr;
	}
	
	public static Date converStingToDate(String dateStr, SimpleDateFormat format)
	{
		Date date = null;
		if ( StringUtils.hasText(dateStr))
		{
			try {
				date = format.parse(dateStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return date;
	}
	
	public static Date converStingToDate(String dateStr)
	{	
		return converStingToDate(dateStr, EventHubUtils.defaultFormat);
	}
	
	public static Response emptyReturn()
    {
    	return Response.serverError().entity(new ArrayList()).build();
    }
    
	
	public static Response errorReturn()
    {
    	return Response.serverError().entity("Fail").build();
    }
    
    public static Response validationError()
    {
    	return Response.serverError().entity("Validation Error").build();
    }
    
    public static void writeImageToDisk(String imageData, String fileName, int eventId)
	{
		byte[] data = DatatypeConverter.parseBase64Binary(imageData);
		StringBuilder dir = new StringBuilder();
	    dir.append(EventHubConstants.FILE_UPLOAD_DIR)
	    	.append(File.separator)
	    	.append(eventId);
	    
	    	
	    Path path = Paths.get(dir.toString());
	    try {
			if ( Files.exists(path))
			{
				FileUtils.cleanDirectory(new File(dir.toString()));
				
			}
			else
			{
				Files.createDirectory(path);
			}
			Files.write(Paths.get(dir + File.separator + fileName), data);
			/*
			 * int index = fileName.lastIndexOf("."); String format =
			 * fileName.substring(index + 1); reduceImage(data, dir + File.separator +
			 * fileName, format);
			 */
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
        
        
	}
    
        
    /*
    * This procedure will replace the original image
    * So you need to do a tmp copy to send before reduce
    */
    public static void reduceImage(byte[] myBytes, String dir, String format) throws IOException {
    	//File input = new File("/tmp/duke.jpg");
    	InputStream myInputStream = new ByteArrayInputStream(myBytes); 
    	float inputFileSize = myBytes.length;
        BufferedImage image = ImageIO.read(myInputStream);

        File output = new File(dir);
        OutputStream out = new FileOutputStream(output);

        ImageWriter writer =  ImageIO.getImageWritersByFormatName(format).next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(out);
        writer.setOutput(ios);
        ImageWriteParam param = writer.getDefaultWriteParam();
        
        if ( inputFileSize > EventHubConstants.MAX_FILE_SIZE )
        {
        	float ratio = EventHubConstants.MAX_FILE_SIZE/inputFileSize;
        	
        	if (param.canWriteCompressed()){
        		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        		param.setCompressionQuality(ratio);
        	}
        }
        
		writer.write(null, new IIOImage(image, null, null), param);
		out.close();
		ios.close();
		
		writer.dispose();
		

       
        
    }
}
