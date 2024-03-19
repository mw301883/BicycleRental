package pl.polsl.BicycleRental.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.polsl.BicycleRental.Model.ModelDB.AdminAccount;
import pl.polsl.BicycleRental.Repository.AdminAccountRepo;

import java.util.Optional;

@Service
public class AdminAccountServ {
    private final AdminAccountRepo adminAccountRepo;
    private  final PasswordEncoder passwordEncoder;
    @Autowired
    AdminAccountServ(AdminAccountRepo adminAccountRepo, PasswordEncoder passwordEncoder){
        this.adminAccountRepo = adminAccountRepo;
        this.passwordEncoder = passwordEncoder;
    }
    public String GetLogin(){
        Optional<AdminAccount> obj = adminAccountRepo.findById(1L);
        return obj.get().getLogin();
    }
    public String GetPassword(){
        Optional<AdminAccount> obj = adminAccountRepo.findById(1L);
        return obj.get().getPassword();
    }
    public void changePassword(String newPassword){
        AdminAccount adminAccount = this.adminAccountRepo.getReferenceById(1L);
        String encodedPassword = this.passwordEncoder.encode(newPassword);
        adminAccount.setPassword(encodedPassword);
        this.adminAccountRepo.save(adminAccount);
    }
}
