package org.smart4j.chapter2.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter2.helper.DataBaseHelper;
import org.smart4j.chapter2.model.Customer;


import java.util.List;
import java.util.Map;

/**
 * 提供客户数据服务
 * */
public class CustomerService {
    private static final Logger LOGGER=LoggerFactory.getLogger(CustomerService.class);
    /**
     * 获取客户列表
     * */
    public List<Customer> getCustomerList(){
        List<Customer> customerList;
        String sql="select * from customer";
        customerList = DataBaseHelper.queryEntityList(Customer.class,sql);
        return  customerList;
    }
    /**
     * 获取客户信息
     * */
        public  Customer getCustomer(long id){
            String sql="select * from customer where id=? ";
            return  DataBaseHelper.queryEntity(Customer.class,sql,id);
        }
    /**
     * 创建客户
     * */
    public  boolean createCustomer(Map<String,Object> fieldMap){
        return DataBaseHelper.insertEntity(Customer.class,fieldMap);
    }
    /**
     * 更新用户
     * */
    public boolean updateCustomer(long id,Map<String,Object> fieldMap){
        return  DataBaseHelper.updateEntity(Customer.class,id,fieldMap);
    }
    /**
     * 删除用户
     * */
    public  boolean deleteCustomer(long id){
        return  DataBaseHelper.deleteEntity(Customer.class,id);
    }

}
