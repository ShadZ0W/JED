<%-- 
    Document   : checkout
    Created on : Dec 11, 2019, 12:50:23 PM
    Author     : User
--%>

<%@page import="fi.valo.CustomerRecord"%>
<%@page import="java.util.List"%>
<%@page import="fi.valo.Orders"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Checkout Page</title>
    </head>
    <body>
        <h1>Checkout</h1>

        <%CustomerRecord cr = (CustomerRecord) session.getAttribute("cust");
            if (session.getAttribute("cust") == null) {
                response.sendRedirect("index.jsp");
            } else {
                double totalPrice = 0.0;
                int totalPoint = 0;
                Boolean b = false;
                List<Orders> Results = (List<Orders>) session.getAttribute("od");
                if (Results == null) {%>
        <p>Hello, <%=cr.getUname()%> </p>
        <p>Your cart is empty</p>
        <a href="search.jsp">Back to search</a>
        <%

        } else {
            for (Orders order : Results) {

                totalPrice += order.getPrice();
                totalPoint += order.getPoints();
        %>
        <form action="addCart" method="post">
            <input type ="hidden" name="itemId" value =<%=order.getItemID()%>>
            <input type ="hidden" name="quantity" value =<%=order.getQuantity()%>>

            <%}%>
            Name:<%=cr.getUname()%><br>
            Total Points Earned: <%=totalPoint%><br>
            Total Price Payable: <%=totalPrice%><br>   

            <input type="hidden" name="custid" value="<%=cr.getCustId()%>">
            <input type="hidden" name="price" value="<%=totalPrice%>">
            <input type="hidden" name="points" value="<%=totalPoint%>">

            <input type ="submit" name ="Pay" value = "Paynow">

        </form>                  

        <%}
            }%>
    </body>
</html>
