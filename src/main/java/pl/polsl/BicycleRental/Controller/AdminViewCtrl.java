package pl.polsl.BicycleRental.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.polsl.BicycleRental.Model.ModelDB.Bicycle;
import pl.polsl.BicycleRental.Model.Service.BicycleServ;
import pl.polsl.BicycleRental.Model.Service.OpinionServ;
import pl.polsl.BicycleRental.Model.Service.OrderServ;

import java.math.BigDecimal;
import java.util.Map;

//W kontrolerach wyświetlamy strony HTML, metody z atnotacją @GetMapping po prostu wyświetlają stronę a z kolei
//metody z adnotacjami @PostMapping pobierają dane z formularzy czyli z <form></form>, backend będziemy łączyli poprzez Thymleaf
//pliki HTML powinny się znaleźć w folderze "templates/AdminView" - zrobiłem tam odpowiednie foldery, pliki CSS powinny się znaleźć w folderze static.CSS
//linkowanie CSS : <link rel="stylesheet" href="CSS\nazwa_pliku.css"/>
@Controller
@RequestMapping("/admin")
public class AdminViewCtrl {
    BicycleServ bicycleServ;
    OpinionServ opinionServ;
    OrderServ orderServ;
    @Autowired
    AdminViewCtrl(BicycleServ bicycleServ, OpinionServ opinionServ, OrderServ orderServ){
        this.bicycleServ = bicycleServ;
        this.opinionServ = opinionServ;
        this.orderServ = orderServ;
    }
    //TODO
    @GetMapping
    public String bicyclesPanelPage(Model model){
        model.addAttribute("bicycles", bicycleServ.findAll());
        return "AdminView/bicyclesPanel";
    }
    @GetMapping("/addBicycleForm")
    public String addBicyclePanelPage(){
        return "AdminView/addBicyclePanel";
    }
    @PostMapping("/addBicycle")
    public String addBicycle(@RequestParam String name, @RequestParam String type, @RequestParam String photoURL,
                             @RequestParam String pricePerDay){
        this.bicycleServ.addBicycle(new Bicycle(name, type, photoURL, Double.parseDouble(pricePerDay)));
        return "redirect:/admin";
    }
    //TODO Michał - dokończyć funkcjonalność guzików save i delete
    @PostMapping("/modifyBicycle")
    public String modifyBicycle(@RequestParam Long bicycleId, @RequestParam Map<String, String> formData) {
        if ("save".equals(formData.get("save"))) {
            this.bicycleServ.updateBicycle(bicycleId, Double.parseDouble(formData.get("pricePerDay")), Boolean.parseBoolean(formData.get("disable")));
        } else if ("delete".equals(formData.get("delete"))) {
            this.bicycleServ.deleteBicycle(bicycleId);
        }
        return "redirect:/admin";
    }

    @GetMapping("/opinions")
    public String opinionsPanelPage(Model model){
        model.addAttribute("opinions", opinionServ.findAll());
        return "AdminView/opinionsPanel";
    }
    @GetMapping("/orders")
    public String ordersPanelPage(Model model){
        model.addAttribute("orders", orderServ.findAll());
        return "AdminView/ordersPanel";
    }
}
