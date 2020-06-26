package com.cognizant.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognizant.stock.domain.StockEntity;

@Repository
public interface StockRepository extends JpaRepository<StockEntity,Long>{
	
	@Query("SELECT s from StockEntity s WHERE s.stockName = ?1")
    StockEntity findByName(String stockName);
}
