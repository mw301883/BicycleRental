package pl.polsl.BicycleRental.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//W kontrolerach wyświetlamy strony HTML, metody z atnotacją @GetMapping po prostu wyświetlają stronę a z kolei
//metody z adnotacjami @PostMapping pobierają dane z formularzy czyli z <form></form>, backend będziemy łączyli poprzez Thymleaf
//pliki HTML powinny się znaleźć w folderze "templates/AdminView" - zrobiłem tam odpowiednie foldery, pliki CSS powinny się znaleźć w folderze static.CSS
//linkowanie CSS : <link rel="stylesheet" href="CSS\nazwa_pliku.css"/>
@Controller
@RequestMapping("/admin")
public class AdminViewCtrl {
    //TODO
    @GetMapping
    public String bicyclesPanelPage(){
        return "AdminView/bicyclesPanel";
    }
    @GetMapping("/orders")
    public String ordersPanelPage(){
        return "AdminView/ordersPanel";
    }
    @GetMapping("/opinions")
    public String opinionsPanelPage(){
        return "AdminView/opinionsPanel";
    }
}
