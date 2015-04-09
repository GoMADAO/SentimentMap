package appserver;

import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.Status;

public class DataUpListener implements DataListener{
	private Connection conn = DBConn.getConnection();
		
	private String[] getGPS(String raw){
		raw = raw.substring("GeoLocation".length());
		Pattern p=Pattern.compile("\\{|\\}");
		Matcher m=p.matcher(raw);                			
		raw=m.replaceAll("");	
		Pattern p1=Pattern.compile("longitude=|latitude=");
		m=p1.matcher(raw);        			
		String location=m.replaceAll("");
		String[] l = location.split(", ");
		//System.out.println(l[0]);
		return l;		
	}
	
	@Override
	public void handleEvent(UpdateEvent ue) {
		// TODO Auto-generated method stub
		
		if(conn!=null){
			Status status = ue.getStatus();
			String[] gps = getGPS(""+status.getGeoLocation());
			String id = status.getId()+"";
				
			String sql = "INSERT INTO info (twid, lati, longi) VALUES ("
					+ "'"+ id+"', '"+gps[0]+"', '"+gps[1]+"');";
			//System.out.println(sql);
			DBConn.doInsert(sql, conn);	
		}		
	}

	
	
	@Override
	public void handleEvent(AddSentEvent ase) {
		// TODO Auto-generated method stub
		if (conn!=null){
			String id = ase.getTwitId();
			String sentiment =JsonParser.Parse(ase.getSentiment()) ;
			String sql = "INSERT INTO twit_sent(twid, sent) VALUES ("
					+ "'"+id+"', '"+sentiment+"');";
			System.out.println("sql:"+sql);
			DBConn.doInsert(sql, conn);
		}
	}

}
