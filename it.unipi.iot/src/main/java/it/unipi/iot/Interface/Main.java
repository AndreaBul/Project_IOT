package it.unipi.iot.Interface;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import it.unipi.iot.*;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Main {
	static Server regServer = new Server(5683);
	
	public static void main(String[] args) {
		new Thread() {
			public void run() {
				regServer.start(); 
			}
		}.start();
		
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader commandLine = new BufferedReader(input);
		showCommand();
		while (true) {
			String str = "";
			try {
				str = commandLine.readLine();
			} catch(Exception e) {
				System.out.println(e.toString());
			}
			
			String[] command = str.split(" ");

			switch (command[0]) {
				case "list": //show the list of all the resources registered
					showResources();
					break;

			}
		}
	}
	
	public static void showCommand() {
		System.out.println("The available commands are the following:");
		System.out.println("list: to see the resources available");
		System.out.println("get node # sensor: to see the info registered by the sensor choosen");
		System.out.println("get all sensors: to see the info registered by all sensors");
		System.out.println("get node # actuator: to see the status of the actuator choosen");
		System.out.println("get all actuators: to see the status of all actuators");
		System.out.println("set node # *max_temp*: to set the max_temp of the actuator identified by index");
		System.out.println("set all *max_temp*: to set the max_temp of all actuators");
		System.out.println("historic node #: to see the historic of the latest sensing of the node");
		System.out.println("close: to close the application");
	}
	
	public static void showResources() {
		System.out.println("SHOWING THE RESOURCES: ");
		for (RegisteredResource resource : Server.regResources) {
			System.out.println(resource.getName() + " " + resource.getPath());
		}
	}
	


}
