package com.restaurantservice.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaurantservice.dao.RestaurantDao;
import com.restaurantservice.models.RestaurantEntity;
import com.restaurantservice.service.facade.RestaurantService;

@Service
public class RestaurantServiceImpl implements RestaurantService{

	@Autowired
	RestaurantDao restaurantDao;
	
	@Override
	public List<RestaurantEntity> findAll() {
		
		return restaurantDao.findAll();
	}

	@Override
	public List<RestaurantEntity> findByVille(String ville) {
		return restaurantDao.findByVille(ville);
	}

	@Override
	public RestaurantEntity add(RestaurantEntity restaurant) {
		return restaurantDao.save(restaurant);
	}

	@Override
	public void delete(int id) {
		restaurantDao.deleteById(id);
	}

	@Override
	public List<RestaurantEntity> findByFood(String food) {
		// TODO Auto-generated method stub
		return restaurantDao.findByFood(food);
	}

	@Override
	public RestaurantEntity update(int id, RestaurantEntity restaurant) {
		RestaurantEntity r=restaurantDao.getById(id);
		r.setTitle(restaurant.getTitle());
		r.setDescription(restaurant.getDescription());
		r.setOpeningDate(restaurant.getOpeningDate());
		r.setClosingDate(restaurant.getClosingDate());
		r.setVille(restaurant.getVille());
		r.setLatitude(restaurant.getLatitude());
		r.setLongitude(restaurant.getLongitude());
		return restaurantDao.save(r);
		return null;
	}

	@Override
	public List<RestaurantEntity> findByFoodAndVille(String food, String ville) {
		return restaurantDao.findByFoodAndVille(food, ville);
	}



	

}
