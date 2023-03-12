<%--
  Created by IntelliJ IDEA.
  User: phuong
  Date: 04/08/2022
  Time: 13:11
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thrill.io</title>
</head>
<body style="font-family:Arial;font-size:20px;">
<div style="height:65px;align: center;background: #DB5227;font-family: Arial;color: white;">
    <br><b>
    <a href="" style="font-family:garamond;font-size:34px;margin:0 0 0 10px;color:white;text-decoration: none;">thrill.io</a></b>
    <div style="height:25px;background: #DB5227;font-family: Arial;color: white;">
        <b>
            <a href="<%=request.getContextPath()%>/bookmark"
               style="font-size:16px;color:white;margin-left:1150px;text-decoration:none;">Browse</a>
            <a href="<%=request.getContextPath()%>/auth/logout"
               style="font-size: 16px; color: white;margin-left: 10px; text-decoration: none;">Logout</a>
        </b>
    </div>
</div>
<br><br>

<div style="font-size: 24px;color: #333333;padding: 15px 0px 0px;border-bottom: #333333 1px solid;clear: both;">Saved
    Items
</div>
<br><br>

<c:choose>
    <c:when test="${!empty(books)}">
        <table>
            <c:forEach var="book" items="${books}">
                <tr>
                    <td>
                        <img src="${book.imgUrl}" width="175" height="200">
                    </td>

                    <td style="color:gray;">
                        By <span style="color: #B13100;">${book.authors[0]}</span>
                        <br><br>
                        Rating: <span style="color: #B13100;">${book.amazonRating}</span>
                        <br><br>
                        Publication Year: <span style="color: #B13100;">${book.publicationYear}</span>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                </tr>

            </c:forEach>

        </table>
    </c:when>
    <c:otherwise>
        <br><br>
        <span style="font-size: 24px;color: #333333;margin:400px;">You haven't saved any items yet!</span>
    </c:otherwise>
</c:choose>
</body>
</html>