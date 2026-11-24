package tn.esprit.autoloc.web.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autoloc.domain.Employe;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeMapper {
    @Mapping(source = "agence.idAgence", target = "idAgence")
    EmployeDTO toDTO(Employe employe);

    @Mapping(source = "idAgence", target = "agence.idAgence")
    Employe fromDTO(EmployeDTO employeDTO);

    List<EmployeDTO> toDTOList(List<Employe> employes);
    List<Employe> fromDTOList(List<EmployeDTO> employeDTOs);
}