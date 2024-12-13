
import java.sql.*;
import java.io.IOException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author tabas
 */
public class Main {
    public static void main (String args[]) throws IOException {
        MAinpage mainpageobj = new MAinpage();
        mainpageobj.show();
        
        Connection conn = (Connection) DBconn.connect();
        if (conn != null) {
            System.out.println("Database connection is active.");
        }

    }
    
}
