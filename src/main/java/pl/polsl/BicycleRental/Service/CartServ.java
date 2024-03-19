package pl.polsl.BicycleRental.Service;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import pl.polsl.BicycleRental.Model.Cart;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServ {

    public Cart findBySessionID(HttpSession session, ArrayList<Cart> sessionCarts) {
        String sessionID = session.getId();
        if (sessionCarts.stream()
                .filter(cart ->
                        cart.getSessionID().equals(sessionID)
                ).collect(Collectors.toList()).isEmpty()
        ) {
            Cart newSessionCart = new Cart();
            newSessionCart.setSessionID(sessionID);
            session.setAttribute("cart", newSessionCart);
            sessionCarts.add(newSessionCart);
            return newSessionCart;
        }
        return (Cart) session.getAttribute("cart");
    }

    public void updateCart(HttpSession session, Cart cart, ArrayList<Cart> sessionCarts) {
        session.setAttribute("cart", cart);
        Cart sessionCart = sessionCarts.stream()
                .filter(c -> c.getSessionID().equals(session.getId()))
                .findFirst()
                .orElse(null);
        if(sessionCart != null) {
            sessionCart.setBeginRent(cart.getBeginRent());
            sessionCart.setEndRent(cart.getEndRent());
            sessionCart.setPrice(cart.getPrice());
            sessionCart.setBicyclesIDs(cart.getBicyclesIDs());
        }
    }
}
