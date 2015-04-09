package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import appserver.JsonParser;
import config.Global;

public class Alchemy {
	public static void main(String[] args) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost("http://access.alchemyapi.com/calls/text/TextGetTextSentiment");
		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("apikey", Global.AlchemyKEY));
		params.add(new BasicNameValuePair("text", "Hello!"));
		params.add(new BasicNameValuePair("outputMode","json"));
		
		post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		
		//Execute and get the response.
		HttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		
		
		if (entity != null) {
		    InputStream instream = entity.getContent();
		    InputStreamReader isr = new InputStreamReader(instream);
		    BufferedReader br = new BufferedReader(isr);
		    String line;
		    while((line=br.readLine())!=null){
		    	System.out.println(line);
		    }
		    
		    instream.close();
		    isr.close();
		    br.close();
		    
		}
		
		
		
		params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("apikey", Global.AlchemyKEY));
		params.add(new BasicNameValuePair("text", "Next one!"));
		params.add(new BasicNameValuePair("outputMode","json"));
		
		post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		
		 StringBuffer sb = new StringBuffer();
		
		//Execute and get the response.
		response = client.execute(post);
		entity = response.getEntity();
		if (entity != null) {
		    InputStream instream = entity.getContent();
		    InputStreamReader isr = new InputStreamReader(instream);
		    BufferedReader br = new BufferedReader(isr);
		   
		    String line;
		    while((line=br.readLine())!=null){
		    	sb.append(line);
		    	System.out.println(line);
		    }
		    
		    instream.close();
		    isr.close();
		    br.close();
		    
		}
		
		String content = sb.toString();
		System.out.println("Return value"+JsonParser.Parse(content));
		
	}
}
