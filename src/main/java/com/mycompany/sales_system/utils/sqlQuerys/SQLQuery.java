package com.mycompany.sales_system.utils.sqlQuerys;

public enum SQLQuery {

    ADD_CLIENT("INSERT INTO clients(name, email, phone, document) VALUES(?, ?, ?, ?)"),
    SEARCH_CLIENTS("SELECT * from clients where name like ?"),
    EDIT_CLIENT("UPDATE clients SET name=?, email=?,phone=?,document=? WHERE id=?"),
    DELETE_CLIENT("DELETE FROM clients WHERE id = ?"),
    ADD_PRODUCT("INSERT INTO products(name, quantity_stock, price_buy, price_sell) VALUES(?, ?, ?, ?)"),
    SEARCH_PRODUCTS("SELECT * from products where name like ?"),
    SEARCH_PRODUCT_ID("SELECT * from products where id = ?"),
    EDIT_PRODUCT("UPDATE products SET name=?, quantity_stock=?, price_buy=?, price_sell=? WHERE id=?"),
    DELETE_PRODUCT("DELETE FROM products WHERE id = ?"),
    SEARCH_PRODUCTS_SALES("SELECT id, name, quantity_stock, price_sell from products where name like ?"),
    SEARCH_CLIENTS_SALES("SELECT id, name, phone, document from clients where name like ?"),
    ADD_SALE("INSERT INTO sales(sale_date, discount, unit_price, quantity, total, id_client, id_product) VALUES(?,?,?,?,?,?,?)"),
    UPDATE_STOCK("UPDATE products SET quantity_stock=? WHERE id=?"),
    SEARCH_SALES("SELECT s.sale_date as Data_Venda, s.discount as Desconto, s.unit_price as Preco_unitario, s.quantity as quantidade, s.total, c.NAME AS Nome_Cliente, p.NAME AS Nome_Produto FROM SALES s JOIN CLIENTS c ON s.ID_CLIENT = c.ID JOIN PRODUCTS p ON s.ID_PRODUCT = p.ID WHERE c.NAME LIKE ?");

    private final String description;
    
    SQLQuery(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
