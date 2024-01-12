package pl.polsl.BicycleRental.Model.ModelDB;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "AdminAccount")
public class AdminAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adminId")
    private Long id;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;
    private String Authority;
    private boolean enabled;

    public AdminAccount(String Username, String Password) {
        this.login = Username;
        this.password = Password;
        this.Authority = "ADMIN";
        this.enabled = true;
    }
}
