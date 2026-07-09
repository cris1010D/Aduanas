package cl.triskeledu.aduanas.notaria.controller;

import cl.triskeledu.aduanas.notaria.dto.DocumentoRequest;
import cl.triskeledu.aduanas.notaria.dto.DocumentoResponse;
import cl.triskeledu.aduanas.notaria.service.DocumentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Documentos Notariales", description = "Documentos legales asociados a poderes notariales")
@RestController
@RequestMapping("/api/v1/documentos")
@RequiredArgsConstructor
public class DocumentoController {

    private final DocumentoService documentoService;

    @Operation(summary = "Listar todos los documentos notariales")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<DocumentoResponse>>> listarTodos() {
        List<EntityModel<DocumentoResponse>> items = documentoService.listarTodos().stream()
                .map(d -> EntityModel.of(d,
                        linkTo(methodOn(DocumentoController.class).buscarPorId(d.getId())).withSelfRel(),
                        linkTo(methodOn(DocumentoController.class).listarTodos()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(DocumentoController.class).listarTodos()).withSelfRel()));
    }

    @Operation(summary = "Buscar documento notarial por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Documento encontrado"),
        @ApiResponse(responseCode = "404", description = "Documento no encontrado")
    })
    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<DocumentoResponse>> buscarPorId(@PathVariable Integer id) {
        DocumentoResponse response = documentoService.buscarPorId(id);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(DocumentoController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(DocumentoController.class).listarTodos()).withRel("collection"),
                linkTo(methodOn(DocumentoController.class).listarPorPoder(response.getIdPoder())).withRel("por-poder")));
    }

    @Operation(summary = "Listar documentos asociados a un poder notarial")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de documentos del poder")
    })
    @GetMapping("/poder/{idPoder}")
    @SuppressWarnings("null")
    public ResponseEntity<CollectionModel<EntityModel<DocumentoResponse>>> listarPorPoder(@PathVariable Integer idPoder) {
        List<EntityModel<DocumentoResponse>> items = documentoService.listarPorPoder(idPoder).stream()
                .map(d -> EntityModel.of(d,
                        linkTo(methodOn(DocumentoController.class).buscarPorId(d.getId())).withSelfRel(),
                        linkTo(methodOn(DocumentoController.class).listarTodos()).withRel("collection")))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(items,
                linkTo(methodOn(DocumentoController.class).listarPorPoder(idPoder)).withSelfRel()));
    }

    @Operation(summary = "Registrar nuevo documento notarial")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Documento registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    @SuppressWarnings("null")
    public ResponseEntity<EntityModel<DocumentoResponse>> crear(@Valid @RequestBody DocumentoRequest request) {
        DocumentoResponse response = documentoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(response,
                linkTo(methodOn(DocumentoController.class).buscarPorId(response.getId())).withSelfRel(),
                linkTo(methodOn(DocumentoController.class).listarTodos()).withRel("collection")));
    }

    @Operation(summary = "Eliminar documento notarial")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Documento eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Documento no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        documentoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
