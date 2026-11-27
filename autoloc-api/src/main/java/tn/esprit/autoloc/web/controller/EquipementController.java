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
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.service.IGenericService;
import tn.esprit.autoloc.web.ErrorResponse;
import tn.esprit.autoloc.web.dto.EquipementDTO;
import tn.esprit.autoloc.web.dto.EquipementMapper;

import java.util.List;

@RestController
@RequestMapping("/equipements")
@RequiredArgsConstructor
@Tag(name = "Équipements", description = "Gestion des equipements")
public class EquipementController {
    private final IGenericService<Equipement, Long> equipementService;
    private final EquipementMapper equipementMapper;

    @Operation(summary = "Lister les equipements",
            description = "Retourne la liste complète des equipements")
    @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    @GetMapping
    public List<EquipementDTO> findAll() {
        return equipementMapper.toDTOList(equipementService.findAll());
    }

    @Operation(summary = "Consulter un équipement",
            description = "Retourne le équipement correspondant à l'identifiant")
    @ApiResponse(responseCode = "200", description = "Équipement trouvé")
    @ApiResponse(responseCode = "404", description = "Équipement inexistant",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public EquipementDTO findById(
            @Parameter(description = "Identifiant du équipement", example = "1")
            @PathVariable Long id) {
        return equipementMapper.toDTO(equipementService.findById(id));
    }

    @Operation(summary = "Ajouter un équipement",
            description = "L'identifiant est généré automatiquement et ne doit pas être fourni")
    @ApiResponse(responseCode = "201", description = "Équipement créé")
    @ApiResponse(responseCode = "400", description = "Données invalides ou identifiant fourni",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EquipementDTO create(@Valid @RequestBody EquipementDTO equipementDTO) {
        Equipement equipement = equipementMapper.fromDTO(equipementDTO);
        return equipementMapper.toDTO(equipementService.create(equipement));
    }

    @Operation(summary = "Modifier un équipement",
            description = "Met à jour le équipement correspondant à l'identifiant")
    @ApiResponse(responseCode = "200", description = "Équipement modifié")
    @ApiResponse(responseCode = "400", description = "Données invalides",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Équipement inexistant",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    public EquipementDTO update(
            @Parameter(description = "Identifiant du équipement", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody EquipementDTO equipementDTO) {
        Equipement equipement = equipementMapper.fromDTO(equipementDTO);
        return equipementMapper.toDTO(equipementService.update(id, equipement));
    }

    @Operation(summary = "Supprimer un équipement",
            description = "Supprime le équipement correspondant à l'identifiant")
    @ApiResponse(responseCode = "204", description = "Équipement supprimé")
    @ApiResponse(responseCode = "404", description = "Équipement inexistant",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant du équipement", example = "1")
            @PathVariable Long id) {
        equipementService.deleteById(id);
    }
}