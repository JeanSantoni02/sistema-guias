package com.transportista.sistemaguias.repository;

import com.transportista.sistemaguias.model.GuiaOracle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuiaOracleRepository extends JpaRepository<GuiaOracle, Long> {
}