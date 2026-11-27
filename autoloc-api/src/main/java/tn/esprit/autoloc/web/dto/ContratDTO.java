package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContratDTO {

    private Long idContrat;

    @NotNull(message = "Champ 'date de signature' obligatoire")
    private LocalDate dateSignature;

    @NotNull(message = "Champ 'montant total' obligatoire")
    @Positive(message = "Le montant total doit être strictement positif")
    @Digits(integer = 8, fraction = 2, message = "Le montant doit avoir au plus 8 chiffres et 2 décimales")
    private BigDecimal montantTotal;

    private boolean valide;
}