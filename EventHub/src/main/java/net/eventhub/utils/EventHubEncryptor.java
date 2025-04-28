package net.eventhub.utils;

//import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

public class EventHubEncryptor extends SimpleStringPBEConfig{

	@Override
	public void setAlgorithm(String algorithm) {
		// TODO Auto-generated method stub
		super.setAlgorithm("PBEWithMD5AndDES");
	}

	@Override
	public void setPassword(String password) {
		// TODO Auto-generated method stub
		super.setPassword("eventhubum");
	}

		
	

}
