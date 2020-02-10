package upp.project.services;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import upp.project.model.Magazine;
import upp.project.model.MagazineIssue;
import upp.project.repositories.MagazineIssueRepository;

@Service
public class MagazineIssueService {

	@Autowired
	private MagazineIssueRepository magazineIssueRepository;
	
	public void createInitialIssue(Magazine magazine, int number) {
		MagazineIssue issue = new MagazineIssue();
		issue.setMagazine(magazine);
		issue.setNumber(number);
		Date dt = new Date();
		DateTime dtOrg = new DateTime(dt);
		DateTime dtPlusOne = dtOrg.plusMonths(1);
		issue.setPublicationDate(dtPlusOne.toDate());
		issue.setPublished(false);
		this.save(issue);
		
	}
	
	public MagazineIssue save(MagazineIssue issue) {
		MagazineIssue saved = null;
		
		try {
			saved = this.magazineIssueRepository.save(issue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return saved;
	}
	
	public MagazineIssue findByMagazineUnpublished(Magazine magazine) {
		return this.magazineIssueRepository.findByPublishedAndMagazine(false, magazine);
	}
	
	@Scheduled(initialDelay = 10000, fixedRate = 60000)
	public void checkPublishingTime() {
		List<MagazineIssue> issues = this.magazineIssueRepository.findAll();
		for(MagazineIssue issue: issues) {
			if(!issue.isPublished() && issue.getPublicationDate().before(new Date()))
			{
				issue.setPublished(true);
				this.save(issue);
				List<MagazineIssue> magazineIssues = this.magazineIssueRepository.findByMagazine(issue.getMagazine());
				
				if(magazineIssues == null)
					createInitialIssue(issue.getMagazine(), 1);
				else
					createInitialIssue(issue.getMagazine(), magazineIssues.size());
			}
		}
		
	}
}
