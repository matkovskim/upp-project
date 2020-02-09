package upp.project.services;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.glassfish.jersey.internal.guava.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import upp.project.model.Authority;
import upp.project.model.Magazine;
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
	MagazineRepository magazineRepository;

	@Autowired
	private Environment env;

	@Async
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String scientificAreaName=(String)execution.getVariable("NaucneOblasti");
		String mainEditor=(String)execution.getVariable("glavniUrednik");
		
		String magazineName=(String)execution.getVariable("izborCasopisa");
		System.out.println("casopis: "+magazineName);
		Magazine magazine=magazineRepository.findByName(magazineName);
		List<RegistredUser>magazineEditors=Lists.newArrayList(magazine.getEditors());
		
		ScientificArea scientificArea=scientificAreaRepository.findByName(scientificAreaName);
		Authority editorAuthority = this.authorityService.findByName(Role.ROLE_EDITOR);
		List<RegistredUser>editors=registredUserRepository.userWithSpecificAuthorityForArea(scientificArea, editorAuthority);
	
		//ako je glavni urednik u listi izbaci ga
		List<RegistredUser>editordWithoutMain=new ArrayList<RegistredUser>();
		for(RegistredUser ru:editors) {
			if(!ru.getUsername().equals(mainEditor)) {
				editordWithoutMain.add(ru);
			}
		}
		
		List<RegistredUser>finalList=new ArrayList<RegistredUser>();
		for(RegistredUser ru:magazineEditors) {
			for(RegistredUser r:editordWithoutMain) {
				if(ru.getUsername().equals(r.getUsername())) {
					finalList.add(ru);
				}
			}
		}
		
		
		if(finalList.size()==0) {
			System.out.println("Nema urednika, urednik postaje glavni urednik");
			execution.setVariable("urednikOblasti", mainEditor);
		}
		else {
			System.out.println("Postavlajm nekog urednika");
			execution.setVariable("urednikOblasti", finalList.get(0).getUsername());
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