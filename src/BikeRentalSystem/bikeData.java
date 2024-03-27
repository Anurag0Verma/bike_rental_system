package BikeRentalSystem;


import java.sql.Date;
import javafx.scene.image.Image;

/**
 *
 * @author HP
 */
public class bikeData {

    private Integer bikeId;
    private String brand;
    private String model;
    private Double price;
    private String status;
    private Date date;
    private String image;

    public bikeData(Integer bikeId, String brand, String model, Double price, String status
            ,String image, Date date) {
        this.bikeId = bikeId;
        this.brand = brand;
        this.date = date;
        this.model = model;
        this.price = price;
        this.status = status;
        this.image=image;
    }

    public Integer getbikeId(){
        return bikeId;
    }

    public String getBrand(){
        return brand;

    }
    public String getModel(){
        return model;
    }

    public Double getPrice(){
        return price;
    }

    public String getStatus(){
        return status;
    }

    public Date getDate(){
        return date;
    }
    public String getImage(){
        return image;
    }

}