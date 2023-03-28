package it.unipi.iot;

import it.unipi.iot.resource.RegisteredResource;
import it.unipi.iot.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MyClient {
	
	protected static Server server;
	protected static BufferedReader reader;

	//	static final int MIN_VARIATION = 1;
//	static final int MAX_VARIATION = 5;
	private static HashMap<String, RegisteredResource> sensorsList = new HashMap<String, RegisteredResource>();	//ID - bin

	public static void main(String[] args) {
		
		System.out.println("---- CLIENT STARTED ----");
		
		server = new Server();
		
		//new thread to let server running 
		new Thread() {
			public void run() {
				server.start();
			}
		}.start();
		
		/* User interface */
		


		System.out.println("Type \"!help\" to know the commands\n");
		try {
			reader = new BufferedReader(new InputStreamReader(System.in));
			boolean stop = true;
			while(stop) {
				System.out.println("Type a command\n");


				String command = reader.readLine();

				switch (command) {

					case "!help":
						showCommands();
						break;

					case "!getSensors":
						getSensors();
						break;

					case "!getAddressResources":
						getAllDeviceInformation();
						break;

					case "!getAvgBinFullness":
						getAvgCC();
						break;

					case "!removeDevicesAddress":
						removeDevicesAddress();
						break;

					case "!stop":
						stop = false;
						break;

					default:
						System.out.println("Command not defined\n");
						break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			for(String key: sensorsList.keySet()){
				sensorsList.remove(key);
			}

			System.out.println("Stopping the application...\n");
			server.stop();
			server.destroy();
			System.exit(0);
		}

		
	}


	private static void showCommands() {
		
		System.out.println("");
		System.out.println("	--	This is the list of accepted command	--	\n");
		System.out.println("!help 			-->	Get the list of the available commands\n");
		
		
		System.out.println("	--	GET COMMANDS	--	");
		
		System.out.println("!getSensors 		-->	Get the list of registered sensors");
		System.out.println("!getAddressResources	-->	Get the list of registered IDs with a given address");
		System.out.println("!getAvgBinFullness		-->	Get the Avg bin fullness of the last 10 measurements for all the sensors");

		System.out.println("");
		System.out.println("	--	POST COMMANDS	--	");
		System.out.println("!removeDevicesAddress	-->	Remove the devices with given address");
		
		System.out.println("");
		System.out.println("!stop			-->	Stop the application");

		System.out.println("");
		

	}
	
/*
 * 
 * 		GET METHODS
 * 
 */
	
	//Get the list of the sensors (area, id, address, resType)
	private static void getSensors() {
		
		if(sensorsList.isEmpty() ) {
			System.out.println("No Sensor Registered\n");
			return;
		}
	
		System.out.println(" 	--	Sensors List	--	");
		for(String key: sensorsList.keySet()){
			System.out.println("Sensor information: " + sensorsList.get(key).toString());
		}
		System.out.println("");
	}
	

	//For all the bin sensors, get the avg of the last 10 measurements
	private static void getAvgCC() {
		
		System.out.println("	--	Last Average Carbon Monoxide Detected	--	");
		sensorsList.entrySet().stream().forEach(
				(value)->{
					value.getValue().toString();
				}
		);//.average();
//		if(!avarage.isPresent()){
//			System.out.println("	--	Avarage value: "+avarage.getAsDouble()+"	--	");
//		} else {
//			System.out.println("	--	There are not enough data	--	");
//		}
//		System.out.println("");
	}
	
	//IT SHOWS ALL THE RESOURCES WITH A GIVEN ADDRESS
	private static void getAllDeviceInformation() {
		System.out.println("\nType the address of the devices");
	
		try {
			//GET THE ADDRESS
			String address = reader.readLine();
			RegisteredResource registeredResource = sensorsList.get(address);
			if(registeredResource == null) {
				System.out.println("Error! There is no device with the given ID.\n ");
				return;
			}
			
			registeredResource.toString();
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	//Remove devices with given address
	
	//REMOVE AND UNREGISTER DEVICES WITH GIVEN ADDRESS
	private static void removeDevicesAddress() {
		
		System.out.println("\nType the address of the devices");
	
		try {
			String address = reader.readLine();
			RegisteredResource registeredResource = sensorsList.get(address);

			if(registeredResource == null) {
				System.out.println("Error! There is no device with the given ID.\n ");
				return;
			}
			
			sensorsList.remove(address);
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public static void removeDevicesAddress(String address) {

		System.out.println("\nRemoving device with address "+address);
		RegisteredResource registeredResource = sensorsList.get(address);

		if(registeredResource == null) {
			System.out.println("Error! There is no device with the given ID.\n ");
			return;
		}

		sensorsList.remove(address);

	}

	public static void insertNewSensor(RegisteredResource registeredResource){
		sensorsList.put(registeredResource.getName(), registeredResource);
	}


}
