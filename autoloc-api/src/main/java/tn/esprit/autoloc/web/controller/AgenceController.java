package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.service.IAgenceService;
import tn.esprit.autoloc.web.ErrorResponse;
import tn.esprit.autoloc.web.dto.AgenceDTO;
import tn.esprit.autoloc.web.dto.AgenceMapper;

import java.util.List;

@RestController
@RequestMapping("/agences")
@RequiredArgsConstructor
@Tag(name = "Agences", description = "Gestion des agences de location")
public class AgenceController {
    private final IAgenceService agenceService;
    private final AgenceMapper agenceMapper;

    @Operation(summary = "Lister les agences",
            description = "Retourne la liste complète des agences")
    @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    @GetMapping
    public List<AgenceDTO> findAll() {
        return agenceMapper.toDTOList(agenceService.findAll());
    }

    @Operation(summary = "Consulter une agence",
            description = "Retourne l'agence correspondant à l'identifiant")
    @ApiResponse(responseCode = "200", description = "Agence trouvée")
    @ApiResponse(responseCode = "404", description = "Agence inexistante",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")                                  // Q19
    public AgenceDTO findById(
            @Parameter(description = "Identifiant de l'agence", example = "1")
            @PathVariable Long id) {
        return agenceMapper.toDTO(agenceService.findById(id));
    }

    @Operation(summary = "Ajouter une agence",
            description = "L'identifiant est généré automatiquement et ne doit pas être fourni")
    @ApiResponse(responseCode = "201", description = "Agence créée")
    @ApiResponse(responseCode = "400", description = "Données invalides ou identifiant fourni",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping                                          // Q21
    @ResponseStatus(HttpStatus.CREATED)
    public AgenceDTO create(@Valid @RequestBody AgenceDTO agenceDTO) {
        Agence agence = agenceMapper.fromDTO(agenceDTO);
        Agence creee = agenceService.create(agence);
        return agenceMapper.toDTO(creee);
    }

    @Operation(summary = "Modifier une agence",
            description = "Met à jour l'agence correspondant à l'identifiant")
    @ApiResponse(responseCode = "200", description = "Agence modifiée")
    @ApiResponse(responseCode = "400", description = "Données invalides",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Agence inexistante",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")                                  // Q24
    public AgenceDTO update(
            @Parameter(description = "Identifiant de l'agence", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody AgenceDTO agenceDTO) {
        Agence agence = agenceMapper.fromDTO(agenceDTO);
        Agence modifiee = agenceService.update(id, agence);
        return agenceMapper.toDTO(modifiee);
    }

    @Operation(summary = "Supprimer une agence",
            description = "Supprime l'agence. Les véhicules rattachés doivent d'abord être désaffectés")
    @ApiResponse(responseCode = "204", description = "Agence supprimée")
    @ApiResponse(responseCode = "404", description = "Agence inexistante",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")                               // Q25
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant de l'agence", example = "1")
            @PathVariable Long id) {
        agenceService.deleteById(id);
    }

    @GetMapping("/{id}/simuler")
    public String simuler(@PathVariable Long id,
                          @RequestParam int nbJours,
                          @RequestParam VehiculeOffre offre) {
        return agenceService.simuler(agenceService.findById(id), nbJours, offre);
    }

    @PutMapping("/{id}/employes")
    public AgenceDTO ajouterEmployes(@PathVariable Long id, @Valid @RequestBody AgenceDTO agenceDTO) {
        Agence agence = agenceMapper.fromDTO(agenceDTO);
        Agence miseAJour = agenceService.ajouterEmployes(id, agence);
        return agenceMapper.toDTO(miseAJour);
    }
}