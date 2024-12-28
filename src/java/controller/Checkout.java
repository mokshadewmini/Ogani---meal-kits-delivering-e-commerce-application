package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.User_DTO;
import entity.Address;
import entity.Cart;
import entity.City;
import entity.Order_Status;
import entity.Order_item;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.Mail;
import model.Payhere;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "Checkout", urlPatterns = {"/Checkout"})
public class Checkout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();

        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);

        HttpSession httpSession = request.getSession();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        boolean isCurrentAddress = requestJsonObject.get("isCurrentAddress").getAsBoolean();
        String firstName = requestJsonObject.get("firstName").getAsString();
        String lastName = requestJsonObject.get("lastName").getAsString();
        String cityId = requestJsonObject.get("cityId").getAsString();
        String address1 = requestJsonObject.get("address1").getAsString();
        String address2 = requestJsonObject.get("address2").getAsString();
        String postalCode = requestJsonObject.get("postalCode").getAsString();
        String mobile = requestJsonObject.get("mobile").getAsString();     

        if (httpSession.getAttribute("user") != null) {
            //user signed in

            //get user from db
            User_DTO user_DTO = (User_DTO) httpSession.getAttribute("user");
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
            User user = (User) criteria1.uniqueResult();

            if (isCurrentAddress) {
                //get current address

                //get address from db
                Criteria criteria2 = session.createCriteria(Address.class);
                criteria2.add(Restrictions.eq("user", user));
                criteria2.addOrder(Order.desc("id"));
                criteria2.setMaxResults(1);

                if (criteria2.list().isEmpty()) {
                    //current address not found
                    responseJsonObject.addProperty("message", "Current address not found. Please create a new address");

                } else {
                    //get the current address
                    Address address = (Address) criteria2.list().get(0);

                    //***do the checkout process
                    saveOrders(session, transaction, user, address, responseJsonObject);
                }

            } else {
                //create new address

                if (firstName.isEmpty()) {
                    responseJsonObject.addProperty("message", "Please fill first name");

                } else if (lastName.isEmpty()) {
                    responseJsonObject.addProperty("message", "Please fill last name");

                } else if (!Validation.isInteger(cityId)) {
                    responseJsonObject.addProperty("message", "Invalid City");

                } else {
                    //check city from db

                    Criteria criteria3 = session.createCriteria(City.class);
                    criteria3.add(Restrictions.eq("id", Integer.parseInt(cityId)));

                    if (criteria3.list().isEmpty()) {
                        responseJsonObject.addProperty("message", "Invalid city selected");

                    } else {
                        //city found
                        City city = (City) criteria3.list().get(0);

                        if (address1.isEmpty()) {
                            responseJsonObject.addProperty("message", "Please fill address line 1");

                        } else if (address2.isEmpty()) {
                            responseJsonObject.addProperty("message", "Please fill address line 2");

                        } else if (postalCode.isEmpty()) {
                            responseJsonObject.addProperty("message", "Please fill postal code");

                        } else if (postalCode.length() != 5) {
                            responseJsonObject.addProperty("message", "Invalid postal code");

                        } else if (!Validation.isInteger(postalCode)) {
                            responseJsonObject.addProperty("message", "Invalid postal code");

                        } else if (mobile.isEmpty()) {
                            responseJsonObject.addProperty("message", "Please fill mobile number");

                        } else if (!Validation.isMobileValid(mobile)) {
                            responseJsonObject.addProperty("message", "Invalid mobile number");

                        } else {
                            //create new address

                            Address address = new Address();
                            address.setCity(city);
                            address.setFirst_name(firstName);
                            address.setLast_name(lastName);
                            address.setLine1(address1);
                            address.setLine2(address2);
                            address.setMobile(mobile);
                            address.setPostal_code(postalCode);
                            address.setUser(user);

                            session.save(address);

                            //***complete the checkout process
                        }
                    }

                }
            }

        } else {
            //user not signed in
            responseJsonObject.addProperty("message", "User not signed in");
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));

    }

    private void saveOrders(Session session, Transaction transaction, User user, Address address, JsonObject responseJsonObject) {
        try {
            //create order in DB
            entity.Orders order = new entity.Orders();
            order.setAddress(address);
            order.setDate_time(new Date());
            order.setUser(user);
            int order_id = (int) session.save(order);

            //get cart items
            Criteria criteria4 = session.createCriteria(Cart.class);
            criteria4.add(Restrictions.eq("user", user));
            List<Cart> cartList = criteria4.list();

            //get order status (1.Paiad) from DB
            Order_Status order_Status = (Order_Status) session.get(Order_Status.class, 1);

            //create order item in DB
            double amount = 0;
            String items = "";
            for (Cart cartItem : cartList) {

                //calculate amount
                amount += cartItem.getProduct().getPrice() * cartItem.getQty();
                if (address.getCity().getId() == 1) {
                    amount += 1000;
                } else {
                    amount += 2500;
                }
                //calculate amount

                //Get Item details
                items += cartItem.getProduct().getName() + " x" + cartItem.getQty() + " ";
                //Get Item details

                Product product = cartItem.getProduct();

                Order_item order_item = new Order_item();
                order_item.setOrder(order);
                order_item.setOrder_status(order_Status);
                order_item.setProduct(product);
                order_item.setQty(cartItem.getQty());
                session.save(order_item);

                //update product qty in DB
                product.setQty(product.getQty() - cartItem.getQty());
                session.update(product); 

                //delete cart item from DB
                session.delete(cartItem);

            }

            transaction.commit();

            //set payment data (start)
            String merchant_id = "1221748";
            String formatted_amount = new DecimalFormat("0.00").format(amount);
            String currency = "LKR";
            String merchantSecret = "MTgxNTI4NzE0MDg5OTE2ODYxMjYzMTE5NDg2NDE2MDAxMDU5Nzg="; //**
            String merchantSecretMdHash = Payhere.generateMD5(merchantSecret);

            JsonObject payhere = new JsonObject();
            payhere.addProperty("merchant_id", merchant_id);

            payhere.addProperty("return_url", "");
            payhere.addProperty("cancel_url", "");
            payhere.addProperty("notify_url", ""); //***

            payhere.addProperty("first_name", user.getFname());
            payhere.addProperty("last_name", user.getLname());
            payhere.addProperty("email", user.getEmail());

            payhere.addProperty("phone", "");
            payhere.addProperty("address", "");
            payhere.addProperty("city", "");
            payhere.addProperty("country", "");

            payhere.addProperty("order_id", String.valueOf(order_id));
            payhere.addProperty("items", items);
            payhere.addProperty("currency", "LKR");
            payhere.addProperty("amount", formatted_amount);
            payhere.addProperty("sandbox", true);

            //Generate MD5 Hash
            //merahantID + orderID + amountFormatted + currency + getMd5(merchantSecret)
            String md5Hash = Payhere.generateMD5(merchant_id + order_id + formatted_amount + currency + merchantSecretMdHash);
            payhere.addProperty("hash", md5Hash);

            //set payment data (end)
            responseJsonObject.addProperty("success", true);
            responseJsonObject.addProperty("message", "Checkout completed");

            Gson gson = new Gson();
            responseJsonObject.add("payhereJson", gson.toJsonTree(payhere));
            
            // Generate HTML invoice
            String htmlInvoice = generateHtmlInvoice(order_id, items, amount, currency);

            // Send email with invoice
            String emailSubject = "Your Order Invoice - Order ID: " + order_id;
            Mail.sendMail(user.getEmail(), emailSubject, htmlInvoice);

        } catch (Exception e) {
            transaction.rollback();
        }
    }
    
    private String generateHtmlInvoice(int orderId, String items, double amount, String currency) {
        return "<!DOCTYPE html>\n" +
"<html lang=\"en\">\n" +
"<head>\n" +
"    <meta charset=\"UTF-8\">\n" +
"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
"    <title>Invoice</title>\n" +
"</head>\n" +
"<body style=\"font-family: Arial, sans-serif; padding: 20px; background-color: #f8f8f8;\">\n" +
"\n" +
"    <div style=\"max-width: 800px; margin: 0 auto; background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 4px 16px rgba(0,0,0,0.1);\">\n" +
"        <!-- Header Section -->\n" +
"<img src=\"https://themewagon.github.io/ogani/img/logo.png\"/>\n" +
"        <div style=\"text-align: center; margin-bottom: 40px;\">\n" +
"            <h1 style=\"font-size: 36px; color: #333;\">INVOICE</h1>\n" +
"            <p style=\"font-size: 14px; color: #777;\">Invoice #" + orderId + "</p>\n" +
"            <p style=\"font-size: 14px; color: #777;\">Date: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "</p>\n" +
"        </div>\n" +
"\n" +
"        <!-- From and To Section -->\n" +
"        <div style=\"display: flex; justify-content: space-between; margin-bottom: 40px;\">\n" +
"            <div style=\"flex-basis: 45%;\">\n" +
"                <h3 style=\"font-size: 18px; color: #333;\">From:</h3>\n" +
"                <p style=\"font-size: 14px; color: #777;\">Ogani</p>\n" +
"                <p style=\"font-size: 14px; color: #777;\">Colombo, Sri Lanka</p>\n" +
"                <p style=\"font-size: 14px; color: #777;\">Phone:  +94 111 345 654</p>\n" +
"                <p style=\"font-size: 14px; color: #777;\">Email: ogani@gmail.com</p>\n" +
"            </div>\n" +
"\n" +
"\n" +
"        </div>\n" +
"\n" +
"        <!-- Invoice Table -->\n" +
"        <table style=\"width: 100%; border-collapse: collapse; margin-bottom: 40px;\">\n" +
"            <thead>\n" +
"                <tr>\n" +
"                    \n" +
"                    <th style=\"text-align: right; padding: 10px 15px; background-color: #f2f2f2; border-bottom: 2px solid #ddd;\">Items</th>\n" +
"                    <th style=\"text-align: right; padding: 10px 15px; background-color: #f2f2f2; border-bottom: 2px solid #ddd;\">Total</th>\n" +
"                </tr>\n" +
"            </thead>\n" +
"            <tbody>\n" +
"                <tr>\n" +
"                  \n" +
"                    <td style=\"padding: 10px 15px; text-align: right; border-bottom: 1px solid #ddd;\">" + items + "</td>\n" +
"                    <td style=\"padding: 10px 15px; text-align: right; border-bottom: 1px solid #ddd;\">" + amount + " " + currency + "</td>\n" +
"                </tr>\n" +
"                <!-- Repeat the above row for each additional item -->\n" +
"            </tbody>\n" +
"        </table>\n" +
"\n" +
"       \n" +
"\n" +
"        <!-- Payment Info Section -->\n" +
"        <div style=\"text-align: center; margin-top: 40px;\">\n" +
"            <p style=\"font-size: 14px; color: #777;\">Thank you for your order.</p>\n" +
"          <p style=\"font-size: 14px; color: #777;\">Payment due within 30 days.</p>\n" +
"            <p style=\"font-size: 14px; color: #777;\">Ogani, Transaction with PayHere Payment Gateway</p>\n" +
"        </div>\n" +
"\n" +
"        <!-- Footer Section -->\n" +
"        <div style=\"text-align: center; margin-top: 50px; font-size: 12px; color: #aaaaaa;\">\n" +
"            <p>&copy; 2024 Ogani. All rights reserved.</p>\n" +
"        </div>\n" +
"    </div>\n" +
"\n" +
"</body>\n" +
"</html>";
    }

}
