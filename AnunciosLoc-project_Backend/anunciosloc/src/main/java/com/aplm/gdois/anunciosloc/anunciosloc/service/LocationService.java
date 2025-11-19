package com.aplm.gdois.anunciosloc.anunciosloc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aplm.gdois.anunciosloc.anunciosloc.entity.Location;
import com.aplm.gdois.anunciosloc.anunciosloc.repository.LocationRepository;

import jakarta.transaction.Transactional;

@Service
public class LocationService {

    @Autowired private LocationRepository locationRepo;

    @Transactional
    public Location criar(Location location, Long userId) {
        validarLocal(location);
        location.setCreatedBy(userId);
        return locationRepo.save(location);
    }

    public List<Location> meusLocais(Long userId) {
        return locationRepo.findByCreatedBy(userId);
    }

    public List<Location> todos() {
        return locationRepo.findAll();
    }

    public Location buscarPorId(Long id) {
        return locationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Local não encontrado: " + id));
    }

    @Transactional
    public Location atualizar(Long id, Location novo, Long userId) {
        Location existente = buscarPorId(id);
        if (!existente.getCreatedBy().equals(userId)) {
            throw new SecurityException("Só o criador pode editar");
        }
        validarLocal(novo);
        existente.setName(novo.getName());
        existente.setType(novo.getType());
        existente.setLatitude(novo.getLatitude());
        existente.setLongitude(novo.getLongitude());
        existente.setRadius(novo.getRadius());
        existente.setWifiSsids(novo.getWifiSsids());
        return locationRepo.save(existente);
    }

    @Transactional
    public void apagar(Long id, Long userId) {
        Location loc = buscarPorId(id);
        if (!loc.getCreatedBy().equals(userId)) {
            throw new SecurityException("Só o criador pode apagar");
        }
        locationRepo.delete(loc);
    }

    private void validarLocal(Location loc) {
        if (loc.getName() == null || loc.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if ("GPS".equalsIgnoreCase(loc.getType())) {
            if (loc.getLatitude() == null || loc.getLongitude() == null || loc.getRadius() == null) {
                throw new IllegalArgumentException("GPS precisa de coordenadas e raio");
            }
            loc.setWifiSsids(null);
        } else if ("WIFI".equalsIgnoreCase(loc.getType())) {
            if (loc.getWifiSsids() == null || loc.getWifiSsids().length == 0) {
                throw new IllegalArgumentException("WiFi precisa de pelo menos um SSID");
            }
            loc.setLatitude(null);
            loc.setLongitude(null);
            loc.setRadius(null);
        } else {
            throw new IllegalArgumentException("Tipo deve ser GPS ou WIFI");
        }
    }
}