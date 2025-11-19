package com.aplm.gdois.anunciosloc.anunciosloc.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aplm.gdois.anunciosloc.anunciosloc.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByCreatedBy(Long userId);
    List<Location> findByType(String type); // GPS ou WIFI
}