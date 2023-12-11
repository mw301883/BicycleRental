package pl.polsl.BicycleRental.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    @Autowired
    CustomerViewCtrl(OpinionServ opinionServ, BicycleServ bicycleServ){

        this.opinionServ = opinionServ;
        this.bicycleServ = bicycleServ;
    }
    @GetMapping("/store")
    public String mainPage(Model model){
        model.addAttribute("bicycles", this.bicycleServ.findAll());
        return "CustomerView/index";
    }
    @GetMapping("/opinions")
    public String opinionsPage(){
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
    public String aboutUsPage(){
        return "CustomerView/aboutUs";
    }
    @GetMapping("/regulations")
    public String regulationsPage(){
        //TODO HTML do strony z regulaminem
        return "CustomerView/rules";
    }
    @GetMapping("/contact")
    public String MainPage(){
        //TODO HTML do strony z informacjami kontaktowymi
        return "CustomerView/contact";
    }
}
