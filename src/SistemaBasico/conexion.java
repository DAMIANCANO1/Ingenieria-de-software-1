/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SistemaBasico;

/**
 *clase conexion 
 * que permitira conectarse a la base de datos 
 * en xampp
 * @author canod
 */

//importar la libreria de las herramientas a usar 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class conexion {
    //1.crear una variable que permita ejecutar las actividades en la BD
    //desde java
    Connection cn = null;
    
    //2/definir un memtodo que permite establecer
    //la conexion con la BD desde java
    public Connection conexionBD(){
        try {
            //2.1 definir el tipo de lenguaje a utilizar
            //como base de datos desde java

            Class.forName("com.mysql.jdbc.Driver");
            //2.2. definir la ruta donde se encuentra la BD 
            cn = DriverManager.getConnection("jdbc:mysql://localhost/empresa","root","");
            JOptionPane.showMessageDialog(null, "conexion establecida","conexion a base de datos", JOptionPane.WARNING_MESSAGE);
        } catch (ClassNotFoundException ex) {
            System.getLogger(conexion.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(conexion.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        //2.5devolver el resultado de la conexion
        return cn;
        
    }
    
    
    
}
