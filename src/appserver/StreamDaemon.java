package appserver;


import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.amazonaws.services.sqs.AmazonSQS;

import config.Global;

public class StreamDaemon extends Thread{
	private AmazonSQS sqs;
	private String queueURL;
	
	public StreamDaemon(AmazonSQS sqs, String queueURL){
		this.sqs=sqs;
		this.queueURL=queueURL;
	}
	@Override
	public void run(){
		ConfigurationBuilder cb = new ConfigurationBuilder();    	 
		//System.out.println(test);
		
		StreamSource stream = new StreamSource();
		DataUpListener dul = new DataUpListener();
		stream.addDataListener(dul);
		
		
		
		
		
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey(Global.TwitterConsumerKey)
          .setOAuthConsumerSecret(Global.TwitterConsumerSecret)
          .setOAuthAccessToken(Global.TwitterAccessToken)
          .setOAuthAccessTokenSecret(Global.TwitterAccessTokenSecret);
                      
       TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
       
       StatusListener listener = new StatusListener() {
           @Override
           public void onStatus(Status status) {
           	if(status.getGeoLocation()!=null && status.getLang().equals("en")){
           		System.out.println("ID:"+status.getGeoLocation()+ status.getCreatedAt()+ status.getId());
           		
           			String temp =""+status.getGeoLocation();
               		temp = temp.substring("GeoLocation".length());
           			System.out.println("Text:"+ status.getText());
           			System.out.println();
           			sqs.sendMessage(queueURL, status.getText());
           		}
           }
                       
           @Override
           public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
               //System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
           }

           @Override
           public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
               //System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
           }

           @Override
           public void onScrubGeo(long userId, long upToStatusId) {
               //System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
           }

           @Override
           public void onStallWarning(StallWarning warning) {
               //System.out.println("Got stall warning:" + warning);
           }

           @Override
           public void onException(Exception ex) {
               ex.printStackTrace();
           }
       };
       twitterStream.addListener(listener);
       twitterStream.sample();
	}
}
