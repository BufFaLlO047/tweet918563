# tweet918563
Tweet App built using Java, JavaScript, Angular, HTML,CSS, AWS. Testing - JSON Karma, JMockito, Jira. Cloud - AWS. Database: Mongo DB( NoSQL - JSON).
Junit Test cases included.
For the application to run, initialize kafka and start the server for consumer and producer in seperate console.
Initialize MongoDB through Atlas and connect it with the local host.
Start the project by RunAs > Java Application
Test cases can be executed in Eclipse after the application is up and live connected to Mongo Db.
For AWS, initialize 2 EC2 instances seperately for Kafka producer and consumer.  Start both the instances in seperate CLI ( with kakfa installed).
Once initialized, upload the back end code as .jar file to ASW ELB. Once its running oyu should see White Label Error.
Upload the front end in S3 and change the view to "Public" and save the link.
Once the Kafka is up and running and ELB connected to S3, you can access the application through S3 website.

Future Scope:
 Https needs to be done.
 Strong code needs to be done. 
 Inactive users can be automatically deleted along with their tweets after their inactivity period.
 Using AWS glue with Rekognition can be used to monitor and learn popularity of videos, photos posted in the tweet app. Can track app usage of users and predict the application scope and API handling scenarios and failures.
 
