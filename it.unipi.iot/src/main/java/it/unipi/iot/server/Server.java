package it.unipi.iot.server;

import it.unipi.iot.resource.Registration;
import org.eclipse.californium.core.CaliforniumLogger;
import org.eclipse.californium.core.CoapServer;

public class Server extends CoapServer{
	
	public Server() {
		this.add(new Registration("registration"));
		this.start();
		CaliforniumLogger.disableLogging();
		System.out.println("---- SERVER STARTED ----");
	}


}
