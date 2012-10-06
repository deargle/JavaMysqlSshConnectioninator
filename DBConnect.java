package dbconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Deargle
 */
public class DBConnect {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String db_name = "";
        String db_host = "localhost";
        String db_user = "";
        String db_password = "";
        
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            if (db_name.isEmpty() || db_host.isEmpty() || db_user.isEmpty() || db_password.isEmpty()) {
                throw new Exception("Set all of the the db_vars at the beginning of DBConnect, please.");
            }
            
            int localBindingPort = PortForwarder.openConnection();
            
            String url = String.format("jdbc:mysql://%s:%s/%s", db_host, localBindingPort, db_name);
            
            con = DriverManager.getConnection(url, db_user, db_password);
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM internetsearch_submit");

            while (rs.next()) {
                System.out.println(rs.getString(2)); // dpeck5, this is where you work your magic
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DBConnect.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } catch (Exception e) {
            System.err.println(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DBConnect.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
            
            // Close the ssh tunnel, too
            try {
                PortForwarder.closeConnection();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}
