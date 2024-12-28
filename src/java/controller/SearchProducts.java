
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Category;
import entity.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author EDI
 */
@WebServlet(name = "SearchProducts", urlPatterns = {"/SearchProducts"})
public class SearchProducts extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);

        //get request json
        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        Session session = HibernateUtil.getSessionFactory().openSession();

        //search all products
        Criteria criteria1 = session.createCriteria(Product.class);

        //add category filter
        if (requestJsonObject.has("category_name")) {
            //category selected
            
            String categoryName = requestJsonObject.get("category_name").getAsString();

            if (!categoryName.equals("All")) {
                 //get category list from Db
            Criteria criteria2 = session.createCriteria(Category.class);
            criteria2.add(Restrictions.eq("name", categoryName));
            List<Category> categoryList = criteria2.list();

            //filter products by model list from DB
            criteria1.add(Restrictions.in("category", categoryList));
            }
           
        }

        String startPriceString = String.valueOf(requestJsonObject.get("price_range_start")).replace("$", "").trim();
        String endPriceString = String.valueOf(requestJsonObject.get("price_range_end")).replace("$", "").trim();

        // Remove any non-numeric characters except for a decimal point
        startPriceString = startPriceString.replaceAll("[^\\d.]", "");
        endPriceString = endPriceString.replaceAll("[^\\d.]", "");

        if (startPriceString.isEmpty()) {
            startPriceString = "0";
        }
        if (endPriceString.isEmpty()) {
            endPriceString = "0";
        }

        double startPrice = Double.parseDouble(startPriceString);
        double endPrice = Double.parseDouble(endPriceString);

        System.out.println("Start Price: " + startPrice);
        System.out.println("End Price: " + endPrice);

        criteria1.add(Restrictions.ge("price", startPrice));
        criteria1.add(Restrictions.le("price", endPrice));

        if (requestJsonObject.has("search")) {
        //search 
            String search = requestJsonObject.get("search").getAsString();
            System.out.println(search);
            criteria1.add(Restrictions.like("name", "%" + search + "%"));
            
        }
        
        //filter products by sort from Db
        String sortText = requestJsonObject.get("sort_text").getAsString();

        if (sortText.equals("Sort by Latest")) {
            criteria1.addOrder(Order.desc("id"));

        } else if (sortText.equals("Sort by Oldest")) {
            criteria1.addOrder(Order.asc("id"));

        } else if (sortText.equals("Sort by Name")) {
            criteria1.addOrder(Order.asc("title"));

        } else if (sortText.equals("price_asc")) {
            criteria1.addOrder(Order.asc("price"));

        } else if (sortText.equals("price_desc")) {
            criteria1.addOrder(Order.desc("price"));

        }

        //get all product count
        responseJsonObject.addProperty("allProductCount", criteria1.list().size());

        //get product list
        List<Product> productList = criteria1.list();
        System.out.println(productList.size());

        //get product list
        for (Product product : productList) {
            product.setUser(null);
        }

        responseJsonObject.addProperty("success", true);
        responseJsonObject.add("productList", gson.toJsonTree(productList));

        //send response
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));

    }
}
