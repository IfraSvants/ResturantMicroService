package com.restaurantservice.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.util.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.restaurantservice.models.ImageRestaurantEntity;
import com.restaurantservice.models.RestaurantEntity;
import com.restaurantservice.service.facade.RestaurantService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

	final static Logger log = LogManager.getLogger(RestaurantController.class);
	
	@Value("${upload.dir}") 
    private String uploadDir;
	
	@Autowired
	private RestaurantService restaurantService;
	
	@PostMapping( value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
	public ResponseEntity<?> save( 
			@Valid @RequestParam("title")  String title,
			@RequestParam("food_category")  String food_category,
			@RequestParam("description")  String description,
			@RequestParam ("openingDate") String openingDate,
			@RequestParam("closingDate")  String closingDate,
			@RequestParam("latitude")  String latitude,
			@RequestParam("longitude")  String longitude,
			@RequestParam("ville")  String ville,
			@RequestPart("imageFiles") List<MultipartFile> imageFiles
			)throws IOException{
		
		Date openingDateConvert;
	    Date closingDateConvert;
	    
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    try {
	    	openingDateConvert = dateFormat.parse(openingDate);
	    	closingDateConvert = dateFormat.parse(closingDate);
	    } catch (ParseException e) {
	        // Gérer l'erreur de format de date invalide
	        return ResponseEntity.badRequest().body("Format de date invalide");
	    }
		
		RestaurantEntity placeDto = new RestaurantEntity(null, title, food_category, description, openingDateConvert, closingDateConvert, Float.parseFloat(latitude), Float.parseFloat(longitude), ville, null,null);
		
		if(imageFiles == null || imageFiles.isEmpty()) {
			
			log.warn("Please select a file to upload");
			return ResponseEntity.accepted().body(placeDto);
			
		}
		
		List<ImageRestaurantEntity> images = new ArrayList<>();
		
		for (MultipartFile imageFile : imageFiles) {
			
			ImageRestaurantEntity image = new ImageRestaurantEntity();
			String fileId = UUID.randomUUID().toString().substring(0, 8);
			String filename = StringUtils.cleanPath(imageFile.getOriginalFilename());
			// Trouvez l'index du dernier point dans le nom du fichier
	        int lastIndex = filename.lastIndexOf('.');
	        
	        // Extraire le nom de fichier sans l'extension
	        String fileNameWithoutExtension = lastIndex >= 0 ? filename.substring(0, lastIndex) : filename;

	        // Extraire l'extension du fichier
	        String fileExtension = lastIndex >= 0 && lastIndex < filename.length() - 1
	                ? filename.substring(lastIndex + 1)
	                : "";

			
			// Ajoutez l'identifiant au nom du fichier
		    String newFileName = fileNameWithoutExtension + "-" + fileId + "."+fileExtension;
		    
		    Path pathFile = Paths.get( uploadDir , newFileName);
			try {
				
				if (imageFile.isEmpty()) {
			        log.error("Failed to store empty file " + filename);
			        return ResponseEntity.accepted().body(placeDto);
			    }
				if (filename.contains("..")) {
			        // This is a security check
			        log.error("Cannot store file with relative path outside current directory " + filename);
			        return ResponseEntity.accepted().body(placeDto);
			    }
				try (InputStream inputStream = imageFile.getInputStream()) {
			        
			        // save file to target(server)
			        Files.copy(inputStream, pathFile, StandardCopyOption.REPLACE_EXISTING);

			        // read file to bytes
			        // Files.readAllBytes(pathFile);
			        image.setPath(newFileName);
			        
			        images.add(image);
			        
			        log.info("You successfully uploaded '" + filename + "'");
			        
			      }
				
			} catch (IOException e) {
				log.error("Failed to store file " + filename, e);
			}
	        
		}
		
		placeDto.setImages(images);
		RestaurantEntity saved = restaurantService.add(placeDto);
		
		return ResponseEntity.accepted().body(saved);
	}
	
	//-----------
	@GetMapping("/getAll")
	public List<RestaurantEntity> getAll(){
		return restaurantService.findAll();
	}
	@GetMapping("/getByVille/{ville}")
	public List<RestaurantEntity> getByVille(@PathVariable String ville){
		return restaurantService.findByVille(ville);
	}
	@GetMapping("/getByfood/{food}")
	public List<RestaurantEntity> getByFood(@PathVariable String food){
		return restaurantService.findByFood(food);
	}
	@GetMapping("/getByfoodVille/{food}/{ville}")
	public List<RestaurantEntity> getByFood(@PathVariable String food,@PathVariable String ville){
		return restaurantService.findByFoodAndVille(food,ville);
	}
	//---------
	@PutMapping("/update/{id}")
	public RestaurantEntity getByVille(@RequestBody RestaurantEntity restaurant,@PathVariable int id){
		return restaurantService.update(id,restaurant);
	}
	//---------
	@DeleteMapping("/delete/{id}")
	public void deleteById(@PathVariable int id){
		 restaurantService.delete(id);
	}

	
}
