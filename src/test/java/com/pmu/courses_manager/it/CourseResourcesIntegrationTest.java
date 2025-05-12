package com.pmu.courses_manager.it;

import com.pmu.courses_manager.application.api.dto.PartantDto;
import com.pmu.courses_manager.application.api.request.CreateCourseRequest;
import com.pmu.courses_manager.application.api.request.AddPartantRequest;
import com.pmu.courses_manager.application.api.response.CreateCourseResponse;
import com.pmu.courses_manager.application.exception.ErrorResponse;
import com.pmu.courses_manager.conf.TestcontainersConfiguration;
import com.pmu.courses_manager.domain.model.CourseId;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.kafka.KafkaContainer;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
public class CourseResourcesIntegrationTest {
    private static final CourseId ID_VALIDE = new CourseId(1L);
    private static final String NOM_VALIDE = "Course de Test";
    private static final LocalDate DATE_VALIDE = LocalDate.of(2025, 5, 5);
    private static final Integer NUMERO_VALIDE = 1;
    private static final List<String> PARTANTS_VALIDES = List.of("Cheval 1", "Cheval 2", "Cheval 3");
    private static final Logger log = LoggerFactory.getLogger(String.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private KafkaContainer kafkaContainer;

    @Autowired
    private PostgreSQLContainer<?> postgresContainer;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void cleanDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE partants CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE courses CASCADE");
    }
    @BeforeAll
    void setUp() {
        // Verify containers are running
        Assertions.assertTrue(kafkaContainer.isRunning());
        Assertions.assertTrue(postgresContainer.isRunning());
    }

    @Test
    @DisplayName("Doit créer une course avec succès")
    void shouldCreateCourseSuccessfully() {
        // Given
        CreateCourseRequest request = new CreateCourseRequest(
                NOM_VALIDE,
                DATE_VALIDE,
                NUMERO_VALIDE,
                PARTANTS_VALIDES
        );

        // When
        ResponseEntity<CreateCourseResponse> response = restTemplate.postForEntity(
                "/api/courses",
                request,
                CreateCourseResponse.class
        );

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(NOM_VALIDE, response.getBody().nom());
        assertEquals(NUMERO_VALIDE, response.getBody().numero());
        assertEquals(3, response.getBody().partants().size());
        List<PartantDto> partants = response.getBody().partants();
        assertThat(partants)
                .extracting(PartantDto::nom)
                .containsAll(PARTANTS_VALIDES);

        List<Integer> numeros = partants.stream()
                .map(PartantDto::numero)
                .sorted()
                .collect(Collectors.toList());
        List<Integer> expectedSequence = IntStream.rangeClosed(
                        Collections.min(numeros),
                        Collections.max(numeros))
                .boxed()
                .collect(Collectors.toList());

        assertThat(numeros).isEqualTo(expectedSequence);
    }

    @Test
    @DisplayName("Doit renvoyer 409 si moins de 3 partants")
    void shouldThrowIllegalArgumentException() {
        // Given
        CreateCourseRequest request = new CreateCourseRequest(
                NOM_VALIDE,
                DATE_VALIDE,
                null,
                List.of("Cheval 1", "Cheval 2")
        );

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/courses",
                request,
                ErrorResponse.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getCode()).isEqualTo("ILLEGAL_ARGUMENT");
        assertThat(response.getBody().getMessage()).contains("au minimum 3 partants");    }

    @Test
    @DisplayName("Doit récupérer une course par son ID")
    void shouldGetCourseById() {
        // Given
        CreateCourseRequest request = new CreateCourseRequest(
                NOM_VALIDE,
                DATE_VALIDE,
                NUMERO_VALIDE,
                PARTANTS_VALIDES
        );

        ResponseEntity<CreateCourseResponse> createResponse = restTemplate.postForEntity(
                "/api/courses",
                request,
                CreateCourseResponse.class
        );

        Long courseId = createResponse.getBody().id();
        String url = "/api/courses/" + courseId;

        // When
        ResponseEntity<CreateCourseResponse> response = restTemplate.getForEntity(
                url,
                CreateCourseResponse.class
        );

        // Then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(NOM_VALIDE, response.getBody().nom());
    }

    @Test
    @DisplayName("Doit ajouter un participant avec succès")
    void shouldAddParticipantToCourse() {
        // Given - Create a course first
        CreateCourseRequest request = new CreateCourseRequest(
                NOM_VALIDE,
                DATE_VALIDE,
                NUMERO_VALIDE,
                PARTANTS_VALIDES
        );

        ResponseEntity<CreateCourseResponse> createCourseResponse = restTemplate.postForEntity(
                "/api/courses",
                request,
                CreateCourseResponse.class
        );

        Long courseId = createCourseResponse.getBody().id();

        AddPartantRequest partantRequest = new AddPartantRequest("Nouveau Cheval");

        // When
        ResponseEntity<PartantDto> response = restTemplate.postForEntity(
                "/api/courses/" + courseId + "/partants",
                partantRequest,
                PartantDto.class
        );

        // Then
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals("Nouveau Cheval", response.getBody().nom());
        int nextPartantNumero = createCourseResponse.getBody().partants().stream()
                .mapToInt(PartantDto::numero)
                .max().getAsInt() + 1;
        assertEquals(nextPartantNumero, response.getBody().numero());
    }


    @Test
    @DisplayName("Doit renvoyer 404 si course n'existe pas")
    void shouldReturnNotFoundForNonExistentCourse() {
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/courses/99999", // Assuming this ID doesn't exist
                String.class
        );

        // Then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}