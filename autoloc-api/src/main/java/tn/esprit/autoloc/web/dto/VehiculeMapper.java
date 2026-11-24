package tn.esprit.autoloc.web.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autoloc.domain.Vehicule;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehiculeMapper {
    @Mapping(source = "agence.idAgence", target = "idAgence")
    VehiculeDTO toDTO(Vehicule vehicule);

    @Mapping(source = "idAgence", target = "agence.idAgence")
    @Mapping(target = "reservations", ignore = true)
    @Mapping(target = "equipements", ignore = true)
    Vehicule fromDTO(VehiculeDTO vehiculeDTO);

    List<VehiculeDTO> toDTOList(List<Vehicule> vehicules);
    List<Vehicule> fromDTOList(List<VehiculeDTO> vehiculeDTOs);
}