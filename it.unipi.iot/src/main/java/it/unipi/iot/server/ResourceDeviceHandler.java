package it.unipi.iot.server;

import it.unipi.iot.resource_devices.Area;
import it.unipi.iot.resource_devices.ResourceDevice;
import it.unipi.iot.resource_devices.Sensor;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class ResourceDeviceHandler {
	
	//A single instance of this class is needed to maintain shared and consistent info of the devices
	private static ResourceDeviceHandler singleInstance = null;
	
	//ID counter to be assigned to devices
	private static int deviceID = 0;
	
	/* DATA STRUCTURES TO HOLD DEVICES */
	protected HashMap<Integer, Sensor> binSensors = new HashMap<Integer, Sensor>();	//ID - bin
	protected HashMap<Integer, ResourceDevice> idDeviceMap = new HashMap<Integer, ResourceDevice>();	//id - Device
	protected HashMap<String, ArrayList<Integer>> addressIDs = new HashMap<String, ArrayList<Integer>>();	//address - List of IDs
	
	
	
	/* CONSTATS DEFINITION */
	static final int MIN_VARIATION = 1;
	static final int MAX_VARIATION = 5;
	static final int DEFAULT_AREA_MAX_BIN = 100;
	static final int DEFAULT_AREA_MIN_BIN = 0;
	
	/* DATA STRUCTURES FOR AREAS */
	protected HashMap<String, Area> idArea = new HashMap<String, Area>();	//Area ID - Area object
	protected HashMap<Area, ArrayList<ResourceDevice>> areas = new HashMap<Area, ArrayList<ResourceDevice>>();	//Area - List of devices

	private ResourceDeviceHandler()
    {
		//Create the "default" area and set its parameters
		Area area = new Area("default", DEFAULT_AREA_MAX_BIN, DEFAULT_AREA_MIN_BIN);
        this.idArea.put("default", area);
        ArrayList<ResourceDevice> devices = new ArrayList<ResourceDevice>();
        this.areas.put(area, devices);
      
    }
	
	//Get the instance of ResourceDeviceHandler. Only one instance atomically shared between everyone
	public static ResourceDeviceHandler getInstance(){
		if (singleInstance == null) {
            singleInstance = new ResourceDeviceHandler();
		}
 
        return singleInstance;
    }
	
	public int getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(int deviceID) {
		ResourceDeviceHandler.deviceID = deviceID;
	}


	


	public void addBinSens(Integer id, Sensor sensor) {
		binSensors.put(id, sensor);
	}
	

	public HashMap<Integer, Sensor> getBinSensors() {
		return binSensors;
	}
	
	public HashMap<Area, ArrayList<ResourceDevice>> getAreas(){
		return areas;
	}
	
	public ResourceDevice getDeviceFromId(Integer id) {
		return idDeviceMap.get(id);
	}

	public HashMap<Integer,ResourceDevice> getIdDeviceMap(){
		return this.idDeviceMap;
	}	
	
	public void setIdDeviceMap(Integer id, ResourceDevice rd){
		this.idDeviceMap.put(id, rd);
	}
	
	public HashMap<String, ArrayList<Integer>> getAddressIDs() {
		return addressIDs;
	}
	
	public HashMap<String, Area> getIdArea() {
		return idArea;
	}
	
/*
 * 
 * 		RETURNS DEVICES LISTS
 * 
 */
	
	// PRINT THE LIST OF ALL THE SPRINKLER ACTUATORS

	
	// PRINT THE LIST OF ALL THE LIGHT ACTUATORS


	//	PRINT THE LIST OF ALL THE BIN SENSORS
	public void binSensorList() {
		for(Integer id: binSensors.keySet()) {
			Sensor s = binSensors.get(id);
			System.out.println("Area: " + s.getArea() + ", ID: " + s.getId() + ", addr: " + s.getHostAddress() + ", type: " + s.getDeviceType() + ", Resource: " + s.getResourceType());
		}
	}
	
	//	PRINT THE LIST OF ALL THE DEVICES
	public void devicesList() {
		this.binSensorList();
	}
	
	// RETURNS IF A DEVICE WITH THAT ADDRESS IS PRESENT
	public boolean getDevice(Integer id) {

		if(binSensors.containsKey(id))
			return true;

		return false;
	}

	//PRINT ADDRESSES
	public void getAddressesList() {
		
		System.out.print("[");
		for(String addr: addressIDs.keySet()) {
			System.out.print(" " + addr);
		}
		System.out.println(" ]\n");
	}
	
	//PRINT THE LIST OF IDs FOR A GIVEN ADDRESS
	public void getAddressIDs(String address){
		
		if(!addressIDs.containsKey(address))
			System.out.println("[" + address + "]: []");
		else {
			System.out.println("[" + address + "]:");
			for(Integer id: addressIDs.get(address)) {
				System.out.println("[ ID: " + id + ", Resource: " + idDeviceMap.get(id).getResourceType() + " ]");
			}
			System.out.println("");
		}
		
	}
	
	// PRINT LIST OF AREAS
	public void getAreasList() {
		
		if(areas.isEmpty()) {
			System.out.println("No Area defined yet\n");
			return;
		}
		
		for(Area area_obj: areas.keySet()) {
			
			//print area name
			System.out.print("[ " + area_obj.getId() + " ] :");
			
			//get device list
			ArrayList<ResourceDevice> device = areas.get(area_obj);
			
			for(ResourceDevice d: device) {
					System.out.print("\n{ID: " + d.getId() + ", IP: " + d.getHostAddress() + ", type: " 
							+ d.getDeviceType() + ", res: " + d.getResourceType() + " } " );
					

			}
			System.out.println("");
					
			
		}
		
			
		
	}

	
	//GET THE BIN AVG OF ALL THE SENSORS
	public void getSensorsBinFullness() {
		for(Integer id: binSensors.keySet()) {
			Sensor s = binSensors.get(id);
			System.out.println("Sensor Area: " + s.getArea() + ", Address: " + s.getHostAddress() + 
					", Average Bin Fullness: " + s.getLastAvgObservation());
		}
	}


	
	//Unregister Device
	public boolean unRegisterDevice(String address) {
		
		CoapClient c = new CoapClient("coap://[" + address + "]:5683/unregister");
		
		JSONObject json = new JSONObject();
		
		
		json.put("unregister", "true");
			
		
		//send post request
		CoapResponse response = c.post(json.toString(), MediaTypeRegistry.APPLICATION_JSON);
		
		//Check the return code: Success 2.xx
		if(!response.getCode().toString().startsWith("2")) {
			System.out.println("Error code: " + response.getCode().toString());
			return false;
		}
		
		return true;
		
	}
	
	 
	
	/*
	 * 
	 * 		DEVICE - AREA RELATED 
	 * 
	 */
	
	
	// ADD DEVICE TO AREA
	public void addDeviceArea(Integer id, String area) {
		
		boolean find = false;
		
		ResourceDevice rd = idDeviceMap.get(id);
		if(rd != null) {
			addResourceArea(rd, area);
			find = true;
		}
		
		if(!find) {
			System.out.println("No Device with that id\n");
			
		}
		
		
		
		
	}
	
	//Add the resource to the area
	public void addResourceArea(ResourceDevice rd, String area) {
		
		Area old = null;
			
		//If a area was already set, take the area. If no error, the device will be removed from that list.
		if(rd.getArea() != null && (rd.getArea().compareTo(area)!=0)) {
			old = idArea.get(rd.getArea());
		}
		
		
		if(!areas.containsKey(idArea.get(area))) {
			System.out.println("The area " + area + " does not exist. Start creation.");
			Area area_obj = null;
			
			
			if(idArea.containsKey(area))	//Area already exists
				area_obj = idArea.get(area);
			else							//Area must be created
				area_obj = generateArea(area);
			
			if(area_obj == null) {
				System.out.println("Error in generating the area\n");
				return;
			}
			ArrayList<ResourceDevice> list = new ArrayList<>();
			list.add(rd);
			areas.put(area_obj, list);
		}else {
			System.out.println("The area " + area + " already exists. Just add the device.");
			if(!areas.get(idArea.get(area)).contains(rd)) {
				areas.get(idArea.get(area)).add(rd);
			}
			
			
		}
		
		rd.setArea(area);
		System.out.println("Device Area set to: \"" +rd.getArea() + "\" for resource: " + rd.getResourceType() + "\n");
		
		//If a area was already set, remove the device from that list.
		if(old != null) {
			areas.get(old).remove(rd);
			
			//If the area remains empty remove it 
			if(areas.get(old).isEmpty()){
				areas.remove(old);
				if(old.getId().compareTo("default") != 0)
					idArea.remove(old.getId());
			}
		}
				
		
	}
	
	//REMOVE DEVICE FROM AREA
	public void removeDeviceArea(Integer id) {
		
		boolean find = false;
		
		ResourceDevice rd = idDeviceMap.get(id);
		if(rd != null) {
			removeResourceArea(rd);
			find = true;
		}
		
	
		if(!find) {
			System.out.println("No Device with that id\n");
			
		}
		
			
	}
	
	// Remove resource from the area
	private void removeResourceArea(ResourceDevice rd) {
		
		//If a area was already set, remove the device from that list.
		
		String old = rd.getArea();
		if(rd.getArea().compareTo("default") != 0) {	//If is already in default area, do nothing
			addDeviceArea(rd.getId(), "default");
	
			
			System.out.println("Resource Device: " + rd.getResourceType() + " removed from area " + old + "\n");
			return;
		}
		
		System.out.println("Resource Device " + rd.getResourceType() + ": was already in \"default\" area\n");
		return;
	}


	// CREATE A NEW AREA
	public Area generateArea(String id) {
		if(!idArea.containsKey(id)) {
			System.out.println("	--Generating Area " + id + "--	");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			
	
			try {
			    
				System.out.print("Insert max bin fullness tolerated in this area: ");
				int max_h = 0;
				try{
					max_h = Integer.parseInt(reader.readLine());
				} catch(NumberFormatException e) {
					System.out.println("The input must be a number\n");
					return null;
				}
			        
			    
				System.out.print("Insert min bin fullness tolerated in this area: ");
				int min_h = 0;
				try{
					min_h = Integer.parseInt(reader.readLine());
				} catch(NumberFormatException e) {
					System.out.println("The input must be a number\n");
					return null;
				}
			   
				if(max_h < min_h){
					System.out.println("Error in typing parameters\n");
					return null;
				}
					
				
				Area area = new Area(id, max_h, min_h);
				idArea.put(id, area);
				
				return area;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
			
	}
	
	
	/*
	 * 
	 * REMOVE DEVICES
	 * 
	 */
	
	//REMOVE ALL DEVICES WITH GIVEN ADDRESS
	public boolean removeDevicesAddress(String address) {
		
		//I take all the IDs of the device with the given address
		ArrayList<Integer> ids = new ArrayList<>();
		for(Integer el: this.getAddressIDs().get(address))
			ids.add(el);

		//For each device, I remove it
		for(Integer id: ids) {
			if(!removeDevice(id))
				return false;
		}
		
		ids.clear();
		System.out.println("Removed devices with address: " + address);
		
		//Notify the device that has been unregistered from the application
		if(unRegisterDevice(address))
			System.out.println("Unregister devices with address: " + address + "\n");
		return true;
			
	}
	
	//REMOVE DEVICE WITH A GIVE ID
	public boolean removeDevice(Integer id) {
		
		ResourceDevice rd = idDeviceMap.get(id);
		String address = rd.getHostAddress();
		
		switch(rd.getResourceType()) {
		case "bin":
			removeDeviceArea(id);	//remove from an area and put it in the default one
			areas.get(idArea.get("default")).remove(rd);	//remove also from default area
			binSensors.remove(id);		//remove from hbinSens map
			idDeviceMap.remove(id);
			break;
		default:
			System.out.println("Error in removing device " + id + "\n");
			return false;
		}
		
		//Remove the ID for the list 
		int index = this.getAddressIDs().get(address).indexOf(id);	//take the index position
		this.getAddressIDs().get(address).remove(index);

		
		System.out.println("Device " + id + " removed\n");
		
		//remove the address from the map
		if(this.getAddressIDs().get(address).isEmpty()) {
			System.out.println("No more devices with the address: " + address + ". Remove it\n");
			this.getAddressIDs().remove(address);
		}
		
		return true;
		
	}

	//REMOVE ALL DEVICES
	public boolean removeAllDevices() {
		
		System.out.println("Removing all the Devices...");
		
		//I take all the devices addresses in the system
		ArrayList<String> addresses = new ArrayList<>();
		for(String address: addressIDs.keySet())
			addresses.add(address);
		
		//For each address, I call the remove function
		for(String address: addresses)
			if(!removeDevicesAddress(address))
				return false;
		
		addresses.clear();
		System.out.println("All devices have been unregistered and removed\n");
		return true;
	}
	
}
