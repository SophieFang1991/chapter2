package org.smart4j.chapter2.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter2.helper.DataBaseHelper;
import org.smart4j.chapter2.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 提供客户数据服务
 * */
public class CustomerService {
    private static final Logger LOGGER=LoggerFactory.getLogger(CustomerService.class);
    /*
    添加config.properties 文件 将这些配置信息配置到该配置文件中
   private static final String DRIVER="com.mysql.jdbc.Driver";
    private static final String URL="jdbc:mysql://localhost:3306/demo?serverTimezone=GMT&useSSL=false";
    private static final String USERNAME="root";
    private static final String  PASSWORD="123456";
    添加DatabaseHelper文件，将获取数据库连接和关闭数据库连接的方法放入helper文件中
    */

    /**
     * 获取客户列表
     * */
    public List<Customer> getCustomerList(){
       // Connection connection=DataBaseHelper.getConnection();
        List<Customer> customerList;
        //Class.forName(DRIVER);
       //connection=DriverManager.getConnection(URL,USERNAME,PASSWORD);
        String sql="select * from customer";
        customerList = DataBaseHelper.queryEntityList(Customer.class,sql);
      /* PreparedStatement statement= connection.prepareStatement(sql);
       ResultSet resultSet=statement.executeQuery();
       while(resultSet.next()){
           Customer customer=new Customer();
           customer.setId(resultSet.getLong("id"));
           customer.setName(resultSet.getString("name"));
           customer.setContact(resultSet.getString("contact"));
           customer.setEmail(resultSet.getString("email"));
           customer.setTelephone(resultSet.getString("telephone"));
           customer.setRemark(resultSet.getString("remark"));
            customerList.add(customer);
       }*/
        return  customerList;
    }
    /**
     * 获取客户信息
     * */
        public  Customer getCustomer(long id){
            //todo
            return  null;
        }
    /**
     * 创建客户
     * */
    public  boolean createCustomer(Map<String,Object> fieldMap){
        //todo
        return  false;
    }
    /**
     * 更新用户
     * */
    public boolean updateCustomer(long id,Map<String,Object> fieldMap){
        //todo
        return  false;
    }
    /**
     * 删除用户
     * */
    public  boolean deleteCustomer(long id){
        //todo
        return  false;
    }

}
