package pl.polsl.BicycleRental.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.polsl.BicycleRental.Model.ModelDB.AdminAccount;
import pl.polsl.BicycleRental.Model.ModelDB.Bicycle;
import pl.polsl.BicycleRental.Model.Repository.AdminAccountRepo;
import pl.polsl.BicycleRental.Model.Repository.BicycleRepo;

import java.math.BigDecimal;
import java.util.List;
//Pomocnicza klasa służąca do inicjalizacji bazy danych
@Configuration
public class DbInit implements CommandLineRunner {
    private final AdminAccountRepo adminAccountRepo;
    private final BicycleRepo bicycleRepo;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public DbInit(AdminAccountRepo adminAccountRepo, BicycleRepo bicycleRepo, PasswordEncoder passwordEncoder) {
        this.adminAccountRepo = adminAccountRepo;
        this.bicycleRepo = bicycleRepo;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void run(String... args) throws Exception {
        this.bicycleRepo.saveAll(List.of(
                new Bicycle("Rower MTB TORPADO 7 Biegów 29 cali", "Górski", "https://a.allegroimg.com/s400/1153b2/ab751e6a4b16ad8ba29188dc2652/Rower-gorski-MTB-TORPADO-7-Biegow-29-cali-meski", 25),
                new Bicycle("Rower Dartmoor Primal Evo 29 2022 M", "Górski", "https://enduro-cross.pl/img/products/49/62/1.jpg", 50),
                new Bicycle("Rower Trek District+ 1 z niskim przekrokiem", "Elektryczny", "https://bondariew.pl/wp-content/uploads/2023/08/DistrictPlus1Midstep_21_33480_A_Primary-400x300.jpg", 70),
                new Bicycle("Rower Dartmoor Two6player Evo 26\"", "Górski", "https://enduro-cross.pl/img/products/49/33/38.jpg", 25)
        ));
        String encodedPassword = passwordEncoder.encode("password");
        this.adminAccountRepo.save(new AdminAccount("admin", encodedPassword));
    }
}
