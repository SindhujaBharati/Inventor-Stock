package com.cognizant.stock.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.stock.exception.ResourceNotFoundException;
import com.cognizant.stock.domain.Stock;
import com.cognizant.stock.repository.StockRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/cognizant/stock")
@Api(value = "Stock Management", description = "Operations pertaining to Stock in Inventory Management System")
public class StockController {
	Logger logger = LoggerFactory.getLogger(StockController.class);
	
	@Autowired
	private Stock stock;

	@Autowired
	private StockRepository stockRepository;
	
	int stockByNameResult;
	
	@ApiOperation(value = "Add Stock Without Purchase", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully added Stock"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
	@PostMapping("/addStock/v1")
    public Stock createStockWithoutPurchase(@RequestBody Stock stockRequest) {
		
		logger.debug("StockController::createStockWithoutPurchase::entry()");
	
		stock.setStockName(stockRequest.getStockName());
		stock.setStockType(stockRequest.getStockType());
		stock.setStockCount(stockRequest.getStockCount());
		
		logger.debug("StockController::createStockWithoutPurchase::exit()");
		
        return stockRepository.save(stock);
        
    }
	
	@ApiOperation(value = "Add Stock With Purchase", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully added Stock"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
	@PostMapping("/addStock/v2")
    public Stock createStockWithPurchase(@RequestBody Stock stockRequest) {
		
		logger.debug("StockController::createStockWithPurchase::entry()");
		
		logger.debug("StockController::createStockWithPurchase::exit()");
		
        return stockRepository.save(stockRequest);
        
    }
	
	
	@ApiOperation(value = "View a list of stocks present in the inventory", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved stock list"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })	
	@GetMapping("/getStock/v1")
	@Cacheable(value = "stock")
    public List<Stock> getAllStock() {
		
		logger.debug("StockController::getAllStock::entry()");
		
		logger.debug("StockController::getAllStock::exit()");
		
        return stockRepository.findAll();
    }
	
	@ApiOperation(value = "Get a stock by Id")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully got Stock By Id"),
	        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	    })
	@GetMapping("/getStock/v2/{id}")
	@Cacheable(value = "stock", key = "#stockId")
    public ResponseEntity<Stock> getStockById(@PathVariable(value = "id") Long stockId)
        throws ResourceNotFoundException {
		
		logger.debug("StockController::getStockById::entry()");
		
		Stock stock = stockRepository.findById(stockId)
          .orElseThrow(() -> new ResourceNotFoundException("Stock not found for this id :: " + stockId));
		
		logger.debug("StockController::getStockById::exit()");
		
        return ResponseEntity.ok().body(stock);
    }
	
	@ApiOperation(value = "Get a stock by Name")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully got Stock By Name"),
	        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	    })
	@GetMapping("/getstock/v3/{name}")
	@Cacheable(value = "stock", key = "#stockName")
    public Stock getStockByName(@PathVariable(value = "name") String stockName)
        throws ResourceNotFoundException {
		
		logger.debug("StockController::getStockByName::entry()");
		try {
		 stock = stockRepository.findByName(stockName);
		}
		catch(Exception e) {
			logger.debug("Stock Not found");
			
		}
		
		logger.debug("StockController::getStockByName::exit()");
		
        return stock;
    }
	
	@ApiOperation(value = "Update a Stock Without Purchase")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully updated Stock Order"),
	        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	    })
	@PutMapping("/updateStock/v1/{id}")
	@CachePut(value = "stock", key = "#stockeId")
    public ResponseEntity<Stock> updateStockWithoutPurchase(@PathVariable(value = "id") Long stockeId,
         @RequestBody Stock stockDetails) throws ResourceNotFoundException {
		
		logger.debug("StockController::updateStockWithoutPurchase::entry()");
		
		Stock stock = stockRepository.findById(stockeId)
        .orElseThrow(() -> new ResourceNotFoundException("Stock not found for this id :: " + stockeId));
		
		stock.setStockName(stockDetails.getStockName());
		stock.setStockType(stockDetails.getStockType());
		stock.setStockCount(stockDetails.getStockCount());
		
		final Stock updatedStock = stockRepository.save(stock);
		
		logger.debug("StockController::updateStockWithoutPurchase::exit()");
		
        return ResponseEntity.ok(updatedStock);
    }
	
	@ApiOperation(value = "Update a Stock With Purchase")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully updated Stock Order"),
	        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	    })
	@PutMapping("/updateStock/v2/{id}")
	@CachePut(value = "stock", key = "#stockeId")
    public ResponseEntity<Stock> updateStockWithPurchase(@PathVariable(value = "id") Long stockeId,
         @RequestBody Stock stockDetails) throws ResourceNotFoundException {
		
		logger.debug("StockController::updateStockWithPurchase::entry()");
		
		System.out.print("id:::"+stockeId);
		
		Stock stock = stockRepository.findById(stockeId)
        .orElseThrow(() -> new ResourceNotFoundException("Stock not found for this id :: " + stockeId));
		
		stock.setStockName(stockDetails.getStockName());
		stock.setStockType(stockDetails.getStockType());
		stock.setStockCount(stockDetails.getStockCount());
		
		final Stock updatedStock = stockRepository.save(stock);
		
		logger.debug("StockController::updateStockWithPurchase::exit()");
		
        return ResponseEntity.ok(updatedStock);
    }
	
	@ApiOperation(value = "Delete a stock")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully deleted Stock "),
	        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	    })
	@DeleteMapping("/deleteStock/v1/{id}")
	@CacheEvict(value = "stock", allEntries=true)
    public Map<String, Boolean> deleteStock(@PathVariable(value = "id") Long stockId)
         throws ResourceNotFoundException {
		
		logger.debug("StockController::deleteStock::entry()::id");
		
		Stock stock = stockRepository.findById(stockId)
       .orElseThrow(() -> new ResourceNotFoundException("Stock not found for this id :: " + stockId));

		stockRepository.delete(stock);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        
        logger.debug("StockController::deleteStock::exit()");
        
        return response;
    }
	


	
}
