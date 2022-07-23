package it.unipi.iot;

import it.unipi.iot.resource_devices.Area;
import it.unipi.iot.server.ResourceDeviceHandler;
import it.unipi.iot.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyClient {
	
	protected static Server server;
	protected static BufferedReader reader;
	protected static ResourceDeviceHandler handler;
	
	public static void main(String[] args) {
		
		System.out.println("---- CLIENT STARTED ----");
		
		server = new Server();
		
		//new thread to let server running 
		new Thread() {
			public void run() {
				server.start();
			}
		}.start();
		
		// Take ResourceDeviceHandler instance
		handler = ResourceDeviceHandler.getInstance();
		
		/* User interface */
		
		reader = new BufferedReader(new InputStreamReader(System.in));
		

		System.out.println("Type \"!help\" to know the commands\n");
				
		while(true) {
			System.out.println("Type a command\n");
			
			try {
				
				String command = reader.readLine();
				
				switch(command) {
					
					case "!help":
						showCommands();
						break;
						
					case "!getSensors":
						getSensors();
						break;
						

					case "!getAddressResources":
						getAddressResources();
						break;
						
					case "!getAreasList":
						getAreasList();
						break;
						
					case "!getAreasInfo":
						showAreasInfo();
						break;

					
					case "!getAvgBinFullness":
						getAvgBinFullness();
						break;
						

					case "!setDeviceArea":
						setDeviceArea();
						break;
						
					case "!removeDeviceArea":
						removeDeviceArea();
						break;
						
					case "!switchAreaMode":
						switchAreaMode();
						break;
						
					case "!editAreaThreshold":
						editAreaThreshold();
						break;
						
					case "!removeDevicesAddress":
						removeDevicesAddress();
						break;
						
					case "!stop":
						stop();
						break;
						
					default:
						System.out.println("Command not defined\n");
						break;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		
		
		
	}


	private static void showCommands() {
		
		System.out.println("");
		System.out.println("	--	This is the list of accepted command	--	\n");
		System.out.println("!help 			-->	Get the list of the available commands\n");
		
		
		System.out.println("	--	GET COMMANDS	--	");
		
		System.out.println("!getSensors 		-->	Get the list of registered sensors");
		System.out.println("!getAddressResources	-->	Get the list of registered IDs with a given address");
		System.out.println("!getAreasInfo		-->	Get the list of areas and their info");
		System.out.println("!getAreasList		-->	Get the list of areas and their devices");
		System.out.println("!getAvgBinFullness		-->	Get the Avg bin fullness of the last 10 measurements for all the sensors");

		System.out.println("");
		System.out.println("	--	POST COMMANDS	--	");
		System.out.println("!setDeviceArea		-->	Set the area the device belongs to");
		System.out.println("!removeDeviceArea	-->	Remove the area the device belongs to");
		System.out.println("!switchAreaMode		-->	Set the management mode of an area");
		System.out.println("!editAreaThreshold	-->	Modify the min/max thresholds for bin fullness");
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
		
		if(handler.getBinSensors().isEmpty() ) {
			System.out.println("No Sensor Registered\n");
			return;
		}
	
		System.out.println(" 	--	Sensors List	--	");
		handler.binSensorList();
		System.out.println("");
	}
	

	//For all the bin sensors, get the avg of the last 10 measurements
	private static void getAvgBinFullness() {
		
		System.out.println("	--	Last Average Bin fullness Detected	--	");
		handler.getSensorsBinFullness();
		System.out.println("");
	}


	
	//Get the list of the areas with their devices
	private static void getAreasList() {
		System.out.println("	--	Get Areas Info	--	");
		handler.getAreasList();
		System.out.println("");
	}
	
	//IT SHOWS THE INFO OF AN AREA (THRESHOLDS, MANAGEMENT MODE)
	private static boolean showAreasInfo() {
		System.out.println("Available areas: ");
		for(String areaId: handler.getIdArea().keySet()) {
			System.out.print("[ ");
			handler.getIdArea().get(areaId).printAreaInfo();
			System.out.println(" ]");
		}
		System.out.println("");
		
		if(handler.getIdArea().keySet().size() == 0)
			return false;
		return true;
		
	}
	
	//IT SHOWS ALL THE RESOURCES WITH A GIVEN ADDRESS
	private static void getAddressResources() {
		
		System.out.println("Available Devices: ");
		handler.getAddressesList();;
		
		System.out.println("\nType the address of the devices");
	
		try {
			//GET THE ADDRESS
			String address = reader.readLine();
			
			if(!handler.getAddressIDs().containsKey(address)) {
				System.out.println("Error! This is not a device address.\n ");
				return;
			}
			
			handler.getAddressIDs(address);
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
/*
 * 
 * 		SET METHODS
 * 
 */
	


	//Set the area a device belongs to
	private static void setDeviceArea() {
		
		System.out.println("Available Devices: ");
		handler.devicesList();
		
		System.out.println("\nType the ID of the device");
		
		//Get the device ID and check the validity
		try {
			String id = reader.readLine();
			
			int deviceID = 0;
			try{
				deviceID = Integer.parseInt(id);
			} catch(NumberFormatException e) {
				System.out.println("The input must be a number\n");
				return;
			}
		        
		    
			if(!handler.getDevice(deviceID)) {
				System.out.println("Error! This is not a device id.\n");
				return;
			}
			
			System.out.println("Type the area");
		
			//Get area ID
			String area = reader.readLine().toLowerCase();
			handler.addDeviceArea(deviceID, area);
			
		} catch (IOException e) {
			e.printStackTrace();
		} 

		
	}
	
	//REMOVE DEVICE FROM GIVEN AREA
	//Remove the area a device belongs to
	private static void removeDeviceArea() {
		
		System.out.println("Available Devices: ");
		handler.devicesList();
		
		System.out.println("\nType the ID of the device");
	
		//Get the Device ID and check the validity
		try {
			String id = reader.readLine();
			
			int deviceID = 0;
			try{
				deviceID = Integer.parseInt(id);
			} catch(NumberFormatException e) {
				System.out.println("The input must be a number\n");
				return;
			}
			if(!handler.getDevice(deviceID)) {
				System.out.println("Error! This is not a device id.\n ");
				return;
			}
			
			handler.removeDeviceArea(deviceID);
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	//GIVEN AN AREA IT ALLOWS TO SWITCH AUTO/MANUAL MANAGEMENT
	
	//SWITCH AREA MANAGEMENT MODE: AUTO/MANUAL
	private static void switchAreaMode() {
		
		if(showAreasInfo()) {
			try {
				System.out.print("Type the area where management mode will be changed: ");
				
			
				String area = reader.readLine();
				
				if(handler.getIdArea().get(area) == null) {
					System.out.println("Error! Area Id present in the system");
					return;
				}
				
				System.out.println("Type the new management mode (Auto[1] / Manual[0]):");
			
				String auto = reader.readLine();
				
				
				if(auto.compareTo("0") == 0)
					handler.getIdArea().get(area).setAutoManage(false);
				else if(auto.compareTo("1") == 0)
					handler.getIdArea().get(area).setAutoManage(true);
				else {
					System.out.println("Error: value not valid\n");
					return;
				}
		
					
			} catch(IOException e) {
				
				e.printStackTrace();
			}
		}else {
			return;
		}
			
		System.out.println("");
	}
	
	//GIVEN AN AREA IT ALLOWS TO CHANGE THE THRESHOLDS
	
	//EDIT THE AREA THRESHOLDS
	private static void editAreaThreshold() {
		
		if(showAreasInfo()) {
			
			try {
				
				System.out.print("Type the area where threshold values will be changed: ");
				
				
				String area = reader.readLine();
				
				if(handler.getIdArea().get(area) == null) {
					System.out.println("Error! Area Id present in the system");
					return;
				}
				
				Area area_obj = handler.getIdArea().get(area);
			  
				System.out.print("Insert max bin fullness tolerated in this area: ");
				int max_h = 0;
				try{
					max_h = Integer.parseInt(reader.readLine());
				} catch(NumberFormatException e) {
					System.out.println("The input must be a number\n");
					return;
				}
			        
			   
				System.out.print("Insert min bin fullness tolerated in this area: ");
				int min_h = 0;
				try{
					min_h = Integer.parseInt(reader.readLine());
				}catch(NumberFormatException e) {
					System.out.println("The input must be a number\n");
					return;
				}
			        
			    
				if(max_h < min_h) {
					System.out.println("Error in typing parameters. No modification done\n");
					return;
				}
				
				area_obj.setMaxBinFullness(max_h);
				area_obj.setMinBinFullness(min_h);
				
				System.out.println("Modifications done");
				
				
			}catch(IOException e) {
				
				e.printStackTrace();
			}
			
			
		}else
			return;
		
		System.out.println("");
	}
	
	//Remove devices with given address
	
	//REMOVE AND UNREGISTER DEVICES WITH GIVEN ADDRESS
	private static void removeDevicesAddress() {
		
		System.out.println("Available Devices: ");
		handler.devicesList();
		
		System.out.println("\nType the address of the devices");
	
		try {
			String address = reader.readLine();
			
			if(!handler.getAddressIDs().containsKey(address)) {
				System.out.println("Error! This is not a device address.\n ");
				return;
			}
			
			handler.removeDevicesAddress(address);
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	

	//STOP THE APPLICATION
	private static void stop() {
		
		handler.removeAllDevices();
		
		System.out.println("Stopping the application...\n");
		server.stop();
		server.destroy();
		System.exit(0);
		
	}

	
	
	
	
	
}
