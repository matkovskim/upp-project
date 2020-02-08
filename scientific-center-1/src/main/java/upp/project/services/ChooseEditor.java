package upp.project.services;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import upp.project.model.Authority;
import upp.project.model.FormSubmissionDto;
import upp.project.model.RegistredUser;
import upp.project.model.Role;
import upp.project.model.ScientificArea;
import upp.project.repository.MagazineRepository;
import upp.project.repository.RegistredUserRepository;
import upp.project.repository.ScientificAreaRepository;

@Service
public class ChooseEditor implements JavaDelegate {

	@Autowired
	private RegistredUserRepository registredUserRepository;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private ScientificAreaRepository scientificAreaRepository;
	
	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private Environment env;

	@Async
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String scientificAreaName=(String)execution.getVariable("NaucneOblasti");
		String mainEditor=(String)execution.getVariable("glavniUrednik");

		ScientificArea scientificArea=scientificAreaRepository.findByName(scientificAreaName);
		Authority editorAuthority = this.authorityService.findByName(Role.ROLE_EDITOR);
		List<RegistredUser>editors=registredUserRepository.userWithSpecificAuthorityForArea(scientificArea, editorAuthority);
	
		if(editors.size()==0) {
			System.out.println("Nema urednika, urednik postaje glavni urednik");
			execution.setVariable("urednikOblasti", mainEditor);
		}
		else {
			System.out.println("Postavlajm nekog urednika");
			execution.setVariable("urednikOblasti", editors.get(0).getUsername());
		}
		
		System.out.println("UREDNIK NAUCNE OBLASTI JE: "+(String)execution.getVariable("urednikOblasti"));
		
		//slanje maila izabranom urdniku
		SimpleMailMessage mail = new SimpleMailMessage();
		RegistredUser regUser=registredUserRepository.findByUsername((String)execution.getVariable("urednikOblasti"));
		
		mail.setTo(regUser.getEmail());
		mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setSubject("Urednik naučne oblasti");
		mail.setText(
				"Poštovani korisniče, proglašeni ste za urednika naučne oblasti u okviru rada rada.");
		javaMailSender.send(mail);
		
	}
	
}