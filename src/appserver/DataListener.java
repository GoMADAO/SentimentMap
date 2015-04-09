package appserver;

import java.util.EventListener;

public interface DataListener extends EventListener{
	public void handleEvent(UpdateEvent ue);
	public void handleEvent(AddSentEvent ase);
}
