package tn.esprit.autoloc.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.autoloc.domain.StatutReservation;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private Long idReservation;

    @NotNull(message = "Champ 'date de début' obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "Champ 'date de fin' obligatoire")
    private LocalDate dateFin;

    @NotNull(message = "Champ 'statut' obligatoire")
    private StatutReservation statut;

    @NotNull(message = "Champ 'véhicule' obligatoire")
    private Long idVehicule;

    @NotNull(message = "Champ 'client' obligatoire")
    private Long idClient;

    private Long idContrat;

    @JsonIgnore
    @AssertTrue(message = "La date de fin doit être postérieure ou égale à la date de début")
    public boolean isPeriodeValide() {
        return dateDebut == null || dateFin == null || !dateFin.isBefore(dateDebut);
    }
}