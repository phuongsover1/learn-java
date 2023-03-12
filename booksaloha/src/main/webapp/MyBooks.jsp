<%--
Created by IntelliJ IDEA.
User: phuong
Date: 03/08/2022
Time: 11:33
To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Books</title>
</head>
<body style="font-family:Arial;font-size:20px;">
<div style="height:65px;align: center;background: #DB5227;font-family: Arial;color: white;">
    <br><b>
    <a href="" style="font-family:garamond;font-size:34px;margin:0 0 0 10px;color:white;text-decoration: none;">Books<i>Aloha!</i></a></b>
</div>
<br><br>

<table>
    <%-- Displaying books --%>

    <c:forEach var="book" items="${myBooks}">
        <c:if test="${book.rating > 4.0}">
            <tr>
                <td>
                    <img src=" ${book.imageUrl}">
                </td>

                <td style="color:gray;">
                    By <span style="color: #B13100;">${book.author}</span>
                    <br><br>
                    Rating: <span style="color: #B13100;">${book.rating}</span>
                </td>
            </tr>
        </c:if>
    </c:forEach>


</table>
</body>
</html>