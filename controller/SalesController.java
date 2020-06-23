package com.cognizant.inventory.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.inventory.domain.SalesOrder;
import com.cognizant.inventory.domain.Stock;
import com.cognizant.inventory.exception.ResourceNotFoundException;
import com.cognizant.inventory.repository.SalesRepository;
import com.cognizant.inventory.repository.StockRepository;

@RestController
@RequestMapping("/cognizant/sales")
@Api(value = "Sales Management", description = "Operations pertaining to Sales in Inventory Management System")
public class SalesController {
	@Autowired
	private SalesRepository salesRepository;
	
	@Autowired
	private Stock stock;
	
	@Autowired
	private StockRepository stockRepository;
	
		@ApiOperation(value = "Add a sales")
		@PostMapping("/addSales")
	    public String createOrder(@RequestBody SalesOrder Order) {

			String salesName=Order.getSalesName();
			String salesType=Order.getSalesType();
			Stock stockUpdate=stockRepository.findByName(salesName);
			String response;

			if(stockUpdate!=null && stockUpdate.getStockName().equals(salesName) && stockUpdate.getStockType().equals(salesType)){
				int previousCount=stockUpdate.getStockCount();
				int currentCount=Order.getSalesCount();
				int totalcount=previousCount-currentCount;
				if(totalcount>0){
				stockUpdate.setStockCount(totalcount);
				stockRepository.save(stockUpdate);	
					SalesOrder salesOrder=new SalesOrder();
					salesOrder.setSalesName(Order.getSalesName());
					salesOrder.setSalesType(Order.getSalesType());
					salesOrder.setSalesCount(Order.getSalesCount());
					salesOrder.setSalesAmtperCount(Order.getSalesAmtperCount());
					salesOrder.setSalesAmtTotal(Order.getSalesAmtperCount()*Order.getSalesCount());
					salesRepository.save(salesOrder);
				}
				response="Sales Order placed Successfully";
			}
			else{
				response="Out of Stock";
			}
			return response;     
	    }
		 @ApiOperation(value = "View a list of Sales done", response = List.class)
		    @ApiResponses(value = {
		        @ApiResponse(code = 200, message = "Successfully retrieved list"),
		        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
		    })
		 
		@GetMapping("/getSales")
	    public List<SalesOrder> getAllSales() {
	        return salesRepository.findAll();
	    }
		 @ApiOperation(value = "Get a sales by Id")
		@GetMapping("/getSales/{id}")
	    public ResponseEntity<SalesOrder> getSalesById(@PathVariable(value = "id") Long salesId)
	        throws ResourceNotFoundException {
			SalesOrder sales = salesRepository.findById(salesId)
	          .orElseThrow(() -> new ResourceNotFoundException("Sales not found for this id :: " + salesId));
	        return ResponseEntity.ok().body(sales);
	    }
		@ApiOperation(value = "Update a sales")
		@PutMapping("/updateSales/{id}")
	    public ResponseEntity<SalesOrder> updateSales(@PathVariable(value = "id") Long salesId,
	         @RequestBody SalesOrder salesDetails) throws ResourceNotFoundException {
			SalesOrder sales = salesRepository.findById(salesId)
	        .orElseThrow(() -> new ResourceNotFoundException("Sales not found for this id :: " + salesId));

			sales.setSalesName(salesDetails.getSalesName());
			sales.setSalesType(salesDetails.getSalesType());
			sales.setSalesCount(salesDetails.getSalesCount());
			sales.setSalesAmtperCount(salesDetails.getSalesAmtperCount());
			sales.setSalesAmtTotal(salesDetails.getSalesAmtperCount()*salesDetails.getSalesCount());
			final SalesOrder updatedSales = salesRepository.save(sales);
	        return ResponseEntity.ok(updatedSales);
	    }
		@ApiOperation(value = "Delete a sales")
		@DeleteMapping("/deleteSales/{id}")
	    public Map<String, Boolean> deleteSales(@PathVariable(value = "id") Long salesId)
	         throws ResourceNotFoundException {
			SalesOrder sales = salesRepository.findById(salesId)
	       .orElseThrow(() -> new ResourceNotFoundException("Sales not found for this id :: " + salesId));

			salesRepository.delete(sales);
	        Map<String, Boolean> response = new HashMap<>();
	        response.put("deleted", Boolean.TRUE);
	        return response;
	    }
}
