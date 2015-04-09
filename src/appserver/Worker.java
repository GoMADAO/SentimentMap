package appserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;

import config.Global;

public class Worker implements Runnable{
	private List<Message> messages;
	private String queueURL;
	private AmazonSQS sqs;
	
	public Worker(List<Message> m, String queueURL, AmazonSQS s){
		this.messages =m;
		this.queueURL = queueURL;
		this.sqs = s;
	}
	@Override
	public void run(){
		System.out.println(Thread.currentThread().toString());
		for(Message message : messages){
			System.out.println("  Message");
            System.out.println("    MessageId:     " + message.getMessageId());
            System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
            System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
            System.out.println("    Body:          " + message.getBody());
			for (Entry<String, String> entry : message.getAttributes().entrySet()) {
                System.out.println("  Attribute");
                System.out.println("    Name:  " + entry.getKey());
                System.out.println("    Value: " + entry.getValue());
            }
		}
		
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost("http://access.alchemyapi.com/calls/text/TextGetTextSentiment");
		HttpResponse response = null;
		HttpEntity entity = null;
		
		
		for(Message message: messages){
						
			// Request parameters and other properties.
			
			
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("apikey", Global.AlchemyKEY));
			params.add(new BasicNameValuePair("text", message.getBody()));
			params.add(new BasicNameValuePair("outputMode","json"));
			
			try {
				post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Execute and get the response.
			try {
				response = client.execute(post);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			entity = response.getEntity();
		
			// Parse response
			if (entity != null) {
			    BufferedReader br=null;
			    String line;
				try {
					br = new BufferedReader(new InputStreamReader(entity.getContent()));
					while((line=br.readLine())!=null){
						System.out.println(line);
					}
					br.close();
				} catch (IllegalStateException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}			    						    			    
			}						
			String messageRecieptHandle = message.getReceiptHandle();
			sqs.deleteMessage(new DeleteMessageRequest(queueURL, messageRecieptHandle));
		}
	}
}
