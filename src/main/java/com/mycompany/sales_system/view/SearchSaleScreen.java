package com.mycompany.sales_system.view;

import com.mycompany.sales_system.factory.ConnectionFactory;
import com.mycompany.sales_system.utils.sqlQuerys.SQLQuery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

public class SearchSaleScreen extends javax.swing.JFrame {

    Connection connection;
    PreparedStatement pst;
    ResultSet rs;

    public SearchSaleScreen(Connection connection) {
        this.connection = connection;
        initComponents();
        searchSales();
    }

private void searchSales() {
    try {
        if (connection == null) {
            connection = ConnectionFactory.CONNECT_DATABASE();
        }

        String clientName = jTextFieldSalesSearch.getText();
        try (PreparedStatement statement = connection.prepareStatement(SQLQuery.SEARCH_SALES.getDescription())) {
            statement.setString(1, clientName + "%");
            ResultSet resultSet = statement.executeQuery();
            jTableSalesTable.setModel(DbUtils.resultSetToTableModel(resultSet));
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, ex);
        ex.printStackTrace();
    }
}

private void insertDataIntoTableSearchSales() {
    int set = jTableSalesTable.getSelectedRow();
    jTextFieldSearchSaleProducName.setText(jTableSalesTable.getModel().getValueAt(set, 6).toString());
    jTextFieldSearchSalesPrice.setText(jTableSalesTable.getModel().getValueAt(set, 2).toString());
    jTextFieldSearchSalesDateSale.setText(jTableSalesTable.getModel().getValueAt(set, 0).toString());
    jTextFieldSearchSalesQuantity.setText(jTableSalesTable.getModel().getValueAt(set, 3).toString());
    jTextFieldSearchSalesDiscount.setText(jTableSalesTable.getModel().getValueAt(set, 1).toString());
    jTextFieldSearchSalesTotal.setText(jTableSalesTable.getModel().getValueAt(set, 4).toString());
}
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableSalesTable = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jTextFieldSalesSearch = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabelSearchSalesProductName = new javax.swing.JLabel();
        jTextFieldSearchSaleProducName = new javax.swing.JTextField();
        jLabelSearchSalesDateSale = new javax.swing.JLabel();
        jTextFieldSearchSalesDateSale = new javax.swing.JTextField();
        jTextFieldSearchSalesPrice = new javax.swing.JTextField();
        jLabelSearchSalesPrice = new javax.swing.JLabel();
        jLabelSearchSalesQuantity = new javax.swing.JLabel();
        jTextFieldSearchSalesQuantity = new javax.swing.JTextField();
        jLabelSearchSalesDiscount = new javax.swing.JLabel();
        jTextFieldSearchSalesDiscount = new javax.swing.JTextField();
        jLabelSearchSalesTotal = new javax.swing.JLabel();
        jTextFieldSearchSalesTotal = new javax.swing.JTextField();

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 0, 48)); // NOI18N
        jLabel1.setText("Vendas");

        jTableSalesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTableSalesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableSalesTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableSalesTable);

        jTextFieldSalesSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSalesSearchKeyReleased(evt);
            }
        });

        jLabel2.setText("Pesquisar");

        jLabelSearchSalesProductName.setText("Nome do produto:");

        jTextFieldSearchSaleProducName.setEditable(false);

        jLabelSearchSalesDateSale.setText("Data da venda:");

        jTextFieldSearchSalesDateSale.setEditable(false);

        jTextFieldSearchSalesPrice.setEditable(false);

        jLabelSearchSalesPrice.setText("Valor unitário:");

        jLabelSearchSalesQuantity.setText("Quantidade de peças:");

        jTextFieldSearchSalesQuantity.setEditable(false);

        jLabelSearchSalesDiscount.setText("Desconto:");

        jTextFieldSearchSalesDiscount.setEditable(false);

        jLabelSearchSalesTotal.setText("Total do pedido:");

        jTextFieldSearchSalesTotal.setEditable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 996, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabelSearchSalesProductName)
                                            .addComponent(jTextFieldSearchSaleProducName, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextFieldSearchSalesDateSale, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabelSearchSalesDateSale)))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jTextFieldSalesSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(28, 28, 28)
                                                .addComponent(jLabel2))
                                            .addComponent(jTextFieldSearchSalesPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabelSearchSalesPrice))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabelSearchSalesQuantity)
                                            .addComponent(jTextFieldSearchSalesQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabelSearchSalesTotal)
                                            .addComponent(jTextFieldSearchSalesTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(101, 101, 101)))))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSearchSalesDiscount)
                    .addComponent(jTextFieldSearchSalesDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldSalesSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelSearchSalesProductName)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldSearchSaleProducName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelSearchSalesDateSale)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldSearchSalesDateSale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelSearchSalesPrice)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldSearchSalesPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelSearchSalesQuantity)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldSearchSalesQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelSearchSalesDiscount)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldSearchSalesDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelSearchSalesTotal)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldSearchSalesTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(317, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(425, 425, 425)
                .addComponent(jLabel1)
                .addContainerGap(425, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setBounds(0, 0, 1024, 768);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldSalesSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSalesSearchKeyReleased
        searchSales();
    }//GEN-LAST:event_jTextFieldSalesSearchKeyReleased

    private void jTableSalesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableSalesTableMouseClicked
        insertDataIntoTableSearchSales();
    }//GEN-LAST:event_jTableSalesTableMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelSearchSalesDateSale;
    private javax.swing.JLabel jLabelSearchSalesDiscount;
    private javax.swing.JLabel jLabelSearchSalesPrice;
    private javax.swing.JLabel jLabelSearchSalesProductName;
    private javax.swing.JLabel jLabelSearchSalesQuantity;
    private javax.swing.JLabel jLabelSearchSalesTotal;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTableSalesTable;
    private javax.swing.JTextField jTextFieldSalesSearch;
    private javax.swing.JTextField jTextFieldSearchSaleProducName;
    private javax.swing.JTextField jTextFieldSearchSalesDateSale;
    private javax.swing.JTextField jTextFieldSearchSalesDiscount;
    private javax.swing.JTextField jTextFieldSearchSalesPrice;
    private javax.swing.JTextField jTextFieldSearchSalesQuantity;
    private javax.swing.JTextField jTextFieldSearchSalesTotal;
    // End of variables declaration//GEN-END:variables
}
