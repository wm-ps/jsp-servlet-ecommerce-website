package com.ecommerce.control;

import com.ecommerce.dao.HibernateCatalogDao;
import com.ecommerce.entity.Category;
import com.ecommerce.entity.Product;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// Same page as /category, but loaded through Hibernate. Optional ?category_id=N.
@WebServlet(name = "HibernateShopControl", value = "/hibernate-shop")
public class HibernateShopControl extends HttpServlet {
    HibernateCatalogDao catalogDao = new HibernateCatalogDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String categoryParam = request.getParameter("category_id");
        List<Product> productList = categoryParam == null
                ? catalogDao.getAllProducts()
                : catalogDao.getCategoryProducts(Integer.parseInt(categoryParam));
        List<Category> categoryList = catalogDao.getAllCategories();

        request.setAttribute("product_list", productList);
        request.setAttribute("category_list", categoryList);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("shop.jsp");
        requestDispatcher.forward(request, response);
    }
}
