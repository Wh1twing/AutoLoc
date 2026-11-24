package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.repository.IContratRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ContratServiceImplTest {

    @Mock
    private IContratRepository repository;

    private ContratServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ContratServiceImpl(repository);
    }

    private Contrat contrat(LocalDate date, BigDecimal montant) {
        Contrat c = new Contrat();
        c.setDateSignature(date);
        c.setMontantTotal(montant);
        return c;
    }

    @Test
    void getEntityName_retourneContrat() {
        assertEquals("Contrat", service.getEntityName());
    }

    @Test
    void getId_retourneIdentifiantDuContrat() {
        Contrat contrat = new Contrat();
        contrat.setIdContrat(7L);

        assertEquals(Long.valueOf(7L), service.getId(contrat));
    }

    @Test
    void check_contratValide() {
        assertTrue(service.check(contrat(LocalDate.of(2026, 1, 15), BigDecimal.valueOf(500)), true));
    }

    @Test
    void check_sansDateSignature_invalide() {
        assertFalse(service.check(contrat(null, BigDecimal.valueOf(500)), true));
    }

    @Test
    void check_sansMontant_invalide() {
        assertFalse(service.check(contrat(LocalDate.of(2026, 1, 15), null), true));
    }

    @Test
    void check_null_invalide() {
        assertFalse(service.check(null, true));
    }

    @Test
    void setFields_copieLesChampsEtLaReservation() {
        Contrat existant = new Contrat();
        Contrat source = contrat(LocalDate.of(2026, 2, 1), BigDecimal.valueOf(900));
        source.setValide(true);
        Reservation reservation = mock(Reservation.class);
        source.setReservation(reservation);

        service.setFields(existant, source);

        assertEquals(LocalDate.of(2026, 2, 1), existant.getDateSignature());
        assertEquals(BigDecimal.valueOf(900), existant.getMontantTotal());
        assertTrue(existant.isValide());
        assertSame(reservation, existant.getReservation());
    }

    @Test
    void setFields_sansReservation_conserveLaReservationExistante() {
        Contrat existant = new Contrat();
        Reservation reservation = mock(Reservation.class);
        existant.setReservation(reservation);
        Contrat source = contrat(LocalDate.of(2026, 2, 1), BigDecimal.valueOf(900));

        service.setFields(existant, source);

        assertSame(reservation, existant.getReservation());
    }
}