package com.mycompany.sales_system.view;

import com.mycompany.sales_system.factory.ConnectionFactory;
import com.mycompany.sales_system.utils.sqlQuerys.SQLQuery;
import java.awt.Frame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import net.proteanit.sql.DbUtils;

public class SalesScreen extends javax.swing.JInternalFrame {

    Connection connection;
    PreparedStatement pst;
    ResultSet rs;

    public SalesScreen(Connection connection) {
        this.connection = connection;
        initComponents();
        searchProducts();
        searchClients();
        jTextFieldProductTotal1.setText("");
    }

    private void searchProducts() {
        try {
            pst = connection.prepareStatement(SQLQuery.SEARCH_PRODUCTS_SALES.getDescription());

            pst.setString(1, jTextFieldSearchProduct.getText() + "%");
            rs = pst.executeQuery();
            jTableProducts.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void searchClients() {
        try {
            pst = connection.prepareStatement(SQLQuery.SEARCH_CLIENTS_SALES.getDescription());

            pst.setString(1, jTextFieldSearchClient.getText() + "%");
            rs = pst.executeQuery();
            jTableClients.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void insertDataIntoTableProduct() {
        int selectedRow = jTableProducts.getSelectedRow();
        if (selectedRow >= 0) {
            jTextFieldIdProduct.setText(jTableProducts.getModel().getValueAt(selectedRow, 0).toString());
            jTextFieldProductName1.setText(jTableProducts.getModel().getValueAt(selectedRow, 1).toString());
            jTextFieldProductStock1.setText(jTableProducts.getModel().getValueAt(selectedRow, 2).toString());
            jTextFieldProductPrice1.setText(jTableProducts.getModel().getValueAt(selectedRow, 3).toString());

            Double total = calculateTotalOrder();
            if (total != null) {
                jTextFieldProductTotal1.setText(total.toString());
            } else {
                jTextFieldProductTotal1.setText("");
            }
        }
    }

    private void insertDataIntoTableClient() {
        int set = jTableClients.getSelectedRow();
        jTextFieldIdClient.setText(jTableClients.getModel().getValueAt(set, 0).toString());
    }

    private Double calculateTotalOrder() {
        String sellPriceText = jTextFieldProductPrice1.getText();
        String quantityText = jTextFieldProductQuantity1.getText();
        String discountText = jTextFieldProductDiscount1.getText();
        Double total = null;

        if (!sellPriceText.isBlank() && !quantityText.isBlank()) {
            try {
                Double sellPrice = Double.valueOf(sellPriceText);
                Integer quantity = Integer.valueOf(quantityText);
                Double discount = discountText.isEmpty() ? 0.0 : Double.valueOf(discountText);

                total = (sellPrice * quantity) - discount;
            } catch (NumberFormatException ex) {
                System.out.println(ex);
            }
        }
        System.out.println("sell price " + sellPriceText);
        System.out.println("quantity " + quantityText);
        System.out.println("discount " + discountText);
        System.out.println("total " + total);

        return total;
    }

    private void saveSale() {
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma o pedido deste produto?", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                pst = connection.prepareStatement(SQLQuery.ADD_SALE.getDescription());

                Integer productId = parseInteger(jTextFieldIdProduct.getText());
                if (productId == null) {
                    JOptionPane.showMessageDialog(null, "Selecione um produto.");
                    return;
                }
                pst.setInt(7, productId);

                Integer clientId = parseInteger(jTextFieldIdClient.getText());
                if (clientId == null) {
                    JOptionPane.showMessageDialog(null, "Selecione um cliente.");
                    return;
                }
                pst.setInt(6, clientId);

                pst.setDate(1, new java.sql.Date(new Date().getTime()));

                double discount = parseDouble(jTextFieldProductDiscount1.getText());
                pst.setDouble(2, discount);

                double price = parseDouble(jTextFieldProductPrice1.getText());
                pst.setDouble(3, price);

                Integer quantity = parseInteger(jTextFieldProductQuantity1.getText());
                if (quantity == null) {
                    JOptionPane.showMessageDialog(null, "Informe a quantidade.");
                    return;
                } else if (quantity < 1) {
                    JOptionPane.showMessageDialog(null, "A quantidade deve ser igual ou maior que 1.");
                    return;
                }
                pst.setInt(4, quantity);

                double total = parseDouble(jTextFieldProductTotal1.getText());
                pst.setDouble(5, total);

                if (!searchStock(productId.toString())) {
                    JOptionPane.showMessageDialog(null, "Não foi possível finalizar pedido.");
                } else {
                    updateStock(productId.toString(), quantity);

                    pst = connection.prepareStatement(SQLQuery.ADD_SALE.getDescription());
                    pst.setDate(1, new java.sql.Date(new Date().getTime()));
                    pst.setDouble(2, discount);
                    pst.setDouble(3, price);
                    pst.setInt(4, quantity);
                    pst.setDouble(5, total);
                    pst.setInt(6, clientId);
                    pst.setInt(7, productId);
                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Pedido adicionado com sucesso");
                    clearInputs();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao salvar o pedido: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private double parseDouble(String text) {
        if (text.isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(text);
    }

    private Integer parseInteger(String text) {
        if (text.isEmpty()) {
            return null;
        }
        return Integer.valueOf(text);
    }

    private boolean searchStock(String id) {
        try {
            pst = connection.prepareStatement(SQLQuery.SEARCH_PRODUCT_ID.getDescription());
            pst.setString(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                int currentStock = rs.getInt("quantity_stock");
                int quantity = Integer.parseInt(jTextFieldProductQuantity1.getText());

                // Calculate new stock
                int newStock = currentStock - quantity;

                // Output for debugging
                System.out.println("Current Stock: " + currentStock);
                System.out.println("Quantity Requested: " + quantity);
                System.out.println("New Stock: " + newStock);

                // Display appropriate messages based on stock availability
                if (currentStock < 1) {
                    JOptionPane.showMessageDialog(null, "Produto sem estoque, não é possível completar a venda.");
                    return false;
                } else if (currentStock < quantity) {
                    JOptionPane.showMessageDialog(null, "Quantidade do pedido maior que estoque, não é possível completar a venda.");
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Produto não encontrado.");
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("Error searching stock: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private void updateStock(String id, int quantity) {
        try {

            // Verifica se o produto existe no estoque
            pst = connection.prepareStatement(SQLQuery.SEARCH_PRODUCT_ID.getDescription());
            pst.setString(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                int currentStock = rs.getInt("quantity_stock");
                int newStock = currentStock - quantity;

                // Atualiza o estoque
                String updateQuery = "UPDATE products SET quantity_stock = ? WHERE id = ?";
                pst = connection.prepareStatement(updateQuery);
                pst.setInt(1, newStock);
                pst.setString(2, id);
                pst.executeUpdate();
            } else {
                JOptionPane.showMessageDialog(null, "Produto não encontrado no estoque.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar o estoque: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearInputs() {
        jTextFieldProductName.setText(null);
        jTextFieldProductQuantity1.setText(null);
        jTextFieldProductPrice1.setText(null);
        jTextFieldProductStock.setText(null);
        jTextFieldProductDiscount1.setText(null);
        jTextFieldProductTotal1.setText(null);
        jTextFieldIdProduct.setText(null);
        jTextFieldIdClient.setText(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTextFieldSearchProduct = new javax.swing.JTextField();
        jLabelSearchProduct = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableProducts = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jLabelVendas = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldIdProduct = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldIdClient = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldProductName = new javax.swing.JTextField();
        jTextFieldProductPrice = new javax.swing.JTextField();
        jTextFieldProductStock = new javax.swing.JTextField();
        jTextFieldProductQuantity = new javax.swing.JTextField();
        jTextFieldProductDiscount = new javax.swing.JTextField();
        jTextFieldProductTotal = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldProductName1 = new javax.swing.JTextField();
        jTextFieldProductPrice1 = new javax.swing.JTextField();
        jTextFieldProductStock1 = new javax.swing.JTextField();
        jTextFieldProductQuantity1 = new javax.swing.JTextField();
        jTextFieldProductDiscount1 = new javax.swing.JTextField();
        jTextFieldProductTotal1 = new javax.swing.JTextField();
        jButtonSave = new javax.swing.JButton();
        jButtonSearch = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jTextFieldProductName2 = new javax.swing.JTextField();
        jTextFieldProductPrice2 = new javax.swing.JTextField();
        jTextFieldProductStock2 = new javax.swing.JTextField();
        jTextFieldProductQuantity2 = new javax.swing.JTextField();
        jTextFieldProductDiscount2 = new javax.swing.JTextField();
        jTextFieldProductTotal2 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTextFieldSearchClient = new javax.swing.JTextField();
        jLabelSearchClient = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableClients = new javax.swing.JTable();

        setClosable(true);
        setTitle("Vendas");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldSearchProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearchProductActionPerformed(evt);
            }
        });
        jTextFieldSearchProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSearchProductKeyReleased(evt);
            }
        });

        jLabelSearchProduct.setText("Pesquisar");

        jTableProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nome", "Quantidade", "Preço de venda"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextFieldSearchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelSearchProduct)
                        .addGap(0, 35, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldSearchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelSearchProduct))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabelVendas.setFont(new java.awt.Font("Liberation Sans", 0, 36)); // NOI18N
        jLabelVendas.setText("Vendas");
        jPanel3.add(jLabelVendas, new java.awt.GridBagConstraints());

        jLabel1.setText("ID Produto:");

        jTextFieldIdProduct.setEditable(false);

        jLabel2.setText("ID Cliente:");

        jTextFieldIdClient.setEditable(false);

        jLabel3.setText("Nome do Produto:");

        jLabel4.setText("Valor unitário:");

        jLabel5.setText("Quantidade:");

        jLabel6.setText("Peças em estoque:");

        jLabel7.setText("Desconto:");

        jLabel8.setText("Total:");

        jTextFieldProductName.setEditable(false);
        jTextFieldProductName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldProductNameActionPerformed(evt);
            }
        });

        jTextFieldProductPrice.setEditable(false);

        jTextFieldProductStock.setEditable(false);

        jTextFieldProductQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldProductQuantityKeyReleased(evt);
            }
        });

        jTextFieldProductDiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldProductDiscountKeyReleased(evt);
            }
        });

        jTextFieldProductTotal.setEditable(false);
        jTextFieldProductTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldProductTotalActionPerformed(evt);
            }
        });

        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("jButton2");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jTextFieldProductName)
                            .addComponent(jTextFieldProductStock)
                            .addComponent(jTextFieldProductDiscount, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap(223, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(125, 125, 125)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jButton2))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel5)
                        .addComponent(jLabel4)
                        .addComponent(jLabel8)
                        .addComponent(jTextFieldProductPrice)
                        .addComponent(jTextFieldProductQuantity)
                        .addComponent(jTextFieldProductTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)))
                .addGap(57, 57, 57))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldProductPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldProductStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldProductQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldProductDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldProductTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel9.setText("Nome do Produto:");

        jLabel10.setText("Valor unitário:");

        jLabel11.setText("Quantidade:");

        jLabel12.setText("Peças em estoque:");

        jLabel13.setText("Desconto:");

        jLabel14.setText("Total:");

        jTextFieldProductName1.setEditable(false);
        jTextFieldProductName1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldProductName1ActionPerformed(evt);
            }
        });

        jTextFieldProductPrice1.setEditable(false);

        jTextFieldProductStock1.setEditable(false);

        jTextFieldProductQuantity1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldProductQuantity1KeyReleased(evt);
            }
        });

        jTextFieldProductDiscount1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldProductDiscount1ActionPerformed(evt);
            }
        });
        jTextFieldProductDiscount1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldProductDiscount1KeyReleased(evt);
            }
        });

        jTextFieldProductTotal1.setEditable(false);
        jTextFieldProductTotal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldProductTotal1ActionPerformed(evt);
            }
        });

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jButtonSearch.setText("Pesquisar");
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jTextFieldProductName1)
                    .addComponent(jTextFieldProductStock1)
                    .addComponent(jTextFieldProductDiscount1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 114, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10)
                    .addComponent(jLabel14)
                    .addComponent(jTextFieldProductPrice1)
                    .addComponent(jTextFieldProductQuantity1)
                    .addComponent(jTextFieldProductTotal1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                .addGap(10, 10, 10))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(228, 228, 228)
                .addComponent(jButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100)
                .addComponent(jButtonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldProductName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldProductPrice1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldProductStock1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldProductQuantity1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldProductDiscount1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldProductTotal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSave)
                    .addComponent(jButtonSearch))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel15.setText("Nome do Produto:");

        jLabel16.setText("Valor unitário:");

        jLabel17.setText("Quantidade:");

        jLabel18.setText("Peças em estoque:");

        jLabel19.setText("Desconto:");

        jLabel20.setText("Total:");

        jTextFieldProductName2.setEditable(false);
        jTextFieldProductName2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldProductName2ActionPerformed(evt);
            }
        });

        jTextFieldProductPrice2.setEditable(false);

        jTextFieldProductStock2.setEditable(false);

        jTextFieldProductQuantity2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldProductQuantity2KeyReleased(evt);
            }
        });

        jTextFieldProductDiscount2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldProductDiscount2KeyReleased(evt);
            }
        });

        jTextFieldProductTotal2.setEditable(false);
        jTextFieldProductTotal2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldProductTotal2ActionPerformed(evt);
            }
        });

        jButton5.setText("Save");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("jButton2");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel15)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19)
                            .addComponent(jTextFieldProductName2)
                            .addComponent(jTextFieldProductStock2)
                            .addComponent(jTextFieldProductDiscount2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addContainerGap(223, Short.MAX_VALUE)
                        .addComponent(jButton5)
                        .addGap(125, 125, 125)))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jButton6))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel17)
                        .addComponent(jLabel16)
                        .addComponent(jLabel20)
                        .addComponent(jTextFieldProductPrice2)
                        .addComponent(jTextFieldProductQuantity2)
                        .addComponent(jTextFieldProductTotal2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)))
                .addGap(57, 57, 57))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldProductName2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldProductPrice2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel17))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldProductStock2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldProductQuantity2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldProductDiscount2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldProductTotal2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldIdProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(224, 224, 224)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldIdClient, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldIdProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldIdClient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextFieldSearchClient.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSearchClientKeyReleased(evt);
            }
        });

        jLabelSearchClient.setText("Pesquisar");

        jTableClients.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nome", "Telefone", "CPF"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
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
        jScrollPane2.setViewportView(jTableClients);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextFieldSearchClient, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelSearchClient)
                        .addGap(0, 34, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldSearchClient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelSearchClient))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 776, Short.MAX_VALUE)
                    .addComponent(jSeparator2)
                    .addComponent(jSeparator1)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(253, 253, 253))
        );

        setBounds(0, 0, 800, 654);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldSearchProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchProductActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldSearchProductActionPerformed

    private void jTextFieldSearchProductKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchProductKeyReleased
        searchProducts();
    }//GEN-LAST:event_jTextFieldSearchProductKeyReleased

    private void jTableClientsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableClientsMouseClicked
        insertDataIntoTableClient();
    }//GEN-LAST:event_jTableClientsMouseClicked

    private void jTextFieldSearchClientKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchClientKeyReleased
        searchClients();
    }//GEN-LAST:event_jTextFieldSearchClientKeyReleased

    private void jTextFieldProductNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldProductNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldProductNameActionPerformed

    private void jTextFieldProductQuantityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldProductQuantityKeyReleased
        Double total = calculateTotalOrder();
        if (total != null) {
            jTextFieldProductTotal1.setText(total.toString());
        } else {
            jTextFieldProductTotal1.setText("");
        }
        insertDataIntoTableProduct();
    }//GEN-LAST:event_jTextFieldProductQuantityKeyReleased

    private void jTextFieldProductTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldProductTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldProductTotalActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        saveSale();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextFieldProductDiscountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldProductDiscountKeyReleased
        calculateTotalOrder();
        insertDataIntoTableProduct();
    }//GEN-LAST:event_jTextFieldProductDiscountKeyReleased

    private void jTextFieldProductName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldProductName1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldProductName1ActionPerformed

    private void jTextFieldProductQuantity1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldProductQuantity1KeyReleased
        Double total = calculateTotalOrder();
        if (total != null) {
            jTextFieldProductTotal1.setText(total.toString());
        } else {
            jTextFieldProductTotal1.setText("");
        }
        insertDataIntoTableProduct();
    }//GEN-LAST:event_jTextFieldProductQuantity1KeyReleased

    private void jTextFieldProductDiscount1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldProductDiscount1KeyReleased
        calculateTotalOrder();
        insertDataIntoTableProduct();
    }//GEN-LAST:event_jTextFieldProductDiscount1KeyReleased

    private void jTextFieldProductTotal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldProductTotal1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldProductTotal1ActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        saveSale();
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jTextFieldProductName2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldProductName2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldProductName2ActionPerformed

    private void jTextFieldProductQuantity2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldProductQuantity2KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldProductQuantity2KeyReleased

    private void jTextFieldProductDiscount2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldProductDiscount2KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldProductDiscount2KeyReleased

    private void jTextFieldProductTotal2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldProductTotal2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldProductTotal2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed
        SearchSaleScreen searchSaleScreen = new SearchSaleScreen(connection);
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Search Sale", true);
        dialog.getContentPane().add(searchSaleScreen.getContentPane());
        dialog.pack();
        dialog.setVisible(true);
    }//GEN-LAST:event_jButtonSearchActionPerformed

    private void jTableProductsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableProductsMouseClicked
        insertDataIntoTableProduct();
    }//GEN-LAST:event_jTableProductsMouseClicked

    private void jTextFieldProductDiscount1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldProductDiscount1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldProductDiscount1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelSearchClient;
    private javax.swing.JLabel jLabelSearchProduct;
    private javax.swing.JLabel jLabelVendas;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTableClients;
    private javax.swing.JTable jTableProducts;
    private javax.swing.JTextField jTextFieldIdClient;
    private javax.swing.JTextField jTextFieldIdProduct;
    private javax.swing.JTextField jTextFieldProductDiscount;
    private javax.swing.JTextField jTextFieldProductDiscount1;
    private javax.swing.JTextField jTextFieldProductDiscount2;
    private javax.swing.JTextField jTextFieldProductName;
    private javax.swing.JTextField jTextFieldProductName1;
    private javax.swing.JTextField jTextFieldProductName2;
    private javax.swing.JTextField jTextFieldProductPrice;
    private javax.swing.JTextField jTextFieldProductPrice1;
    private javax.swing.JTextField jTextFieldProductPrice2;
    private javax.swing.JTextField jTextFieldProductQuantity;
    private javax.swing.JTextField jTextFieldProductQuantity1;
    private javax.swing.JTextField jTextFieldProductQuantity2;
    private javax.swing.JTextField jTextFieldProductStock;
    private javax.swing.JTextField jTextFieldProductStock1;
    private javax.swing.JTextField jTextFieldProductStock2;
    private javax.swing.JTextField jTextFieldProductTotal;
    private javax.swing.JTextField jTextFieldProductTotal1;
    private javax.swing.JTextField jTextFieldProductTotal2;
    private javax.swing.JTextField jTextFieldSearchClient;
    private javax.swing.JTextField jTextFieldSearchProduct;
    // End of variables declaration//GEN-END:variables
}
