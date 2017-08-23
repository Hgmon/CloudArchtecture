package kr.co.t3q.biz.customer.thrift.server;

import kr.co.t3q.biz.customer.thrift.CustomerApp;
import kr.co.t3q.thrift.server.ZkManagedServer;

/**
 * 
 * @author Warmpark
 * 
 */
public class CustomerAppServer extends ZkManagedServer {
	final static String ZK_SERVERS = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183,127.0.0.1:2184,127.0.0.1:2185";
	final static String APP_SERVER_GROUP = "/CustomerAppServer";
	final static CustomerAppHandler HANDLER = new CustomerAppHandler();
	final static CustomerApp.Processor<CustomerAppHandler> PROCESSOR = new CustomerApp.Processor<CustomerAppHandler>(HANDLER);
	static int port;

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: AppServer port ");
			System.exit(0);
		}

		port = Integer.valueOf(args[0]).intValue();

		Runnable simple = new Runnable() {
			public void run() {
				runServer(port);
			}
		};
		new Thread(simple).start();
	}

	private static void runServer(int port) {
		if(LOGGER.isInfoEnabled()){
			LOGGER.info("======= Main Method is caled for Server Star!!!!!!!!");
		}
		CustomerAppServer server = new CustomerAppServer();
		try {
			server.start(PROCESSOR, port, ZK_SERVERS, APP_SERVER_GROUP);
		} catch (NumberFormatException e) {
			System.out.println("port must be number ");
			System.exit(0);
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

}
