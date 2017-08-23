package kr.co.t3q.thrift.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class Client  implements Watcher {
	CountDownLatch connMonitor = new CountDownLatch(1);
	Object nodeMonitor = new Object();
	List<String> appServerList = new ArrayList<String>(1);
	ZooKeeper zk;
	private Random rand = new Random();
	AppServerWatcher appServerWatcher;

	public Client() {
		super();
	}

	public void startZKClient( String zkServers,String appServerGroupName) throws Exception {
		appServerWatcher = new AppServerWatcher(appServerGroupName);
		zk = new ZooKeeper(zkServers, 20*1000, this);
		connMonitor.await();
	
		synchronized (nodeMonitor) {
			loadAppServers(appServerGroupName);
			if (appServerList.size() == 0) {
				nodeMonitor.wait();
			}
		}
	}

	protected void loadAppServers(String appServerGroupName) throws Exception {
		appServerList.clear();
		System.out.println("Load HelloServer: " + appServerGroupName);
		appServerList.addAll(zk.getChildren(appServerGroupName,	appServerWatcher));
		System.out.println("LoadAppServers ============]" + appServerList + "[=============");

		if (appServerList.size() != 0) {
			nodeMonitor.notifyAll();
		}
	}

	protected TProtocol getProtocol(String appServerGroupName) {
		String appServer =null;
		TTransport transport =null; ;
		TProtocol protocol = null;
		
		synchronized (nodeMonitor) {
			if (appServerList.size() == 0) {
				System.out.println("No live server");
				try {
					nodeMonitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else if (appServerList.size() == 1) {
				appServer = appServerList.get(0);
			} else {
				appServer = appServerList.get(rand.nextInt(appServerList.size()));
			}
		}			
	
		String[] serverInfos = appServer.split(":");
		
		//1. Transport 생성
		transport = new TSocket(serverInfos[0],Integer.parseInt(serverInfos[1]));
		try {
			transport.open();
		} catch (TTransportException e) {
			e.printStackTrace();
			try {
				nodeMonitor.wait();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		//2. Protocol 생성
		protocol = new TBinaryProtocol(transport);
		return  protocol;
	}

	
	
	@Override
	public void process(WatchedEvent event) {
		if (event.getType() == Event.EventType.None) {
			if (event.getState() == Event.KeeperState.SyncConnected) {
				System.out.println("======== Zookeeper와의 세션이 연결되었습니다. ====");
				connMonitor.countDown();
			}
		}
	}
	
	
	class AppServerWatcher implements Watcher {
		private String appServerGroupName;
		
		public AppServerWatcher(String appServerGroupName) {
			this.appServerGroupName = appServerGroupName;
		}

		@Override
		public void process(WatchedEvent event) {
			System.out.println(event.getType().toString() + "======== Zookeeper Node 정보가 변경되었습니다.  ====");
			synchronized (nodeMonitor) {
				try {
					loadAppServers(appServerGroupName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}