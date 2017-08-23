
include "CustomerApp.dto.thrift"

namespace java kr.co.t3q.biz.customer.thrift
service CustomerApp {
    CustomerApp.dto.CustomerDTO getCustomer(1:CustomerApp.dto.CustomerDTO dto)
    CustomerApp.dto.CustomerDTO createCustomer(1:CustomerApp.dto.CustomerDTO dto)
    CustomerApp.dto.CustomerDTO updateCustomer(1:CustomerApp.dto.CustomerDTO dto)
    CustomerApp.dto.CustomerDTO deleteCustomer(1:CustomerApp.dto.CustomerDTO dto)
} 