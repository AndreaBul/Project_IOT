package it.unipi.iot;

public class Sensor {

    private final String path;
    private final String title;
    private final String addr;
    private final String uri;
    private final Boolean observable;

    public Sensor(String path, String title, String addr, String uri){
        this.path = path;
        this.title = title;
        this.addr = addr;
        this.uri = uri;
        this.observable = true;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getAddr() {
        return addr;
    }

    public String getUri() {
        return uri;
    }
    
    
//    public void observeResource() {
//
//		if(observable) {
//
//			client.observe(
//					new CoapHandler() {
//						public void onLoad(CoapResponse response) {
//							JSONObject responseJSON = new JSONObject(response.getResponseText());
//
//							//read and store the value in the array
//							status = responseJSON.getString(resourceType);
//
//
//
//						}
//							public void onError() {
//								System.err.println("--- Observation Failed ---");
//							}
//					}, MediaTypeRegistry.APPLICATION_JSON);
//
//
//		}else {
//			System.out.println("The resource " + resourceType + " is not observable");
//			return;
//		}
	
}
