package com.mycompany.sales_system.view;

import com.mycompany.sales_system.factory.ConnectionFactory;
import com.mycompany.sales_system.utils.sqlQuerys.SQLQuery;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import net.proteanit.sql.DbUtils;

public final class ClientScreen extends javax.swing.JInternalFrame {

    Connection connection;
    PreparedStatement pst;
    ResultSet rs;

    public ClientScreen(Connection connection) {
        this.connection = connection;
        initComponents();
        searchClients();
        disableButtons();
    }
    
    private void disableButtons() {
        jButtonEdit.setEnabled(false);
        jButtonDelete.setEnabled(false);
    }
    
     private void enableButtons() {
        jButtonSave.setEnabled(false);
        jButtonEdit.setEnabled(true);
        jButtonDelete.setEnabled(true);
    }

    public void saveClient() {
        try {
            pst = connection.prepareStatement(SQLQuery.ADD_CLIENT.getDescription());
            pst.setString(1, jTextFieldName.getText());
            pst.setString(2, jTextFieldEmail.getText());
            pst.setString(3, jTextFieldPhone.getText());
            pst.setString(4, jTextFieldDocument.getText());
            if (jTextFieldEmail.getText().isBlank()
                    || jTextFieldName.getText().isBlank()
                    || jTextFieldPhone.getText().isBlank()
                    || jTextFieldDocument.getText().isBlank()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
                int addedClient = pst.executeUpdate();
                if (addedClient > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente adicionado com sucesso");
                    clearInputs();
                }
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(null, "Email já existente.\nEscolha outro email.");
            jTextFieldEmail.setText(null);
            jTextFieldEmail.requestFocus();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void editClient() {
        int confirma = JOptionPane.showConfirmDialog(null, "Confima as alterações nos dados deste cliente?", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                pst = connection.prepareStatement(SQLQuery.EDIT_CLIENT.getDescription());

                pst.setString(1, jTextFieldName.getText());
                pst.setString(2, jTextFieldEmail.getText());
                pst.setString(3, jTextFieldPhone.getText());
                pst.setString(4, jTextFieldDocument.getText());
                int id = Integer.parseInt(jTextFieldId.getText());
                pst.setInt(5, id);

                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do cliente alterados com sucesso");
                    clearInputs();
                } else {
                    JOptionPane.showMessageDialog(null, "Nenhum dado foi alterado. Verifique os valores fornecidos.");
                }
            } catch (SQLIntegrityConstraintViolationException e1) {
                JOptionPane.showMessageDialog(null, "Email não preenchido ou já existente.\nEscolha outro email.");
                jTextFieldEmail.setText(null);
                jTextFieldEmail.requestFocus();
            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao executar a consulta: " + e.getMessage());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "O campo ID deve ser um número inteiro.");
                jTextFieldId.requestFocus();
            }
        }
    }

    public void searchClients() {
        try {
            pst = connection.prepareStatement(SQLQuery.SEARCH_CLIENTS.getDescription());

            pst.setString(1, jTextFieldSearch.getText() + "%");
            rs = pst.executeQuery();
            jTableClients.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void insertDataIntoTable() {
        int set = jTableClients.getSelectedRow();
        jTextFieldId.setText(jTableClients.getModel().getValueAt(set, 0).toString());
        jTextFieldName.setText(jTableClients.getModel().getValueAt(set, 1).toString());
        jTextFieldEmail.setText(jTableClients.getModel().getValueAt(set, 2).toString());
        jTextFieldPhone.setText(jTableClients.getModel().getValueAt(set, 3).toString());
        jTextFieldDocument.setText(jTableClients.getModel().getValueAt(set, 4).toString());
    }

    public void deleteClient() {
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a exclusão deste cliente?", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                pst = connection.prepareStatement(SQLQuery.DELETE_CLIENT.getDescription());
                pst.setString(1, jTextFieldId.getText());
                int deletedClient = pst.executeUpdate();
                if (deletedClient > 0) {
                    clearInputs();
                    JOptionPane.showMessageDialog(null, "Cliente removido com sucesso");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    public void clearInputs() {
        jTextFieldId.setText(null);
        jTextFieldEmail.setText(null);
        jTextFieldName.setText(null);
        jTextFieldPhone.setText(null);
        jTextFieldDocument.setText(null);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabelName = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jLabelEmail = new javax.swing.JLabel();
        jTextFieldEmail = new javax.swing.JTextField();
        jLabelPhone = new javax.swing.JLabel();
        jTextFieldPhone = new javax.swing.JTextField();
        jLabelDocument = new javax.swing.JLabel();
        jTextFieldDocument = new javax.swing.JTextField();
        jButtonSave = new javax.swing.JButton();
        jButtonEdit = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jTextFieldSearch = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableClients = new javax.swing.JTable();
        jLabelId = new javax.swing.JLabel();
        jTextFieldId = new javax.swing.JTextField();
        jButtonAddress = new javax.swing.JButton();

        setClosable(true);
        setTitle("Cliente");
        setPreferredSize(new java.awt.Dimension(800, 640));

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 0, 36)); // NOI18N
        jLabel1.setText("Clientes");
        jLabel1.setAlignmentY(0.0F);

        jLabelName.setText("* Nome: ");

        jLabelEmail.setText("* Email: ");

        jLabelPhone.setText("* Telefone: ");

        jTextFieldPhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldPhoneActionPerformed(evt);
            }
        });

        jLabelDocument.setText("* CPF: ");

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jButtonEdit.setText("Edit");
        jButtonEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditActionPerformed(evt);
            }
        });

        jButtonDelete.setText("Delete");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jTextFieldSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearchActionPerformed(evt);
            }
        });
        jTextFieldSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSearchKeyReleased(evt);
            }
        });

        jLabel2.setText("Pesquisar");

        jTableClients.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nome", "Email", "Phone", "CPF"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableClients.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableClientsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableClients);

        jLabelId.setText("ID Cliente:");

        jTextFieldId.setEditable(false);
        jTextFieldId.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldIdActionPerformed(evt);
            }
        });

        jButtonAddress.setText("Endereço");
        jButtonAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddressActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(330, 330, 330)
                        .addComponent(jLabel1)
                        .addGap(0, 324, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelId)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldId, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabelName)
                                    .addComponent(jLabelEmail)
                                    .addComponent(jTextFieldEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                    .addComponent(jTextFieldName))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabelDocument)
                                    .addComponent(jTextFieldDocument)
                                    .addComponent(jLabelPhone)
                                    .addComponent(jTextFieldPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(50, 50, 50))))
            .addGroup(layout.createSequentialGroup()
                .addGap(166, 166, 166)
                .addComponent(jButtonSave)
                .addGap(59, 59, 59)
                .addComponent(jButtonEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonAddress)
                .addGap(81, 81, 81)
                .addComponent(jButtonDelete)
                .addGap(99, 99, 99))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                .addGap(40, 40, 40)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelId)
                    .addComponent(jTextFieldId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelPhone)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelDocument)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldDocument, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelName)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelEmail)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonSave)
                        .addComponent(jButtonEdit)
                        .addComponent(jButtonDelete))
                    .addComponent(jButtonAddress))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        setBounds(0, 0, 799, 640);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditActionPerformed
        editClient();
    }//GEN-LAST:event_jButtonEditActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        // TODO add your handling code here:
        deleteClient();
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        try {
            saveClient();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jTextFieldSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldSearchActionPerformed

    private void jTextFieldSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchKeyReleased
        searchClients();
    }//GEN-LAST:event_jTextFieldSearchKeyReleased

    private void jTableClientsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableClientsMouseClicked
        insertDataIntoTable();
        enableButtons();
    }//GEN-LAST:event_jTableClientsMouseClicked

    private void jTextFieldPhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldPhoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldPhoneActionPerformed

    private void jTextFieldIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldIdActionPerformed

    private void jButtonAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddressActionPerformed
        AddressClient addressClient = new AddressClient(connection);
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Address Client", true);
        dialog.getContentPane().add(addressClient.getContentPane());
        dialog.pack();
        dialog.setVisible(true);   
    }//GEN-LAST:event_jButtonAddressActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddress;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonEdit;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelDocument;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelId;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelPhone;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTableClients;
    private javax.swing.JTextField jTextFieldDocument;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldId;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldPhone;
    private javax.swing.JTextField jTextFieldSearch;
    // End of variables declaration//GEN-END:variables
}
