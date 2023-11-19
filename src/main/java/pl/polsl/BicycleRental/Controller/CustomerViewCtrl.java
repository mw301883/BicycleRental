package pl.polsl.BicycleRental.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.polsl.BicycleRental.Model.ModelDB.Opinion;
import pl.polsl.BicycleRental.Model.Service.OpinionServ;
//W kontrolerach wyświetlamy strony HTML, metody z atnotacją @GetMapping po prostu wyświetlają stronę a z kolei
//metody z adnotacjami @PostMapping pobierają dane z formularzy czyli z <form></form>, backend będziemy łączyli poprzez Thymleaf
//pliki HTML powinny się znaleźć w folderze "templates/CustomerView" - zrobiłem tam odpowiednie foldery, pliki CSS powinny się znaleźć w folderze static.CSS
//linkowanie CSS : <link rel="stylesheet" href="CSS\nazwa_pliku.css"/>
@Controller
@RequestMapping("/")
public class CustomerViewCtrl {
    private final OpinionServ opinionServ;
    @Autowired
    CustomerViewCtrl(OpinionServ opinionServ){
        this.opinionServ = opinionServ;
    }
    @GetMapping("/store")
    public String mainPage(){
        //TODO HTML do strony głównej
        return "";
    }
    @GetMapping("/opinions")
    public String opinionsPage(){
        return "CustomerView/OpinionsView";
    }
    @PostMapping("/uploadOpinion")
    public void uploadOpinion(@RequestBody Opinion newOpinion){
        this.opinionServ.addOpinion(newOpinion);
    }
    @GetMapping("/regulations")
    public String regulationsPage(){
        //TODO HTML do strony z regulaminem
        return "";
    }
    @GetMapping("/contact")
    public String MainPage(){
        //TODO HTML do strony z informacjami kontaktowymi
        return "";
    }
}
