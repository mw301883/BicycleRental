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
import pl.polsl.BicycleRental.Model.ModelDB.Opinion;
import pl.polsl.BicycleRental.Model.Service.AdminAccountServ;
import pl.polsl.BicycleRental.Model.Service.BicycleServ;
import pl.polsl.BicycleRental.Model.Service.OpinionServ;
import pl.polsl.BicycleRental.Model.Service.OrderServ;
import pl.polsl.BicycleRental.Model.ModelDB.Order;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
//TODO dodać obwługę zwracania rowerów do oferty - panel zamówień, dodać guzik finalizuj, który wyzeruje daty wypożyczenia rowerów z zamówienia
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
    AdminAccountServ adminAccountServ;

    @Autowired
    AdminViewCtrl(BicycleServ bicycleServ, OpinionServ opinionServ, OrderServ orderServ, AdminAccountServ adminAccountServ) {
        this.bicycleServ = bicycleServ;
        this.opinionServ = opinionServ;
        this.orderServ = orderServ;
        this.adminAccountServ = adminAccountServ;
    }

    @GetMapping
    public String bicyclesPanelPage(Model model) {
        model.addAttribute("bicycles", bicycleServ.findAll()
                .stream()
                .sorted(Comparator.comparing(Bicycle::getId))
                .collect(Collectors.toList()));
        return "AdminView/bicyclesPanel";
    }

    @GetMapping("/addBicycleForm")
    public String addBicyclePanelPage() {
        return "AdminView/addBicyclePanel";
    }

    @PostMapping("/addBicycle")
    public String addBicycle(@RequestParam String name, @RequestParam String type, @RequestParam String photoURL,
                             @RequestParam String pricePerDay, RedirectAttributes redirectAttributes) {
        if (photoURL.length() > 255) {
            redirectAttributes.addFlashAttribute("error", "Podano zbyt długi adres URL zdjęcia roweru - spróbuj ponownie.");
            return "redirect:/admin/addBicycleForm";
        }
        this.bicycleServ.addBicycle(new Bicycle(name, type, photoURL, Double.parseDouble(pricePerDay)));
        redirectAttributes.addFlashAttribute("message", "Nowy rower został dodany do floty.");
        return "redirect:/admin";
    }

    @PostMapping("/modifyBicycle")
    public String modifyBicycle(@RequestParam Map<String, String> formData, RedirectAttributes redirectAttributes) {
        if ("save".equals(formData.get("action"))) {
            this.bicycleServ.updateBicycle(Long.parseLong(
                            formData.get("bicycleId")),
                    Double.parseDouble(formData.get("pricePerDay")),
                    formData.get("disable") != null && formData.get("disable").equals("on") ? true : false
            );
            redirectAttributes.addFlashAttribute("message", "Atrybuty roweru o ID : " + formData.get("bicycleId")
                    + " zostały pomyślnie zaktualizowane.");
        } else if ("delete".equals(formData.get("action"))) {
            this.bicycleServ.deleteBicycle(Long.parseLong(formData.get("bicycleId")));
            redirectAttributes.addFlashAttribute("message", "Rower o ID : " + formData.get("bicycleId")
                    + " został usunięty z floty.");
        }
        return "redirect:/admin";
    }

    @GetMapping("/opinions")
    public String opinionsPanelPage(Model model) {
        model.addAttribute("opinions", opinionServ.findAll()
                .stream()
                .sorted(Comparator.comparing(Opinion::getId))
                .collect(Collectors.toList()));
        return "AdminView/opinionsPanel";
    }

    @PostMapping("/deleteOpinion")
    public String deleteOpinion(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        this.opinionServ.deleteOpinion(id);
        redirectAttributes.addFlashAttribute("message", "Opinia o ID : " + id + " została usunięta.");
        return "redirect:/admin/opinions";
    }

    @GetMapping("/orders")
    public String ordersPanelPage(Model model) {
        orderServ.updateOrdersPnalty();
        model.addAttribute("orders", orderServ.findAll()
                .stream()
                .sorted(Comparator.comparing(Order::getId))
                .collect(Collectors.toList()));
        return "AdminView/ordersPanel";
    }

    @PostMapping("/addPenalty")
    public String addPenalty(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        this.orderServ.addPenaltyById(id);
        redirectAttributes.addFlashAttribute("message", "Do zamówienia o ID : " + id + " została naliczona kara za opóźnione oddanie.");
        return "redirect:/admin/orders";
    }

    @PostMapping("/cancelPenalty")
    public String cancelPenalty(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        this.orderServ.cancelPenaltyById(id);
        redirectAttributes.addFlashAttribute("message", "Do zamówienia o ID : " + id + " została odlicznona kara za opóźnione oddanie.");
        return "redirect:/admin/orders";
    }

    @PostMapping("/deleteOrder")
    public String deleteOrder(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        this.orderServ.deleteOrder(id);
        redirectAttributes.addFlashAttribute("message", "Zamówienie o ID : " + id + " zostało usunięte.");
        return "redirect:/admin/orders";
    }

    @GetMapping("/password")
    public String changePasswordPanelPage() {
        return "AdminView/changePasswordPanel";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam String password, @RequestParam String passwordConfirm, RedirectAttributes redirectAttributes) {
        if (!password.equals(passwordConfirm)) {
            redirectAttributes.addFlashAttribute("error", "Podane hasła są różne. Spróbuj ponownie.");
            return "redirect:/admin/password";
        }
        this.adminAccountServ.changePassword(password);
        redirectAttributes.addFlashAttribute("message", "Hasło zostało zmienione.");
        return "redirect:/admin";
    }
}
