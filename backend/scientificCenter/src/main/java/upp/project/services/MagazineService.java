package upp.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Magazine;
import upp.project.model.RegisteredUser;
import upp.project.repositories.MagazineRepository;

@Service
public class MagazineService {
	
	@Autowired
	private MagazineRepository magazineRepository;
	
	public Magazine findById(Long id) {
		return this.magazineRepository.findByIdAndDeleted(id, false);
	}
	
	public Magazine findByEmail(String email) {
		return this.magazineRepository.findByEmailAndDeleted(email, false);
	}
	
	public List<Magazine> findByEditor(RegisteredUser editor) {
		return this.magazineRepository.findByEditor(editor);
	}
	
	public Magazine findByIdActivated(Long id) {
		return this.magazineRepository.findByIdAndDeletedAndApproved(id, false, true);
	}
	
	public List<Magazine> findAll() {
		return this.magazineRepository.findByDeleted(false);
	}
	
	public List<Magazine> findAllActivated() {
		return this.magazineRepository.findByDeletedAndApprovedAndRegisteredOnPaymentHub(false, true, true);
	}
	
	
	public Magazine save(Magazine magazine) {
		Magazine saved = null;
		
		try {
			saved = this.magazineRepository.save(magazine);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return saved;
	}

}
