package pl.polsl.BicycleRental.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.polsl.BicycleRental.Model.Cart;
import pl.polsl.BicycleRental.Model.ModelDB.Bicycle;
import pl.polsl.BicycleRental.Model.ModelDB.Opinion;
import pl.polsl.BicycleRental.Model.ModelDB.Order;
import pl.polsl.BicycleRental.Model.Service.BicycleServ;
import pl.polsl.BicycleRental.Model.Service.OpinionServ;
import pl.polsl.BicycleRental.Model.Service.OrderServ;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;

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
    private Cart sessionCart;
    @Autowired
    CustomerViewCtrl(OpinionServ opinionServ, BicycleServ bicycleServ, OrderServ orderServ){
        this.opinionServ = opinionServ;
        this.bicycleServ = bicycleServ;
        this.orderServ = orderServ;
        this.sessionCart = new Cart();
    }
    @GetMapping("/store")
    public String mainPage(Model model){
        model.addAttribute("bicycles", this.bicycleServ.findAll());
        model.addAttribute("cartSize",this.sessionCart.getCartSize());
        return "CustomerView/index";
    }
    @GetMapping("/opinions")
    public String opinionsPage(Model model){
        model.addAttribute("cartSize",this.sessionCart.getCartSize());
        return "CustomerView/opinions";
    }
    @PostMapping("/uploadOpinion")
    public String uploadOpinion(@RequestParam String name, @RequestParam String email, @RequestParam String subject,
                                @RequestParam String message, RedirectAttributes redirectAttributes){
        if (name.isEmpty() || email.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Wszystkie pola są wymagane!");
            return "redirect:/opinions";
        }
        this.opinionServ.addOpinion(new Opinion(name, email, subject, message));
        return "redirect:/opinions";
    }
    @GetMapping("/aboutUs")
    public String aboutUsPage(Model model){
        model.addAttribute("cartSize",this.sessionCart.getCartSize());
        return "CustomerView/aboutUs";
    }
    @GetMapping("/regulations")
    public String regulationsPage(Model model){
        model.addAttribute("cartSize",this.sessionCart.getCartSize());
        return "CustomerView/rules";
    }
    @GetMapping("/contact")
    public String contactPage(Model model){
        model.addAttribute("cartSize",this.sessionCart.getCartSize());
        return "CustomerView/contact";
    }
    @GetMapping("/cart")
    public String cartPage(Model model){
        ArrayList<Long> bicycleIDs = new ArrayList<>();
        BigDecimal price = new BigDecimal(0);

        for(Bicycle bicycle : this.sessionCart.getBicyclesInCart()){
            bicycleIDs.add(bicycle.getId());
            //przemnożyć przez ilość dni wypożyczenia
            price.add(bicycle.getPricePerDay());
        }

        long differenceMillis = this.sessionCart.getBeginRent().getTimeInMillis() - this.sessionCart.getEndRent().getTimeInMillis();
        long differenceDays = differenceMillis / (24 * 60 * 60 * 1000);
        //TODO fix set price
        this.sessionCart.setPrice(price.multiply(new BigDecimal(differenceDays)));
        this.sessionCart.setBicyclesIDs(bicycleIDs);

        model.addAttribute("bicyclesInCart", this.sessionCart.getBicyclesInCart());
        model.addAttribute("price", this.sessionCart.getPrice());
        model.addAttribute("cartSize", this.sessionCart.getCartSize());
        return "CustomerView/cart";
    }
    @PostMapping("/removeFromCart")
    public String removeFromCart(@RequestParam int index){
        this.sessionCart.removeBicycleFromCart(index);
        return "redirect:/cart";
    }
    @GetMapping("/summary")
    public String summaryPage(){
        return "CustomerView/summary";
    }
    @PostMapping("/makeOrder")
    public String makeOrder(@RequestParam String firstName, @RequestParam String lastName,
                            @RequestParam String address, @RequestParam String city, @RequestParam String postalCode,
                            @RequestParam String email, @RequestParam String phoneNum, RedirectAttributes redirectAttributes){
        this.orderServ.makeOrder(new Order(this.sessionCart.getBicyclesIDs(), this.sessionCart.getBeginRent(),
                this.sessionCart.getEndRent(), firstName, lastName, address, city, postalCode,
                email, phoneNum, this.sessionCart.getPrice()));
        redirectAttributes.addFlashAttribute("message", "Zamówienie zostało złożone.");
        return "redirect:/store";
    }
    @PostMapping("/addToCart")
    public String addToCart(@RequestParam Long LongId, RedirectAttributes redirectAttributes){
        Bicycle bicycle = this.bicycleServ.getBicycleById(LongId);
        if (bicycle != null) {
            this.sessionCart.addBicycleToCart(bicycle);
            redirectAttributes.addFlashAttribute("message", "Bicycle added to the cart.");
        }
        return "redirect:/store";
    }
    @PostMapping("setRentalDate")
    public String seRentalDate(@RequestParam("beginDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beginDate,
                               @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTimeInMillis(
                Instant.from(beginDate.atStartOfDay(ZoneId.systemDefault())).getEpochSecond() * 1000
        );
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(
                Instant.from(endDate.atStartOfDay(ZoneId.systemDefault())).getEpochSecond() * 1000
        );
        this.sessionCart.setBeginRent(beginCalendar);
        this.sessionCart.setEndRent(endCalendar);
        return "redirect:/store";
    }
}
