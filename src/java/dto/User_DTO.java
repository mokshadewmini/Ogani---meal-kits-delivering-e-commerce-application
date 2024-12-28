
package dto;

import com.google.gson.annotations.Expose;
import entity.User;
import java.io.Serializable;


public class User_DTO implements Serializable {

    @Expose
    private int id;
    
    @Expose
    private String fname;
    
    @Expose
    private String lname;
    
    @Expose
    private String email;
    
    @Expose(deserialize = true ,serialize = false)
    private String password;

     // Implement the fromEntity method
    public static User_DTO fromEntity(User user) {
        User_DTO dto = new User_DTO();
        dto.setId(user.getId());
        dto.setFname(user.getFname());
        dto.setLname(user.getLname());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());  // If you don't want to include password, skip this line
        return dto;
    }
    public User_DTO() {
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the fname
     */
    public String getFname() {
        return fname;
    }

    /**
     * @param fname the fname to set
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * @return the lname
     */
    public String getLname() {
        return lname;
    }

    /**
     * @param lname the lname to set
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
