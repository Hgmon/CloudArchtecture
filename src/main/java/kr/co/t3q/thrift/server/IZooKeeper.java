package kr.co.t3q.thrift.server;

import org.apache.zookeeper.Watcher;

public interface IZooKeeper extends Watcher{
	
	public void zkServerStarted(int port,String zkServers, String applicationServerGroup) ;
	
}
