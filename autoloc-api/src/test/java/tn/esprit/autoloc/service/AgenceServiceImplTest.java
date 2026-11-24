package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.RoleEmploye;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgenceServiceImplTest {

    @Mock
    private IAgenceRepository repository;
    @Mock
    private IVehiculeService vehiculeService;
    @Mock
    private IEmployeService employeService;

    private AgenceServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AgenceServiceImpl(repository, vehiculeService, employeService);
    }

    private Agence agence(Long id, String nom) {
        Agence a = new Agence();
        a.setIdAgence(id);
        a.setNom(nom);
        a.setVille("Tunis");
        a.setAdresse("1 Rue Hedi");
        a.setTelephone("71585874");
        return a;
    }

    private Employe employe(String nom) {
        Employe e = new Employe();
        e.setNom(nom);
        e.setPrenom("Test");
        e.setRole(RoleEmploye.AGENT);
        return e;
    }

    @Test
    void simuler_additionneLesTarifsDesVehicules() {
        Agence agence = agence(1L, "Agence ariana");
        Vehicule v1 = new Vehicule();
        Vehicule v2 = new Vehicule();
        agence.setVehicules(new HashSet<>(List.of(v1, v2)));
        when(vehiculeService.calculTarif(v1, 5, VehiculeOffre.STANDARD)).thenReturn(100.0);
        when(vehiculeService.calculTarif(v2, 5, VehiculeOffre.STANDARD)).thenReturn(200.0);

        String rapport = service.simuler(agence, 5, VehiculeOffre.STANDARD);

        assertTrue(rapport.contains("Agence ariana"));
        assertTrue(rapport.contains("Total : 300.0"));
    }

    @Test
    void hasVehicules_avecVehicules_retourneVrai() {
        Agence agence = agence(1L, "Agence ariana");
        agence.setVehicules(new HashSet<>(List.of(new Vehicule())));
        when(repository.findById(1L)).thenReturn(Optional.of(agence));

        assertTrue(service.hasVehicules(1L));
    }

    @Test
    void hasVehicules_sansVehicule_retourneFaux() {
        Agence agence = agence(1L, "Agence ariana");
        agence.setVehicules(new HashSet<>());
        when(repository.findById(1L)).thenReturn(Optional.of(agence));

        assertFalse(service.hasVehicules(1L));
    }

    @Test
    void check_agenceValide() {
        assertTrue(service.check(agence(null, "Agence ariana"), true));
    }

    @Test
    void check_sansNom_invalide() {
        assertFalse(service.check(agence(null, ""), true));
    }

    @Test
    void ajouterEmployes_metAJourAgenceInsereEmployesEtEffectif() {
        Agence existante = agence(1L, "Ancien nom");
        when(repository.findById(1L)).thenReturn(Optional.of(existante));
        when(repository.save(any(Agence.class))).thenAnswer(inv -> inv.getArgument(0));

        Agence demande = agence(null, "Nouveau nom");
        Employe e1 = employe("Gharbi");
        Employe e2 = employe("Trabelsi");
        demande.setEmployes(new HashSet<>(List.of(e1, e2)));

        Agence resultat = service.ajouterEmployes(1L, demande);

        assertEquals("Nouveau nom", resultat.getNom());
        assertEquals(Integer.valueOf(2), resultat.getEffectif());
        assertSame(existante, e1.getAgence());
        assertSame(existante, e2.getAgence());
        verify(employeService, times(2)).create(any(Employe.class));
    }

    @Test
    void ajouterEmployes_employeInvalide_propageLException() {
        Agence existante = agence(1L, "Ancien nom");
        when(repository.findById(1L)).thenReturn(Optional.of(existante));
        when(repository.save(any(Agence.class))).thenAnswer(inv -> inv.getArgument(0));
        when(employeService.create(any(Employe.class)))
                .thenThrow(new IllegalArgumentException("Employe invalide"));

        Agence demande = agence(null, "Nouveau nom");
        demande.setEmployes(new HashSet<>(List.of(employe("Gharbi"))));

        assertThrows(IllegalArgumentException.class, () -> service.ajouterEmployes(1L, demande));
    }

    @Test
    void ajouterEmployes_estTransactionnelle() throws NoSuchMethodException {
        Method methode = AgenceServiceImpl.class.getMethod("ajouterEmployes", Long.class, Agence.class);

        assertTrue(methode.isAnnotationPresent(Transactional.class));
    }
}