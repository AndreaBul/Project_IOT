package it.unipi.iot.resource_devices;

public class Area {
	
	protected String id;
	protected int maxBinFullness = 0;
	protected int minBinFullness = 0;
	protected String sprinklersStatus = "OFF";
	protected String lightsStatus = "OFF";
	protected boolean autoManage = false;
	
	

	public Area(String id,  int maxBinFullness, int minBinFullness) {
		this.id = id;
		this.maxBinFullness = maxBinFullness;
		this.minBinFullness = minBinFullness;
		System.out.println("New area " + id + " has been created");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMaxBinFullness() {
		return maxBinFullness;
	}

	public void setMaxBinFullness(int maxBinFullness) {
		this.maxBinFullness = maxBinFullness;
	}

	public int getMinBinFullness() {
		return minBinFullness;
	}

	public void setMinBinFullness(int minBinFullness) {
		this.minBinFullness = minBinFullness;
	}
	
	public boolean isAutoManage() {
		return autoManage;
	}

	public void setAutoManage(boolean autoManage) {
		this.autoManage = autoManage;
	}
	
	public String getSprinklersStatus() {
		return sprinklersStatus;
	}

	public void setSprinklersStatus(String sprinklersStatus) {
		this.sprinklersStatus = sprinklersStatus;
	}

	public String getLightsStatus() {
		return lightsStatus;
	}

	public void setLightsStatus(String lightsStatus) {
		this.lightsStatus = lightsStatus;
	}

	
	public void printAreaInfo() {
		System.out.print("Area: " + this.getId() + ", AutoMode: " + this.isAutoManage() + 
				", Sprinklers: " + this.sprinklersStatus + ", Lights: " + this.lightsStatus +
				", Min Bin fullness: " + this.getMinBinFullness() + ", Max Bin fullness: " + this.getMaxBinFullness());
	}
	
	
	

}
