package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.repository.IContratRepository;

@Service
public class ContratServiceImpl extends GenericServiceImpl<Contrat, Long> {

    public ContratServiceImpl(IContratRepository repository) {
        super(repository);
    }

    @Override
    protected String getEntityName() {
        return "Contrat";
    }

    @Override
    protected Long getId(Contrat entity) {
        return entity.getIdContrat();
    }

    @Override
    protected void setFields(Contrat existing, Contrat entity) {
        existing.setDateSignature(entity.getDateSignature());
        existing.setMontantTotal(entity.getMontantTotal());
        existing.setValide(entity.isValide());
        if (entity.getReservation() != null) {
            existing.setReservation(entity.getReservation());
        }
        // les paiements ne sont pas modifiés par ce PUT
    }

    @Override
    public boolean check(Contrat entity, boolean isNew) {
        return entity != null
                && entity.getDateSignature() != null
                && entity.getMontantTotal() != null;
    }
}