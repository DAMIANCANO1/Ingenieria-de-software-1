/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package SistemaBasico;

/**
 *
 * @author canod
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ModificaUsuarios extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(ModificaUsuarios.class.getName());

    conexion miConexion = new conexion();
    Connection cn = miConexion.conexionBD();

    PreparedStatement pps;
    ResultSet rs;

    public ModificaUsuarios() {
        initComponents();
        obtenerRegistroUsuarios();
        this.setTitle("MODIFICAR USUARIOS");
        this.setLocationRelativeTo(null);
    }

    // 1. Carga los IDs de la tabla empleados en comboUsuarios
    public void obtenerRegistroUsuarios() {
        try {
            pps = cn.prepareStatement("SELECT id FROM empleados");
            rs = pps.executeQuery();
            comboUsuarios.removeAllItems();
            while (rs.next()) {
                comboUsuarios.addItem(String.valueOf(rs.getInt("id")));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al obtener IDs de empleados", ex);
        }
    }

    // 2. Busca la información del ID seleccionado en comboUsuarios y la llena en el formulario
    public void obtieneInfoRegistros() {
        if (comboUsuarios.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Selecciona un ID válido de la lista.");
            return;
        }

        String registro = comboUsuarios.getSelectedItem().toString();
        try {
            pps = cn.prepareStatement("SELECT * FROM empleados WHERE id = ?");
            pps.setString(1, registro);
            rs = pps.executeQuery();

            if (rs.next()) {
                campoNombre.setText(rs.getString("nombre"));
                campoAp.setText(rs.getString("apellidop"));
                CampoApMat.setText(rs.getString("apellidom"));
                campoTelefono.setText(rs.getString("telefono"));
                campoEmail.setText(rs.getString("correo"));
                menuEC.setSelectedItem(rs.getString("estado_civil"));

                if (rs.getInt("hijos") == 1) {
                    radioSi.setSelected(true);
                } else {
                    radioNo.setSelected(true);
                }

                // Limpiar checkboxes antes de verificar cuáles activar
                checkLibros.setSelected(false);
                checkMusica.setSelected(false);
                checkDeportes.setSelected(false);
                checkOtros.setSelected(false);

                String intereses = rs.getString("intereses");
                if (intereses != null && !intereses.isEmpty()) {
                    String[] elementos = intereses.split("[ ,]+");
                    for (String el : elementos) {
                        if (el.equalsIgnoreCase("Libros")) {
                            checkLibros.setSelected(true);
                        }
                        if (el.equalsIgnoreCase("Musica")) {
                            checkMusica.setSelected(true);
                        }
                        if (el.equalsIgnoreCase("Deportes")) {
                            checkDeportes.setSelected(true);
                        }
                        if (el.equalsIgnoreCase("Otros")) {
                            checkOtros.setSelected(true);
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Registro no encontrado", "ERROR", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al consultar usuario", ex);
        }
    }

    // 3. Captura los datos editados y ejecuta el UPDATE con los 9 parámetros
    public void capturaActualizaDatos() {
        if (comboUsuarios.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Selecciona un ID antes de actualizar.");
            return;
        }

        String idRegistro = comboUsuarios.getSelectedItem().toString();
        String nombre = campoNombre.getText().trim();
        String aP = campoAp.getText().trim();
        String aM = CampoApMat.getText().trim();
        String telefono = campoTelefono.getText().trim();
        String email = campoEmail.getText().trim();
        String estado_civil = menuEC.getSelectedItem() != null ? menuEC.getSelectedItem().toString() : "";

        int hijos = radioSi.isSelected() ? 1 : 0;

        StringBuilder interesesBuilder = new StringBuilder();
        if (checkLibros.isSelected()) {
            interesesBuilder.append("Libros,");
        }
        if (checkDeportes.isSelected()) {
            interesesBuilder.append("Deportes,");
        }
        if (checkMusica.isSelected()) {
            interesesBuilder.append("Musica,");
        }
        if (checkOtros.isSelected()) {
            interesesBuilder.append("Otros ");
        }

        String intereses = interesesBuilder.toString().trim();

        try {
            String sql = "UPDATE empleados SET nombre = ?, apellidop = ?, apellidom = ?, telefono = ?, correo = ?, estado_civil = ?, hijos = ?, intereses = ? WHERE id = ?";
            pps = cn.prepareStatement(sql);

            pps.setString(1, nombre);
            pps.setString(2, aP);
            pps.setString(3, aM);
            pps.setString(4, telefono);
            pps.setString(5, email);
            pps.setString(6, estado_civil);
            pps.setInt(7, hijos);
            pps.setString(8, intereses);
            pps.setString(9, idRegistro); // Noveno parámetro para el WHERE id = ?

            int res = pps.executeUpdate();

            if (res > 0) {
                JOptionPane.showMessageDialog(null, "DATOS ACTUALIZADOS CON ÉXITO");
                limpiaCaampo();
                obtenerRegistroUsuarios(); // Recarga la lista por si hubo cambios
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo actualizar el registro.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar los datos: " + ex.getMessage(), "ERROR SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 4. Limpia los controles del formulario
    public void limpiaCaampo() {
        campoNombre.setText("");
        campoAp.setText("");
        CampoApMat.setText("");
        campoTelefono.setText("");
        campoEmail.setText("");
        menuEC.setSelectedIndex(0);
        radioSi.setSelected(false);
        radioNo.setSelected(false);
        checkLibros.setSelected(false);
        checkMusica.setSelected(false);
        checkDeportes.setSelected(false);
        checkOtros.setSelected(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        campoAp = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        campoNombre = new javax.swing.JTextField();
        CampoApMat = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        campoTelefono = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        campoEmail = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        campoEC = new javax.swing.JLabel();
        menuEC = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        radioSi = new javax.swing.JRadioButton();
        radioNo = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        checkLibros = new javax.swing.JCheckBox();
        checkMusica = new javax.swing.JCheckBox();
        checkDeportes = new javax.swing.JCheckBox();
        checkOtros = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        btnActualizarUsuario = new javax.swing.JButton();
        btnRegresar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        btnBuscarRegsitroUsuario = new javax.swing.JButton();
        comboUsuarios = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Elephant", 0, 36)); // NOI18N
        jLabel1.setText("ALTA DE USUARIOS");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Nombre (S):");

        campoAp.addActionListener(this::campoApActionPerformed);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Apellido Paterno:");

        campoNombre.addActionListener(this::campoNombreActionPerformed);

        CampoApMat.addActionListener(this::CampoApMatActionPerformed);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Apellido Materno:");

        campoTelefono.addActionListener(this::campoTelefonoActionPerformed);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Telefono:");

        campoEmail.addActionListener(this::campoEmailActionPerformed);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Email:");

        campoEC.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        campoEC.setText("Estado Civil:");

        menuEC.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SOLTERO", "CASADO", "OTRO", " " }));
        menuEC.addActionListener(this::menuECActionPerformed);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("HIJOS?");

        radioSi.setText("SI");

        radioNo.setText("NO");
        radioNo.addActionListener(this::radioNoActionPerformed);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("INTERESES?");

        checkLibros.setText("Libros");
        checkLibros.addActionListener(this::checkLibrosActionPerformed);

        checkMusica.setText("Musica");
        checkMusica.addActionListener(this::checkMusicaActionPerformed);

        checkDeportes.setText("Deportes");

        checkOtros.setText("Otros");
        checkOtros.addActionListener(this::checkOtrosActionPerformed);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/img/usuarios.png"))); // NOI18N

        btnActualizarUsuario.setBackground(new java.awt.Color(102, 204, 0));
        btnActualizarUsuario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnActualizarUsuario.setText("ACTUALIZAR");
        btnActualizarUsuario.setToolTipText("");
        btnActualizarUsuario.addActionListener(this::btnActualizarUsuarioActionPerformed);

        btnRegresar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnRegresar.setText("REGRESAR");
        btnRegresar.addActionListener(this::btnRegresarActionPerformed);

        btnSalir.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSalir.setText("SALIR");
        btnSalir.addActionListener(this::btnSalirActionPerformed);

        jLabel10.setText("BUSCA USUSARIOS");

        btnBuscarRegsitroUsuario.setText("BUSCAR");
        btnBuscarRegsitroUsuario.addActionListener(this::btnBuscarRegsitroUsuarioActionPerformed);

        comboUsuarios.addActionListener(this::comboUsuariosActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel9)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(61, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel5))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(campoTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(radioSi)
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel10)
                                            .addComponent(radioNo)))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(campoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(110, 110, 110)
                                .addComponent(comboUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnBuscarRegsitroUsuario)))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(campoAp, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel6))
                                .addGap(36, 36, 36)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(campoEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(checkLibros)
                                        .addGap(31, 31, 31)
                                        .addComponent(checkMusica)
                                        .addGap(31, 31, 31)
                                        .addComponent(checkDeportes)))))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(18, 18, 18))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(campoEC)
                                        .addGap(29, 29, 29)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(menuEC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CampoApMat, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(checkOtros))
                        .addGap(26, 26, 26))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(170, 170, 170)
                        .addComponent(btnActualizarUsuario)
                        .addGap(18, 18, 18)
                        .addComponent(btnSalir)
                        .addGap(38, 38, 38)
                        .addComponent(btnRegresar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(306, 306, 306)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(campoAp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(campoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CampoApMat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(campoTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(campoEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(campoEC)
                    .addComponent(menuEC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(radioSi)
                    .addComponent(radioNo)
                    .addComponent(jLabel8)
                    .addComponent(checkLibros)
                    .addComponent(checkMusica)
                    .addComponent(checkDeportes)
                    .addComponent(checkOtros))
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnActualizarUsuario)
                            .addComponent(jLabel10)
                            .addComponent(btnSalir)
                            .addComponent(btnRegresar))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnBuscarRegsitroUsuario)
                            .addComponent(comboUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void campoApActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoApActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoApActionPerformed

    private void campoNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoNombreActionPerformed

    private void CampoApMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CampoApMatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CampoApMatActionPerformed

    private void campoTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoTelefonoActionPerformed

    private void campoEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoEmailActionPerformed

    private void radioNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioNoActionPerformed

    private void checkLibrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkLibrosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkLibrosActionPerformed

    private void checkMusicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkMusicaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkMusicaActionPerformed

    private void checkOtrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkOtrosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkOtrosActionPerformed

    private void btnActualizarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarUsuarioActionPerformed
        capturaActualizaDatos();
    }//GEN-LAST:event_btnActualizarUsuarioActionPerformed

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        MenuUsers mU = new MenuUsers();
        mU.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnBuscarRegsitroUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarRegsitroUsuarioActionPerformed
        obtieneInfoRegistros();
    }//GEN-LAST:event_btnBuscarRegsitroUsuarioActionPerformed

    private void menuECActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuECActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuECActionPerformed

    private void comboUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboUsuariosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboUsuariosActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new ModificaUsuarios().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CampoApMat;
    private javax.swing.JButton btnActualizarUsuario;
    private javax.swing.JButton btnBuscarRegsitroUsuario;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JTextField campoAp;
    private javax.swing.JLabel campoEC;
    private javax.swing.JTextField campoEmail;
    private javax.swing.JTextField campoNombre;
    private javax.swing.JTextField campoTelefono;
    private javax.swing.JCheckBox checkDeportes;
    private javax.swing.JCheckBox checkLibros;
    private javax.swing.JCheckBox checkMusica;
    private javax.swing.JCheckBox checkOtros;
    private javax.swing.JComboBox<String> comboUsuarios;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox<String> menuEC;
    private javax.swing.JRadioButton radioNo;
    private javax.swing.JRadioButton radioSi;
    // End of variables declaration//GEN-END:variables
}
