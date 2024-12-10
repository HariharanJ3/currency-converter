package com.currency.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.currency.model.CurrencyMaster;


@Repository
public interface CurrencyMasterRepository extends JpaRepository<CurrencyMaster, Integer>{
	
}
