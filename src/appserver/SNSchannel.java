package appserver;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;

public class SNSchannel {
	static String public_ip = "http://52.5.74.236";
	static String port = ":8080";
	static String topicName = "sentimentMap";
	static String endpoint = public_ip + port + "/SNS_servlet";
	static String topicArn = "arn:aws:sns:us-east-1:836606636789:sentimentMap";
	
	static String createTopic(AmazonSNSClient snsClient, String topicName){
		CreateTopicRequest createTopicRequest = new CreateTopicRequest(topicName);
		CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
		//TopicArn, format: {TopicArn: arn:aws:sns:us-east-1:836606636789:NewTopic}
		String Arn = createTopicResult.toString();
		String topicArn = Arn.split("TopicArn: ")[1];
		topicArn = topicArn.split("}")[0];
		System.out.println("Topic Arn: "+topicArn);
		//get request id for CreateTopicRequest from SNS metadata		
		System.out.println("CreateTopicRequest - " + snsClient.getCachedResponseMetadata(createTopicRequest));
		return topicArn;
	}
	static void subscribeEndpoint(AmazonSNSClient snsClient, String topicArn, String endpoint){
		SubscribeRequest subRequest = new SubscribeRequest(topicArn, "http", endpoint);
		SubscribeResult subscribereseult= snsClient.subscribe(subRequest);
		System.out.println("SubscribeRequest - " + snsClient.getCachedResponseMetadata(subRequest));
		System.out.println("subscribereseult - " + subscribereseult);
	}
	static void sendMsg(AmazonSNSClient snsClient, String topicArn, String msg){
		PublishRequest publishRequest = new PublishRequest(topicArn, msg);
		PublishResult publishResult = snsClient.publish(publishRequest);
		//print MessageId of message published to SNS topic
		System.out.println("MessageId - " + publishResult.getMessageId());
	}
	static void deleteTopic(AmazonSNSClient snsClient, String topicArn){
		DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest(topicArn);
		snsClient.deleteTopic(deleteTopicRequest);
		//get request id for DeleteTopicRequest from SNS metadata
		System.out.println("DeleteTopicRequest - " + snsClient.getCachedResponseMetadata(deleteTopicRequest));
		System.out.println("Successfully deleted topic!");
	}
	static AmazonSNSClient newSNSClient(){
		AWSCredentials credentials = new ProfileCredentialsProvider("default").getCredentials();
		AmazonSNSClient snsClient = new AmazonSNSClient(credentials);		                           
		snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));
		return snsClient;
	}
}
