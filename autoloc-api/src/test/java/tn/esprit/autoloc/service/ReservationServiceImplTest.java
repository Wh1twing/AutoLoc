package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IReservationRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private IReservationRepository repository;

    private ReservationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ReservationServiceImpl(repository);
    }

    private Reservation reservation(LocalDate debut, LocalDate fin) {
        Reservation r = new Reservation();
        r.setDateDebut(debut);
        r.setDateFin(fin);
        return r;
    }

    @Test
    void getEntityName_retourneReservation() {
        assertEquals("Reservation", service.getEntityName());
    }

    @Test
    void getId_retourneIdentifiantDeLaReservation() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation(6L);

        assertEquals(Long.valueOf(6L), service.getId(reservation));
    }

    @Test
    void check_reservationValide() {
        assertTrue(service.check(reservation(LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 5)), true));
    }

    @Test
    void check_sansDateDebut_invalide() {
        assertFalse(service.check(reservation(null, LocalDate.of(2026, 4, 5)), true));
    }

    @Test
    void check_sansDateFin_invalide() {
        assertFalse(service.check(reservation(LocalDate.of(2026, 4, 1), null), true));
    }

    @Test
    void check_null_invalide() {
        assertFalse(service.check(null, true));
    }

    @Test
    void setFields_copieLesChampsEtLesRelations() {
        Reservation existante = new Reservation();
        Reservation source = reservation(LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 5));
        Vehicule vehicule = mock(Vehicule.class);
        Client client = mock(Client.class);
        Contrat contrat = mock(Contrat.class);
        source.setVehicule(vehicule);
        source.setClient(client);
        source.setContrat(contrat);

        service.setFields(existante, source);

        assertEquals(LocalDate.of(2026, 4, 1), existante.getDateDebut());
        assertEquals(LocalDate.of(2026, 4, 5), existante.getDateFin());
        assertSame(vehicule, existante.getVehicule());
        assertSame(client, existante.getClient());
        assertSame(contrat, existante.getContrat());
    }
}