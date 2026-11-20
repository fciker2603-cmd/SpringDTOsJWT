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

import com.example.demo.model.Comment;
import com.example.demo.model.Producto;
import com.example.demo.repo.comentarioRepo;
import com.example.demo.repo.productoRepo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import org.springframework.http.ResponseEntity;


@RestController
Tag(name = "Comentarios", description = "Operaciones para consultar y administrar comentarios")
@SecurityRequirement(name = "bearerAuth")
public class ComentarioController {

@Autowired
comentarioRepo repo;

@Autowired
productoRepo repoProducto;

@GetMapping("comentarios")
  @SecurityRequirements
  @Operation(summary = "Listar comentarios")
  public ResponseEntity<List<Comment>> findAllcomentarios() {
    return ResponseEntity.ok(repo.finAll());
  }
  
@PostMapping("comentario")
 @Operation(summary = "Crear comentario")
  public ResponseEntity<Comment> addcomentario(@RequestBody Comment comentario) {
    comentario.setId(null);
    Comment saved = repo.save(comentario);
    URI location = URI.create("/comentario/" + saved.getId());
    return ResponseEntity.created(location).body(saved);
  }

@PostMapping("comentaio/{productID}")
@Operation (summary = "Crear comentario para un producto")
public ResponseEntity<Comment> addcomentario(
  @RequestBody Comment comentario, @PathVariable String productId) {
    return repoProducto.findById(productId)
    .map(producto ->{
      comentario.setId(null);
      comentario.setProducto(producto);
      Comment saved = repo.save(comentario);
       URI location = URI.create("/comentario/" + saved.getId());
       return ResponseEntity.created(location).body(saved);
    })
    .orElseGet(() -> ResponseEntity.notFound().build());  
  }

  @GetMapping("comentario/producto/{productId}")
  @SecurityRequirements 
  @Operation(summary = "Listar comentarios asociados a un producto")
  public ResponseEntity<Set<Comment>> getCommentsByProduct(@PathVariable String productId) {
    return repoProducto.findById(productId)
    .map(p -> ResponseEntity.ok(p.getComentarios()))
    .orElseGet(() -> ResponseEntity.notFound().build());
  }
  


@GetMapping("comentario/{id}")
  @SecurityRequirements
  @Operation(summary = "Buscar comentario por ID")
public ResponseEntity<Comment> getById(@PathVariable String id) {
  return repo.findById(id)
  .map(value -> ResponseEntity.ok(value))
  .orElseGet(() -> ResponseEntity.notFound().build());
  }


@PutMapping("comentario/{id}")
  @Operation(summary = "Reemplazar comentario")
public ResponseEntity<Comment> putComentario(
  @PathVariable String id, @RequestBody Comment comentario) {
 if (!repo.existsById(id)) {
  return ResponseEntity.notFound().build();
 }
 comentario.setId(id);
 return ResponseEntity.ok(repo.save(comentario));

  }

@DeleteMapping("comentario/{id}")
@Operation(summary = "Eliminar comentario")
public ResponseEntity<Comment> deleteComentario(@PathVariable String id) {
    if (!repo.existsById(id)) {
        return ResponseEntity.notFound().build();
    }
    repo.deleteById(id);
    return ResponseEntity.ok().build();

  }
}
