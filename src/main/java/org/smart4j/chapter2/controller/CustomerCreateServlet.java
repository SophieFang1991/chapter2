package org.smart4j.chapter2.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * 创建客户
 * */
@WebServlet(name = "CustomerCreateServlet",urlPatterns = "/customer_create")
public class CustomerCreateServlet extends HttpServlet {
    /**
     * 处理创建用户请求
     * */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //todo
    }

    /**
     * 进入创建用户界面
     * */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //todo
    }
}
