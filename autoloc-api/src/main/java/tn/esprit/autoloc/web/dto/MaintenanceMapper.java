package tn.esprit.autoloc.web.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autoloc.domain.Maintenance;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MaintenanceMapper {
    @Mapping(source = "vehicule.idVehicule", target = "idVehicule")
    MaintenanceDTO toDTO(Maintenance maintenance);

    @Mapping(source = "idVehicule", target = "vehicule.idVehicule")
    Maintenance fromDTO(MaintenanceDTO maintenanceDTO);

    List<MaintenanceDTO> toDTOList(List<Maintenance> maintenances);
    List<Maintenance> fromDTOList(List<MaintenanceDTO> maintenanceDTOs);
}