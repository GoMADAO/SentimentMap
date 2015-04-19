package appserver;

import java.util.EventListener;

public interface DataListener extends EventListener{
	public void handleEvent(UpdateEvent ue);
	public void handleEvent(AddSentEvent ase);
	public void handleEvent(SNSEvent se);
	public void handleEvent(StreamEvent sme);
}
