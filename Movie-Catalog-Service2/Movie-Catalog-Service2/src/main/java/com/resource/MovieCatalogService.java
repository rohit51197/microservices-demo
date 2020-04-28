package com.resource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.model.CatalogItem;
import com.model.Movie;
import com.model.Rating;
import com.model.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
		
		
		UserRating ratings=restTemplate.getForObject("http://rating-info-service/ratingsdata/users/"+ userId,UserRating.class);
		
		
		return ratings.getUserRating().stream()
				.map(rating->{
					
				Movie movie=restTemplate.getForObject("http://movie-info-service/movie/" +rating.getMovieId(),Movie.class);
				
				return new CatalogItem(movie.getName(),"Desc",rating.getRating());
				
				})
				.collect(Collectors.toList());
	}

}
