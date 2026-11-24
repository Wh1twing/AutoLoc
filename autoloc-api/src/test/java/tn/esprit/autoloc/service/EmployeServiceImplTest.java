package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.RoleEmploye;
import tn.esprit.autoloc.repository.IEmployeRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class EmployeServiceImplTest {

    @Mock
    private IEmployeRepository repository;

    private EmployeServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EmployeServiceImpl(repository);
    }

    private Employe employe(String nom, String prenom, RoleEmploye role) {
        Employe e = new Employe();
        e.setNom(nom);
        e.setPrenom(prenom);
        e.setRole(role);
        return e;
    }

    @Test
    void getEntityName_retourneEmploye() {
        assertEquals("Employe", service.getEntityName());
    }

    @Test
    void getId_retourneIdentifiantDeLEmploye() {
        Employe employe = new Employe();
        employe.setIdEmploye(3L);

        assertEquals(Long.valueOf(3L), service.getId(employe));
    }

    @Test
    void check_employeValide() {
        assertTrue(service.check(employe("Gharbi", "Sonia", RoleEmploye.AGENT), true));
    }

    @Test
    void check_sansNom_invalide() {
        assertFalse(service.check(employe("", "Sonia", RoleEmploye.AGENT), true));
    }

    @Test
    void check_sansPrenom_invalide() {
        assertFalse(service.check(employe("Gharbi", null, RoleEmploye.AGENT), true));
    }

    @Test
    void check_sansRole_invalide() {
        assertFalse(service.check(employe("Gharbi", "Sonia", null), true));
    }

    @Test
    void check_null_invalide() {
        assertFalse(service.check(null, true));
    }

    @Test
    void setFields_copieLesChampsDeLaSource() {
        Employe existant = new Employe();
        Employe source = employe("Trabelsi", "Amira", RoleEmploye.MANAGER);
        Agence agence = mock(Agence.class);
        source.setAgence(agence);

        service.setFields(existant, source);

        assertEquals("Trabelsi", existant.getNom());
        assertEquals("Amira", existant.getPrenom());
        assertEquals(RoleEmploye.MANAGER, existant.getRole());
        assertSame(agence, existant.getAgence());
    }
}