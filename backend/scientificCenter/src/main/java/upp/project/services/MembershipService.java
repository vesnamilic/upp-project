package upp.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Membership;
import upp.project.repositories.MembershipRepository;

@Service
public class MembershipService {

	@Autowired
	private MembershipRepository membershipRepository;
	
	public Membership findMembership(Long magazineId, String username) {
		System.out.println(magazineId);
		System.out.println(username);
		return this.membershipRepository.findMagazineMembership(magazineId, username);
	}
	
	public Membership getOne(Long id) {
		return this.membershipRepository.getOne(id);
	}
	
	public Membership saveMembership(Membership mem) {
		Membership saved = null;
		try {
			saved = this.membershipRepository.save(mem);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return saved;
	}
}
