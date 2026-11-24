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
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.service.IGenericService;
import tn.esprit.autoloc.web.ErrorResponse;
import tn.esprit.autoloc.web.dto.ReservationDTO;
import tn.esprit.autoloc.web.dto.ReservationMapper;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "Réservations", description = "Gestion des reservations")
public class ReservationController {
    private final IGenericService<Reservation, Long> reservationService;
    private final ReservationMapper reservationMapper;

    @Operation(summary = "Lister les reservations",
            description = "Retourne la liste complète des reservations")
    @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    @GetMapping
    public List<ReservationDTO> findAll() {
        return reservationMapper.toDTOList(reservationService.findAll());
    }

    @Operation(summary = "Consulter une réservation",
            description = "Retourne la réservation correspondant à l'identifiant")
    @ApiResponse(responseCode = "200", description = "Réservation trouvée")
    @ApiResponse(responseCode = "404", description = "Réservation inexistante",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public ReservationDTO findById(
            @Parameter(description = "Identifiant de la réservation", example = "1")
            @PathVariable Long id) {
        return reservationMapper.toDTO(reservationService.findById(id));
    }

    @Operation(summary = "Ajouter une réservation",
            description = "L'identifiant est généré automatiquement et ne doit pas être fourni")
    @ApiResponse(responseCode = "201", description = "Réservation créée")
    @ApiResponse(responseCode = "400", description = "Données invalides ou identifiant fourni",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationDTO create(@RequestBody ReservationDTO reservationDTO) {
        Reservation reservation = reservationMapper.fromDTO(reservationDTO);
        return reservationMapper.toDTO(reservationService.create(reservation));
    }

    @Operation(summary = "Modifier une réservation",
            description = "Met à jour la réservation correspondant à l'identifiant")
    @ApiResponse(responseCode = "200", description = "Réservation modifiée")
    @ApiResponse(responseCode = "400", description = "Données invalides",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Réservation inexistante",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    public ReservationDTO update(
            @Parameter(description = "Identifiant de la réservation", example = "1")
            @PathVariable Long id,
            @RequestBody ReservationDTO reservationDTO) {
        Reservation reservation = reservationMapper.fromDTO(reservationDTO);
        return reservationMapper.toDTO(reservationService.update(id, reservation));
    }

    @Operation(summary = "Supprimer une réservation",
            description = "Supprime la réservation correspondant à l'identifiant")
    @ApiResponse(responseCode = "204", description = "Réservation supprimée")
    @ApiResponse(responseCode = "404", description = "Réservation inexistante",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant de la réservation", example = "1")
            @PathVariable Long id) {
        reservationService.deleteById(id);
    }
}