package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    @Column(name = "first_name", length = 45, nullable = false)
    private String fname;
    @Column(name = "last_name", length = 45, nullable = false)
    private String lname;
    
     @Column(name = "email", length = 45, nullable = false)
    private String email;
    @Column(name = "password", length = 45, nullable = false)
    private String password;
   
    @Column(name = "verification", length = 45, nullable = false)
    private String verification;
    
    
    public User(){}

   
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
     * @return the verification
     */
    public String getVerification() {
        return verification;
    }

    /**
     * @param verification the verification to set
     */
    public void setVerification(String verification) {
        this.verification = verification;
    }
    
    

}
