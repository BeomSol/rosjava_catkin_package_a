package com.github.rosjava.rosjava_catkin_package_a.my_pub_sub_tutorial;

import org.apache.commons.logging.Log;
import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class Server1{
	static String custom_order = null;
	static String[] order;
	
	@SuppressWarnings("unused")
	void run() throws IOException {
		Socket clientSocket = null;
		BufferedReader in = null;	
		ServerSocket serverSocket = new ServerSocket(5000); 
		try {		
			clientSocket = serverSocket.accept();
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			custom_order = in.readLine(); 
			in.close();
			clientSocket.close();
			serverSocket.close(); 
		} catch (Exception e) {
			in.close();
			clientSocket.close();
			serverSocket.close();
		}
	}
}

 

public class Talker extends AbstractNodeMain {
  @Override
  public GraphName getDefaultNodeName() {
    return GraphName.of("rosjava/custom");
  }

  @Override
  public void onStart(final ConnectedNode connectedNode) {
	  final Log log = connectedNode.getLog();
	  final Publisher<std_msgs.String> publisher0 = connectedNode.newPublisher("custom_order", std_msgs.String._TYPE);
	  log.info("onStart");

	  connectedNode.executeCancellableLoop(new CancellableLoop() {
		  @Override
		  protected void loop() throws InterruptedException {
			  Server1 server1 = new Server1();
			  try {
				  server1.run();
				  log.info("custom_order : " + server1.custom_order);
			  }catch (Exception e) {
				  log.info("error");	
			  }

			  std_msgs.String str0 = publisher0.newMessage();
			  str0.setData(server1.custom_order);
			  publisher0.publish(str0);
			  Thread.sleep(10);
		  }
	  });
  }
}
