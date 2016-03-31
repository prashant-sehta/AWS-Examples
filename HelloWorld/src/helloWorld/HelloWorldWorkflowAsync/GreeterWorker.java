package helloWorld.HelloWorldWorkflowAsync;

import java.util.Properties;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.flow.ActivityWorker;
import com.amazonaws.services.simpleworkflow.flow.WorkflowWorker;

public class GreeterWorker {
	public static void main(String[] args) throws Exception {
	     ClientConfiguration config = new ClientConfiguration().withSocketTimeout(70*1000);

	     Properties properties = new Properties();
         properties.load(GreeterWorker.class.getResourceAsStream("/AwsCredentials.properties"));
         
         String swfAccessId = properties.getProperty( "aws_access_key_id" );
         String swfSecretKey = properties.getProperty( "aws_secret_access_key" );
	     
	     //String swfAccessId = System.getenv("aws_access_key_id");
	     //String swfSecretKey = System.getenv("aws_secret_access_key");
	     AWSCredentials awsCredentials = new BasicAWSCredentials(swfAccessId, swfSecretKey);

	     AmazonSimpleWorkflow service = new AmazonSimpleWorkflowClient(awsCredentials, config);
	     service.setEndpoint("https://swf.us-west-2.amazonaws.com");

	     String domain = "helloWorldWalkthroughPrashant";
	     String taskListToPoll = "HelloWorldAsyncList";

	     ActivityWorker aw = new ActivityWorker(service, domain, taskListToPoll);
	     aw.addActivitiesImplementation(new GreeterActivitiesImpl());
	     aw.start();

	     WorkflowWorker wfw = new WorkflowWorker(service, domain, taskListToPoll);
	     wfw.addWorkflowImplementationType(GreeterWorkflowImpl.class);
	     wfw.start();
	     
	     /*GreeterWorkflowClientExternalFactory factory = new GreeterWorkflowClientExternalFactoryImpl(service, domain);
	     GreeterWorkflowClientExternal greeter = factory.getClient("someID");
	     greeter.greet();*/
	   }

}
