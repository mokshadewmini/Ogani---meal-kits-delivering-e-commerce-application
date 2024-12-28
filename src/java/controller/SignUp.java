
package controller;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Response_DTO;
import dto.User_DTO;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Mail;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//          System.out.println("done");
        Response_DTO response_DTO = new Response_DTO();

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        User_DTO user_DTO = gson.fromJson(request.getReader(), User_DTO.class);

        System.out.println(user_DTO.getFname());
        if (user_DTO.getFname().isEmpty()) {
            response_DTO.setContent("please enter your first name");
        } else if (user_DTO.getLname().isEmpty()) {
            response_DTO.setContent("please enter your last name");
        } else if (user_DTO.getEmail().isEmpty()) {
            response_DTO.setContent("please enter your email");

        } else if (!Validation.isEmailValid(user_DTO.getEmail())) {
            response_DTO.setContent("please enter Valid password");

        } else if (user_DTO.getPassword().isEmpty()) {
            response_DTO.setContent("please enter your password");
        } else {

            //hibanate verification code sending
            Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", user_DTO.getEmail()));

            if (!criteria.list().isEmpty()) {
                response_DTO.setContent("User With this Email already exists");

            } else {
                //genarate Vreification code

                // Generate 6 random digits
                int code = (int) (Math.random() * 1000000);

                User user = new User();
                user.setEmail(user_DTO.getEmail());
                user.setFname(user_DTO.getFname());
                user.setLname(user_DTO.getLname());
                user.setPassword(user_DTO.getPassword());
                user.setVerification(String.valueOf(code));

                //send Email Verifycation
                
                        Mail.sendMail(user_DTO.getEmail(),
                                "Ogani User Account verification Code",
                                "<!DOCTYPE html>\n" +
"<html lang=\"en\">\n" +
"<head>\n" +
"    <meta charset=\"UTF-8\">\n" +
"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
"    <title>Email Verification</title>\n" +
"    <style>\n" +
"        body {\n" +
"            font-family:  Tahoma, Geneva, Verdana, sans-serif;\n" +
"            background-color: #f4f4f4;\n" +
"            padding: 20px;\n" +
"        }\n" +
"        .email-container {\n" +
"            max-width: 600px;\n" +
"            margin: 0 auto;\n" +
"            background-color: #ffffff;\n" +
"            padding: 30px;\n" +
"            border-radius: 10px;\n" +
"            box-shadow: 0 4px 16px rgba(0,0,0,0.1);\n" +
"            text-align: center;\n" +
"        }\n" +
"        .logo {\n" +
"            width: 200px;\n" +
"            margin-bottom: 20px;\n" +
"        }\n" +
"        .email-content {\n" +
"            margin-bottom: 20px;\n" +
"            color: #333333;\n" +
"            line-height: 1.6;\n" +
"        }\n" +
"        .verification-code {\n" +
"            display: inline-block;\n" +
"            background-color: #28a745;\n" +
"            color: #ffffff;\n" +
"            padding: 10px 20px;\n" +
"            border-radius: 5px;\n" +
"            font-size: 24px;\n" +
"            letter-spacing: 2px;\n" +
"            margin: 20px 0;\n" +
"        }\n" +
"        .footer {\n" +
"            margin-top: 20px;\n" +
"            font-size: 12px;\n" +
"            color: #aaaaaa;\n" +
"        }\n" +
"    </style>\n" +
"</head>\n" +
"<body>\n" +
"    <div class=\"email-container\">\n" +
"        <img src=\"https://themewagon.github.io/ogani/img/logo.png\" alt=\"Logo\" class=\"logo\">\n" +
"        <h2>Email Verification Required</h2>\n" +
"        <div class=\"email-content\">\n" +
"            <p>Hello,"+user.getFname()+"</p>\n" +
"            <p>An user action requires verification. Please use the verification code below to proceed.</p>\n" +
"            <div class=\"verification-code\">"+user.getVerification()+"</div>\n" +
"            <p>If you did not request this action, please contact support immediately.</p>\n" +
"            <p>Thank you,<br>The SoftLeaRNER Team</p>\n" +
"        </div>\n" +
"        <div class=\"footer\">\n" +
"            <p>&copy; 2024 SoftLeaRNER. All rights reserved.</p>\n" +
"            <p>Wellawattha, Colombo 06, Sri Lanka.</p>\n" +
"        </div>\n" +
"    </div>\n" +
"</body>\n" +
"</html>");
                    

                session.save(user);
                session.beginTransaction().commit();
                request.getSession().setAttribute("email", user_DTO.getEmail());
                response_DTO.setSuccess(true);
                response_DTO.setContent("Registration Complite. please check your inbox"
                        + "for Verification Code!");

            }

            session.close();

        }

        response.setContentType("application/json");

        response.getWriter().write(gson.toJson(response_DTO));
        System.out.println(gson.toJson(response_DTO));
    }

}
