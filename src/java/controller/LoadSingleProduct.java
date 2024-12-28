package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Product;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadSingleProduct", urlPatterns = {"/LoadSingleProduct"})
public class LoadSingleProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            // Get product ID from the request parameter
            String productId = request.getParameter("pid");

            if (Validation.isInteger(productId)) {
                int pid = Integer.parseInt(productId);

                // Use Criteria API to fetch the product by its ID
                Criteria criteriaProduct = session.createCriteria(Product.class);
                criteriaProduct.add(Restrictions.eq("id", pid));
                Product product = (Product) criteriaProduct.uniqueResult();

                if (product != null) {
                    // Exclude sensitive information from the user entity
                    product.getUser().setEmail(null);
                    product.getUser().setPassword(null);
                    product.getUser().setVerification(null);

                    // Create a JSON object for the response
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.add("product", gson.toJsonTree(product));

                    // Send the product details as JSON response
                    response.setContentType("application/json");
                    response.getWriter().write(gson.toJson(jsonObject));

                } else {
                    // If no product found, send an error response
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                }
            } else {
                // If the product ID is invalid, send a bad request response
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Product ID");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request");
        } finally {
            session.close();  // Ensure session is closed
        }
    }
}