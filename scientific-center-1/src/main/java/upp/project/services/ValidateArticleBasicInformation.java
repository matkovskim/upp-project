package upp.project.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateArticleBasicInformation implements JavaDelegate {

	@Autowired
	AuthentificationService authentificationService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String title=(String)execution.getVariable("naslov");
		String keyWords=(String)execution.getVariable("klucniPojmovi");
		String apstrakt=(String)execution.getVariable("apstrakt");
		String scientificAreaName=(String)execution.getVariable("NaucneOblasti");
		String text=(String)execution.getVariable("tekstRada");
		Long numberOfCoAuthors=(Long)execution.getVariable("brojKoautora");
		
		System.out.println(scientificAreaName);
		if(title==null || keyWords==null || apstrakt==null || text==null || numberOfCoAuthors==null || numberOfCoAuthors<0) {
			execution.setVariable("isValidBasicInfo", false);
		}
		else {
			execution.setVariable("isValidBasicInfo", true);
		}

	}

}