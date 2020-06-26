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
import com.cognizant.stock.domain.StockEntity;
import com.cognizant.stock.dto.StockRequestDto;
import com.cognizant.stock.service.StockService;

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
	private StockService stockService;
	
	int stockByNameResult;
	
	@ApiOperation(value = "Add Stock Without Purchase", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully added Stock"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
	@PostMapping("/addStock/v1")
    public StockEntity createStockWithoutPurchase(@RequestBody StockRequestDto stockRequest) {
		return stockService.createStock(stockRequest);
        
    }
	
	@ApiOperation(value = "Add Stock With Purchase", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully added Stock"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
	@PostMapping("/addStock/v2")
    public StockEntity createStockWithPurchase(@RequestBody StockRequestDto stockRequest) {

        return stockService.save(stockRequest);
        
    }
	
	
	@ApiOperation(value = "View a list of stocks present in the inventory", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved stock list"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })	
	@GetMapping("/getStock/v1")
	//@Cacheable(value = "stock")
    public List<StockEntity> getAllStock() {
		
        return stockService.findAll();
    }
	
	@ApiOperation(value = "Get a stock by Id")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully got Stock By Id"),
	        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	    })
	@GetMapping("/getStock/v2/{id}")
	//@Cacheable(value = "stock", key = "#stockId")
    public ResponseEntity<StockEntity> getStockById(@PathVariable(value = "id") Long stockId)
        throws ResourceNotFoundException {
		
		StockEntity stock =stockService.getStockById(stockId);
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
	//@Cacheable(value = "stock", key = "#stockName")
    public StockEntity getStockByName(@PathVariable(value = "name") String stockName)
        throws ResourceNotFoundException {
		StockEntity stock =stockService.findByName(stockName);
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
	//@CachePut(value = "stock", key = "#stockeId")
    public ResponseEntity<StockEntity> updateStockWithoutPurchase(@PathVariable(value = "id") Long stockeId,
         @RequestBody StockRequestDto stockDetails) throws ResourceNotFoundException {
		
		final StockEntity updatedPurchase = stockService.updateStockWithoutPurchase(stockeId, stockDetails);
		return ResponseEntity.ok(updatedPurchase);
		
    }
	
	@ApiOperation(value = "Update a Stock With Purchase")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully updated Stock Order"),
	        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	    })
	@PutMapping("/updateStock/v2/{id}")
	//@CachePut(value = "stock", key = "#stockeId")
    public ResponseEntity<StockEntity> updateStockWithPurchase(@PathVariable(value = "id") Long stockeId,
         @RequestBody StockRequestDto stockDetails) throws ResourceNotFoundException {
		
		final StockEntity updatedPurchase = stockService.updateStockWithPurchase(stockeId, stockDetails);
		return ResponseEntity.ok(updatedPurchase);
    }
	
	@ApiOperation(value = "Delete a stock")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully deleted Stock "),
	        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	    })
	@DeleteMapping("/deleteStock/v1/{id}")
	//@CacheEvict(value = "stock", allEntries=true)
    public Map<String, Boolean> deleteStock(@PathVariable(value = "id") Long stockId)
         throws ResourceNotFoundException {
		
		stockService.deleteStock(stockId);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
    }
	


	
}
