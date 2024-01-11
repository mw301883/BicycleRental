package pl.polsl.BicycleRental.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    AdminViewCtrl(BicycleServ bicycleServ, OpinionServ opinionServ, OrderServ orderServ) {
        this.bicycleServ = bicycleServ;
        this.opinionServ = opinionServ;
        this.orderServ = orderServ;
    }

    //TODO
    @GetMapping
    public String bicyclesPanelPage(Model model) {
        model.addAttribute("bicycles", bicycleServ.findAll());
        return "AdminView/bicyclesPanel";
    }

    @GetMapping("/addBicycleForm")
    public String addBicyclePanelPage() {
        return "AdminView/addBicyclePanel";
    }

    @PostMapping("/addBicycle")
    public String addBicycle(@RequestParam String name, @RequestParam String type, @RequestParam String photoURL,
                             @RequestParam String pricePerDay, RedirectAttributes redirectAttributes) {
        if(photoURL.length() > 255){
            redirectAttributes.addFlashAttribute("error", "Podano zbyt długi adres URL zdjęcia roweru - spróbuj ponownie.");
            return "redirect:/admin/addBicycleForm";
        }
        this.bicycleServ.addBicycle(new Bicycle(name, type, photoURL, Double.parseDouble(pricePerDay)));
        redirectAttributes.addFlashAttribute("message", "Nowy rower został dodany do floty.");
        return "redirect:/admin";
    }
    //TODO naprawić funkcjonalność - źle wysyłane dane z formularza
    @PostMapping("/modifyBicycle")
    public String modifyBicycle(@RequestParam Map<String, String> formData, RedirectAttributes redirectAttributes) {
        if ("save".equals(formData.get("action"))) {
            this.bicycleServ.updateBicycle(Long.parseLong(
                            formData.get("bicycleId")),
                    Double.parseDouble(formData.get("pricePerDay")),
                    formData.get("disable") != null && formData.get("disable").equals("on") ? true : false
            );
            redirectAttributes.addFlashAttribute("message", "Atrybuty roweru o ID : " + formData.get("bicycleId")
                    + " zostały pomyślnie zaktualizowane." );
        } else if ("delete".equals(formData.get("action"))) {
            this.bicycleServ.deleteBicycle(Long.parseLong(formData.get("bicycleId")));
            redirectAttributes.addFlashAttribute("message", "Rower o ID : " + formData.get("bicycleId")
                    + " został usunięty z floty." );
        }
        return "redirect:/admin";
    }

    @GetMapping("/opinions")
    public String opinionsPanelPage(Model model) {
        model.addAttribute("opinions", opinionServ.findAll());
        return "AdminView/opinionsPanel";
    }

    @GetMapping("/orders")
    public String ordersPanelPage(Model model) {
        model.addAttribute("orders", orderServ.findAll());
        return "AdminView/ordersPanel";
    }

    @GetMapping("/changePassword")
    public String changePasswordPanelPage(){
        return "AdminView/changePasswordPanel";
    }
}
