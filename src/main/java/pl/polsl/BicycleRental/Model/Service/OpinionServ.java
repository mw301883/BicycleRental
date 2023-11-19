package pl.polsl.BicycleRental.Model.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.polsl.BicycleRental.Model.ModelDB.Opinion;
import pl.polsl.BicycleRental.Model.Repository.OpinionRepo;
//W klasach z adnotacją @Service piszemy całą logikę zarządzania DB na przykład operacje CRUD czy inne zarządanie danymi.
@Service
public class OpinionServ {
    private final OpinionRepo opinionRepo;
    @Autowired
    OpinionServ(OpinionRepo opinionRepo){
        this.opinionRepo = opinionRepo;
    }
    public void addOpinion(Opinion opinion){
        this.opinionRepo.save(opinion);
    }
}
