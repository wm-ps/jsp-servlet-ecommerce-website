package com.ecommerce.dao;

import com.ecommerce.entity.Category;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.Product;
import com.ecommerce.hibernate.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

// Hibernate (hbm.xml mapped) counterpart of CategoryDao / ProductDao / order queries.
public class HibernateCatalogDao {
    public List<Category> getAllCategories() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Category c order by c.id", Category.class).list();
        }
    }

    public List<Product> getAllProducts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Product p where p.isDeleted = false", Product.class).list();
        }
    }

    public List<Product> getCategoryProducts(int categoryId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Product p where p.category.id = :categoryId and p.isDeleted = false", Product.class)
                    .setParameter("categoryId", categoryId)
                    .list();
        }
    }

    public List<Order> getAccountOrders(int accountId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Order> orders = session.createQuery(
                            "from Order o where o.account.id = :accountId order by o.dateCreate desc", Order.class)
                    .setParameter("accountId", accountId)
                    .list();
            orders.forEach(o -> o.getDetails().size()); // initialize lazy collection before session closes
            return orders;
        }
    }
}
