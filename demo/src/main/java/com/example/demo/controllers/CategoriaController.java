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

import com.example.demo.model.Department;
import com.example.demo.model.Producto;
import com.example.demo.repo.categoriaRepo;
import com.example.demo.repo.productoRepo;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import org.springframework.http.ResponseEntity;


@RestController
@Tag(name = "Categorías", description = "Operaciones para consultar y administrar categorías")
@SecurityRequirement(name = "bearerAuth")
public class CategoriaController {

@Autowired
categoriaRepo repo;

@Autowired
productoRepo repoProducto;

@GetMapping("categorias")
   @SecurityRequirements
  @Operation(summary = "Listar categorías")
 public ResponseEntity<List<Department>> findAllcategoria() {
    return ResponseEntity.ok(repo.findAll());
  }

@PostMapping("categoria")
 @Operation(summary = "Crear categoría")
   public ResponseEntity<Department> addcategoria(@RequestBody Department categoria) {
    categoria.setId(null);
    Department saved = repo.save(categoria);
    URI location = URI.create("/categoria/" + saved.getId());
    return ResponseEntity.created(location).body(saved);
  }

  @PostMapping("addProductoCategoria/{productId}/{categoriaId}")
  @Operation(summary = "Asociar un producto con una categoría")
  public ResponseEntity<Department> addProductoCategoria(
      @PathVariable String productId, @PathVariable String categoriaId) {
    Optional<Producto> p = repoProducto.findById(productId);
    Optional<Department> d = repo.findById(categoriaId);
    if (p.isEmpty() || d.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    d.get().getProductos().add(p.get());
    return ResponseEntity.ok(repo.save(d.get()));
  }

  @GetMapping("categoria/producto/{productId}")
  @SecurityRequirements
  @Operation(summary = "Listar categorías asociadas a un producto")
  public ResponseEntity<Set<Department>> getDepartmentsByProduct(@PathVariable String productId) {
    return repoProducto.findById(productId)
        .map(p -> ResponseEntity.ok(p.getDeparments()))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

@GetMapping("categoria/{id}")
  @SecurityRequirements
  @Operation(summary = "Buscar categoría por ID")
  public ResponseEntity<Department> getById(@PathVariable String id) {
    return repo.findById(id)
        .map(value -> ResponseEntity.ok(value))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

@PutMapping("categoria/{id}")
 @Operation(summary = "Reemplazar categoría")
  public ResponseEntity<Department> putDepartment(
      @PathVariable String id, @RequestBody Department categoria) {
    if (!repo.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    categoria.setId(id);
    return ResponseEntity.ok(repo.save(categoria));
  }


@DeleteMapping("categoria/{id}")
  @Operation(summary = "Eliminar categoría")
  public ResponseEntity<Void> deleteDeparment(@PathVariable String id) {
    if (!repo.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    repo.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}