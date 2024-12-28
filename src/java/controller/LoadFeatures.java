
package controller;

import model.HibernateUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.Category;
import entity.Ingredient;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 *
 * @author SLuda
 */
@WebServlet(name = "LoadFeatures", urlPatterns = {"/LoadFeatures"})
public class LoadFeatures extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new Gson();

        Session session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria1 = session.createCriteria(Category.class);
        criteria1.addOrder(Order.asc("name")); 
        List<Category> categoryList = criteria1.list();


        Criteria criteria3 = session.createCriteria(Ingredient.class);
        criteria3.addOrder(Order.asc("name"));
        List<Ingredient> ingredientList = criteria3.list();

        

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("categoryList", gson.toJsonTree(categoryList));
        jsonObject.add("ingredientList", gson.toJsonTree(ingredientList));
       

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));
        
        session.close();
    }

}
