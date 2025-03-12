import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DisplayUsersServlet")
public class DisplayUsersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<html><head><title>Student Data</title></head>");
        out.println("<body style='font-family:Arial,sans-serif; text-align:center; background-color:#f2f2f2;'>");
        out.println("<h2>Registered Students</h2>");
        out.println("<table border='1' style='width:80%; margin:auto; background:white;'>");
        out.println("<tr style='background: #007bff; color:white;'>");
        out.println("<th>Username</th><th>Email</th><th>DOB</th><th>Gender</th><th>Mobile No</th><th>Course</th></tr>");

        try {
            // ✅ Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.OracleDriver");

            // ✅ Connect to Oracle Database
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "System", "admin");

            // ✅ Corrected SQL Query (Removed ID)
            String sql = "SELECT username, email, dob, gender, mobileno, course FROM users";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("username") + "</td>");
                out.println("<td>" + rs.getString("email") + "</td>");
                out.println("<td>" + rs.getDate("dob") + "</td>");
                out.println("<td>" + rs.getString("gender") + "</td>");
                out.println("<td>" + rs.getString("mobileno") + "</td>");
                out.println("<td>" + rs.getString("course") + "</td>");
                out.println("</tr>");
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            out.println("<p style='color:red;'>Database error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }

        out.println("</table>");
        out.println("<br><a href='wel.html' style='text-decoration:none; background:#007bff; color:white; padding:10px 20px; border-radius:5px;'>Back</a>");
        out.println("</body></html>");
    }
}
