package BikeRentalSystem;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author HP
 */
public class database {

    public static Connection connectDb(){
        try{
            Class.forName("com.mysql.jdbc.Driver");

            Connection connect= DriverManager.getConnection("jdbc:mysql://localhost/rentbike1","root","");
//            root is for default username while "" or empty is for the password
            return connect;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}