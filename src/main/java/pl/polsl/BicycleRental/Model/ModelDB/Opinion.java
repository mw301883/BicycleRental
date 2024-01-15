package pl.polsl.BicycleRental.Model.ModelDB;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//W pakietach model tworzymy encje do bazy danych, konfiguracja DB jest aktualnie ustawiona na "update" tzn, że mozna
//ją tylko  modyfikować (to można zmienic w application properties np na create wtedy gdy odpalimy projekt baza zostanie wyczyszczona
// przy kazdym odpaleniu apki).
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table (name = "Opinion")
public class Opinion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "opinionId")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "customerEmail")
    private String customerEmail;
    @Column(name = "subject")
    private String subject;
    @Column(name = "message")
    private String message;

    public Opinion(String name, String email, String subject, String message) {
        this.name = name;
        this.customerEmail = email;
        this.subject = subject;
        this.message = message;
    }
}
