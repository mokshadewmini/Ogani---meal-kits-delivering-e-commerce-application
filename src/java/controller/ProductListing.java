package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Category;
import entity.Ingredient;
import entity.NutriInfo;
import entity.Product;
import entity.User;
import entity.Product_Status;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.enterprise.inject.Model;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@MultipartConfig
@WebServlet(name = "ProductListing", urlPatterns = {"/ProductListing"})
public class ProductListing extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Response_DTO response_DTO = new Response_DTO();

        Gson gson = new Gson();
        Session session = HibernateUtil.getSessionFactory().openSession();

        String categoryId = request.getParameter("categoryId");
        String ingredientId = request.getParameter("ingredient");
        String title = request.getParameter("mealKit");
        String caloryId = request.getParameter("calories");
        String fatId = request.getParameter("fat");
        String carbohydrateId = request.getParameter("carbohydrate");
        String proteinId = request.getParameter("protein");
        String vamId = request.getParameter("vitaminsMinerals");

        String price = request.getParameter("price");
        String quantity = request.getParameter("qty");
        String description = request.getParameter("description");

        Part image1 = request.getPart("img1");
        Part image2 = request.getPart("img2");
        Part image3 = request.getPart("img3");

//        System.out.println(categoryId);
//        System.out.println(ingredientId);
//        System.out.println(title);
//        System.out.println(caloryId);
//        System.out.println(fatId);
//        System.out.println(carbohdrateId);
//        System.out.println(proteinId);
//        System.out.println(vamId);
//        System.out.println(price);
//        System.out.println(quantity);
//        System.out.println(description);
        if (!Validation.isInteger(categoryId)) {
            System.out.println("Invalid Category");
            response_DTO.setContent("Invalid Category");

        } else if (!Validation.isInteger(ingredientId)) {
            response_DTO.setContent("Invalid Ingredient");

        } else if (title.isEmpty()) {
            response_DTO.setContent("Please fill Meal kit name");

        } else if (description.isEmpty()) {
            System.out.println("Invalid des");
            response_DTO.setContent("Please fill Description");

        } else if (price.isEmpty()) {
            response_DTO.setContent("Please fill Price");

        } else if (!Validation.isDouble(price)) {
            response_DTO.setContent("Invalid price");

        } else if (Double.parseDouble(price) <= 0) {
            response_DTO.setContent("Price must be greater than 0");

        } else if (quantity.isEmpty()) {
            response_DTO.setContent("Invalid Quantity");

        } else if (!Validation.isInteger(quantity)) {
            response_DTO.setContent("Invalid Quantity");

        } else if (Integer.parseInt(quantity) <= 0) {
            response_DTO.setContent("Quantity must be greater than 0");

        } else if (image1.getSubmittedFileName() == null) {
            response_DTO.setContent("Please upload image1");

        } else if (image2.getSubmittedFileName() == null) {
            response_DTO.setContent("Please upload image2");

        } else if (image3.getSubmittedFileName() == null) {
            response_DTO.setContent("Please upload image3");

        } else {

            Category category = (Category) session.get(Category.class, Integer.parseInt(categoryId));

            if (category == null) {
                response_DTO.setContent("Please select a valid Category");

            } else {

                Ingredient ingredient = (Ingredient) session.get(Ingredient.class, Integer.parseInt(ingredientId));

                if (ingredient == null) {
                    response_DTO.setContent("Please select a valid Inngredient");

                } else {
                    //to do Insert 
                    Product product = new Product();
                    product.setCategory(category);
                    product.setDate_time(new Date());
                    product.setDescription(description);
                    product.setIngredient(ingredient);
                    product.setPrice(Double.parseDouble(price));
                    product.setDate_time(new Date());
                    product.setDescription(description);
                    product.setName(title);
                    product.setQty(Integer.parseInt(quantity));

                    //get active status
                    Product_Status product_Status = (Product_Status) session.load(Product_Status.class, 1);
                    product.setProduct_Status(product_Status);

                    //get user
                    // Get the User entity from the session
                    final User user = (User) request.getSession().getAttribute("user");

// If user exists, convert to User_DTO
                    User_DTO user_DTO = null;
                    if (user != null) {
                        user_DTO = User_DTO.fromEntity(user); // Convert entity to DTO
                    }

// Now you can use user_DTO safely
                    if (user_DTO != null) {
                        Criteria criteria = session.createCriteria(User.class);
                        criteria.add(Restrictions.eq("email", user_DTO.getEmail()));
                        User foundUser = (User) criteria.uniqueResult();
                        product.setUser(foundUser);
                    }

                    int pid = (int) session.save(product);
                    NutriInfo nutriInfo = new NutriInfo();
                    nutriInfo.setCalories(caloryId);
                    nutriInfo.setCarbohdrate(carbohydrateId);
                    nutriInfo.setFat(fatId);
                    nutriInfo.setProductId(pid);
                    nutriInfo.setProtein(proteinId);
                    nutriInfo.setVam(vamId);
                    session.save(nutriInfo);
                    session.beginTransaction().commit();  // Ensure that transaction is committed to get the `pid`

// Get the application's real path
                    String applicationPath = request.getServletContext().getRealPath("");
                    String newApplicationPath = applicationPath.replace("build" + File.separator + "web", "web");

// Create the directory for the product images
                    File folder = new File(newApplicationPath + File.separator + "product-images" + File.separator + pid);

// Ensure the directory exists, create all necessary parent directories
                    if (!folder.exists()) {
                        if (!folder.mkdirs()) {  // Use mkdirs() instead of mkdir() to create all directories in the path
                            throw new IOException("Failed to create directory: " + folder.getAbsolutePath());
                        }
                    }

                    try {
                        // Handle image 1
                        if (image1 != null && image1.getSize() > 0) {
                            File file1 = new File(folder, "4.jpg");
                            try (InputStream inputStream = image1.getInputStream()) {
                                Files.copy(inputStream, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            }
                        }

                        // Handle image 2
                        if (image2 != null && image2.getSize() > 0) {
                            File file2 = new File(folder, "pend.jpg");
                            try (InputStream inputStream2 = image2.getInputStream()) {
                                Files.copy(inputStream2, file2.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            }
                        }

                        // Handle image 3
                        if (image3 != null && image3.getSize() > 0) {
                            File file3 = new File(folder, "2.jpg");
                            try (InputStream inputStream3 = image3.getInputStream()) {
                                Files.copy(inputStream3, file3.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            }
                        }

                    } catch (IOException e) {
                        throw new ServletException("Error saving images", e);
                    }

                    response_DTO.setSuccess(true);
                    response_DTO.setContent("Product Added Successfully!");
                    System.out.println("Successfully");

                }

            }

        }
        session.close();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
