package it.unipi.iot;

import java.net.InetAddress;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;

public class Registrant extends CoapResource {

	public Registrant(String name) {
		super(name);
	}
	
	public void handlePOST(CoapExchange exchange) {

		InetAddress address = exchange.getSourceAddress();
		boolean obs;
		String[] path;
		String title;

		//System.out.println("Registering " + address);
		
		/*Retrieve all the resource of the node*/
		CoapClient client = new CoapClient("coap://[" + address.getHostAddress() + "]:5683/.well-known/core");
		CoapResponse response = client.get(MediaTypeRegistry.APPLICATION_JSON);
		String responseText = response.getResponseText();

		//Check the return code: Success 2.xx
		if(!response.getCode().toString().startsWith("2")) {
			System.out.println("Error code: " + response.getCode().toString());
			return;
		}
		//System.out.println(response);
		String[] resources = responseText.split("\n");
		for (int i = 0; i < resources.length; i++) {
			String[] parameters = resources[i].split(";");
			if (resources[i].contains("</.well-known/core>;")) {
				path = parameters[1].split(",");
				title = parameters[2];
				obs = (parameters.length == 6);
			} else {
				path = parameters[0].split(",");
				title = parameters[1];
				obs = (parameters.length == 5);
			}
			RegisteredResource newOne = new RegisteredResource(path[1].replace("<", "").replace(">", ""), title, address.toString().replace("/",""), obs);

		}
		Response accept = new Response(CoAP.ResponseCode.CREATED);
		String payload = "Accept";
		accept.setPayload(payload);
		accept.setDestination(address);
		exchange.respond(accept);
	}
	

	
}
