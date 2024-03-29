package it.unipi.iot.resource;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.LinkedList;
import java.util.Queue;

public class ObserveResource  implements CoapHandler {
    Queue<SensorInfo> sensored_values = new LinkedList<SensorInfo>();
    private String name;
    int max_size = 35;

    public ObserveResource(String name) {
        super();
        this.name = name;
    }

    @Override
    public void onLoad(CoapResponse response) {
        try {
            // System.out.println(response.getResponseText().toString());
            JSONObject msg = (JSONObject) JSONValue.parseWithException(response.getResponseText().toString());

            if (sensored_values.size() >= max_size) {
                sensored_values.poll();
            }
            SensorInfo t = new SensorInfo(msg);
            sensored_values.add(t);
        } catch(org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        String tmp = "";
        for (SensorInfo s : sensored_values) {
            tmp += s + "\n";
        }
        return tmp;
    }

    @Override
    public void onError() {
        System.out.println("An error occurred while observing");

    }

}
