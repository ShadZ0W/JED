/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.valo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 *
 * @author User
 */
@WebServlet("/addCart")
public class CheckoutServlet extends HttpServlet {

    @Resource(name = "jdbc/Proj")
    private DataSource orders;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String itemID = req.getParameter("itemId");
        int iID = Integer.parseInt(itemID);
        String quan = req.getParameter("quantity");
        int qty =  Integer.parseInt(quan);
        String price = req.getParameter("price");
        double pri = Double.parseDouble(price);
        String pts = req.getParameter("points");
        int pt = Integer.parseInt(pts);

        String email = req.getParameter("email");
        String cust = req.getParameter("custid");

        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement st = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        PreparedStatement ps4 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        try {
            conn = orders.getConnection();
            st = conn.prepareStatement("SELECT * FROM orders where customerid IN(Select customerId from customer where email = '" + email + "')");
            rs = st.executeQuery();
            if (rs.next()) {
                PrintWriter err = resp.getWriter();
                err.println("<script>alert(\"\")</script>");
                err.println("<script type=\"text/javascript\">");
                err.println("alert('Failed to purchase');");
                err.println("location='search.jsp';");
                err.println("</script>");

            } else {

                ps = conn.prepareStatement("INSERT INTO orders (customerid,orderprice,orderpoints) values(?,?,?)");

                ps.setString(1, cust);
                ps.setDouble(2, pri);
                ps.setInt(3, pt);
                ps.executeUpdate();
                HttpSession session = req.getSession();
                session.setAttribute("od", null);
                PrintWriter err = resp.getWriter();
                err.println("<script type=\"text/javascript\">");
                err.println("alert('Successfully purchased');");
                err.println("location='search.jsp';");
                err.println("</script>");
                String select = "(SELECT orderid from orders where customerid ='" + cust + "')";
                String select2 = "(SELECT itemId from item where itemId = '"+iID+"')";
                ps2 = conn.prepareStatement(select);
                rs2 = ps2.executeQuery();
                ps3 = conn.prepareStatement(select2);
                rs3 = ps3.executeQuery();
                String insert = "INSERT INTO orderdetails(";
                String values = "values(";
                
                if (select != null){
                    insert += "orderid";
                    values += "?";
                }
                if(select2 != null){
                    insert +=  ", itemid";
                    values += ", ?";
                }
                if (qty != 0){
                    insert +=  ", quantity";
                    values += ", ?";
                }
                
                insert += ") ";
                values += ") ";
                ps4 = conn.prepareStatement(insert + values);
                int count = 1;
                if(rs2.next()){
                   ps4.setInt(count++,rs2.getInt(1));
                   System.out.println(rs2.getInt(1));
                }
                 if(rs3.next()){
                    ps4.setInt(count++,rs3.getInt(1));
                }
                 if(qty !=0){
                     ps4.setInt(count++,qty);
                 }
                ps4.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.err.println(ex.getMessage());
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.err.println(ex.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.err.println(ex.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.err.println(ex.getMessage());
                }
            }
        }
    }

}
