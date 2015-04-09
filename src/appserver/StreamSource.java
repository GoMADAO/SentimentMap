package appserver;

import java.util.HashMap;


public class StreamSource {
	private DataUpListener listener;
	
	public void addDataListener(DataUpListener dl){
		this.listener= dl;	
	}
	public void notifyUpdateListener(HashMap hm) {
		
		
		UpdateEvent ue = new UpdateEvent(this, null);
		listener.handleEvent(ue);
	}
	
	
}
