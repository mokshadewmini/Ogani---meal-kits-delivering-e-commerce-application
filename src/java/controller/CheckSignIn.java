package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Product;
import entity.User;
import java.io.IOException;
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

@WebServlet(name = "CheckSignIn", urlPatterns = {"/CheckSignIn"})
public class CheckSignIn extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JsonObject jsonObject = new JsonObject();

        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        Session session = HibernateUtil.getSessionFactory().openSession();

        if (request.getSession().getAttribute("user") != null) {
            //already signed in 

            final User_DTO user_DTO = (User_DTO) request.getSession().getAttribute("user");

// If user exists, convert to User_DTO
           

// Now you can use user_DTO safely
            
                Criteria criteria = session.createCriteria(User.class);
                criteria.add(Restrictions.eq("email", user_DTO.getEmail()));
                User foundUser = (User) criteria.uniqueResult();
                response_DTO.setSuccess(true);
                response_DTO.setContent(user_DTO);
            

        }

        jsonObject.add("response_dto", gson.toJsonTree(response_DTO));

        //get 3 most recent products
        Criteria criteria = session.createCriteria(Product.class);
        criteria.addOrder(Order.desc("id"));
        criteria.setMaxResults(8);
        List<Product> productList = criteria.list();
        for (Product product : productList) {
            product.setUser(null);
            
        }
        
         

        Gson gson1 = new Gson();
        jsonObject.add("products", gson1.toJsonTree(productList));

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));

    }

}
