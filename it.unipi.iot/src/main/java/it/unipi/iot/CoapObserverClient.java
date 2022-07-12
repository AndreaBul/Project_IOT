package unipi.iot;

import java.util.ArrayList;

import it.unipi.iot.Sensor;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class CoapObserverClient extends CoapClient {
	private Sensor sensor;
	CoapObserveRelation coapObserveRelation;

	public CoapObserverClient(Sensor sensor) {
		super(sensor.getUri());
		this.sensor = sensor;
	}

	public void startObserving() {
		coapObserveRelation = this.observe(new CoapHandler () {
			public void onLoad(CoapResponse response) {
				try {
					String value;
					JSONObject jo = (JSONObject) JSONValue.parseWithException(response.getResponseText());
					System.out.println(jo.toString());
					if (jo.containsKey("fullness")) {
	
					} else {
						System.out.println("Bin fullness value not correct.");
						return;
					}

	
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
	
			public void onError() {
				System.out.println("Error in observing.");
			}
		});
	}


}
