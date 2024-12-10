package com.currency.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.currency.model.CurrencyMaster;
import com.currency.service.CurrencyConverterService;

@RestController
@RequestMapping("/converter")
public class UserActions {
	
	@Autowired
	private CurrencyConverterService service;
	
    @GetMapping("/convert")
    public Integer convert(@RequestParam("source") String source, @RequestParam("target") String target,@RequestParam("amount") Integer amount) {
	 
		 Integer convertedAmount = 0;
		 
		 try 
		 {
			 CurrencyMaster currencyMaster = service.selectCurrencyMasterBySourceAndTarget(source,target);
		 
			if(currencyMaster != null)
			{
				 Double storedRate = currencyMaster.getRate();
				 LocalDateTime createdTime = currencyMaster.getCreatedAt();
				 LocalDateTime minusTime = LocalDateTime.now().minusHours(1);
				 
				//Check if there was a call to Currency Beacon made within the last one hour for the source and target currency combination
				 if(createdTime.isBefore(minusTime))
				 {
					 Double latestRate = service.getDataFromBeaconApi(source,target);
					 
					 if(latestRate != null)
					 {
						 convertedAmount = (int) (amount*latestRate);
						 
						 currencyMaster.setRate(latestRate);
						 currencyMaster.setCreatedAt(LocalDateTime.now());
						 
						 service.saveOrUpdateCurrencyMaster(currencyMaster);
					 }
					 else
					 {
						 convertedAmount = (int) (amount*storedRate);
					 }
				 }
				 else
				 {
					 convertedAmount = (int) (amount*storedRate);
				 }
			 }
			 else
			 {
				 Double latestRate = service.getDataFromBeaconApi(source,target);
				 
				 if(latestRate != null)
				 {
					 convertedAmount = (int) (amount*latestRate);
					 
					 CurrencyMaster currencyMasterNew = new CurrencyMaster();
					 currencyMasterNew.setSourcecurrency(source);
					 currencyMasterNew.setTargetcurrency(target);
					 currencyMasterNew.setRate(latestRate);
					 currencyMasterNew.setCreatedAt(LocalDateTime.now());
					 
					 service.saveOrUpdateCurrencyMaster(currencyMasterNew);
				 }
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
         return convertedAmount;
     }
}
