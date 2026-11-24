package tn.esprit.autoloc.web.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autoloc.domain.Reservation;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    @Mapping(source = "vehicule.idVehicule", target = "idVehicule")
    @Mapping(source = "client.idClient", target = "idClient")
    @Mapping(source = "contrat.idContrat", target = "idContrat")
    ReservationDTO toDTO(Reservation reservation);

    @Mapping(source = "idVehicule", target = "vehicule.idVehicule")
    @Mapping(source = "idClient", target = "client.idClient")
    @Mapping(source = "idContrat", target = "contrat.idContrat")
    Reservation fromDTO(ReservationDTO reservationDTO);

    List<ReservationDTO> toDTOList(List<Reservation> reservations);
    List<Reservation> fromDTOList(List<ReservationDTO> reservationDTOs);
}