package pl.polsl.BicycleRental.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.polsl.BicycleRental.Model.Cart;
import pl.polsl.BicycleRental.Model.ModelDB.Bicycle;
import pl.polsl.BicycleRental.Model.ModelDB.Opinion;
import pl.polsl.BicycleRental.Model.Service.BicycleServ;
import pl.polsl.BicycleRental.Model.Service.OpinionServ;
//W kontrolerach wyświetlamy strony HTML, metody z atnotacją @GetMapping po prostu wyświetlają stronę a z kolei
//metody z adnotacjami @PostMapping pobierają dane z formularzy czyli z <form></form>, backend będziemy łączyli poprzez Thymleaf
//pliki HTML powinny się znaleźć w folderze "templates/CustomerView" - zrobiłem tam odpowiednie foldery, pliki CSS powinny się znaleźć w folderze static.CSS
//linkowanie CSS : <link rel="stylesheet" href="CSS\nazwa_pliku.css"/>
@Controller
@RequestMapping("/")
public class CustomerViewCtrl {
    private final OpinionServ opinionServ;
    private final BicycleServ bicycleServ;
    private Cart sessionCart;
    @Autowired
    CustomerViewCtrl(OpinionServ opinionServ, BicycleServ bicycleServ){
        this.opinionServ = opinionServ;
        this.bicycleServ = bicycleServ;
        this.sessionCart = new Cart();
        sessionCart.addBicycleToCart(new Bicycle("Rower MTB TORPADO 7 Biegów 29 cali", "Górski", "https://a.allegroimg.com/s400/1153b2/ab751e6a4b16ad8ba29188dc2652/Rower-gorski-MTB-TORPADO-7-Biegow-29-cali-meski", 25));
        sessionCart.addBicycleToCart(new Bicycle("Rower MTB TORPADO 7 Biegów 29 cali", "Górski", "https://a.allegroimg.com/s400/1153b2/ab751e6a4b16ad8ba29188dc2652/Rower-gorski-MTB-TORPADO-7-Biegow-29-cali-meski", 25));
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
    public String uploadOpinion(@RequestParam String name, @RequestParam String email, @RequestParam String subject, @RequestParam String message){
        //TODO wysłać alert o błędzie
        if(name.isEmpty() || email.isEmpty() || subject.isEmpty() || message.isEmpty())
            return "redirect:/opinions";
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
        model.addAttribute("bicyclesInCart", this.sessionCart.getBicyclesInCart());
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
}
