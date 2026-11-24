package tn.esprit.autoloc.web.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autoloc.domain.Equipement;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EquipementMapper {
    EquipementDTO toDTO(Equipement equipement);

    @Mapping(target = "vehicules", ignore = true)
    Equipement fromDTO(EquipementDTO equipementDTO);

    List<EquipementDTO> toDTOList(List<Equipement> equipements);
    List<Equipement> fromDTOList(List<EquipementDTO> equipementDTOs);
}