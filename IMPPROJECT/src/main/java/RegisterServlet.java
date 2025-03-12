import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String dobStr = request.getParameter("dob");
        String set_username = request.getParameter("set_username"); // Corrected
        String gender = request.getParameter("gender");
        String mobileno = request.getParameter("mobileno"); // Corrected
        String course = request.getParameter("course");

        // Convert dob (String) to java.sql.Date
        java.sql.Date dob = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = sdf.parse(dobStr);
            dob = new java.sql.Date(parsedDate.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error parsing date: " + e.getMessage());
            return;
        }

        // Database Connection and Insertion
        try {
            // ✅ Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.OracleDriver");

            // ✅ Connect to Oracle Database
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "System", "admin");
            conn.setAutoCommit(false); 

            String sql = "INSERT INTO users (username, email, password, dob, set_username, gender, mobileno, course) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setDate(4, dob);
            ps.setString(5, set_username);
            ps.setString(6, gender);
            ps.setString(7, mobileno);
            ps.setString(8, course);

            int rowsInserted = ps.executeUpdate();
            
            if (rowsInserted > 0) {
                conn.commit();  
                response.sendRedirect("succ.html");
            } else {
                response.getWriter().println("Error in registration.");
            }

            ps.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Database error: " + e.getMessage());
        }
    }
}
