package pl.polsl.BicycleRental.Service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import pl.polsl.BicycleRental.Model.Cart;

@Service
public class CartServ {

    public Cart findBySessionID(HttpSession session, String currentSessionID) {
        String sessionID = session.getId();
        if (sessionID != currentSessionID) {
            Cart newSessionCart = new Cart();
            newSessionCart.setSessionID(sessionID);
            session.setAttribute("cart", newSessionCart);
            return newSessionCart;
        }
        return (Cart) session.getAttribute("cart");
    }

    public void updateCart(HttpSession session, Cart cart) {
        session.setAttribute("cart", cart);
    }
}
