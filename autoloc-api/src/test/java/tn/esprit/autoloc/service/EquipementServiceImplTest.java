package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.repository.IEquipementRepository;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EquipementServiceImplTest {

    @Mock
    private IEquipementRepository repository;

    private EquipementServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EquipementServiceImpl(repository);
    }

    private Equipement equipement(String libelle) {
        Equipement e = new Equipement();
        e.setLibelle(libelle);
        return e;
    }

    @Test
    void getEntityName_retourneEquipement() {
        assertEquals("Equipement", service.getEntityName());
    }

    @Test
    void getId_retourneIdentifiantDeLEquipement() {
        Equipement equipement = new Equipement();
        equipement.setIdEquipement(4L);

        assertEquals(Long.valueOf(4L), service.getId(equipement));
    }

    @Test
    void check_equipementValide() {
        assertTrue(service.check(equipement("GPS"), true));
    }

    @Test
    void check_sansLibelle_invalide() {
        assertFalse(service.check(equipement(" "), true));
    }

    @Test
    void check_null_invalide() {
        assertFalse(service.check(null, true));
    }

    @Test
    void setFields_copieLeLibelle() {
        Equipement existant = equipement("ancien");
        Equipement source = equipement("Siège bébé");

        service.setFields(existant, source);

        assertEquals("Siège bébé", existant.getLibelle());
    }
}