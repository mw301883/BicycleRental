package pl.polsl.BicycleRental.Model.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.BicycleRental.Model.ModelDB.Cart;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {

    Cart findBySessionID(String sessionID);
}
