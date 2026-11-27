package tn.esprit.autoloc.web.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeMapper {

    @Mapping(source = "agence.idAgence", target = "idAgence")
    EmployeDTO toDTO(Employe employe);

    @Mapping(source = "idAgence", target = "agence", qualifiedByName = "agenceDepuisId")
    Employe fromDTO(EmployeDTO employeDTO);

    List<EmployeDTO> toDTOList(List<Employe> employes);
    List<Employe> fromDTOList(List<EmployeDTO> employeDTOs);

    @Named("agenceDepuisId")
    default Agence agenceDepuisId(Long idAgence) {
        if (idAgence == null) {
            return null;
        }
        Agence agence = new Agence();
        agence.setIdAgence(idAgence);
        return agence;
    }
}