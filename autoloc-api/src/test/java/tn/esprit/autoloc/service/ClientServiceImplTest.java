package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.repository.IClientRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private IClientRepository repository;

    private ClientServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ClientServiceImpl(repository);
    }

    @Test
    void getEntityName_retourneClient() {
        assertEquals("Client", service.getEntityName());
    }

    @Test
    void getId_retourneIdentifiantDuClient() {
        Client client = new Client();
        client.setIdClient(5L);

        assertEquals(Long.valueOf(5L), service.getId(client));
    }

    @Test
    void setFields_copieLesChampsDeLaSource() {
        Client existant = new Client();
        Client source = new Client();
        source.setNom("Ben Salah");
        source.setPrenom("Sami");
        source.setEmail("sami@mail.tn");
        source.setTelephone("22111222");
        source.setNumPermis("P12345");

        service.setFields(existant, source);

        assertEquals("Ben Salah", existant.getNom());
        assertEquals("Sami", existant.getPrenom());
        assertEquals("sami@mail.tn", existant.getEmail());
        assertEquals("22111222", existant.getTelephone());
        assertEquals("P12345", existant.getNumPermis());
    }

    @Test
    void check_comportementParDefaut_accepteUnClientNonNull() {
        assertTrue(service.check(new Client(), true));
    }

    @Test
    void check_clientNull_invalide() {
        assertFalse(service.check(null, true));
    }

    @Test
    void create_clientValide_sauvegarde() {
        Client client = new Client();
        when(repository.save(client)).thenReturn(client);

        assertSame(client, service.create(client));
    }
}