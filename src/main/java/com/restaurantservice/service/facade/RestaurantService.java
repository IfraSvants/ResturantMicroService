package com.restaurantservice.service.facade;

import java.util.List;

import com.restaurantservice.models.RestaurantEntity;

public interface RestaurantService {

	// Recuperation
	
	List<RestaurantEntity> findAll();
	List<RestaurantEntity> findByVille(String ville);
//	List<RestaurantEntity> findByFood(String food);
	
	// Ajout
	RestaurantEntity add(RestaurantEntity restaurant);
	
	// Supression
	void delete(int id);

	
	// Modification
	RestaurantEntity update(int id,RestaurantEntity restaurant);

}
