package com.currency.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="currencymaster")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String sourcecurrency;
	private String targetcurrency;
	private Double rate;
	private LocalDateTime createdAt;

}
