package appserver;

import java.sql.Connection;
import java.util.HashMap;

public class DataUpListener implements DataListener{
	private static Connection conn = DBConn.getConnection();
	private static HashMap<String, Integer> keywords = new HashMap<String, Integer>();
	
	
	
	
	@Override
	public void handleEvent(UpdateEvent ue) {
		// TODO Auto-generated method stub
		System.out.println("now get a update event");
		String sql = "insert into ";
		
		
		
	}

}
