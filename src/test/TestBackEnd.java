package test;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;





import appserver.StreamDaemon;
import appserver.Worker;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityBatchRequestEntry;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

public class TestBackEnd {
	private static AmazonSQS sqs;
	private static String queueURL = "https://sqs.us-east-1.amazonaws.com/362491276831/SQStest";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("running first");
		AWSCredentials credentials = null;
		credentials = new ProfileCredentialsProvider("default").getCredentials();
		sqs = new AmazonSQSClient(credentials);
		
		
		Runnable input = new StreamDaemon(sqs, queueURL);
		//input.setDaemon(true);
		
		
		
		ExecutorService e = Executors.newFixedThreadPool(10);
		
		e.execute(input);
		
		
		//AmazonSQSClient sqsClient = new AmazonSQSClient(credentials);
		
		
		while(true){
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueURL);
			//Message m = sqsClient.receiveMessage(receiveMessageRequest);
			
			List<Message> messages  = sqs.receiveMessage(receiveMessageRequest).getMessages();
			if(messages.isEmpty()){
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else{
				Future<?> task = e.submit(new Worker(messages,  queueURL,  sqs));
				e.execute(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						boolean done = false;
						
						
						int id=0;
						List<ChangeMessageVisibilityBatchRequestEntry> list = 
								new LinkedList<ChangeMessageVisibilityBatchRequestEntry>();
						for(Message message : messages){
							id++;
							ChangeMessageVisibilityBatchRequestEntry entry
							= new ChangeMessageVisibilityBatchRequestEntry 
							(id+"", message.getReceiptHandle());
							entry.setVisibilityTimeout(30);
							list.add(entry);
							
						}						
						while(!done){							
							try {
								task.get(20, TimeUnit.SECONDS);
								done =true;
							} catch (InterruptedException ie){
								Thread.currentThread().interrupt();
								done = true;
							}catch(ExecutionException ee){
								done =true;
							}catch(java.util.concurrent.TimeoutException e) {
								// TODO Auto-generated catch block
								//e.printStackTrace();
								sqs.changeMessageVisibilityBatch(queueURL, list);
								
							}catch(java.util.concurrent.CancellationException ce){
								System.out.println("wrong");
							}
								
							
						}
						
					}
					
					
					
				});
				
				
			}
			
		}
		
	}
}
