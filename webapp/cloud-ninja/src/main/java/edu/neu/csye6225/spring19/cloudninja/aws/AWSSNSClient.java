package edu.neu.csye6225.spring19.cloudninja.aws;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Profile("dev")
public class AWSSNSClient {

    private AmazonSNS snsClient;

    @Value("${topic.arn}")
    private String topicARN;

    @Value("${amazonProperties.clientRegion}")
    private String clientRegion;

    @PostConstruct
    private void initializeAmazon() {
        this.snsClient = AmazonSNSClientBuilder.standard().withRegion(clientRegion).withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).build();
        //this.snsClient = AmazonSNSClientBuilder.standard().withRegion("us-east-1").withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).build();
    }

    public String publishToTopic(String msg){

        //PublishRequest publishRequest = new PublishRequest(topicARN, msg);
        PublishRequest publishRequest = new PublishRequest(topicARN, msg);
        PublishResult publishResponse = snsClient.publish(publishRequest);
        return publishResponse.getMessageId();

    }

}
