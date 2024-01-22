package pl.polsl.BicycleRental.Model.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.polsl.BicycleRental.Model.ModelDB.Bicycle;
import pl.polsl.BicycleRental.Model.ModelDB.Order;
import pl.polsl.BicycleRental.Model.Repository.BicycleRepo;
import pl.polsl.BicycleRental.Model.Repository.OrderRepo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

//W klasach z adnotacją @Service piszemy całą logikę zarządzania DB na przykład operacje CRUD czy inne zarządanie danymi.
@Service
public class OrderServ {
    private final OrderRepo orderRepo;
    private final BicycleRepo bicycleRepo;

    @Autowired
    OrderServ(OrderRepo orderRepo, BicycleRepo bicycleRepo) {
        this.orderRepo = orderRepo;
        this.bicycleRepo = bicycleRepo;
    }

    public Order getOrderById(Long id){
        return this.orderRepo.findById(id).get();
    }

    public void makeOrder(Order order) {
        this.orderRepo.save(order);
    }

    public List<Order> findAll() {
        return this.orderRepo.findAll();
    }

    public ArrayList<Long> getOrderBicyclesById(Long id){
        return this.orderRepo.findById(id).get().getBicycles();
    }

    public void deleteOrder(Long id) {
        this.orderRepo.deleteById(id);
    }

    public void addPenaltyById(Long id) {
        Optional<Order> order = this.orderRepo.findById(id);
        order.get().addPenalty();
        this.orderRepo.save(order.get());
    }

    public void cancelPenaltyById(Long id) {
        Optional<Order> order = this.orderRepo.findById(id);
        order.get().cancelPenalty();
        this.orderRepo.save(order.get());
    }

    public void updateOrdersPnalty() {
        Calendar now = Calendar.getInstance();
        List<Order> orderList = this.orderRepo.findAll();
        for (Order order : orderList) {
            if (now.compareTo(order.getEndRent()) > 0) {
                long millisecondsDifference = now.getTimeInMillis() - order.getEndRent().getTimeInMillis();
                long daysDifference = millisecondsDifference / (24 * 60 * 60 * 1000);
                long currentPenaltyPrice = order.getPenaltyPrice().longValue();
                long newPenalyuPrice = 15 * daysDifference;
                if (currentPenaltyPrice != newPenalyuPrice) {
                    order.setPenaltyPrice(
                            order
                                    .getPenaltyPrice()
                                    .add(BigDecimal.valueOf(15 * daysDifference))
                    );
                }
            }
        }

        this.orderRepo.saveAll(orderList);
    }

    public void releaseOrderBicycles(ArrayList<Long> bycyclesIds) {
        for (Long id : bycyclesIds) {
            Bicycle bicycle = this.bicycleRepo.getById(id);
            bicycle.setRentStartDate(null);
            bicycle.setRentEndDate(null);
            this.bicycleRepo.save(bicycle);
        }
    }

    public void finalizeOrderById(Long id){
        Order order = this.orderRepo.findById(id).get();
        order.setIsFinalized(true);
        this.orderRepo.save(order);
    }

}
