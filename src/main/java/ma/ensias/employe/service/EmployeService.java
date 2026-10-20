package ma.ensias.employe.service;

import ma.ensias.employe.entite.Employe;
import ma.ensias.employe.reponse.EmployeReponse;
import ma.ensias.employe.repository.EmployeRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmployeService {

    @Autowired
    private EmployeRepo employeRepo;

    @Autowired
    private ModelMapper mapper;

    public EmployeReponse getEmployeById(int id) {
        // Correction de l'énoncé : on récupère l'Employe contenu dans l'Optional
        Employe employe = employeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Employé introuvable : " + id));
        return mapper.map(employe, EmployeReponse.class);
    }
}