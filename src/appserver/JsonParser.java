package appserver;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
	public static String Parse(String content){
		String sent = null;
		try {
			JSONObject pkg = new JSONObject(content);
			String status = pkg.getString("status");
			System.out.println("this is the status"+status);
			if(!status.equals("OK"))
				return null;
			
			JSONObject docSen =pkg.getJSONObject("docSentiment"); 
			sent = docSen.getString("type");			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sent;
	}

}
