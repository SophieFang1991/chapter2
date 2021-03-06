package org.smart4j.chapter2.helper;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter2.util.PropsUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class DataBaseHelper {
    private static final Logger LOGGER= LoggerFactory.getLogger(DataBaseHelper.class);

    /**
     * 使用ThreadLocal来存放本地线程，不会出现线程不安全的问题
     * */
    private static final ThreadLocal<Connection> CONNECTION_HOLDER;
    private static  final QueryRunner QUERY_RUNNER;
    private static final BasicDataSource DATA_SOURCE;//使用Apache的连接池,解决每次连接数据库新建connection的问题

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static{
        CONNECTION_HOLDER=new ThreadLocal<>();
        QUERY_RUNNER=new QueryRunner();

        //读取数据库配置信息,对成员变量初始化
        Properties conf= PropsUtil.loadProps("config.properties");
        DRIVER=conf.getProperty("jdbc.driver");
        URL=conf.getProperty("jdbc.url");
        USERNAME=conf.getProperty("jdbc.username");
        PASSWORD=conf.getProperty("jdbc.password");

        //初始化连接池
        DATA_SOURCE=new BasicDataSource();
        DATA_SOURCE.setDriverClassName(DRIVER);
        DATA_SOURCE.setUrl(URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);

    }

    /**
     * 执行sql文件
     * */
    public  static void executeSqlFile(String filePath){
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        try{
            String sql;
            while ((sql=reader.readLine())!=null){
               executeUpdate(sql);
            }
        }catch (Exception e){
            LOGGER.error("execute sql file failure",e);
            throw  new RuntimeException(e);
        }
    }

    /**
     * 查询实体列表
     * */
    public static <T>List queryEntityList(Class<T> entityClass, String sql, Object... params){
        List<T> entityList;
        try{
            Connection connection = getConnection();
            entityList=QUERY_RUNNER.query(connection,sql,new BeanListHandler<T>(entityClass),params);
        }catch (SQLException e){
            LOGGER.error("query entityList error",e);
            throw  new RuntimeException(e);
        }
        return  entityList;
    }

    /**
     * 查询实体
     * */
    public static <T>T queryEntity(Class<T> entityClass,String sql,Object...params){
        T entity;
        try{
            Connection connection = getConnection();
            entity = QUERY_RUNNER.query(connection,sql,new BeanHandler<T>(entityClass),params);
        }catch (SQLException e){
            LOGGER.error("query entity failure",e);
            throw new RuntimeException(e);
        }
        return  entity;
    }
    /**
     * 执行查询语句
     * */
    public static  List<Map<String,Object>> executeQuery(String sql,Object... params) {
        List<Map<String, Object>> result = null;
        try {
            Connection connection = getConnection();
            result = QUERY_RUNNER.query(connection, sql, new MapListHandler(), params);
        } catch (Exception e) {
            LOGGER.error("execute query error", e);
        }
        return result;
    }
    /**
     * 执行更新语句（包括update，insert，delete）
     * */
    public static int executeUpdate(String sql,Object... params){
        int row=0;
        try{
            Connection connection = getConnection();
            row = QUERY_RUNNER.update(connection, sql, params);
        }catch (SQLException e){
            LOGGER.error("execute update error",e);
            throw new RuntimeException(e);
        }
        return  row;
    }
    /**
     * 插入实体
     * */
    public static <T>boolean insertEntity(Class<T> entityClass,Map<String,Object> fieldMap){
        if (MapUtils.isEmpty(fieldMap)){
            LOGGER.error("can not insert entity:fieldMap is empty");
            return  false;
        }
        String sql="insert into "+getTableName(entityClass);
        StringBuilder columns=new StringBuilder(" (");
        StringBuilder values=new StringBuilder(" (");
        for(String fieldName:fieldMap.keySet()){
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(", "),columns.length(),") ");
        values.replace(values.lastIndexOf(", "),values.length(),") ");
        sql+=columns+" values "+values;
        Object[] params=fieldMap.values().toArray();
        return  executeUpdate(sql,params)==1;
    }
    /**
     * 更新实体
     * */
    public static <T>boolean updateEntity(Class<T> entityClass,long id, Map<String,Object> fieldMap){
        if (MapUtils.isEmpty(fieldMap)){
            LOGGER.error("can not update entity: fieldMap is empty");
            return  false;
        }
        String sql="update "+getTableName(entityClass)+" set ";
        StringBuilder columns=new StringBuilder();
        for (String fieldName:
             fieldMap.keySet()) {
            columns.append(fieldName).append("=?,");
        }
        sql+=columns.substring(0,columns.lastIndexOf(","))+" where id=? ";
        List<Object> paramsList=new ArrayList<>();
        paramsList.addAll(fieldMap.values());
        paramsList.add(id);

        Object[] params=paramsList.toArray();
        return  executeUpdate(sql,params)==1;
    }
    /**
     * 删除实体
     * */
    public static <T>boolean deleteEntity(Class<T> entityClass,long id){
        String sql= "delete from "+getTableName(entityClass)+" where id=?";
        return  executeUpdate(sql,id)==1;
    }
    /**
     * 根据类(class对象)找到类名(string)
    * */
    public static String getTableName(Class entityClass){
        String className = entityClass.getSimpleName();
        return className;
    }

    /**
     * 获取数据库连接
     * */
    public static Connection getConnection(){
        Connection connection=CONNECTION_HOLDER.get();
        if (connection==null){
            try{
                //从使用DriverManager获取connection到使用连接池获取连接
                connection = DATA_SOURCE.getConnection();
            }catch (SQLException e){
                LOGGER.error("get connection failure",e);
                throw  new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(connection);
            }
        }
        return  connection;
    }

}
