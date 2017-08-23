package kr.co.t3q.thrift.server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;

import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZkManagedServer implements IZooKeeper, IThriftServer {
	final public static Logger LOGGER = LoggerFactory.getLogger(ZkManagedServer.class.getName());
	protected int zkSessionTimeOut = 20 * 1000;
	protected CountDownLatch connMonitor = new CountDownLatch(1);

	public ZkManagedServer() {
		super();
	}



	@Override
	public void start(TProcessor processor, int port,String zkServers, String applicationServerGroup) {
		try {
			TServerTransport serverTransport = new TServerSocket(port);
			// TServer server = new TSimpleServer(new Args(serverTransport).processor(PROCESSOR));
	
			//1. Use this for a multithreaded server
			TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverTransport);
			args.processor(processor);
			args.minWorkerThreads = 100;
			args.maxWorkerThreads = 2000;
			TServer server = new TThreadPoolServer(args);
			
			//2. zookeper ��������
			zkServerStarted(port,zkServers,applicationServerGroup);
			
			
			System.out.println("Starting AppServer...PORT "+port);
			//3. Start Server
			server.serve();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void zkServerStarted(int port,String zkServers, String applicationServerGroup) {
		ZooKeeper zk;
		try {
			zk = new ZooKeeper(zkServers, zkSessionTimeOut, this);
			
			//ī��尡 0�� �� �� ����(Zookeeper ������ ������ �� ������) ��� Thread�� ��� ��Ų��. 
			//connMonitor.countDown()�޼ҵ�� ī��带 1 ���� ��Ų��. 
			connMonitor.await();
			
			
			if (zk.exists(applicationServerGroup, false) == null) { //APPSERVER_GROUP�� ��ϵ��� �ʾҴٸ�
				zk.create(applicationServerGroup, null, Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
			}
			
			String serverInfo = InetAddress.getLocalHost().getHostName() + ":" 	+ port;
			if(zk.exists(applicationServerGroup + "/" + serverInfo, false)== null){
				zk.create(applicationServerGroup + "/" + serverInfo, null,	Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			}else{
				LOGGER.error("Port Number "+port +" is allread used !!");
				System.exit(0);
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(WatchedEvent event) {
		LOGGER.info(event.toString());
	    if(event.getType() == Event.EventType.None) {//���ǿ��� ���¿� ���� �̺�Ʈ �߻� ��
	      if(event.getState() == Event.KeeperState.SyncConnected) {//��Ű�� ������ ������ �Ϸ�� ����
	    	 connMonitor.countDown(); 
	      }
	    }
	}
	//===== �̻� zookeeper ����

}