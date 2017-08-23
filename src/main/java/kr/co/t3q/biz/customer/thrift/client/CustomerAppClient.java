package kr.co.t3q.biz.customer.thrift.client;

import kr.co.t3q.biz.customer.thrift.CustomerApp;
import kr.co.t3q.biz.customer.thrift.dto.CustomerDTO;
import kr.co.t3q.thrift.client.Client;

import org.apache.thrift.protocol.TProtocol;

public class CustomerAppClient extends Client {

	final static String ZK_SERVERS = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183,127.0.0.1:2184,127.0.0.1:2185";
	final static String ZK_APP_SERVER_GROUP_NAME = "/CustomerAppServer";

	public static void main(String[] args) throws Exception {
		CustomerAppClient client = new CustomerAppClient();
		client.startZKClient(ZK_SERVERS, ZK_APP_SERVER_GROUP_NAME);
		TProtocol protocol = null;

		while (true) {
			// System.out.println("====================================");
			Thread.sleep(1000 * 1);
			try {
				protocol = client.getProtocol(ZK_APP_SERVER_GROUP_NAME);

				// 3. Client 생성
				CustomerApp.Client app = new CustomerApp.Client(protocol);
				CustomerDTO dto = new CustomerDTO();
				// dto.setName("ClientCall");
				dto = app.createCustomer(dto);
				System.out.println("Received [" + dto.getName() + "]");

				protocol.getTransport().close();
			} catch (Exception e) {
				System.out.println(e.getMessage() + "로 인해 요청을 처리하지 못하고 다음 건을 진행 합니다.");
			}

		}

	}

}