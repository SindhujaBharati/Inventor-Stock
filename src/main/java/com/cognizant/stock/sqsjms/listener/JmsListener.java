package com.cognizant.stock.sqsjms.listener;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cognizant.stock.dto.StockRequestDto;
import com.cognizant.stock.exception.ResourceNotFoundException;
import com.cognizant.stock.service.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JmsListener {
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private StockService stockService;

	@org.springframework.jms.annotation.JmsListener(destination = "PurchaseQueue")
	public void receive(String message) {
		StockRequestDto stockRequesDto;
		try {
			stockRequesDto = objectMapper.readValue(message, StockRequestDto.class);
			stockService.updateStockWithPurchase(stockRequesDto.getStockId(), stockRequesDto);

		} catch (IOException | ResourceNotFoundException e) {
			e.printStackTrace();
		}
	}

}
