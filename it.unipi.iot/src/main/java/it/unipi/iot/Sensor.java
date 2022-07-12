package it.unipi.iot;

public class Sensor {

    private final String path;
    private final String title;
    private final String addr;
    private final String uri;

    public Sensor(String path, String title, String addr, String uri){
        this.path = path;
        this.title = title;
        this.addr = addr;
        this.uri = uri;
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
}
