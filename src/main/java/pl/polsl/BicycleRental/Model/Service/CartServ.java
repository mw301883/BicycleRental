package pl.polsl.BicycleRental.Model.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.polsl.BicycleRental.Model.ModelDB.Cart;
import pl.polsl.BicycleRental.Model.Repository.CartRepo;

@Service
public class CartServ {

    private final CartRepo cartRepo;

    @Autowired
    CartServ(CartRepo cartRepo) {
        this.cartRepo = cartRepo;
    }

    public Cart findBySessionID(String sessionID) {
        Cart cart = this.cartRepo.findBySessionID(sessionID);
        if(cart == null) {
            Cart newSessionCart = new Cart();
            newSessionCart.setSessionID(sessionID);
            return this.cartRepo.save(newSessionCart);
        }
        return cart;
    }

    public void updateCart(Cart cart) {
        this.cartRepo.save(cart);
    }
}
