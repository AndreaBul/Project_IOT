package it.unipi.iot;

import java.util.ArrayList;

import org.eclipse.californium.core.CaliforniumLogger;
import org.eclipse.californium.core.CoapServer;

public class Server extends CoapServer {
	public Server() {
		this.add(new Registrant("registration"));
		this.start();
		CaliforniumLogger.disableLogging();
		System.out.println("---- SERVER STARTED ----");
	}
}
