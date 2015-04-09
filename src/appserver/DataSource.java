package appserver;

import twitter4j.Status;


public class DataSource {
	private DataUpListener listener;
	
	public void addDataListener(DataUpListener dl){
		this.listener= dl;	
	}
	
	public void notifyUpdateListener(Status status) {		
		UpdateEvent ue = new UpdateEvent(this, status);
		listener.handleEvent(ue);
	}
	public void notifyAddSentListener(String id, String json){
		AddSentEvent ase = new AddSentEvent(this, id, json);
		listener.handleEvent(ase);
	}
	
}
