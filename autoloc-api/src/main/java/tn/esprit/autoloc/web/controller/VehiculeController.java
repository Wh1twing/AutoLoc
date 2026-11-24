package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.web.ErrorResponse;
import tn.esprit.autoloc.web.dto.VehiculeDTO;
import tn.esprit.autoloc.web.dto.VehiculeMapper;

import java.util.List;

@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
@Tag(name = "Véhicules", description = "Gestion des vehicules")
public class VehiculeController {
    private final IVehiculeService vehiculeService;
    private final VehiculeMapper vehiculeMapper;

    @Operation(summary = "Lister les vehicules",
            description = "Retourne la liste complète des vehicules")
    @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    @GetMapping
    public List<VehiculeDTO> findAll() {
        return vehiculeMapper.toDTOList(vehiculeService.findAll());
    }

    @Operation(summary = "Consulter un véhicule",
            description = "Retourne le véhicule correspondant à l'identifiant")
    @ApiResponse(responseCode = "200", description = "Véhicule trouvé")
    @ApiResponse(responseCode = "404", description = "Véhicule inexistant",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public VehiculeDTO findById(
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable Long id) {
        return vehiculeMapper.toDTO(vehiculeService.findById(id));
    }

    @Operation(summary = "Ajouter un véhicule",
            description = "L'identifiant est généré automatiquement et ne doit pas être fourni")
    @ApiResponse(responseCode = "201", description = "Véhicule créé")
    @ApiResponse(responseCode = "400", description = "Données invalides ou identifiant fourni",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehiculeDTO create(@RequestBody VehiculeDTO vehiculeDTO) {
        Vehicule vehicule = vehiculeMapper.fromDTO(vehiculeDTO);
        return vehiculeMapper.toDTO(vehiculeService.create(vehicule));
    }

    @Operation(summary = "Modifier un véhicule",
            description = "Met à jour le véhicule correspondant à l'identifiant")
    @ApiResponse(responseCode = "200", description = "Véhicule modifié")
    @ApiResponse(responseCode = "400", description = "Données invalides",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Véhicule inexistant",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    public VehiculeDTO update(
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable Long id,
            @RequestBody VehiculeDTO vehiculeDTO) {
        Vehicule vehicule = vehiculeMapper.fromDTO(vehiculeDTO);
        return vehiculeMapper.toDTO(vehiculeService.update(id, vehicule));
    }

    @Operation(summary = "Supprimer un véhicule",
            description = "Supprime le véhicule correspondant à l'identifiant")
    @ApiResponse(responseCode = "204", description = "Véhicule supprimé")
    @ApiResponse(responseCode = "404", description = "Véhicule inexistant",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable Long id) {
        vehiculeService.deleteById(id);
    }

    @PutMapping("desaffecter/{id}")
    public VehiculeDTO desaffecter(@PathVariable Long id) {
        return vehiculeMapper.toDTO(vehiculeService.desaffecter(id));
    }

    @PutMapping("affecter/{id}/{idAgence}")
    public VehiculeDTO affecter(@PathVariable Long id, @PathVariable Long idAgence) {
        return vehiculeMapper.toDTO(vehiculeService.affecter(id, idAgence));
    }

    @PutMapping("affecter_equipement/{id}/{idEquipement}")
    public VehiculeDTO affecterEquipement(@PathVariable Long id, @PathVariable Long idEquipement) {
        return vehiculeMapper.toDTO(vehiculeService.affecterEquipement(id, idEquipement));
    }
}