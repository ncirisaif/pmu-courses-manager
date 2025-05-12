package com.pmu.courses_manager.application.api;


import com.pmu.courses_manager.application.api.request.CreateCourseRequest;
import com.pmu.courses_manager.application.api.request.AddPartantRequest;
import com.pmu.courses_manager.application.api.response.CreateCourseResponse;
import com.pmu.courses_manager.application.api.dto.PartantDto;
import com.pmu.courses_manager.application.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Interface de documentation pour l'API de gestion des courses
 */
@Tag(name = "Courses", description = "API pour la gestion des courses sportives et des partants")
public interface CourseResourcesApi {

    /**
     * Crée une nouvelle course
     */
    @Operation(
            summary = "Crée une nouvelle course",
            description = "Crée une nouvelle course avec un nom, une date et un numéro. " +
                    "Le numéro doit être unique pour la date donnée."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Course créée avec succès",
                    content = @Content(schema = @Schema(implementation = CreateCourseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données de requête invalides",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Une course avec cette date et ce numéro existe déjà",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<CreateCourseResponse> createCourse(@Valid @RequestBody CreateCourseRequest courseRequestDto);

    /**
     * Récupère une course par son identifiant
     */
    @Operation(
            summary = "Récupère une course par son ID",
            description = "Renvoie les détails d'une course spécifique identifiée par son ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Course récupérée avec succès",
                    content = @Content(schema = @Schema(implementation = CreateCourseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course non trouvée",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<CreateCourseResponse> getCourseById(
            @Parameter(description = "ID de la course à récupérer") @PathVariable Long id);

    /**
     * Ajoute un participant à une course
     */
    @Operation(
            summary = "Ajoute un participant à une course",
            description = "Ajoute un nouveau participant à une course existante. " +
                    "Le numero doit être unique dans la course."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Participant ajouté avec succès",
                    content = @Content(schema = @Schema(implementation = PartantDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données de requête invalides",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course non trouvée",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Dossard déjà utilisé dans cette course",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<PartantDto> addParticipant(
            @Parameter(description = "ID de la course") @PathVariable Long courseId,
            @Valid @RequestBody AddPartantRequest request);
}