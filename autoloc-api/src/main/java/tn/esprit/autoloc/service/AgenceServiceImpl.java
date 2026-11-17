package tn.esprit.autoloc.service;

import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;

@Primary
@Service
public class AgenceServiceImpl extends GenericServiceImpl<Agence, Long> implements IAgenceService, IAgenceSimulationService, ISimuler {

    private final IVehiculeService vehiculeService;
    private final IEmployeService employeService;
    public AgenceServiceImpl(IAgenceRepository agenceRepository, IVehiculeService vehiculeService, IEmployeService employeService) {
        super(agenceRepository);
        this.vehiculeService = vehiculeService;
        this.employeService = employeService;
    }

    @Override
    protected String getEntityName() {
        return "Agence";
    }

    @Override
    protected Long getId(Agence agence) {
        return agence.getIdAgence();
    }

    @Override
    protected void setFields(Agence source, Agence target) {
        target.setNom(source.getNom());
        target.setVille(source.getVille());
        target.setAdresse(source.getAdresse());
        target.setTelephone(source.getTelephone());
    }

    @Override
    public boolean check(Agence agence, boolean isNew) {
        return Utils.isNotEmpty(agence.getNom()) && Utils.isNotEmpty(agence.getTelephone());
    }

    @Override
    public boolean hasVehicules(Long id) {
        return !findById(id).getVehicules().isEmpty();
    }

    public String simuler(Agence agence, int nbjours, VehiculeOffre offre) {
        StringBuilder rapport = new StringBuilder();
        rapport.append("Id : "+ agence.getIdAgence() + "Nom : " + agence.getNom() + "Telephone : " + agence.getTelephone()+"\n");

        double total = 0;
        for(Vehicule vehicule : agence.getVehicules()){
            total += vehiculeService.calculTarif(vehicule, nbjours, offre);
        }

        rapport.append("Total : " + total +"\n");
        return rapport.toString();
    }

    @Override
    @Transactional
    public Agence ajouterEmployes(Long id, Agence agence) {
        // 1. mettre à jour d'abord l'agence
        var existante = update(id, agence);

        // 2. insérer les employés via le service des employés
        var nouveaux = agence.getEmployes();
        for (Employe employe : nouveaux) {
            employe.setAgence(existante);
            employeService.create(employe);
        }

        // 3. mettre à jour l'effectif avec la taille de la liste
        existante.setEffectif(nouveaux.size());
        return repository.save(existante);
    }
}
