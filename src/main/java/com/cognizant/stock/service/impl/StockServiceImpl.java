package com.cognizant.stock.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.stock.controller.StockController;
import com.cognizant.stock.domain.StockEntity;
import com.cognizant.stock.dto.StockRequestDto;
import com.cognizant.stock.exception.ResourceNotFoundException;
import com.cognizant.stock.repository.StockRepository;
import com.cognizant.stock.service.StockService;

@Service
public class StockServiceImpl implements StockService{
	Logger logger = LoggerFactory.getLogger(StockController.class);

	@Autowired
	private StockRepository stockRepository;

	@Override
	public StockEntity createStock(StockRequestDto stockRequest) {
		logger.debug("StockServiceImpl::createStockWithoutPurchase::entry()");
		StockEntity stockEntity = new StockEntity();
		
		stockEntity.setStockName(stockRequest.getStockName());
		stockEntity.setStockType(stockRequest.getStockType());
		stockEntity.setStockCount(stockRequest.getStockCount());
		
		logger.debug("StockServiceImpl::createStockWithoutPurchase::exit()");
		return stockRepository.save(stockEntity);
	}

	@Override
	public StockEntity save(StockRequestDto stockRequest) {
		logger.debug("StockServiceImpl::createStockWithPurchase::entry()");
		StockEntity stockEntity = new StockEntity();
		stockEntity.setStockName(stockRequest.getStockName());
		stockEntity.setStockType(stockRequest.getStockType());
		stockEntity.setStockCount(stockRequest.getStockCount());
		logger.debug("StockServiceImpl::createStockWithPurchase::exit()");
		return stockRepository.save(stockEntity);
	}

	@Override
	public List<StockEntity> findAll() {
		logger.debug("StockServiceImpl::getAllStock::entry()");
		
		logger.debug("StockServiceImpl::getAllStock::exit()");
		
        return stockRepository.findAll();
	}

	@Override
	public StockEntity getStockById(Long stockId) throws ResourceNotFoundException {
		logger.debug("StockServiceImpl::getStockById::entry()");
		
		logger.debug("StockServiceImpl::getStockById::exit()");
		
		return stockRepository.findById(stockId)
				.orElseThrow(() -> new ResourceNotFoundException("Purchase not found for this id :: " + stockId));
	}

	@Override
	public StockEntity findByName(String stockName) {
		logger.debug("StockServiceImpl::getStockByName::entry()");
		
		logger.debug("StockServiceImpl::getStockByName::exit()");
		return stockRepository.findByName(stockName);
	}

	@Override
	public StockEntity updateStockWithoutPurchase(Long stockeId, StockRequestDto stockDetails) throws ResourceNotFoundException {
		logger.debug("StockServiceImpl::updateStockWithoutPurchase::entry()");
		StockEntity stock = stockRepository.findById(stockeId)
				.orElseThrow(() -> new ResourceNotFoundException("Purchase not found for this id :: " + stockeId));
		stock.setStockName(stock.getStockName());
		stock.setStockType(stock.getStockType());
		stock.setStockCount(stock.getStockCount());
		logger.debug("StockServiceImpl::updateStockWithoutPurchase::exit()");
		return stockRepository.save(stock);
	}

	@Override
	public StockEntity updateStockWithPurchase(Long stockeId, StockRequestDto stockDetails) throws ResourceNotFoundException {
		logger.debug("StockServiceImpl::updateStockWithPurchase::entry()");
		StockEntity stock = stockRepository.findById(stockeId)
				.orElseThrow(() -> new ResourceNotFoundException("Purchase not found for this id :: " + stockeId));
		stock.setStockName(stock.getStockName());
		stock.setStockType(stock.getStockType());
		stock.setStockCount(stock.getStockCount());
		logger.debug("StockServiceImpl::updateStockWithPurchase::exit()");
		return stockRepository.save(stock);
	}

	@Override
	public void deleteStock(Long stockId) throws ResourceNotFoundException {
		logger.debug("StockServiceImpl::deleteStock::entry()");
		StockEntity purchase = stockRepository.findById(stockId)
				.orElseThrow(() -> new ResourceNotFoundException("Purchase not found for this id :: " + stockId));
		stockRepository.delete(purchase);
		logger.debug("StockServiceImpl::deleteStock::exit()");
		
	}
		
}
