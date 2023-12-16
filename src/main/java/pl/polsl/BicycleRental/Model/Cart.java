package pl.polsl.BicycleRental.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.polsl.BicycleRental.Model.ModelDB.Bicycle;

import java.util.ArrayList;
import java.util.Calendar;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {
    private ArrayList<Bicycle> bicyclesInCart = new ArrayList<>();
    private Calendar beginRent;
    private Calendar endRent;
    private String customerFirstName;
    private String customerLastName;
    private String customerAddress;
    private String customerCity;
    private String customerPostalCode;
    private String customerEmail;
    private String customerPhoneNum;
    public int getCartSize(){
        return this.bicyclesInCart.size();
    }
    public void addBicycleToCart(Bicycle bicycle){
        this.bicyclesInCart.add(bicycle);
    }
    public void removeBicycleFromCart(int index){
        this.bicyclesInCart.remove(index);
    }
//    public void setCustomerData(String customerFirstName, String customerLastName, String customerAddress, String customerCity,
//                                String customerPostalCode, String customerEmail, String customerPhoneNum ){
//
//    }
}
