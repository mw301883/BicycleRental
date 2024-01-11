package pl.polsl.BicycleRental.Model.ModelDB;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Calendar;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Bicycle")
public class Bicycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bicycleId")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private String type;
    @Column(name = "photoURL")
    private String photoURL;
    @Column(name = "pricePerDay")
    private BigDecimal pricePerDay;
    @Column(name = "rentStartDate")
    private Calendar rentStartDate;
    @Column(name = "rentEndDate")
    private Calendar rentEndDate;
    @Column(name = "Disable")
    private Boolean disable;
    public Bicycle(String name, String type, String photoURL, double pricePerDay){
        this.name = name;
        this.type = type;
        this.photoURL = photoURL;
        this.pricePerDay = new BigDecimal(pricePerDay);
        this.rentStartDate = null;
        this.rentEndDate = null;
        this.disable = false;
    }
    //TODO dopracować tak aby można rower wyswietlał się w momencie gdy użytkownik poda datę, w której rower nie jest zarezerwowany.
    public boolean isRented() {
        return rentStartDate != null && rentEndDate != null;
    }
    public void rentBicycle(Calendar start, Calendar end) {
        this.rentStartDate = start;
        this.rentEndDate = end;
    }

}
