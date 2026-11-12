/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Catflix;

/**
 *
 * @author canod
 */
public class Play {
    public static void main(String[] args) {
        conexion con = new conexion();
        con.conexionBD();
        Inicio inicio = new Inicio();
        inicio.setLocationRelativeTo(null); // Centra la ventana en tu pantalla
        inicio.setVisible(true);
        
    }
    
}
