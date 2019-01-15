<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%
request.setCharacterEncoding("GBK");
	String checkCode = request.getParameter("checkCode");
	if ("".equals(checkCode) || checkCode == null) {
		out.println("<script>alert('请输入验证码！');window.location.href='test/index.jsp';</script>");
	} else {
		if (!checkCode.equals(session.getAttribute("randCheckCode"))) {
			out.println("<script>alert('您输入的验证码不正确！');history.back(-1);</script>");
		}
	}
	if("".equals(request.getParameter("username")) || "".equals(request.getParameter("pwd"))){
		out.println("<script>alert('请输入用户名或密码！');window.location.href='test/index.jsp';</script>");
	}else{
		if(!("mr".equals(request.getParameter("username")) && "mrsoft".equals(request.getParameter("pwd")))){
			out.println("<script>alert('您输入的用户名或密码不正确！');window.location.href='test/index.jsp';</script>");
		}
	}
%>
<html>
	<head>
	<title>处理页</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
<link href="Css/style.css" rel="stylesheet">
	</head>

	<body>
<div style="height:70px"></div>
		<table width="799" height="479" border="0" align="center" cellpadding="0" cellspacing="0" background="images/success.gif">
          <tr>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td align="center">欢迎访问本网站！<br>
            <a href="test/index.jsp">返回</a></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
          </tr>
        </table>
	</body>
</html>
