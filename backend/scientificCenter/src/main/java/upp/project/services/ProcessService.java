package upp.project.services;

import java.util.Date;
import java.util.List;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ProcessService {

	@Autowired
	private HistoryService historyService;

	@Autowired
	private RuntimeService runtimeService;

	@Scheduled(initialDelay = 1800000, fixedRate = 1800000)
	public void checkOldProcessInstances() {
		List<HistoricProcessInstance> hpi = historyService.createHistoricProcessInstanceQuery().active().unfinished().list();
		Date yesterday = new Date();
		DateTime dtOrg = new DateTime(yesterday);
		DateTime dtPlusOne = dtOrg.minusDays(1);
		for (HistoricProcessInstance h : hpi) {
			Date startDate = h.getStartTime();
			if (startDate.before(dtPlusOne.toDate())) {
				runtimeService.deleteProcessInstance(h.getId(), "EXPIRED");
			}
		}
	}
}
