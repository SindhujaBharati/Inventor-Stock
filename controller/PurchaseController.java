package com.cognizant.inventory.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.cognizant.inventory.domain.PurchaseOrder;
import com.cognizant.inventory.domain.Stock;
import com.cognizant.inventory.exception.ResourceNotFoundException;
import com.cognizant.inventory.repository.PurchaseRepository;
import com.cognizant.inventory.repository.StockRepository;

@RestController
@RequestMapping("/cognizant/purchase")
@Api(value = "Purchase Management", description = "Operations pertaining to Purchase in Inventory Management System")
public class PurchaseController {
	
	@Autowired
	private PurchaseRepository purchaseRepository;
	
	@Autowired
	private Stock stock;
	
	@Autowired
	private PurchaseOrder purchaseOrder;
	
	@Autowired
	private StockRepository stockRepository;
	
	@ApiOperation(value = "Add a purchase")
	@PostMapping("/addPurchase")
    public PurchaseOrder createOrder(@RequestBody PurchaseOrder Order) {
		
		PurchaseOrder purchaseOrder=new PurchaseOrder();
		purchaseOrder.setPurchaseName(Order.getPurchaseName());
		purchaseOrder.setPurchaseType(Order.getPurchaseType());
		purchaseOrder.setPurchaseCount(Order.getPurchaseCount());
		purchaseOrder.setpurchaseAmtperCount(Order.getpurchaseAmtperCount());
		purchaseOrder.setpurchaseAmtTotal(Order.getpurchaseAmtperCount()*Order.getPurchaseCount());
		
		String purchaseName=Order.getPurchaseName();
		String purchaseType=Order.getPurchaseType();
		Stock stockUpdate=stockRepository.findByName(purchaseName);

		if(stockUpdate!=null && stockUpdate.getStockName().equals(purchaseName) && stockUpdate.getStockType().equals(purchaseType)){
			int previousCount=stockUpdate.getStockCount();
			int currentCount=Order.getPurchaseCount();
			int totalcount=previousCount+currentCount;
			stockUpdate.setStockCount(totalcount);
			stockRepository.save(stockUpdate);
		}
		else{
			Stock stock=new Stock();
			stock.setStockName(Order.getPurchaseName());
			stock.setStockType(Order.getPurchaseType());
			stock.setStockCount(Order.getPurchaseCount());
			stockRepository.save(stock);
		}
        return purchaseRepository.save(purchaseOrder);
        
    }
	@ApiOperation(value = "View a list of purchases done", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved list"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
	@GetMapping("/getPurchase")
    public List<PurchaseOrder> getAllPurchase() {
        return purchaseRepository.findAll();
    }
	@ApiOperation(value = "Get a purchase by Id")
	@GetMapping("/getPurchase/{id}")
    public ResponseEntity<PurchaseOrder> getPurchaseById(@PathVariable(value = "id") Long purchaseId)
        throws ResourceNotFoundException {
		PurchaseOrder purchase = purchaseRepository.findById(purchaseId)
          .orElseThrow(() -> new ResourceNotFoundException("Purchase not found for this id :: " + purchaseId));
        return ResponseEntity.ok().body(purchase);
    }
	@ApiOperation(value = "Update a purchase")
	@PutMapping("/updatePurchase/{id}")
    public ResponseEntity<PurchaseOrder> updatePurchase(@PathVariable(value = "id") Long purchaseId,
         @RequestBody PurchaseOrder purchaseDetails) throws ResourceNotFoundException {
		PurchaseOrder purchase = purchaseRepository.findById(purchaseId)
        .orElseThrow(() -> new ResourceNotFoundException("Purchase not found for this id :: " + purchaseId));

		
		purchase.setPurchaseName(purchaseDetails.getPurchaseName());
		purchase.setPurchaseType(purchaseDetails.getPurchaseType());
		purchase.setPurchaseCount(purchaseDetails.getPurchaseCount());
		purchase.setpurchaseAmtperCount(purchaseDetails.getpurchaseAmtperCount());
		purchase.setpurchaseAmtTotal(purchaseDetails.getpurchaseAmtperCount()*purchaseDetails.getPurchaseCount());
		final PurchaseOrder updatedPurchase = purchaseRepository.save(purchase);
        return ResponseEntity.ok(updatedPurchase);
    }
	@ApiOperation(value = "Delete a purchase")
	@DeleteMapping("/deletePurchase/{id}")
    public Map<String, Boolean> deleteSales(@PathVariable(value = "id") Long purchaseId)
         throws ResourceNotFoundException {
		PurchaseOrder purchase = purchaseRepository.findById(purchaseId)
       .orElseThrow(() -> new ResourceNotFoundException("Purchase not found for this id :: " + purchaseId));

		purchaseRepository.delete(purchase);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
