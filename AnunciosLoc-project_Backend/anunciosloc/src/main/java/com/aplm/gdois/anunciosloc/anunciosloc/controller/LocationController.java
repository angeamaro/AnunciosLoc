package com.aplm.gdois.anunciosloc.anunciosloc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aplm.gdois.anunciosloc.anunciosloc.entity.Location;
import com.aplm.gdois.anunciosloc.anunciosloc.service.LocationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*")
public class LocationController {

    @Autowired private LocationService locationService;

    @GetMapping
    public List<Location> listarTodos() {
        return locationService.todos();
    }

    @GetMapping("/meus")
    public List<Location> meus(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("authenticatedUserId");
        return userId != null ? locationService.meusLocais(userId) : List.of();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> buscar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(locationService.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Location location, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("authenticatedUserId");
        if (userId == null) return ResponseEntity.status(401).body("Login necessário");

        try {
            Location salvo = locationService.criar(location, userId);
            return ResponseEntity.ok(salvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Location location, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("authenticatedUserId");
        if (userId == null) return ResponseEntity.status(401).body("Login necessário");

        try {
            Location atualizado = locationService.atualizar(id, location, userId);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagar(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("authenticatedUserId");
        if (userId == null) return ResponseEntity.status(401).body("Login necessário");

        try {
            locationService.apagar(id, userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}