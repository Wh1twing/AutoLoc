package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.repository.IPaiementRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaiementServiceImplTest {

    @Mock
    private IPaiementRepository repository;

    private PaiementServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PaiementServiceImpl(repository);
    }

    @Test
    void getEntityName_retournePaiement() {
        assertEquals("Paiement", service.getEntityName());
    }

    @Test
    void getId_retourneIdentifiantDuPaiement() {
        Paiement paiement = new Paiement();
        paiement.setIdPaiement(2L);

        assertEquals(Long.valueOf(2L), service.getId(paiement));
    }

    @Test
    void findAll_retourneLesPaiements() {
        when(repository.findAll()).thenReturn(List.of(new Paiement(), new Paiement()));

        assertEquals(2, service.findAll().size());
    }

    @Test
    void findById_existant_retournePaiement() {
        Paiement paiement = new Paiement();
        when(repository.findById(1L)).thenReturn(Optional.of(paiement));

        assertSame(paiement, service.findById(1L));
    }

    @Test
    void create_refuse_lectureSeule() {
        Paiement paiement = new Paiement();

        assertThrows(UnsupportedOperationException.class, () -> service.create(paiement));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void update_refuse_lectureSeule() {
        Paiement paiement = new Paiement();

        assertThrows(UnsupportedOperationException.class, () -> service.update(1L, paiement));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteById_refuse_lectureSeule() {
        assertThrows(UnsupportedOperationException.class, () -> service.deleteById(1L));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void setFields_refuse_lectureSeule() {
        Paiement existant = new Paiement();
        Paiement source = new Paiement();

        assertThrows(UnsupportedOperationException.class, () -> service.setFields(existant, source));
    }
}