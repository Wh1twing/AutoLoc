package tn.esprit.autoloc.web.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autoloc.domain.Client;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDTO toDTO(Client client);

    @Mapping(target = "reservations", ignore = true)
    Client fromDTO(ClientDTO clientDTO);

    List<ClientDTO> toDTOList(List<Client> clients);
    List<Client> fromDTOList(List<ClientDTO> clientDTOs);
}