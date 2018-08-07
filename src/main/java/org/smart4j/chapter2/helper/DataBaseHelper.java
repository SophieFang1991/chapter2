package org.smart4j.chapter2.helper;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter2.util.PropsUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class DataBaseHelper {
    private static final Logger LOGGER= LoggerFactory.getLogger(DataBaseHelper.class);

    //private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static{
      Properties conf= PropsUtil.loadProps("config.properties");
      URL=conf.getProperty("jdbc.url");
      USERNAME=conf.getProperty("jdbc.username");
      PASSWORD=conf.getProperty("jdbc.password");
    }

    private static  final QueryRunner QUERY_RUNNER=new QueryRunner();
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
        }finally {
            closeConnection();
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
        }finally {
            closeConnection();
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
        } finally {
            closeConnection();
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
        }finally {
            closeConnection();
        }
        return  row;
    }

    /**
     * 使用ThreadLocal来存放本地线程，不会出现线程不安全的问题
     * */
    private static final ThreadLocal<Connection> CONNECTION_HOLDER=new ThreadLocal<>();
    /**
     * 获取数据库连接
     * */
    public static Connection getConnection(){
        Connection connection=CONNECTION_HOLDER.get();
        if (connection==null){
            try{
                connection= DriverManager.getConnection(URL,USERNAME,PASSWORD);
            }catch (SQLException e){
                LOGGER.error("get connection failure",e);
                throw  new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(connection);
            }
        }
        return  connection;
    }
    /**
     * 关闭数据库链接
     * */
    public static void closeConnection(){
        Connection connection = CONNECTION_HOLDER.get();
        if (connection!=null){
            try{
                connection.close();
            }catch (SQLException e){
                LOGGER.error("connection close failure",e);
            }finally {
                CONNECTION_HOLDER.remove();
            }

        }
    }

}
