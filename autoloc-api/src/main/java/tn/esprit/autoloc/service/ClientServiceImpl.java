package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.repository.IClientRepository;

@Service
public class ClientServiceImpl extends GenericServiceImpl<Client, Long> implements IGenericService<Client, Long> {

    public ClientServiceImpl(IClientRepository repository) { super(repository); }

    @Override protected String getEntityName() { return "Client"; }
    @Override protected Long getId(Client c)   { return c.getIdClient(); }

    @Override
    protected void setFields(Client existant, Client source) {
        existant.setNom(source.getNom());
        existant.setPrenom(source.getPrenom());
        existant.setEmail(source.getEmail());
        existant.setTelephone(source.getTelephone());
        existant.setNumPermis(source.getNumPermis());
        existant.setDateInscription(source.getDateInscription());
    }
}
