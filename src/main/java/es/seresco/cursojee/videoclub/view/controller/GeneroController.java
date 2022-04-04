package es.seresco.cursojee.videoclub.view.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.seresco.cursojee.videoclub.business.service.GeneroService;
import es.seresco.cursojee.videoclub.view.dto.genero.GeneroDTO;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/api/privado/generos/")
@Slf4j
public class GeneroController {

	@Resource
	private GeneroService generoService;

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<GeneroDTO> getGeneros()
	{
		log.info("Listing generos...");
		return generoService.findAll();
	}

}
