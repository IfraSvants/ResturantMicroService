package com.restaurantservice.Dto;

import java.util.Date;
import java.util.List;

import com.restaurantservice.models.ImageRestaurantEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class RestaurantDto {
	Integer restaurantId;
	String title;
	String food_category;
	String description;
	Date openingDate;
	Date closingDate;
	float latitude;
	float longitude;
	String ville;
	List<ImageRestaurantDto> images;

}
