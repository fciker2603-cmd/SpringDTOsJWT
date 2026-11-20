package com.example.demo.controllers;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.model.Producto;
import com.example.demo.model.Image;
import com.example.demo.repo.imagenRepo;
import com.example.demo.repo.productoRepo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.net.URI;
import org.springframework.http.ResponseEntity;


@RestController
@Tag(name = "Imágenes", description = "Operaciones para consultar y administrar imágenes")
@SecurityRequirement(name = "bearerAuth")
public class ImagenController {

@Autowired
imagenRepo repo;
@Autowired
productoRepo repoProducto;


@GetMapping("imagenes")
  @SecurityRequirements
  @Operation(summary = "Listar imágenes")
  public ResponseEntity<List<Image>> findAllimagenes() {
    return ResponseEntity.ok(repo.findAll());
  }

@PostMapping("imagen")
  @Operation(summary = "Crear imagen")
public ResponseEntity<Image> addimagen(@RequestBody Image imagen) {
  imagen.setId(null);
  Image saved = repo.save(imagen);
  URI location = URI.create("/imagen/" + saved.getId());
  return ResponseEntity.created(location).body(saved);
  }

  @PostMapping("imagen/{productId}")
  @Operation(summary = "Crear imagen para un producto")
  public ResponseEntity<Image> addimagen(@RequestBody Image imagen, @PathVariable String productId) {
    return repoProducto.findById(productId)
        .map(producto -> {
          imagen.setId(null);
          imagen.setProducto(producto);
          Image saved = repo.save(imagen);
          URI location = URI.create("/imagen/" + saved.getId());
          return ResponseEntity.created(location).body(saved);
        })
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  
  @GetMapping("imagenes/producto/{productId}")
  @SecurityRequirements
  @Operation(summary = "Listar imágenes de un producto")
  public ResponseEntity<Set<Image>> getImagensByProduct(@PathVariable String productId) {
    return repoProducto.findById(productId)
        .map(p -> ResponseEntity.ok(p.getImagenes()))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

@GetMapping("imagen/{id}")
  @SecurityRequirements
  @Operation(summary = "Buscar imagen por ID")
public ResponseEntity<Image> getById(@PathVariable String id) {
  return repo.findById(id)
  .map(value -> ResponseEntity.ok(value))
  .orElseGet(() -> ResponseEntity.notFound().build());
  }


@PutMapping("imagen/{id}")
  @Operation(summary = "Reemplazar imagen")
  public ResponseEntity<Image> putImagen(@PathVariable String id, @RequestBody Image imagen) {
    if (!repo.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    imagen.setId(id);
    return ResponseEntity.ok(repo.save(imagen));
  }

@DeleteMapping("imagen/{id}")
  @Operation(summary = "Eliminar imagen")
public ResponseEntity<Void> deleteImagen(@PathVariable String id) {
  if (!repo.existsById(id)) {
    return ResponseEntity.notFound().build();
  }
  repo.deleteById(id);
  return ResponseEntity.ok().build();
}
}

