package it.unipi.iot.resource;


import it.unipi.iot.MyClient;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;

public class Registration extends CoapResource{

	public Registration(String name) {
		super(name);
		System.out.println("Registration Resource created\n");
	}
	
	public void handleGET(CoapExchange exchange) {
		
		System.out.println("[SERVER]: Handling Registration Request");

		//	Get node address	 
		String source_address = exchange.getSourceAddress().getHostAddress();
		System.out.println("SRC ADDR: " + source_address);
		
		// Create CoapClient Object	and get node information
		CoapClient client = new CoapClient("coap://[" + source_address + "]:5683/.well-known/core");
		CoapResponse response = client.get(MediaTypeRegistry.APPLICATION_JSON);
		
		
		String responseText = response.getResponseText();
		
		//Check the return code: Success 2.xx
		if(!response.getCode().toString().startsWith("2")) {
			System.out.println("Error code: " + response.getCode().toString());
			return;
		}
		
		
		/*
		 * Client response: </.well-known/core>;ct=40,
		 * </bin>;title="Bin fullness Sensor";rt="bin";if="sensor";obs,
		 * </unregister>;"title=\"Unregister\";rt=\"unregister\";if=\"unregister\""
		 */
		
		
		//Take each resource
		String []fragment = responseText.split(",");
		for(int i = 1; i < fragment.length; i++) {
			//for each, register in the application
			if(!deviceRegistration(source_address, fragment[i])) {
				System.out.println("Error in registering a resource\n");
				//TODO, handle exceptions and case when device is not registred correctly
				continue;

			}
		}
		
		//Prepare and set response to the node
		Response accept = new Response(ResponseCode.CREATED);
		String payload = "Accept";

	    accept.setPayload(payload);
	    exchange.respond(accept);
	}
	
	//For each resource, register the device
	private boolean deviceRegistration(String address, String resource) {
		String[] parameters = resource.split(";");

		String[] path = parameters[1].split(",");
		String title = parameters[2];
		Boolean obs = (parameters.length == 6);

		RegisteredResource newOne = new RegisteredResource(path[1].replace("<", "").replace(">", ""), title, address.toString().replace("/",""), obs);
		MyClient.insertNewSensor(newOne);
		return true;
	}
	
	
	

}
