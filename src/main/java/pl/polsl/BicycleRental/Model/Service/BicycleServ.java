package pl.polsl.BicycleRental.Model.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.polsl.BicycleRental.Model.Cart;
import pl.polsl.BicycleRental.Model.ModelDB.Bicycle;
import pl.polsl.BicycleRental.Model.Repository.BicycleRepo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//W klasach z adnotacją @Service piszemy całą logikę zarządzania DB na przykład operacje CRUD czy inne zarządanie danymi.
@Service
public class BicycleServ {
    private final BicycleRepo bicycleRepo;
    @Autowired
    BicycleServ(BicycleRepo bicycleRepo){
        this.bicycleRepo = bicycleRepo;
    }
    public List<Bicycle> findAll(Cart cart){
//        List<Bicycle> bicycles = this.bicycleRepo.findAll()
//                .stream()
//                .filter(bicycle -> !bicycle.isRented())
//                .filter(bicycle -> !cart.getBicyclesInCart().contains(bicycle))
//                .collect(Collectors.toList());
//        List<Bicycle> bicycles = this.bicycleRepo.findAll();
//        List<Long> ids = new ArrayList<>();
//        for(Bicycle bicycle : bicycles){
//            if(cart.getBicyclesInCart().contains(bicycle)){
//                ids.add(bicycle.getId());
//            }
//        }
//        for(Long id : ids){
//            bicycles.remove(id);
//        }
        return this.bicycleRepo.findAll()
                .stream()
                .filter(bicycle -> !bicycle.isRented())
                .collect(Collectors.toList());
    }
    public Bicycle getBicycleById(Long id) {
        Optional<Bicycle> bicycleOptional = this.bicycleRepo.findById(id);
        return bicycleOptional.orElse(null);
    }
    public void setRentalTimeBicycle(Long id, Calendar beginDate, Calendar endDate){
        Optional<Bicycle> optionalBicycle = this.bicycleRepo.findById(id);
        if (optionalBicycle.isPresent()) {
            Bicycle bicycle = optionalBicycle.get();
            bicycle.setRentStartDate(beginDate);
            bicycle.setRentEndDate(endDate);
            this.bicycleRepo.save(bicycle);
        }
    }
}
