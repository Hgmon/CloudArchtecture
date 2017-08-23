package kr.co.t3q.biz.customer.thrift.server;



import kr.co.t3q.biz.customer.thrift.CustomerApp.Iface;
import kr.co.t3q.biz.customer.thrift.dto.CustomerDTO;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @author Warmpark
 *
 */
public class CustomerAppHandler implements Iface {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerAppHandler.class.getName());
	@Override
	public CustomerDTO getCustomer(CustomerDTO dto) throws TException {
		LOGGER.info("Call public CustomerDTO getCustomer(CustomerDTO dto) throws TException");
		for (int i = 0; i < 10000; i++) {
			System.out.println("=== ^^^^^=====");
		}
		LOGGER.info("END public CustomerDTO getCustomer(CustomerDTO dto) throws TException");
		dto.setName("getCustomer");
		return dto;
	}

	@Override
	public CustomerDTO createCustomer(CustomerDTO dto) throws TException {
		LOGGER.info("Call public CustomerDTO createCustomer(CustomerDTO dto) throws TException");
		for (int i = 0; i < 10000; i++) {
			System.out.println("=== ^^^^^=====");
		}
		LOGGER.info("END public CustomerDTO createCustomer(CustomerDTO dto) throws TException");

		dto.setName("createCustomer");
		return dto;
	}

	@Override
	public CustomerDTO updateCustomer(CustomerDTO dto) throws TException {
		LOGGER.info("Call public CustomerDTO updateCustomer(CustomerDTO dto) throws TException");
		for (int i = 0; i < 10000; i++) {
			System.out.println("=== ^^^^^=====");
		}
		LOGGER.info("END public CustomerDTO updateCustomer(CustomerDTO dto) throws TException");


		
		dto.setName("updateCustomer");
		return dto;
	}

	@Override
	public CustomerDTO deleteCustomer(CustomerDTO dto) throws TException {
		LOGGER.info("Call public CustomerDTO deleteCustomer(CustomerDTO dto) throws TException");
		for (int i = 0; i < 10000; i++) {
			System.out.println("=== ^^^^^=====");
		}
		LOGGER.info("END public CustomerDTO deleteCustomer(CustomerDTO dto) throws TException");

		dto.setName("deleteCustomer");
		return dto;
	}

}

