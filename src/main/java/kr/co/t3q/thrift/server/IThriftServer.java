package kr.co.t3q.thrift.server;

import org.apache.thrift.TProcessor;

public interface IThriftServer {
	public void start(TProcessor processor, int port,String zkServers, String applicationServerGroup) ;

}