package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IMaintenanceRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MaintenanceServiceImplTest {

    @Mock
    private IMaintenanceRepository repository;

    private MaintenanceServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new MaintenanceServiceImpl(repository);
    }

    @Test
    void getEntityName_retourneMaintenance() {
        assertEquals("Maintenance", service.getEntityName());
    }

    @Test
    void getId_retourneIdentifiantDeLaMaintenance() {
        Maintenance maintenance = new Maintenance();
        maintenance.setIdMaintenance(8L);

        assertEquals(Long.valueOf(8L), service.getId(maintenance));
    }

    @Test
    void check_avecDateDebut_valide() {
        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(LocalDate.of(2026, 3, 1));

        assertTrue(service.check(maintenance, true));
    }

    @Test
    void check_sansDateDebut_invalide() {
        assertFalse(service.check(new Maintenance(), true));
    }

    @Test
    void check_null_invalide() {
        assertFalse(service.check(null, true));
    }

    @Test
    void setFields_copieLesChampsDeLaSource() {
        Maintenance existante = new Maintenance();
        Maintenance source = new Maintenance();
        source.setDateDebut(LocalDate.of(2026, 3, 1));
        source.setDateFin(LocalDate.of(2026, 3, 5));
        source.setDescription("Vidange");
        Vehicule vehicule = mock(Vehicule.class);
        source.setVehicule(vehicule);

        service.setFields(existante, source);

        assertEquals(LocalDate.of(2026, 3, 1), existante.getDateDebut());
        assertEquals(LocalDate.of(2026, 3, 5), existante.getDateFin());
        assertEquals("Vidange", existante.getDescription());
        assertSame(vehicule, existante.getVehicule());
    }
}