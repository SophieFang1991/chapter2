package org.smart4j.chapter2.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropsUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);

    /**
     * 加载属性文件
     * */
    public static Properties loadProps(String fileName){
        Properties props=null;
        InputStream inputStream=null;
        try{
            inputStream=Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (inputStream==null){
                throw new FileNotFoundException(fileName+"file is not found");
            }
            props=new Properties();
            props.load(inputStream);
        }catch (IOException e){
            LOGGER.error("load properties file failure",e);
        }finally {
            if (inputStream!=null){
                try {
                    inputStream.close();
                }catch (IOException e){
                    LOGGER.error("close input stream failure",e);
                }

            }
        }
        return  props;
    }
}
