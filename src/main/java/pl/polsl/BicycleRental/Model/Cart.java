package pl.polsl.BicycleRental.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.polsl.BicycleRental.Model.ModelDB.Bicycle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {
    private ArrayList<Bicycle> bicyclesInCart = new ArrayList<>();
    private ArrayList<Long> bicyclesIDs = new ArrayList<>();
    private Calendar beginRent = null;
    private Calendar endRent = null;
    private String customerFirstName;
    private String customerLastName;
    private String customerAddress;
    private String customerCity;
    private String customerPostalCode;
    private String customerEmail;
    private String customerPhoneNum;
    private BigDecimal price = BigDecimal.valueOf(0.0);
    public int getCartSize(){
        return this.bicyclesInCart.size();
    }
    public void addBicycleToCart(Bicycle bicycle){
        this.bicyclesInCart.add(bicycle);
    }
    public void removeBicycleFromCart(int index){
        this.bicyclesInCart.remove(index);
    }
    public void clearCart(){
        this.bicyclesInCart.clear();
        this.beginRent = null;
        this.endRent = null;
        this.customerFirstName = "";
        this.customerLastName = "";
        this.customerAddress = "";
        this.customerCity = "";
        this.customerPostalCode = "";
        this.customerEmail = "";
        this.customerPhoneNum = "";
        this.price = BigDecimal.valueOf(0.0);
    }
}
