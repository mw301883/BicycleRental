package pl.polsl.BicycleRental.Model.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.BicycleRental.Model.ModelDB.Opinion;
//Każda encja musi mieć swój własny interfejs rozszerzający JpaRepository
@Repository
public interface OpinionRepo extends JpaRepository<Opinion, Long> {
}
