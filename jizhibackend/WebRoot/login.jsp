<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'index.jsp' starting page</title>
  </head>
  <body>
    <form action="${pageContext.request.contextPath}/servlet/WXLogin" method="post">
  		用户名：<input type="text" name="username"/><br/>
  		密码：<input type="password" name="password"/><br/>
  		<input type="submit" value="登录"/>
    </form>
  </body>
</html>
