package com.currency.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.currency.model.CurrencyMaster;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Service
public class CurrencyConverterService {
	
	@Autowired
	private CurrencyMasterRepository CurrmasterRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	EntityManager entityManager;
	
	private final String API_URL = "https://api.currencybeacon.com/v1/latest";
    private final String API_KEY = "1SMxQumaAcC996SUXGsnlBPW44t2RR82";
    
	public CurrencyMaster selectCurrencyMasterBySourceAndTarget(String source, String target) 
	{
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<CurrencyMaster> query = builder.createQuery(CurrencyMaster.class);
			Root<CurrencyMaster> root = query.from(CurrencyMaster.class);
			
			query.select(root).where(builder.and(builder.equal(root.get("sourcecurrency"), source),builder.equal(root.get("targetcurrency"), target)));
			
			return entityManager.createQuery(query).getResultList().stream().findFirst().orElse(null);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Double getDataFromBeaconApi(String source,String target)
	{
        String url = API_URL + "?api_key=" + API_KEY + "&base="+source+"&symbols="+target;
        
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        
        Double rate = 0.0d;
        
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Double> rates = (Map<String, Double>) response.getBody().get("rates");
            rate = rates.get(target);
        }

        return rate;
	}

	public void saveOrUpdateCurrencyMaster(CurrencyMaster currencyMaster) {
		CurrmasterRepository.save(currencyMaster);
	}
}
