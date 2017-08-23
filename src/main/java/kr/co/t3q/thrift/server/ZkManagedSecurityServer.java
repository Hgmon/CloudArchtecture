package kr.co.t3q.thrift.server;

import java.util.concurrent.CountDownLatch;

import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.transport.TServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ZkManagedSecurityServer extends ZkManagedServer{
	final static Logger LOGGER = LoggerFactory.getLogger(ZkManagedSecurityServer.class.getName());
	protected int zkSessionTimeOut = 20 * 1000;
	protected CountDownLatch connMonitor = new CountDownLatch(1);

	public ZkManagedSecurityServer() {
		super();
	}

	public void start(TProcessor processor, int port,String zkServers, String applicationServerGroup) {
		try {
			/*
			 * Use TSSLTransportParameters to setup the required SSL parameters.
			 * In this example we are setting the keystore and the keystore
			 * password. Other things like algorithms, cipher suites, client
			 * auth etc can be set.
			 */
			TSSLTransportParameters params = new TSSLTransportParameters();
			// The Keystore contains the private key
			params.setKeyStore("../../lib/java/test/.keystore", "thrift", null,	null);
	
			/*
			 * Use any of the TSSLTransportFactory to get a server transport
			 * with the appropriate SSL configuration. You can use the default
			 * settings if properties are set in the command line. Ex:
			 * -Djavax.net.ssl.keyStore=.keystore and
			 * -Djavax.net.ssl.keyStorePassword=thrift
			 * 
			 * Note: You need not explicitly call open(). The underlying server
			 * socket is bound on return from the factory class.
			 */
			TServerTransport serverTransport = TSSLTransportFactory.getServerSocket(port, 0, null, params);
			//TServer server = new TSimpleServer(new Args(serverTransport).processor(PROCESSOR));
	
			// Use this for a multi threaded server
			//TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(PROCESSOR));
			TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverTransport);
			args.processor(processor);
			//args.minWorkerThreads = 10;
			//args.maxWorkerThreads = 10000;
			TServer server = new TThreadPoolServer(args);
			
			
			//2. zookeper 연결유지
			zkServerStarted(port,zkServers,applicationServerGroup);
			
			System.out.println("Starting AppServer Secure...PORT "+port);
			server.serve();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}