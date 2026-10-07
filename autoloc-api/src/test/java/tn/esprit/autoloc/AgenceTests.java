package tn.esprit.autoloc;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class AgenceTests {

    @Autowired
    private AgenceRepositoryMock agenceRepository;

    @Test
    void addAgence() {
        // Question 7 : création de l'agence
        Agence agence = new Agence();
        agence.setAdresse("1 Rue Hedi");
        agence.setNom("Agence ariana");
        agence.setTelephone("71585874");
        agence.setVille("Tunis");

        // Création des deux véhicules
        Vehicule vehicle12 = new Vehicule();
        vehicle12.setCategorie(CategorieVehicule.SUV);
        vehicle12.setImmatriculation("785414TU96");
        vehicle12.setMarque("Isuzu");
        vehicle12.setModele("DMax");
        vehicle12.setStatut(StatutVehicule.MAINTENANCE);
        vehicle12.setTarifJournalier(BigDecimal.valueOf(100));
        vehicle12.setAgence(agence);

        Vehicule vehicle13 = new Vehicule();
        vehicle13.setCategorie(CategorieVehicule.UTILITAIRE);
        vehicle13.setImmatriculation("785414TU95");
        vehicle13.setMarque("Toyota");
        vehicle13.setModele("Yaris");
        vehicle13.setStatut(StatutVehicule.DISPONIBLE);
        vehicle13.setTarifJournalier(BigDecimal.valueOf(80));
        vehicle13.setAgence(agence);

        // Ajout des véhicules à l'agence
        Set<Vehicule> vehicules = new HashSet<>();
        vehicules.add(vehicle12);
        vehicules.add(vehicle13);
        agence.setVehicules(vehicules);

        // Question 8 : persistance avec save() de CrudRepository
        agenceRepository.save(agence);
    }
    @Test
    public void loadAgence() {
        Iterable<Agence> agences = agenceRepository.findAll();

        StringBuilder sb = new StringBuilder();
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
        Assert.fail(sb.toString());
    }
}

interface AgenceRepositoryMock extends CrudRepository<Agence, Long> {
}