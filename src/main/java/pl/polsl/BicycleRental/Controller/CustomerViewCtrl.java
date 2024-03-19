package pl.polsl.BicycleRental.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.polsl.BicycleRental.Configuration.ConfigConstants;
import pl.polsl.BicycleRental.Model.Cart;
import pl.polsl.BicycleRental.Model.ModelDB.Bicycle;
import pl.polsl.BicycleRental.Model.ModelDB.Opinion;
import pl.polsl.BicycleRental.Model.ModelDB.Order;
import pl.polsl.BicycleRental.Service.BicycleServ;
import pl.polsl.BicycleRental.Service.CartServ;
import pl.polsl.BicycleRental.Service.OpinionServ;
import pl.polsl.BicycleRental.Service.OrderServ;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//W kontrolerach wyświetlamy strony HTML, metody z atnotacją @GetMapping po prostu wyświetlają stronę a z kolei
//metody z adnotacjami @PostMapping pobierają dane z formularzy czyli z <form></form>, backend będziemy łączyli poprzez Thymleaf
//pliki HTML powinny się znaleźć w folderze "templates/CustomerView" - zrobiłem tam odpowiednie foldery, pliki CSS powinny się znaleźć w folderze static.CSS
//linkowanie CSS : <link rel="stylesheet" href="CSS\nazwa_pliku.css"/>
@Controller
@RequestMapping("/")
public class CustomerViewCtrl {
    private final OpinionServ opinionServ;
    private final BicycleServ bicycleServ;
    private final OrderServ orderServ;
    private final CartServ cartServ;
    private ArrayList<Cart> sessionCarts = new ArrayList<>();

    @Autowired
    CustomerViewCtrl(OpinionServ opinionServ, BicycleServ bicycleServ, OrderServ orderServ, CartServ cartServ) {
        this.opinionServ = opinionServ;
        this.bicycleServ = bicycleServ;
        this.orderServ = orderServ;
        this.cartServ = cartServ;
    }

    @GetMapping("/store")
    public String mainPage(Model model, HttpSession session) {
        Cart curreentSessionCart = cartServ.findBySessionID(session, this.sessionCarts);
        Calendar start = curreentSessionCart.getBeginRent();
        Calendar end = curreentSessionCart.getEndRent();
        List<Bicycle> allBicycles = this.bicycleServ.findAll(curreentSessionCart);
        List<Bicycle> availableBicycles = allBicycles.stream()
                .filter(b -> !b.isDisable() && !b.isDateRangeOverlap(start, end)) //b.getRentStartDate() == null && b.getRentEndDate() == null
                .sorted(Comparator.comparing(Bicycle::getId))
                .collect(Collectors.toList());
        updateBicyclesInCartGlobalDisplay(curreentSessionCart);
        model.addAttribute("bicycles", availableBicycles);
        model.addAttribute("cartSize", curreentSessionCart.getCartSize());
        return "CustomerView/index";
    }

    @GetMapping("/opinions")
    public String opinionsPage(Model model, HttpSession session) {
        Cart currentSessionCart = cartServ.findBySessionID(session, this.sessionCarts);
        model.addAttribute("cartSize", currentSessionCart.getCartSize());
        return "CustomerView/opinions";
    }

    @PostMapping("/uploadOpinion")
    public String uploadOpinion(@RequestParam String name, @RequestParam String email, @RequestParam String subject,
                                @RequestParam String message, RedirectAttributes redirectAttributes) {
        if (name.isEmpty() || email.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Wszystkie pola są wymagane!");
            return "redirect:/opinions";
        }
        redirectAttributes.addFlashAttribute("message", "Opinia została wysłana.");
        this.opinionServ.addOpinion(new Opinion(name, email, subject, message));
        return "redirect:/opinions";
    }

    @GetMapping("/aboutUs")
    public String aboutUsPage(Model model, HttpSession session) {
        Cart currentSessionCart = cartServ.findBySessionID(session, this.sessionCarts);
        model.addAttribute("cartSize", currentSessionCart.getCartSize());
        return "CustomerView/aboutUs";
    }

    @GetMapping("/regulations")
    public String regulationsPage(Model model, HttpSession session) {
        Cart currentSessionCart = cartServ.findBySessionID(session, this.sessionCarts);
        model.addAttribute("cartSize", currentSessionCart.getCartSize());
        return "CustomerView/rules";
    }

    @GetMapping("/contact")
    public String contactPage(Model model, HttpSession session) {
        Cart currentSessionCart = cartServ.findBySessionID(session, this.sessionCarts);
        model.addAttribute("cartSize", currentSessionCart.getCartSize());
        return "CustomerView/contact";
    }

    @GetMapping("/cart")
    public String cartPage(Model model, RedirectAttributes redirectAttributes, HttpSession session) {
        Cart currentSessionCart = cartServ.findBySessionID(session, this.sessionCarts);
        ArrayList<Bicycle> bicyclesInCart = new ArrayList<>();
        for (Long id : currentSessionCart.getBicyclesIDs()) {
            bicyclesInCart.add(bicycleServ.getBicycleById(id));
        }
        model.addAttribute("bicyclesInCart", bicyclesInCart);
        model.addAttribute("cartSize", currentSessionCart.getCartSize());
        model.addAttribute("beginDate", currentSessionCart.getBeginRent());
        model.addAttribute("endDate", currentSessionCart.getEndRent());
        if (currentSessionCart.getCartSize() > 4) {
            model.addAttribute("message", "Przysługuje ci zniżka 15% od całej wartości zamówienia!");
            model.addAttribute("info", "(Nowa cena ze zniżką)");
            model.addAttribute("price",
                    currentSessionCart.getPrice().subtract(currentSessionCart.getPrice().multiply(BigDecimal.valueOf(ConfigConstants.discountRate))));
        } else {
            model.addAttribute("price", currentSessionCart.getPrice());
        }
        return "CustomerView/cart";
    }

    @PostMapping("/removeFromCart")
    public String removeFromCart(@RequestParam int index, HttpSession session) {
        Cart currentSessionCart = cartServ.findBySessionID(session, this.sessionCarts);
        BigDecimal pricePerDay = bicycleServ.getBicycleById(currentSessionCart.getBicyclesIDs().get(index)).getPricePerDay();
        currentSessionCart.removeBicycleFromCart(index, pricePerDay);
        updateBicyclesInCartGlobalDisplay(currentSessionCart);
        cartServ.updateCart(session, currentSessionCart, this.sessionCarts);
        return "redirect:/cart";
    }

    @GetMapping("/summary")
    public String summaryPage(Model model, HttpSession session) {
        Cart currentSessionCart = cartServ.findBySessionID(session, this.sessionCarts);
        model.addAttribute("cartSize", currentSessionCart.getCartSize());
        return "CustomerView/summary";
    }

    //TODO sprawdzić przed dodaniem do koszyka czy rower jest dostępny (synchronizacja) - Piter (potem zmodyfikować część kodu sprawdzającą dostępność roweru)
    @PostMapping("/makeOrder")
    public String makeOrder(@RequestParam String firstName, @RequestParam String lastName,
                            @RequestParam String address, @RequestParam String city, @RequestParam String postalCode,
                            @RequestParam String email, @RequestParam String phoneNum,
                            RedirectAttributes redirectAttributes,
                            HttpSession session) {
        Cart currentSessionCart = cartServ.findBySessionID(session, this.sessionCarts);
        if (currentSessionCart.getBicyclesIDs().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Nie można złożyć pustego zamówienia.");
            return "redirect:/store";
        }
        if (currentSessionCart.getBicyclesIDs()
                .stream()
                .anyMatch(id ->
                        this.bicycleServ.getBicycleById(id).getRentStartDate() != null)) {
            redirectAttributes.addFlashAttribute("error", "Przepraszamy, niektóre z rowerów dodane do koszyka zostały wynajęte chwilę temu" +
                    " i nie są już dostępne w żądanym terminie, proszę wybrać inne rowery.");
            currentSessionCart.clearCart();
            cartServ.updateCart(session, currentSessionCart, this.sessionCarts);
            return "redirect:/store";
        }
        this.orderServ.makeOrder(new Order(new ArrayList<>(currentSessionCart.getBicyclesIDs()), currentSessionCart.getBeginRent(),
                currentSessionCart.getEndRent(), firstName, lastName, address, city, postalCode,
                email, phoneNum, (currentSessionCart.getCartSize() > 4)
                ? currentSessionCart.getPrice()
                .subtract(currentSessionCart.getPrice().multiply(BigDecimal.valueOf(ConfigConstants.discountRate))) : currentSessionCart.getPrice()));
        for (Long id : currentSessionCart.getBicyclesIDs()) {
            this.bicycleServ.setRentalTimeBicycle(id, currentSessionCart.getBeginRent(), currentSessionCart.getEndRent());
        }
        currentSessionCart.clearCart();
        cartServ.updateCart(session, currentSessionCart, this.sessionCarts);
        redirectAttributes.addFlashAttribute("message", "Zamówienie zostało złożone. Dziękujemy :)");
        return "redirect:/store";
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam Long LongId, RedirectAttributes redirectAttributes, HttpSession session) {
        Cart currentSessionCart = cartServ.findBySessionID(session, this.sessionCarts);
        Bicycle bicycle = this.bicycleServ.getBicycleById(LongId);
        Calendar start = currentSessionCart.getBeginRent();
        Calendar end = currentSessionCart.getEndRent();
        if (start != null && end != null) {
            if (!bicycle.isDateRangeOverlap(start, end)) {
                currentSessionCart.addBicycleToCart(bicycle.getId());
                redirectAttributes.addFlashAttribute("message", "Rower został dodany do zamówienia.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Nie można dodać roweru do koszyka. Brak daty wynajmu.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Wypełnij wszystkie pola dotyczące daty wynajmu.");
        }
        updateBicyclesInCartGlobalDisplay(currentSessionCart);
        cartServ.updateCart(session, currentSessionCart, sessionCarts);
        return "redirect:/store";
    }

    private void updateBicyclesInCartGlobalDisplay(Cart currentSessionCart) {
        ArrayList<Long> bicycleIDs = new ArrayList<>();
        BigDecimal price = new BigDecimal(0);
        long differenceDays = 0;
        if (currentSessionCart.getBeginRent() != null && currentSessionCart.getEndRent() != null) {
            long differenceMillis = currentSessionCart.getEndRent().getTimeInMillis() - currentSessionCart.getBeginRent().getTimeInMillis();
            differenceDays = differenceMillis / (24 * 60 * 60 * 1000);
        }
        ArrayList<Bicycle> bicyclesInCart = new ArrayList<>();
        for (Long id : currentSessionCart.getBicyclesIDs()) {
            bicyclesInCart.add(bicycleServ.getBicycleById(id));
        }
        if (bicyclesInCart == null) {
            return;
        }
        for (Bicycle bicycle : bicyclesInCart) {
            price = price.add(bicycle.getPricePerDay());
            if (differenceDays != 0) {
                currentSessionCart.setPrice(price.multiply(new BigDecimal(differenceDays)));
            } else {
                currentSessionCart.setPrice(price);
            }
            bicycleIDs.add(bicycle.getId());
        }
        currentSessionCart.setBicyclesIDs(bicycleIDs);
    }

    @PostMapping("setRentalDate")
    public String setRentalDate(@RequestParam("beginDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beginDate,
                                @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {
        Cart currentSessionCart = cartServ.findBySessionID(session, this.sessionCarts);
        LocalDate currentDate = LocalDate.now();
        if (beginDate.isBefore(currentDate) || endDate.isBefore(currentDate)) {
            redirectAttributes.addFlashAttribute("error", "Nie można wybrać daty z przeszłości.");
            return "redirect:/store";
        }
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTimeInMillis(
                Instant.from(beginDate.atStartOfDay(ZoneId.systemDefault())).getEpochSecond() * 1000
        );
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(
                Instant.from(endDate.atStartOfDay(ZoneId.systemDefault())).getEpochSecond() * 1000
        );
        currentSessionCart.setBeginRent(beginCalendar);
        currentSessionCart.setEndRent(endCalendar);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedBeginDate = dateFormat.format(beginCalendar.getTime());
        String formattedEndDate = dateFormat.format(endCalendar.getTime());
        redirectAttributes.addFlashAttribute("message", "Ustawiono datę wypożyczenia od "
                + formattedBeginDate + " do " + formattedEndDate);
        cartServ.updateCart(session, currentSessionCart, this.sessionCarts);
        return "redirect:/store";
    }

    //Endpoint informacyjny ile aktualnie istnieje koszyków w aplikacji
    @GetMapping("/carts")
    public String carts(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", this.sessionCarts.toString());
        return "redirect:/store";
    }
}
