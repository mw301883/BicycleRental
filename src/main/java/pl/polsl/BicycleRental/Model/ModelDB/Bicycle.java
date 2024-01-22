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
    @Getter
    @Column(name = "rentStartDate")
    private Calendar rentStartDate;
    @Getter
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
    public boolean isDisable() {
        return disable;
    }
    //TODO dopracować tak aby rower wyswietlał się w momencie gdy użytkownik poda datę, w której rower nie jest zarezerwowany. Dodać kolumne isAvaiable
    public boolean isRented() {
        return rentStartDate != null && rentEndDate != null;
    }

    public void rentBicycle(Calendar start, Calendar end) {
        this.rentStartDate = start;
        this.rentEndDate = end;
    }
    public boolean isDateRangeOverlap(Calendar start1, Calendar end1, Calendar start2, Calendar end2) {
        try {
            if (start1 != null && end1 != null && start2 != null && end2 != null) {
                // Sprawdź czy daty nakładają się tylko gdy obie pary dat nie są nullami
                return (start1.before(end2) && end1.after(start2)) ||  // Normalny przypadek
                        (start1.equals(start2) || end1.equals(end2));   // Przypadek daty równej

            } else {
                // Jeśli chociaż jedna para dat jest null, nie zwracaj nakładającego się zakresu dat
                return false;
            }
        } catch (NullPointerException e) {
            // Złapano NullPointerException, zwróć false
            return false;
        }
    }

}
