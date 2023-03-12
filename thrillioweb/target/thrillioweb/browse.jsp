<%--
  Created by IntelliJ IDEA.
  User: phuong
  Date: 04/08/2022
  Time: 11:13
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>thrill.io</title>
</head>
<body style="font-family:Arial;font-size:20px;">

<div style="height:65px;align:center;background:#DB5227;font-family: Arial;color: white;">
<br><b>
    <a href="" style="font-family:garamond;font-size:34px;margin:0px 0px 0px 10px;color:white;text-decoration: none;">thrill.io</a></b>

<div style="height:25px;background: #DB5227;font-family: Arial;color: white;">
    <b>
        <a href="<%=request.getContextPath()%>/bookmark/mybooks" style="font-size:16px;color:white;margin-left:1150px;text-decoration:none;">My Books</a>
        <a href="<%=request.getContextPath()%>/auth/logout"
           style="font-size: 16px; color: white;margin-left: 10px; text-decoration: none;">Logout</a>
    </b>
</div>
</div>
<br><br>

<table>
    <%-- Displaying books --%>

    <c:forEach var="book" items="${books}">
        <tr>
            <td>
                <img src=" ${book.imgUrl}">
            </td>

            <td style="color:gray;">
                By <span style="color: #B13100;">${book.authors[0]}</span>
                <br><br>
                Rating: <span style="color: #B13100;">${book.amazonRating}</span>
                <br><br>
                Publication Year: <span style="color: #B13100;">${book.publicationYear}</span>
                <br><br>
                <a href="<%= request.getContextPath()%>/bookmark/save?bid=${book.id}" style="font-size: 18px; color: #0068A6; font-weight: bold; text-decoration: none">Save</a>

            </td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
    </c:forEach>


</table>
</body>
</html>