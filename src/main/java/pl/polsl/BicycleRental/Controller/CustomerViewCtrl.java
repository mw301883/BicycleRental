package pl.polsl.BicycleRental.Controller;

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
import pl.polsl.BicycleRental.Model.Service.BicycleServ;
import pl.polsl.BicycleRental.Model.Service.OpinionServ;
import pl.polsl.BicycleRental.Model.Service.OrderServ;

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
    private Cart sessionCart;

    @Autowired
    CustomerViewCtrl(OpinionServ opinionServ, BicycleServ bicycleServ, OrderServ orderServ) {
        this.opinionServ = opinionServ;
        this.bicycleServ = bicycleServ;
        this.orderServ = orderServ;
        this.sessionCart = new Cart();
    }

    @GetMapping("/store")
    public String mainPage(Model model) {
        Calendar start = this.sessionCart.getBeginRent();
        Calendar end = this.sessionCart.getEndRent();
        List<Bicycle> allBicycles = this.bicycleServ.findAll(this.sessionCart);
        List<Bicycle> availableBicycles = allBicycles.stream()
                .filter(b -> !b.isDisable() && !b.isDateRangeOverlap(start, end)) //b.getRentStartDate() == null && b.getRentEndDate() == null
                .sorted(Comparator.comparing(Bicycle::getId))
                .collect(Collectors.toList());
        updateBicyclesInCartGlobalDisplay();
        model.addAttribute("bicycles", availableBicycles);
        model.addAttribute("cartSize", this.sessionCart.getCartSize());
        return "CustomerView/index";
    }

    @GetMapping("/opinions")
    public String opinionsPage(Model model) {
        model.addAttribute("cartSize", this.sessionCart.getCartSize());
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
    public String aboutUsPage(Model model) {
        model.addAttribute("cartSize", this.sessionCart.getCartSize());
        return "CustomerView/aboutUs";
    }

    @GetMapping("/regulations")
    public String regulationsPage(Model model) {
        model.addAttribute("cartSize", this.sessionCart.getCartSize());
        return "CustomerView/rules";
    }

    @GetMapping("/contact")
    public String contactPage(Model model) {
        model.addAttribute("cartSize", this.sessionCart.getCartSize());
        return "CustomerView/contact";
    }

    @GetMapping("/cart")
    public String cartPage(Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("bicyclesInCart", this.sessionCart.getBicyclesInCart());
        model.addAttribute("cartSize", this.sessionCart.getCartSize());
        model.addAttribute("beginDate", this.sessionCart.getBeginRent());
        model.addAttribute("endDate", this.sessionCart.getEndRent());
        if (this.sessionCart.getCartSize() > 4) {
            model.addAttribute("message", "Przysługuje ci zniżka 15% od całej wartości zamówienia!");
            model.addAttribute("info", "(Nowa cena ze zniżką)");
            model.addAttribute("price",
                    this.sessionCart.getPrice().subtract(this.sessionCart.getPrice().multiply(BigDecimal.valueOf(ConfigConstants.discountRate))));
        } else {
            model.addAttribute("price", this.sessionCart.getPrice());
        }
        return "CustomerView/cart";
    }

    @PostMapping("/removeFromCart")
    public String removeFromCart(@RequestParam int index) {
        this.sessionCart.removeBicycleFromCart(index);
        updateBicyclesInCartGlobalDisplay();
        return "redirect:/cart";
    }

    @GetMapping("/summary")
    public String summaryPage(Model model) {
        model.addAttribute("cartSize", this.sessionCart.getCartSize());
        return "CustomerView/summary";
    }

    @PostMapping("/makeOrder")
    public String makeOrder(@RequestParam String firstName, @RequestParam String lastName,
                            @RequestParam String address, @RequestParam String city, @RequestParam String postalCode,
                            @RequestParam String email, @RequestParam String phoneNum,
                            RedirectAttributes redirectAttributes) {
        if (this.sessionCart.getBicyclesIDs().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Nie można złożyć pustego zamówienia.");
            return "redirect:/store";
        }

        this.orderServ.makeOrder(new Order(this.sessionCart.getBicyclesIDs(), this.sessionCart.getBeginRent(),
                this.sessionCart.getEndRent(), firstName, lastName, address, city, postalCode,
                email, phoneNum, (this.sessionCart.getCartSize() > 4)
                ? this.sessionCart.getPrice()
                .subtract(this.sessionCart.getPrice().multiply(BigDecimal.valueOf(ConfigConstants.discountRate))) : this.sessionCart.getPrice()));

        for (Long id : this.sessionCart.getBicyclesIDs()) {
            this.bicycleServ.setRentalTimeBicycle(id, this.sessionCart.getBeginRent(), this.sessionCart.getEndRent());
        }

        this.bicycleServ.clearCartBicycleIdList();
        this.sessionCart.clearCart();
        redirectAttributes.addFlashAttribute("message", "Zamówienie zostało złożone. Dziękujemy :)");
        return "redirect:/store";
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam Long LongId, RedirectAttributes redirectAttributes) {
        Bicycle bicycle = this.bicycleServ.getBicycleById(LongId);
        Calendar start = this.sessionCart.getBeginRent();
        Calendar end = this.sessionCart.getEndRent();

        if (start != null && end != null) {
            if (!bicycle.isDateRangeOverlap(start, end)) {
                this.sessionCart.addBicycleToCart(bicycle);
                this.bicycleServ.addToCartBicycleIdList(LongId);
                redirectAttributes.addFlashAttribute("message", "Rower został dodany do zamówienia.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Nie można dodać roweru do koszyka. Brak daty wynajmu.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Wypełnij wszystkie pola dotyczące daty wynajmu.");
        }
        updateBicyclesInCartGlobalDisplay();
        return "redirect:/store";
    }

    private void updateBicyclesInCartGlobalDisplay() {
        ArrayList<Long> bicycleIDs = new ArrayList<>();
        BigDecimal price = new BigDecimal(0);
        long differenceDays = 0;
        if (this.sessionCart.getBeginRent() != null && this.sessionCart.getEndRent() != null) {
            long differenceMillis = this.sessionCart.getEndRent().getTimeInMillis() - this.sessionCart.getBeginRent().getTimeInMillis();
            differenceDays = differenceMillis / (24 * 60 * 60 * 1000);
        }
        for (Bicycle bicycle : this.sessionCart.getBicyclesInCart()) {
            price = price.add(bicycle.getPricePerDay());
            if (differenceDays != 0) {
                this.sessionCart.setPrice(price.multiply(new BigDecimal(differenceDays)));
            } else {
                this.sessionCart.setPrice(price);
            }
            bicycleIDs.add(bicycle.getId());
        }
        this.sessionCart.setBicyclesIDs(bicycleIDs);
    }

    @PostMapping("setRentalDate")
    public String setRentalDate(@RequestParam("beginDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beginDate,
                                @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                RedirectAttributes redirectAttributes) {
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
        this.sessionCart.setBeginRent(beginCalendar);
        this.sessionCart.setEndRent(endCalendar);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedBeginDate = dateFormat.format(beginCalendar.getTime());
        String formattedEndDate = dateFormat.format(endCalendar.getTime());
        redirectAttributes.addFlashAttribute("message", "Ustawiono datę wypożyczenia od "
                + formattedBeginDate + " do " + formattedEndDate);
        return "redirect:/store";
    }
}
