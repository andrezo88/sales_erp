package com.mycompany.sales_system.view;

import com.mycompany.sales_system.factory.ConnectionFactory;
import com.mycompany.sales_system.utils.sqlQuerys.SQLQuery;
import java.awt.HeadlessException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

public class ProductScreen extends javax.swing.JInternalFrame {

    PreparedStatement pst;
    ResultSet rs;

    public ProductScreen() {
        initComponents();
        searchProducts();
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

    private void saveProduct() {
        try {
            pst = ConnectionFactory.getInstancy().getConnection().prepareStatement(SQLQuery.ADD_PRODUCT.getQuery());
            pst.setString(1, jTextFieldName.getText());
            pst.setString(2, jTextFieldQuantity.getText());
            pst.setDouble(3, Double.parseDouble(jTextFieldBuyPrice.getText()));
            pst.setDouble(4, Double.parseDouble(jTextFieldSellPrice.getText()));
            if (jTextFieldName.getText().isBlank()
                    || jTextFieldQuantity.getText().isBlank()
                    || jTextFieldBuyPrice.getText().isBlank()
                    || jTextFieldSellPrice.getText().isBlank()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
                int addedProduct = pst.executeUpdate();
                if (addedProduct > 0) {
                    JOptionPane.showMessageDialog(null, "Produto adicionado com sucesso");
                    clearInputs();
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    private void searchProducts() {
        try {
            pst = ConnectionFactory.getInstancy().getConnection().prepareStatement(SQLQuery.SEARCH_PRODUCTS.getQuery());
            pst.setString(1, jTextFieldSearch.getText() + "%");
            rs = pst.executeQuery();
            jTableProducts.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    private void insertDataIntoTable() {
        int set = jTableProducts.getSelectedRow();
        jTextFieldIdProduct.setText(jTableProducts.getModel().getValueAt(set, 0).toString());
        jTextFieldName.setText(jTableProducts.getModel().getValueAt(set, 1).toString());
        jTextFieldQuantity.setText(jTableProducts.getModel().getValueAt(set, 2).toString());
        jTextFieldBuyPrice.setText(jTableProducts.getModel().getValueAt(set, 3).toString());
        jTextFieldSellPrice.setText(jTableProducts.getModel().getValueAt(set, 4).toString());
        if (jTableProducts.getModel().getValueAt(set, 0) == null) {
            disableButtons();
        }
    }

    private void editProduct() {
        int confirma = JOptionPane.showConfirmDialog(null, "Confima as alterações nos dados deste produto?", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                pst = ConnectionFactory.getInstancy().getConnection().prepareStatement(SQLQuery.EDIT_PRODUCT.getQuery());
                pst.setString(1, jTextFieldName.getText());
                pst.setString(2, jTextFieldQuantity.getText());
                pst.setDouble(3, Double.parseDouble(jTextFieldBuyPrice.getText()));
                pst.setDouble(4, Double.parseDouble(jTextFieldSellPrice.getText()));
                int id = Integer.parseInt(jTextFieldIdProduct.getText());
                pst.setInt(5, id);

                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do produto alterados com sucesso");
                    clearInputs();
                } else {
                    JOptionPane.showMessageDialog(null, "Nenhum dado foi alterado. Verifique os valores fornecidos.");
                }
            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao executar a consulta: " + e.getMessage());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "O campo ID deve ser um número inteiro.");
                jTextFieldIdProduct.requestFocus();
            }
        }
    }

    private void deleteProduct() {
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a exclusão deste produto?", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                pst = ConnectionFactory.getInstancy().getConnection().prepareStatement(SQLQuery.DELETE_PRODUCT.getQuery());
                pst.setString(1, jTextFieldIdProduct.getText());
                int deletedClient = pst.executeUpdate();
                if (deletedClient > 0) {
                    clearInputs();
                    JOptionPane.showMessageDialog(null, "Produto removido com sucesso");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    private void clearInputs() {
        jTextFieldName.setText(null);
        jTextFieldQuantity.setText(null);
        jTextFieldBuyPrice.setText(null);
        jTextFieldSellPrice.setText(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabelTitleProduct = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jTextFieldSearch = new javax.swing.JTextField();
        jLabelSearch = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableProducts = new javax.swing.JTable();
        jTextFieldIdProduct = new javax.swing.JTextField();
        jLabelIdProduct = new javax.swing.JLabel();
        jLabelName = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jLabelBuyPrice = new javax.swing.JLabel();
        jTextFieldBuyPrice = new javax.swing.JTextField();
        jLabelQuantity = new javax.swing.JLabel();
        jTextFieldQuantity = new javax.swing.JTextField();
        jLabelSellPrice = new javax.swing.JLabel();
        jTextFieldSellPrice = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jButtonSave = new javax.swing.JButton();
        jButtonEdit = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();

        setClosable(true);
        setTitle("Produto");
        setName(""); // NOI18N

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabelTitleProduct.setFont(new java.awt.Font("Liberation Sans", 0, 36)); // NOI18N
        jLabelTitleProduct.setText("Produto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(43, 324, 39, 327);
        jPanel1.add(jLabelTitleProduct, gridBagConstraints);

        jTextFieldSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSearchKeyReleased(evt);
            }
        });

        jLabelSearch.setText("Pesquisar");

        jTableProducts.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableProducts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableProductsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableProducts);

        jTextFieldIdProduct.setEditable(false);
        jTextFieldIdProduct.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldIdProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldIdProductActionPerformed(evt);
            }
        });

        jLabelIdProduct.setText("ID produto:");

        jLabelName.setText("* Nome:");

        jLabelBuyPrice.setText("* Valor de compra:");

        jTextFieldBuyPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldBuyPriceActionPerformed(evt);
            }
        });

        jLabelQuantity.setText("* Quantidade em estoque:");

        jLabelSellPrice.setText("* Preço de venda: ");

        jTextFieldSellPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSellPriceActionPerformed(evt);
            }
        });

        jButtonSave.setText("Salvar");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jButtonEdit.setText("Editar");
        jButtonEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditActionPerformed(evt);
            }
        });

        jButtonDelete.setText("Deletar");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSave)
                    .addComponent(jButtonEdit)
                    .addComponent(jButtonDelete))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 676, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextFieldSearch)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelSearch)
                        .addGap(306, 306, 306))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelBuyPrice)
                            .addComponent(jLabelName)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabelIdProduct)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldIdProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextFieldName, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                            .addComponent(jTextFieldBuyPrice))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelQuantity)
                            .addComponent(jLabelSellPrice)
                            .addComponent(jTextFieldQuantity)
                            .addComponent(jTextFieldSellPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                        .addGap(50, 50, 50))))
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelSearch))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldIdProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelIdProduct))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelName)
                    .addComponent(jLabelQuantity))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelBuyPrice)
                    .addComponent(jLabelSellPrice))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldBuyPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldSellPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBounds(0, 0, 800, 640);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldIdProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldIdProductActionPerformed
        disableButtons();
    }//GEN-LAST:event_jTextFieldIdProductActionPerformed

    private void jTextFieldBuyPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldBuyPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldBuyPriceActionPerformed

    private void jTextFieldSellPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSellPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldSellPriceActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        saveProduct();
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jTableProductsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableProductsMouseClicked
        insertDataIntoTable();
        enableButtons();
    }//GEN-LAST:event_jTableProductsMouseClicked

    private void jTextFieldSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchKeyReleased
        searchProducts();
    }//GEN-LAST:event_jTextFieldSearchKeyReleased

    private void jButtonEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditActionPerformed
        editProduct();
    }//GEN-LAST:event_jButtonEditActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        deleteProduct();
    }//GEN-LAST:event_jButtonDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonEdit;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JLabel jLabelBuyPrice;
    private javax.swing.JLabel jLabelIdProduct;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelQuantity;
    private javax.swing.JLabel jLabelSearch;
    private javax.swing.JLabel jLabelSellPrice;
    private javax.swing.JLabel jLabelTitleProduct;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTableProducts;
    private javax.swing.JTextField jTextFieldBuyPrice;
    private javax.swing.JTextField jTextFieldIdProduct;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldQuantity;
    private javax.swing.JTextField jTextFieldSearch;
    private javax.swing.JTextField jTextFieldSellPrice;
    // End of variables declaration//GEN-END:variables
}
