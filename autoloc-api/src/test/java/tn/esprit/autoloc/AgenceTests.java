package tn.esprit.autoloc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.service.IAgenceService;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class AgenceTests {

    @Autowired
    private AgenceRepositoryMock basicAgenceRepository;
    @Autowired
    private IAgenceRepository fullAgenceRepository;
    @Autowired
    private IAgenceService agenceService;

    // Q4 + Q6 : privée, sans @Test, avec le dépôt en paramètre
    private void addAgence(CrudRepository<Agence, Long> repository) {
        Agence agence = new Agence();
        agence.setAdresse("1 Rue Hedi");
        agence.setNom("Agence ariana");
        agence.setTelephone("71585874");
        agence.setVille("Tunis");

        int ms = (int) System.currentTimeMillis();        // Q5

        Vehicule vehicle12 = new Vehicule();
        vehicle12.setCategorie(CategorieVehicule.SUV);
        vehicle12.setImmatriculation("785414TU96" + ms);  // Q5 : suffixe ajouté
        vehicle12.setMarque("Isuzu");
        vehicle12.setModele("DMax");
        vehicle12.setStatut(StatutVehicule.MAINTENANCE);
        vehicle12.setTarifJournalier(BigDecimal.valueOf(100));
        vehicle12.setAgence(agence);

        Vehicule vehicle13 = new Vehicule();
        vehicle13.setCategorie(CategorieVehicule.UTILITAIRE);
        vehicle13.setImmatriculation("785414TU95" + ms);
        vehicle13.setMarque("Toyota");
        vehicle13.setModele("Yaris");
        vehicle13.setStatut(StatutVehicule.DISPONIBLE);
        vehicle13.setTarifJournalier(BigDecimal.valueOf(80));
        vehicle13.setAgence(agence);

        Set<Vehicule> vehicules = new HashSet<>();
        vehicules.add(vehicle12);
        vehicules.add(vehicle13);
        agence.setVehicules(vehicules);

        repository.save(agence);                          // uniquement le paramètre
    }

    @Test
    public void basicAddAgence() {                        // Q7
        addAgence(basicAgenceRepository);
    }

    @Test
    public void fullAddAgence() {
        addAgence(fullAgenceRepository);
    }

    // à adapter à la Q10 (privée + paramètre dépôt + type de dépôt affiché)
    // Q10 : privée, sans @Test, avec le dépôt et son type en paramètres
    private void loadAgence(CrudRepository<Agence, Long> repository, String typeDepot) {
        Iterable<Agence> agences = repository.findAll();

        StringBuilder sb = new StringBuilder();
        sb.append("Dépôt utilisé : ").append(typeDepot).append("\n\n");
        for (Agence a : agences) {
            sb.append("Agence ID : ").append(a.getIdAgence()).append("\n");
            sb.append("Nom : ").append(a.getNom()).append("\n");
            sb.append("Nombre de véhicules : ").append(a.getVehicules().size()).append("\n");
            for (Vehicule v : a.getVehicules()) {
                sb.append("   Véhicule ID : ").append(v.getIdVehicule())
                        .append(", Immatriculation : ").append(v.getImmatriculation())
                        .append("\n");
            }
            sb.append("\n");
        }
        Assertions.fail(sb.toString());
    }

    @Test
    public void basicLoadAgence() {
        loadAgence(basicAgenceRepository, "CrudRepository (basic)");
    }

    @Test
    public void fullLoadAgence() {
        loadAgence(fullAgenceRepository, "JpaRepository (full)");
    }

    @Test
    public void loadSortedAgences() {
        // Q12 : toutes les agences triées par id décroissant
        List<Agence> agences = fullAgenceRepository
                .findAll(Sort.by(Sort.Direction.DESC, "idAgence"));

        // Q13 : infos des agences, sans toucher à getVehicules()
        StringBuilder sb = new StringBuilder();
        sb.append("Agences triées par id décroissant\n");
        for (Agence agence : agences) {
            sb.append(agence.getIdAgence())
                    .append(" | ").append(agence.getNom())
                    .append(" | ").append(agence.getVille())
                    .append("\n");
        }

        // Q14 : affichage via une assertion qui échoue
        Assertions.fail(sb.toString());
    }

    @Test
    public void loadPagedAgences() {
        int taille = 2;
        Sort tri = Sort.by(Sort.Direction.DESC, "idAgence");

        Page<Agence> premiere = fullAgenceRepository.findAll(PageRequest.of(0, taille, tri));

        StringBuilder sb = new StringBuilder();
        sb.append("Agences triées par id décroissant, paginées par ").append(taille).append("\n");
        sb.append("Nombre total de pages : ").append(premiere.getTotalPages()).append("\n\n");

        for (int i = 0; i < premiere.getTotalPages(); i++) {
            Page<Agence> page = fullAgenceRepository.findAll(PageRequest.of(i, taille, tri));

            sb.append("--- Page en cours : ").append(page.getNumber()).append(" ---\n");
            for (Agence a : page.getContent()) {
                sb.append("Agence ID : ").append(a.getIdAgence())
                        .append(" | Nom : ").append(a.getNom())
                        .append(" | Ville : ").append(a.getVille())
                        .append("\n");
            }
            sb.append("\n");
        }
        Assertions.fail(sb.toString());
    }

    @Test
    void createAgence() {
        Agence agence = new Agence();
        agence.setNom("Agence Sfax");
        agence.setVille("Sfax");
        agence.setAdresse("5 Rue de la Liberté");
        agence.setTelephone("74000000");

        Agence creee = agenceService.create(agence);

        Assertions.assertNotNull(creee.getIdAgence());
    }
}

    @Repository
    interface AgenceRepositoryMock extends CrudRepository<Agence, Long> {
    }
