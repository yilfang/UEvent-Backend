package net.eventhub.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EventHubConstants {
	
	
	public static int batch_size = 20;
	
	public static double COORDINATES_LIMIT = 0.00015;
	
	public static String IMAGE_APP;
	
	public static String FILE_UPLOAD_DIR;
	
	public static int STATUS_NO_RESPONSE_ID;
	
	public static int MAX_FILE_SIZE;

	@Value("${image_app}")
	public void setIMAGE_APP(String IMAGE_APP) {
		EventHubConstants.IMAGE_APP = IMAGE_APP;
	}

	@Value("${file_upload_dir}")
	public void setFILE_UPLOAD_DIR(String FILE_UPLOAD_DIR) {
		EventHubConstants.FILE_UPLOAD_DIR = FILE_UPLOAD_DIR;
	}

	@Value("${batch_size}")
	public void setBatch_size(int batch_size) {
		EventHubConstants.batch_size = batch_size;
	}

	@Value("${coordinates_limit}")
	public void setCOORDINATES_LIMIT(double COORDINATES_LIMIT) {
		EventHubConstants.COORDINATES_LIMIT = COORDINATES_LIMIT;
	}

	@Value("${invitation_status_no_response_id}")
	public void setSTATUS_NO_RESPONSE_ID(int sTATUS_NO_RESPONSE_ID) {
		STATUS_NO_RESPONSE_ID = sTATUS_NO_RESPONSE_ID;
	}

	@Value("${max_file_size}")
	public void setMAX_FILE_SIZE(int mAX_FILE_SIZE) {
		MAX_FILE_SIZE = mAX_FILE_SIZE;
	}	

}
