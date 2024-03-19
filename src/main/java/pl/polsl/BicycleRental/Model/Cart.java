package pl.polsl.BicycleRental.Model;

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
@Getter
@Setter
@NoArgsConstructor
public class Cart {

    private ArrayList<Long> bicyclesIDs = new ArrayList<>();

    private Calendar beginRent = null;

    private Calendar endRent = null;

    private BigDecimal price = BigDecimal.valueOf(0.0);

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
        this.price = BigDecimal.valueOf(0.0);
    }
}
