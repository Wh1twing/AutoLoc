package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehiculeDTO {

    @NotBlank(message = "Champ 'immatriculation' obligatoire")
    @Size(max = 20, message = "L'immatriculation ne doit pas dépasser 20 caractères")
    private String immatriculation;

    @NotBlank(message = "Champ 'marque' obligatoire")
    @Size(max = 50, message = "La marque ne doit pas dépasser 50 caractères")
    private String marque;

    @NotBlank(message = "Champ 'modèle' obligatoire")
    @Size(max = 50, message = "Le modèle ne doit pas dépasser 50 caractères")
    private String modele;

    @NotNull(message = "Champ 'catégorie' obligatoire")
    private CategorieVehicule categorie;

    @NotNull(message = "Champ 'tarif journalier' obligatoire")
    @Positive(message = "Le tarif journalier doit être strictement positif")
    @Digits(integer = 8, fraction = 2, message = "Le tarif journalier doit avoir au plus 8 chiffres et 2 décimales")
    private BigDecimal tarifJournalier;

    @NotNull(message = "Champ 'statut' obligatoire")
    private StatutVehicule statut;
}