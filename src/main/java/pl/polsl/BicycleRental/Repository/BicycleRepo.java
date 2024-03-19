package pl.polsl.BicycleRental.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.BicycleRental.Model.ModelDB.Bicycle;

//Każda encja musi mieć swój własny interfejs rozszerzający JpaRepository
@Repository
public interface BicycleRepo extends JpaRepository<Bicycle, Long> {
}
