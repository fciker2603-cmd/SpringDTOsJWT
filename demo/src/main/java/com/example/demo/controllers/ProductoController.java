package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Producto;
import com.example.demo.repo.productoRepo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import org.springframework.http.ResponseEntity;



@RestController
@Tag(name = "Productos", description = "Operaciones para consultar y administrar productos")
@SecurityRequirement(name = "bearerAuth")
public class ProductoController {
  
@Autowired
productoRepo repo;

@GetMapping("productos")
@SecurityRequirements
  @Operation(summary = "Listar productos")
   public ResponseEntity<List<Producto>> findAllProductos() {
     return ResponseEntity.ok(repo.findAll());
  }

   @PostMapping("producto")
  @Operation(summary = "Crear producto")
public ResponseEntity<Producto> addProducto(@RequestBody Producto producto) {
    producto.setId(null);
    Producto saved = repo.save(producto);
    URI location = URI.create("/producto/" + saved.getId());
    return ResponseEntity.created(location).body(saved);
  }


  @GetMapping("producto/{id}")
  @SecurityRequirements
  @Operation(summary = "Buscar producto por ID")
  public ResponseEntity<Producto> getById(@PathVariable String id) {
    return repo.findById(id)
        .map(value -> ResponseEntity.ok(value))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping("producto/{id}")
  @Operation(summary = "Reemplazar producto")
 public ResponseEntity<Producto> putProduct(@PathVariable String id, @RequestBody Producto product) {
    if (!repo.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    product.setId(id);
    return ResponseEntity.ok(repo.save(product));
  }

  @DeleteMapping("producto/{id}")
  @Operation(summary = "Eliminar producto")
  public ResponseEntity<Void> deleteProducto(@PathVariable("id") String id) {
    if (!repo.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    repo.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}

