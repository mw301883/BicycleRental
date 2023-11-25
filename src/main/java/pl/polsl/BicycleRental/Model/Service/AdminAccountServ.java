package pl.polsl.BicycleRental.Model.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.polsl.BicycleRental.Model.ModelDB.AdminAccount;
import pl.polsl.BicycleRental.Model.Repository.AdminAccountRepo;

import java.util.Optional;

@Service
public class AdminAccountServ {
    private final AdminAccountRepo adminAccountRepo;
    @Autowired
    AdminAccountServ(AdminAccountRepo adminAccountRepo){
        this.adminAccountRepo = adminAccountRepo;
    }
    public String GetLogin(){
        Optional<AdminAccount> obj = adminAccountRepo.findById(1L);
        return obj.get().getLogin();
    }
    public String GetPassword(){
        Optional<AdminAccount> obj = adminAccountRepo.findById(1L);
        return obj.get().getPassword();
    }
}
