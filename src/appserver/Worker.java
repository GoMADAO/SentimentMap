package appserver;

import java.util.List;
import java.util.Map.Entry;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;

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
		for(Message message: messages){
			String messageRecieptHandle = message.getReceiptHandle();
			sqs.deleteMessage(new DeleteMessageRequest(queueURL, messageRecieptHandle));
		}
	}
}
