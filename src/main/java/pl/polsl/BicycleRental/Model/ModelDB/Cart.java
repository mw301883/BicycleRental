package pl.polsl.BicycleRental.Model.ModelDB;

import jakarta.persistence.*;
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
@Entity
@Table(name = "Cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "bicyclesIDs")
    private ArrayList<Long> bicyclesIDs = new ArrayList<>();
    @Column(name = "beginRentDate")
    private Calendar beginRent = null;
    @Column(name = "endRentDate")
    private Calendar endRent = null;
    @Column(name = "customerFirstName")
    private String customerFirstName;
    @Column(name = "customerLastName")
    private String customerLastName;
    @Column(name = "customerAddress")
    private String customerAddress;
    @Column(name = "customerCity")
    private String customerCity;
    @Column(name = "customerPostalCode")
    private String customerPostalCode;
    @Column(name = "customerEmail")
    private String customerEmail;
    @Column(name = "customerPhoneNum")
    private String customerPhoneNum;
    @Column(name = "price")
    private BigDecimal price = BigDecimal.valueOf(0.0);
    @Column(name = "sessionID")
    private String sessionID;
    public Cart(String sessionID) {
        this.sessionID = sessionID;
    }

    public int getCartSize() {
        return this.bicyclesIDs.size();
    }

    public void addBicycleToCart(Long id) {
        this.bicyclesIDs.add(id);
    }

    public void removeBicycleFromCart(int index, BigDecimal pricePerDay) {
        long millisecondsPerDay = 24 * 60 * 60 * 1000L;

        BigDecimal rentalDurationInDays =
                BigDecimal.valueOf((this.endRent.getTimeInMillis() - this.beginRent.getTimeInMillis()) / millisecondsPerDay);

        BigDecimal bicyclePrice = pricePerDay;
        BigDecimal totalPriceToRemove = bicyclePrice.multiply(rentalDurationInDays);

        this.price = this.price.subtract(totalPriceToRemove);
        this.bicyclesIDs.remove(index);
    }

    public void clearCart() {
        this.bicyclesIDs.clear();
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
