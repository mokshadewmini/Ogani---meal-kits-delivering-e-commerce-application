/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "nutri_info")
public class NutriInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "calories")
    private String calories;
    
    @Column(name = "fat")
    private String fat;
    
    @Column(name = "protein")
    private String protein;
    
    @Column(name = "carbohdrate")
    private String carbohdrate;
    
    @Column(name = "vam")
    private String vam;
    
   
    @Column(name = "meal_kit_id")
    private int productId;
    
    
    public NutriInfo(){}

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
     * @return the calories
     */
    public String getCalories() {
        return calories;
    }

    /**
     * @param calories the calories to set
     */
    public void setCalories(String calories) {
        this.calories = calories;
    }

    /**
     * @return the fat
     */
    public String getFat() {
        return fat;
    }

    /**
     * @param fat the fat to set
     */
    public void setFat(String fat) {
        this.fat = fat;
    }

    /**
     * @return the protein
     */
    public String getProtein() {
        return protein;
    }

    /**
     * @param protein the protein to set
     */
    public void setProtein(String protein) {
        this.protein = protein;
    }

    /**
     * @return the carbohdrate
     */
    public String getCarbohdrate() {
        return carbohdrate;
    }

    /**
     * @param carbohdrate the carbohdrate to set
     */
    public void setCarbohdrate(String carbohdrate) {
        this.carbohdrate = carbohdrate;
    }

    /**
     * @return the vam
     */
    public String getVam() {
        return vam;
    }

    /**
     * @param vam the vam to set
     */
    public void setVam(String vam) {
        this.vam = vam;
    }

    /**
     * @return the productId
     */
    public int getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    

}
