package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.calcul.LongueDureeStrategy;
import tn.esprit.autoloc.calcul.OffreStrategy;
import tn.esprit.autoloc.calcul.StandardStrategy;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.calcul.WeekendStrategy;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehiculeServiceImplTest {

    @Mock
    private IVehiculeRepository repository;
    @Mock
    private IAgenceRepository agenceRepository;
    @Mock
    private IEquipementRepository equipementRepository;

    private VehiculeServiceImpl service;

    @BeforeEach
    void setUp() {
        Map<String, OffreStrategy> strategies = Map.of(
                "standard", new StandardStrategy(),
                "weekend", new WeekendStrategy(),
                "longueDuree", new LongueDureeStrategy());
        service = new VehiculeServiceImpl(repository, agenceRepository, equipementRepository, strategies);
    }

    private Vehicule vehicule(Long id, double tarif) {
        Vehicule v = new Vehicule();
        v.setIdVehicule(id);
        v.setImmatriculation("123TU456");
        v.setMarque("Renault");
        v.setModele("Clio");
        v.setTarifJournalier(BigDecimal.valueOf(tarif));
        return v;
    }

    // ---------- calculTarif ----------

    @Test
    void calculTarif_standard() {
        assertEquals(500.0, service.calculTarif(vehicule(1L, 100), 5, VehiculeOffre.STANDARD), 0.001);
    }

    @Test
    void calculTarif_weekend_reduction20pourcent() {
        assertEquals(400.0, service.calculTarif(vehicule(1L, 100), 5, VehiculeOffre.WEEKEND), 0.001);
    }

    @Test
    void calculTarif_longueDuree_reduction25pourcent() {
        assertEquals(375.0, service.calculTarif(vehicule(1L, 100), 5, VehiculeOffre.LONGUE_DUREE), 0.001);
    }

    @Test
    void calculTarif_longueDuree_totalAu_dessusDe2000_reductionSupplementaire() {
        // 100 * 0.75 * 30 = 2250 > 2000, donc 2250 * 0.95
        assertEquals(2137.5, service.calculTarif(vehicule(1L, 100), 30, VehiculeOffre.LONGUE_DUREE), 0.001);
    }

    @Test
    void calculTarif_standard_pasDeReductionDe5pourcent() {
        assertEquals(3000.0, service.calculTarif(vehicule(1L, 100), 30, VehiculeOffre.STANDARD), 0.001);
    }

    @Test
    void calculTarif_offreSansStrategie_leveException() {
        VehiculeServiceImpl serviceSansStrategie =
                new VehiculeServiceImpl(repository, agenceRepository, equipementRepository, Map.of());
        Vehicule v = vehicule(1L, 100);

        assertThrows(IllegalArgumentException.class,
                () -> serviceSansStrategie.calculTarif(v, 5, VehiculeOffre.STANDARD));
    }

    // ---------- affectations ----------

    @Test
    void desaffecter_retireLAgence() {
        Vehicule v = vehicule(1L, 100);
        v.setAgence(new Agence());
        when(repository.findById(1L)).thenReturn(Optional.of(v));
        when(repository.save(v)).thenReturn(v);

        Vehicule resultat = service.desaffecter(1L);

        assertNull(resultat.getAgence());
    }

    @Test
    void affecter_rattacheLAgence() {
        Vehicule v = vehicule(1L, 100);
        Agence agence = new Agence();
        agence.setIdAgence(2L);
        when(repository.findById(1L)).thenReturn(Optional.of(v));
        when(agenceRepository.findById(2L)).thenReturn(Optional.of(agence));
        when(repository.save(v)).thenReturn(v);

        Vehicule resultat = service.affecter(1L, 2L);

        assertSame(agence, resultat.getAgence());
    }

    @Test
    void affecter_agenceInexistante_leveResourceNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(vehicule(1L, 100)));
        when(agenceRepository.findById(9L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.affecter(1L, 9L));
        verify(repository, never()).save(any());
    }

    @Test
    void affecterEquipement_ajouteLEquipement() {
        Vehicule v = vehicule(1L, 100);
        Equipement equipement = new Equipement();
        equipement.setIdEquipement(3L);
        when(repository.findById(1L)).thenReturn(Optional.of(v));
        when(equipementRepository.findById(3L)).thenReturn(Optional.of(equipement));
        when(repository.save(v)).thenReturn(v);

        Vehicule resultat = service.affecterEquipement(1L, 3L);

        assertTrue(resultat.getEquipements().contains(equipement));
    }

    @Test
    void affecterEquipement_equipementInexistant_leveResourceNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(vehicule(1L, 100)));
        when(equipementRepository.findById(9L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.affecterEquipement(1L, 9L));
    }

    // ---------- check ----------

    @Test
    void check_vehiculeValide() {
        assertTrue(service.check(vehicule(null, 100), true));
    }

    @Test
    void check_sansImmatriculation_invalide() {
        Vehicule v = vehicule(null, 100);
        v.setImmatriculation("");

        assertFalse(service.check(v, true));
    }

    @Test
    void check_sansTarif_invalide() {
        Vehicule v = vehicule(null, 100);
        v.setTarifJournalier(null);

        assertFalse(service.check(v, true));
    }
}