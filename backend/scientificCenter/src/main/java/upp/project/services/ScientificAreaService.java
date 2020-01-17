package upp.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.ScientificArea;
import upp.project.repositories.ScientificAreaRepository;

@Service
public class ScientificAreaService {

	@Autowired
	private ScientificAreaRepository scientificAreaRepository;

	public List<ScientificArea> findAll() {
		return this.scientificAreaRepository.findByDeleted(false);
	}

	public ScientificArea findById(Long id) {
		return this.scientificAreaRepository.findByIdAndDeleted(id, false);
	}

	public ScientificArea findByName(String name) {
		return this.scientificAreaRepository.findByNameAndDeleted(name, false);
	}

	public ScientificArea save(ScientificArea scientificArea) {

		ScientificArea savedArea = null;

		try {
			savedArea = this.scientificAreaRepository.save(scientificArea);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return savedArea;
	}

}
