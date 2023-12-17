package pl.polsl.BicycleRental.Model.ModelDB;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "CustomerOrder")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderId")
    private Long id;
    @Column(name = "bicycles")
    private ArrayList<Long> bicycles = new ArrayList<>();
    @Column(name = "beginRent")
    private Calendar beginRent;
    @Column(name = "endRent")
    private Calendar endRent;
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
    private BigDecimal price;
    @Column(name = "penaltyPrice")
    private BigDecimal penaltyPrice;
    public Order(ArrayList<Long> bicycles, Calendar beginRent, Calendar endRent, String customerFirstName, String customerLastName,
                 String customerAddress, String customerCity, String customerPostalCode, String customerEmail, String customerPhoneNum,
                 BigDecimal price){
        this.bicycles = bicycles;
        this.beginRent = beginRent;
        this.endRent = endRent;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerAddress = customerAddress;
        this.customerCity = customerCity;
        this.customerPostalCode = customerPostalCode;
        this.customerEmail = customerEmail;
        this.customerPhoneNum = customerPhoneNum;
        this.price = price;
        this.penaltyPrice = null;
    }
}
