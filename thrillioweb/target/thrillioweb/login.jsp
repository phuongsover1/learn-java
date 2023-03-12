<%--
  Created by IntelliJ IDEA.
  User: phuong
  Date: 04/08/2022
  Time: 21:29
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
<div style="height:65px;align: center;background: #DB5227;font-family: Arial;color: white;"
">
<br><b>
    <a href="" style="font-family:garamond;font-size:34px;margin:0 0 0 10px;color:white;text-decoration: none;">thrill.io</a></b>
</div>
<br><br>
<form method="POST" action="<%=request.getContextPath()%>/auth">
    <fieldset>
        <legend>Log In</legend>
        <table>
            <tr>
                <td><label>Email:</label></td>
                <td>
                    <input type="text" name="email"><br>
                </td>
            </tr>
            <tr>
                <td><label>Password:</label></td>
                <td>
                    <input type="password" name="password"><br>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td><input type="submit" name="submitLoginForm" value="Log In"></td>
            </tr>
        </table>
    </fieldset>
</form>

</body>
</html>