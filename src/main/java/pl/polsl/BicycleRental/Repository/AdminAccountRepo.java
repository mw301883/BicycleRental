package pl.polsl.BicycleRental.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.BicycleRental.Model.ModelDB.AdminAccount;

@Repository
public interface AdminAccountRepo extends JpaRepository<AdminAccount, Long> {
}
