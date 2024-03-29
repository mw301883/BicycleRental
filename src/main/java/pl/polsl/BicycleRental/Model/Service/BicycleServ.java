package pl.polsl.BicycleRental.Model.Service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.polsl.BicycleRental.Model.Cart;
import pl.polsl.BicycleRental.Model.ModelDB.Bicycle;
import pl.polsl.BicycleRental.Model.Repository.BicycleRepo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//W klasach z adnotacją @Service piszemy całą logikę zarządzania DB na przykład operacje CRUD czy inne zarządanie danymi.
@Service
public class BicycleServ {
    private final BicycleRepo bicycleRepo;
    @Getter
    private List<Long> cartBicycleIdList = new ArrayList<>();
    @Autowired
    BicycleServ(BicycleRepo bicycleRepo){
        this.bicycleRepo = bicycleRepo;
    }

    public void addToCartBicycleIdList(Long value) {
        cartBicycleIdList.add(value);
    }
    public void removeFromCartBicycleIdList(Long value) {
        cartBicycleIdList.remove(value);
    }

    public void clearCartBicycleIdList() {
        cartBicycleIdList.clear();
    }
    public List<Bicycle> findAll(){
        return this.bicycleRepo.findAll()
                .stream()
                .filter(bicycle -> !bicycle.isRented())
                .collect(Collectors.toList());
    }
    public List<Bicycle> findAll(Cart cart){
        return this.bicycleRepo.findAll()
                .stream()
                .filter(bicycle -> !bicycle.isRented() && !cart.getBicyclesIDs().contains(bicycle.getId()))
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
    public void addBicycle(Bicycle bicycle){
        this.bicycleRepo.save(bicycle);
    }
    public void updateBicycle(Long id, double pricePerDay, Boolean disable){
        Optional<Bicycle> optionalBicycle = this.bicycleRepo.findById(id);

        if (optionalBicycle.isPresent()) {
            Bicycle bicycle = optionalBicycle.get();
            bicycle.setPricePerDay(BigDecimal.valueOf(pricePerDay));
            bicycle.setDisable(disable);

            this.bicycleRepo.save(bicycle);
        }
    }
    public void deleteBicycle(Long id){
        this.bicycleRepo.deleteById(id);
    }
    public void setDisableBicycle(Long bicycleId) {
        Optional<Bicycle> optionalBicycle = bicycleRepo.findById(bicycleId);

        if (optionalBicycle.isPresent()) {
            Bicycle bicycle = optionalBicycle.get();
            bicycle.setDisable(true);
            bicycleRepo.save(bicycle);
        } else {
            // Obsługa błędu, np. rzucenie wyjątku lub dodanie logiki obsługi błędu
        }
    }

}
