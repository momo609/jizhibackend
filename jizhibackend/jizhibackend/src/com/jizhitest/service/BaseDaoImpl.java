package com.jizhitest.service;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class BaseDaoImpl {
	private SqlSessionFactory sessionFactory;
	protected SqlSession session;
	private static final String resource = "conf.xml";
	public BaseDaoImpl() {

        InputStream is = BaseDaoImpl.class.getClassLoader().getResourceAsStream(resource);
         sessionFactory = new SqlSessionFactoryBuilder().build(is);
         session=sessionFactory.openSession();
	}
public static void main(String[] args) {
	int i=1,f=5;
	int p;
	p=(int)((float)i/(float)f*100);
	System.out.println(p);
	
}
}
