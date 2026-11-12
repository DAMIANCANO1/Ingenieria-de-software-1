/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Catflix;

/**
 *clase conexion 
 * que permitira conectarse a la base de datos 
 * en xampp
 * @author canod
 */

//importar la libreria de las herramientas a usar 
import SistemaBasico.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class conexion {
    Connection cn = null;
    public Connection conexionBD(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            cn = DriverManager.getConnection("jdbc:mysql://localhost/canoflix","root","");
            JOptionPane.showMessageDialog(null, "conexion establecida","conexion a base de datos", JOptionPane.WARNING_MESSAGE);
        } catch (ClassNotFoundException ex) {
            System.getLogger(conexion.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(conexion.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        return cn;
        
    }
    
    
    
}
