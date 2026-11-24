package tn.esprit.autoloc.web.dto;

import org.mapstruct.Mapper;
import tn.esprit.autoloc.domain.Agence;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AgenceMapper {
    AgenceDTO toDTO(Agence agence);
    Agence fromDTO(AgenceDTO agenceDTO);
    List<AgenceDTO> toDTOList(List<Agence> agences);
    List<Agence> fromDTOList(List<AgenceDTO> agenceDTOs);
}