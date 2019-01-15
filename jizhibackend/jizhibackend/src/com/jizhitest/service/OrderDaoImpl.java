package com.jizhitest.service;

import java.util.List;

import com.jizhibackend.bean.Gift;
import com.jizhibackend.bean.Order;

public class OrderDaoImpl extends BaseDaoImpl{
	private  String addOrderStmt = "com.jizhitest.mapping.orderMapping.addOrder";
 
    public int addOrder(Order o)
    {
    	int i=0;
    	session.insert(addOrderStmt,o);
    	session.commit();
		return i;
    }
}
