# SentimentMap
The repo contains only the App Server part of the sentiment Map. It calls twitter API to
get streaming data, then push to Amazon SQS and stores those data in Amazon MySQL. At
the same time, use a thread pool to process data by calling Alchemy API to acquire sentiment.
After that, push it to Amazon SNS so that Web Server could use it for visualization.

The Web Server part see: https://github.com/AlexandraVon/SentimentMap_WebServer.git

----

# How to use
To use this application, you need to have Amazon credentials in the appserver package and 
create a Global file in your local config package.

In the Global file, you need to have the following fields:

-Alchemy API:

AlchemyKEY

-Twitter API:

TwitterConsumerKey

TwitterConsumerSecret

TwitterAccessToken

TwitterAccessTokenSecret

-rds MysqlDB:

dbName

userName

password

hostname

port

------------


