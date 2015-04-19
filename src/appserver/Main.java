package appserver;

import java.sql.Connection;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

public class Main {

	
//		StreamSource ss = new StreamSource();
//		DataUpListener dul = new DataUpListener();
//		ss.addDataListener(dul);
//		ss.notifyUpdateListener();
			
	private static AmazonSQS sqs;
	private static String queueURL = "https://sqs.us-east-1.amazonaws.com/362491276831/SQStest";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("running first");
		AWSCredentials credentials = null;
		credentials = new ProfileCredentialsProvider("default").getCredentials();
		sqs = new AmazonSQSClient(credentials);
		
		DataSource datasource = new DataSource();
		DataUpListener listener = new DataUpListener();
		datasource.addDataListener(listener);
		
		StreamDaemon input = new StreamDaemon(sqs, queueURL, datasource);
		input.setDaemon(true);
		input.run();
		
		
		Connection con = DBConn.getConnection();
		
		Timer t = new Timer();
        t.schedule(new DataCleaner(con), 60*1000);
		
        
		ExecutorService e = Executors.newFixedThreadPool(15);
		
		
        
		while(true){
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueURL);
			//Message m = sqsClient.receiveMessage(receiveMessageRequest);
			
			List<Message> messages  = sqs.receiveMessage(receiveMessageRequest
					.withMessageAttributeNames("twit_id"))
						.getMessages();
			if(messages.isEmpty()){
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else{
				Future<?> task = e.submit(new Worker(messages,  queueURL,  sqs, datasource));
				e.execute(new Helper(task, queueURL,  sqs, messages));				
			}
			
		}
		
	}
	

}
