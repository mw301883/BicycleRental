package pl.polsl.BicycleRental.Model.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.polsl.BicycleRental.Model.ModelDB.Order;
import pl.polsl.BicycleRental.Model.Repository.OrderRepo;

//W klasach z adnotacją @Service piszemy całą logikę zarządzania DB na przykład operacje CRUD czy inne zarządanie danymi.
@Service
public class OrderServ {
    private final OrderRepo orderRepo;
    @Autowired
    OrderServ(OrderRepo orderRepo){
        this.orderRepo = orderRepo;
    }
    public void makeOrder(Order order){
        this.orderRepo.save(order);
    }
}
