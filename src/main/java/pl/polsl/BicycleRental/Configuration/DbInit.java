package pl.polsl.BicycleRental.Configuration;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.polsl.BicycleRental.Model.ModelDB.AdminAccount;
import pl.polsl.BicycleRental.Model.ModelDB.Bicycle;
import pl.polsl.BicycleRental.Model.ModelDB.Opinion;
import pl.polsl.BicycleRental.Model.ModelDB.Order;
import pl.polsl.BicycleRental.Model.Repository.AdminAccountRepo;
import pl.polsl.BicycleRental.Model.Repository.BicycleRepo;
import pl.polsl.BicycleRental.Model.Repository.OpinionRepo;
import pl.polsl.BicycleRental.Model.Repository.OrderRepo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
//Pomocnicza klasa służąca do inicjalizacji bazy danych
@Configuration
public class DbInit implements CommandLineRunner {
    private final AdminAccountRepo adminAccountRepo;
    private final BicycleRepo bicycleRepo;
    private  final OpinionRepo opinionRepo;
    private  final OrderRepo orderRepo;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public DbInit(AdminAccountRepo adminAccountRepo, BicycleRepo bicycleRepo, OpinionRepo opinionRepo, OrderRepo orderRepo,
                  PasswordEncoder passwordEncoder) {
        this.adminAccountRepo = adminAccountRepo;
        this.bicycleRepo = bicycleRepo;
        this.opinionRepo = opinionRepo;
        this.orderRepo = orderRepo;
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
        this.opinionRepo.saveAll(List.of(
                new Opinion("opinia", "test@test.com", "sprawa", "Treść opinii."),
                new Opinion("opinia", "test@test.com", "sprawa", "Treść opinii.")
        ));
        Calendar beginOne = Calendar.getInstance();
        beginOne.set(2024, Calendar.JANUARY, 8, 14, 30, 0);
        Calendar endOne = Calendar.getInstance();
        endOne.set(2024, Calendar.JANUARY, 10, 14, 30, 0);
        Calendar beginTwo = Calendar.getInstance();
        beginTwo.set(2024, Calendar.JANUARY, 8, 14, 30, 0);
        Calendar endTwo = Calendar.getInstance();
        //endTwo.set(2024, Calendar.JANUARY, 12, 14, 30, 0);
        ArrayList<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        list.add(3L);
        list.add(4L);
        this.orderRepo.saveAll(List.of(
                new Order(list, beginOne, endOne, "Adrian", "Dudu", "ul. Drogowa 3", "Warszawa",
                        "50-200", "dudu@test.com", "500400355", new BigDecimal(170)),
                new Order(list, beginTwo, endTwo, "Jan", "Nowak", "ul. Drogowa 8", "Poznań",
                        "50-200", "dudu@test.com", "500400355", new BigDecimal(200))
        ));
        String encodedPassword = passwordEncoder.encode("password");
        this.adminAccountRepo.save(new AdminAccount("admin", encodedPassword));
    }
}
