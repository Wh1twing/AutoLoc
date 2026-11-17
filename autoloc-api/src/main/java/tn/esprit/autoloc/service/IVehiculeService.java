package tn.esprit.autoloc.service;

import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Vehicule;

public interface IVehiculeService extends IGenericService<Vehicule, Long>{
    double calculTarif(Vehicule vehicule, int nbJours, VehiculeOffre offre);
    Vehicule desaffecter(Long id);
    Vehicule affecter(Long id, Long idAgence);
    Vehicule affecterEquipement(Long idVehicule, Long idEquipement);
}
