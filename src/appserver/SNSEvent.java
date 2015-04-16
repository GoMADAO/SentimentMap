package appserver;

import java.util.EventObject;

public class SNSEvent extends EventObject{

	/**
	 * 
	 */
	private String id;
	private String sentiment;
	
	private static final long serialVersionUID = -8324568048204985554L;

	public SNSEvent(Object source, String id, String json) {
		super(source);
		// TODO Auto-generated constructor stub
		this.id=id;
		this.sentiment = json;
	}
	public String getId(){
		return this.id;
	}
	public String getSentiment(){
		return this.sentiment;
	}
	
}
