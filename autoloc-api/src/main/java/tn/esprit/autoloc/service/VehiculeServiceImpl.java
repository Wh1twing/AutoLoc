package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.calcul.OffreStrategy;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.util.Map;

@Service
public class VehiculeServiceImpl extends GenericServiceImpl<Vehicule, Long> implements IVehiculeService {

    private final Map<String, OffreStrategy> strategies;
    private final IAgenceRepository agenceRepository;
    private final IEquipementRepository equipementRepository;

    public VehiculeServiceImpl(IVehiculeRepository repository,
                               IAgenceRepository agenceRepository,
                               IEquipementRepository equipementRepository,
                               Map<String, OffreStrategy> strategies) {
        super(repository);
        this.agenceRepository = agenceRepository;
        this.equipementRepository = equipementRepository;
        this.strategies = strategies;
    }

    @Override
    protected String getEntityName() {
        return "Vehicule";
    }

    @Override
    protected Long getId(Vehicule entity) {
        return entity.getIdVehicule();
    }

    @Override
    protected void setFields(Vehicule existing, Vehicule entity) {
        existing.setImmatriculation(entity.getImmatriculation());
        existing.setMarque(entity.getMarque());
        existing.setModele(entity.getModele());
        existing.setCategorie(entity.getCategorie());
        existing.setTarifJournalier(entity.getTarifJournalier());
        existing.setStatut(entity.getStatut());
        existing.setAgence(entity.getAgence());
        existing.setReservations(entity.getReservations());
        existing.setEquipements(entity.getEquipements());
    }

    @Override
    public boolean check(Vehicule entity, boolean isNew) {
        return entity != null
                && Utils.isNotEmpty(entity.getImmatriculation())
                && Utils.isNotEmpty(entity.getMarque())
                && Utils.isNotEmpty(entity.getModele())
                && entity.getTarifJournalier() != null;
    }

    @Override
    public double calculTarif(Vehicule vehicule, int nbJours, VehiculeOffre offre) {
        double tarifJournalier = vehicule.getTarifJournalier().doubleValue();
        OffreStrategy strategy = strategies.get(offre.getNomStrategie());
        if (strategy == null) {
            throw new IllegalArgumentException("Offre non supportée : " + offre);
        }
        return strategy.calculer(tarifJournalier, nbJours);
    }

    @Override
    public Vehicule desaffecter(Long id) {
        var vehicule = findById(id);

        vehicule.setAgence(null);
        return repository.save(vehicule);
    }

    @Override
    public Vehicule affecter(Long id, Long idAgence) {
        var vehicule = findById(id);
        var agence = agenceRepository.findById(idAgence)
                .orElseThrow(() -> new ResourceNotFoundException("agence", idAgence.toString()));

        vehicule.setAgence(agence);
        return repository.save(vehicule);
    }

    @Override
    @Transactional
    public Vehicule affecterEquipement(Long idVehicule, Long idEquipement) {
        var vehicule = repository.findById(idVehicule)
                .orElseThrow(() -> new ResourceNotFoundException("vehicule", idVehicule.toString()));
        var equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new ResourceNotFoundException("equipement", idEquipement.toString()));

        vehicule.getEquipements().add(equipement);
        return repository.save(vehicule);
    }
}