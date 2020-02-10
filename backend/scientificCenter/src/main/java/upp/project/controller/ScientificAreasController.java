package upp.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import upp.project.services.ScientificAreaService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/scientificAreas", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScientificAreasController {
	
	@Autowired
	private ScientificAreaService scientificAreasService;
	
	@GetMapping("/all")
	public ResponseEntity<?> getAll() {
		return ResponseEntity.ok(scientificAreasService.findAll());
	}

}
