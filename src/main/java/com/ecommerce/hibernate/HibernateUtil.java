package com.ecommerce.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    private HibernateUtil() {
    }

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
            // Same overrides as com.ecommerce.database.Database.
            override(configuration, "hibernate.connection.url", System.getProperty("db.url"), System.getenv("DB_URL"));
            override(configuration, "hibernate.connection.username", System.getProperty("db.user"), System.getenv("DB_USER"));
            override(configuration, "hibernate.connection.password", System.getProperty("db.pass"), System.getenv("DB_PASS"));
            sessionFactory = configuration.buildSessionFactory();
        }
        return sessionFactory;
    }

    private static void override(Configuration configuration, String key, String... candidates) {
        for (String c : candidates) {
            if (c != null && !c.trim().isEmpty()) {
                configuration.setProperty(key, c);
                return;
            }
        }
    }
}
