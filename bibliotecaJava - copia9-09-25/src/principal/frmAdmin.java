package principal;

import conexion.ConexionMysql;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class frmAdmin extends javax.swing.JFrame {

    private Connection cn;
    
    public frmAdmin() {
        btnActualizar.addActionListener(e -> mostrarLibros());
        btnAgregar.addActionListener(e -> agregarLibro());
        btnEditar.addActionListener(e -> editarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
    }

    private void mostrarLibros() {
        DefaultTableModel modelo = (DefaultTableModel) TblLibros.getModel();
        modelo.setRowCount(0); // Limpia la tabla

        String sql = "SELECT * FROM libros";
        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                modelo.addRow(new Object[] {
                    rs.getInt("id_libro"),
                    rs.getString("nombre"),
                    rs.getString("autor"),
                    rs.getString("editorial"),
                    rs.getInt("paginas"),
                    rs.getString("isbn"),
                    rs.getString("categoria"),
                    rs.getString("fecha_publi"),
                    rs.getDouble("precio")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al mostrar libros: " + e.getMessage());
        }
    }
    
    private void agregarLibro() {
        JTextField txtNombre = new JTextField();
        JTextField txtAutor = new JTextField();
        JTextField txtEditorial = new JTextField();
        JTextField txtPaginas = new JTextField();
        JTextField txtISBN = new JTextField();
        JTextField txtCategoria = new JTextField();
        JTextField txtFecha = new JTextField(); // yyyy-mm-dd
        JTextField txtPrecio = new JTextField();

        Object[] campos = {
            "Nombre:", txtNombre,
            "Autor:", txtAutor,
            "Editorial:", txtEditorial,
            "Páginas:", txtPaginas,
            "ISBN:", txtISBN,
            "Categoría:", txtCategoria,
            "Fecha publicación (yyyy-mm-dd):", txtFecha,
            "Precio:", txtPrecio
        };

        int opcion = JOptionPane.showConfirmDialog(this, campos, "Agregar libro", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                String sql = "INSERT INTO libros (nombre, autor, editorial, paginas, isbn, categoria, fecha_publi, precio) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = cn.prepareStatement(sql);
                ps.setString(1, txtNombre.getText());
                ps.setString(2, txtAutor.getText());
                ps.setString(3, txtEditorial.getText());
                ps.setInt(4, Integer.parseInt(txtPaginas.getText()));
                ps.setString(5, txtISBN.getText());
                ps.setString(6, txtCategoria.getText());
                ps.setString(7, txtFecha.getText());
                ps.setDouble(8, Double.parseDouble(txtPrecio.getText()));
                ps.executeUpdate();
                mostrarLibros();
                JOptionPane.showMessageDialog(this, "Libro agregado correctamente!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al agregar libro: " + e.getMessage());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Páginas y precio deben ser números válidos.");
            }
        }
    }
    private void editarLibro() {
        int fila = TblLibros.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un libro para editar.");
            return;
        }
        DefaultTableModel modelo = (DefaultTableModel) TblLibros.getModel();
        int id_libro = (int) modelo.getValueAt(fila, 0);

        JTextField txtNombre = new JTextField(modelo.getValueAt(fila, 1).toString());
        JTextField txtAutor = new JTextField(modelo.getValueAt(fila, 2).toString());
        JTextField txtEditorial = new JTextField(modelo.getValueAt(fila, 3).toString());
        JTextField txtPaginas = new JTextField(modelo.getValueAt(fila, 4).toString());
        JTextField txtISBN = new JTextField(modelo.getValueAt(fila, 5).toString());
        JTextField txtCategoria = new JTextField(modelo.getValueAt(fila, 6).toString());
        JTextField txtFecha = new JTextField(modelo.getValueAt(fila, 7).toString());
        JTextField txtPrecio = new JTextField(modelo.getValueAt(fila, 8).toString());

        Object[] campos = {
            "Nombre:", txtNombre,
            "Autor:", txtAutor,
            "Editorial:", txtEditorial,
            "Páginas:", txtPaginas,
            "ISBN:", txtISBN,
            "Categoría:", txtCategoria,
            "Fecha publicación (yyyy-mm-dd):", txtFecha,
            "Precio:", txtPrecio
        };

        int opcion = JOptionPane.showConfirmDialog(this, campos, "Editar libro", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                String sql = "UPDATE libros SET nombre=?, autor=?, editorial=?, paginas=?, isbn=?, categoria=?, fecha_publi=?, precio=? WHERE id_libro=?";
                PreparedStatement ps = cn.prepareStatement(sql);
                ps.setString(1, txtNombre.getText());
                ps.setString(2, txtAutor.getText());
                ps.setString(3, txtEditorial.getText());
                ps.setInt(4, Integer.parseInt(txtPaginas.getText()));
                ps.setString(5, txtISBN.getText());
                ps.setString(6, txtCategoria.getText());
                ps.setString(7, txtFecha.getText());
                ps.setDouble(8, Double.parseDouble(txtPrecio.getText()));
                ps.setInt(9, id_libro);
                ps.executeUpdate();
                mostrarLibros();
                JOptionPane.showMessageDialog(this, "Libro editado correctamente!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al editar libro: " + e.getMessage());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Páginas y precio deben ser números válidos.");
            }
        }
    }
    
    private void eliminarLibro() {
        int fila = TblLibros.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un libro para eliminar.");
            return;
        }
        DefaultTableModel modelo = (DefaultTableModel) TblLibros.getModel();
        int id_libro = (int) modelo.getValueAt(fila, 0);

        int opcion = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar el libro?", "Eliminar libro", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM libros WHERE id_libro=?";
                PreparedStatement ps = cn.prepareStatement(sql);
                ps.setInt(1, id_libro);
                ps.executeUpdate();
                mostrarLibros();
                JOptionPane.showMessageDialog(this, "Libro eliminado correctamente!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar libro: " + e.getMessage());
            }
        }
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
        jScrollPane1 = new javax.swing.JScrollPane();
        TblLibros = new javax.swing.JTable();
        btnAgregar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Administrador"));

        TblLibros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Nombre", "Autor", "Editorial", "Paginas", "isbn", "categoria", "fecha_publi", "precio"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Long.class, java.lang.String.class, java.lang.Object.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(TblLibros);

        btnAgregar.setText("Agregar");

        btnEditar.setText("Editar");

        btnEliminar.setText("Eliminar");

        btnActualizar.setText("Actualizar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(102, 102, 102)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(485, 485, 485))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 579, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 612, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TblLibros;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
