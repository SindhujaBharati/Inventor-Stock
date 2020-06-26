package com.cognizant.stock.service;

import java.util.List;

import com.cognizant.stock.domain.StockEntity;
import com.cognizant.stock.dto.StockRequestDto;
import com.cognizant.stock.exception.ResourceNotFoundException;

public interface StockService {

	StockEntity createStock(StockRequestDto stockRequest);

	StockEntity save(StockRequestDto stockRequest);

	List<StockEntity> findAll();

	StockEntity getStockById(Long stockId) throws ResourceNotFoundException;

	StockEntity findByName(String stockName);

	StockEntity updateStockWithoutPurchase(Long stockeId, StockRequestDto stockDetails) throws ResourceNotFoundException;

	StockEntity updateStockWithPurchase(Long stockeId, StockRequestDto stockDetails) throws ResourceNotFoundException;

	void deleteStock(Long stockId) throws ResourceNotFoundException;

}
