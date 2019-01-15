/**
 * 注册非空验证 
 */
function check()
{  
 fr = document.registerform;
 if(fr.name.value=="")//用户名不能为空
 {
alert("用户名不能为空！");
fr.name.focus();
 return false;
 }
 if(fr.email.value == "")//邮箱必须填写
 {
alert("邮箱不能为空！");
fr.email.focus();
 return false;
 }
 if(fr.email.value != "")//验证email的格式
 {
if(!isEmail(fr.email.value)) {
 alert("邮箱格式不正确，请重新输入！");
fr.email.focus();
 return false;
 }
 }
 if((fr.pwd1.value != "") || (fr.pwd.value != ""))//两次密码输入必须一致
 {
if(fr.pwd1.value!=fr.pwd.value)
 {
 alert("密码不一致,请重新输入！");
fr.pwd1.focus();
 return false;
 }
 }
 else {//密码也不能为空
alert("密码不能为空！");
fr.pwd1.focus();
 return false;
 }
 fr.submit();
}




function isEmail(theStr){
    var atindex=theStr.indexOf('@');
    var dotindex=theStr.indexOf('.',atindex);
    var flag=true;
    thesub=theStr.substring(0,dotindex+1);
    if((atindex<1)||(atindex!=theStr.lastIndexOf('@'))||(dotindex<atindex+2)||(theStr.length<=thesub.length)){
      flag=false;
    }else{
      flag=true;
    }
    return(flag);
}
