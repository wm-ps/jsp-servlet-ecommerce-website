package com.ecommerce.dao;

import com.ecommerce.database.Database;
import com.ecommerce.entity.Account;
import com.ecommerce.entity.Category;
import com.ecommerce.entity.Product;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ProductDao {
    // Call DAO class to access other entities' database.
    AccountDao accountDao = new AccountDao();
    CategoryDao categoryDao = new CategoryDao();

    // Method to get blob image from database.
    private String getBase64Image(Blob blob) throws SQLException, IOException {
        InputStream inputStream = blob.getBinaryStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead = -1;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        return Base64.getEncoder().encodeToString(imageBytes);
    }

    // Method to execute query to get list products.
    private List<Product> getListProductQuery(String query) {
        List<Product> list = new ArrayList<>();
        try (Connection connection = new Database().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                double price = resultSet.getDouble(4);
                String description = resultSet.getString(5);
                Category category = categoryDao.getCategory(resultSet.getInt(6));
                Account account = accountDao.getAccount(resultSet.getInt(7));
                boolean isDelete = resultSet.getBoolean(8);
                int amount = resultSet.getInt(9);

                // Get base64 image to display.
                Blob blob = resultSet.getBlob(3);
                String base64Image = getBase64Image(blob);

                list.add(new Product(id, name, base64Image, price, description, category, account, isDelete, amount));
            }
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    // Method to get all products from database.
    public List<Product> getAllProducts() {
        String query = "SELECT * FROM product WHERE product_is_deleted = false";
        return getListProductQuery(query);
    }

    // Method to get a categories by its id from database.
    public List<Product> getAllCategoryProducts(int category_id) {
        String query = "SELECT * FROM product WHERE fk_category_id = " + category_id + " AND product_is_deleted = false";
        return getListProductQuery(query);
    }

    // Method to search a product by a keyword.
    public List<Product> searchProduct(String keyword) {
        String query = "SELECT * FROM product WHERE product_name like '%" + keyword + "%' AND product_is_deleted = false";
        return getListProductQuery(query);
    }

    // Method to get 12 products to display on each page.
    public List<Product> get12ProductsOfPage(int index) {
        String query = "SELECT * FROM product WHERE product_is_deleted = false LIMIT " + ((index - 1) * 12) + ", 12";
        return getListProductQuery(query);
    }

    // Method to get total products in database.
    public int getTotalNumberOfProducts() {
        int totalProduct = 0;
        String query = "SELECT COUNT(*) FROM product WHERE product_is_deleted = false";
        try (Connection connection = new Database().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                totalProduct = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return totalProduct;
    }
}
