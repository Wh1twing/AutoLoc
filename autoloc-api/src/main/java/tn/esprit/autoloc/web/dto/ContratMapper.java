package tn.esprit.autoloc.web.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autoloc.domain.Contrat;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContratMapper {
    ContratDTO toDTO(Contrat contrat);

    @Mapping(target = "paiements", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    Contrat fromDTO(ContratDTO contratDTO);

    List<ContratDTO> toDTOList(List<Contrat> contrats);
    List<Contrat> fromDTOList(List<ContratDTO> contratDTOs);
}