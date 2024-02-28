package com.restaurantservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurantservice.models.RestaurantEntity;
import java.util.List;


@Repository
public interface RestaurantDao extends JpaRepository<RestaurantEntity, Integer>{
	List<RestaurantEntity> findByVille(String ville);
	List<RestaurantEntity> findByFood(String food);
	List<RestaurantEntity> findByFoodAndVille(String food,String ville);

	
	
}
