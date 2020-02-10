package upp.project.services;

import java.util.Date;
import java.util.Set;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Magazine;
import upp.project.model.MembershipFees;
import upp.project.model.RegistredUser;
import upp.project.repository.MagazineRepository;
import upp.project.repository.RegistredUserRepository;

@Service
public class CheckMembershipFees implements JavaDelegate {

	@Autowired
	private MagazineRepository magazineRepository;
	
	@Autowired
	private RegistredUserRepository registredUserRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String magazineName = (String) execution.getVariable("izborCasopisa");
		Magazine magazine = magazineRepository.findByName(magazineName);
		
		String author=(String) execution.getVariable("starter");
		RegistredUser regUser=registredUserRepository.findByUsername(author);
		
		Set<MembershipFees> membershipFees=regUser.getMembershipFees();
		if(membershipFees==null) {
			System.out.println("nema clanarine");
			execution.setVariable("dovoljnoClanarine", "false");
		}
		else {
			for(MembershipFees mf:membershipFees) {
				if(mf.getMagazine().getEmail().equals(magazine.getEmail())){
					if(mf.getExpirationDate().after(new Date())) {
						execution.setVariable("dovoljnoClanarine", "true");
						return;
					}
				}
			}
			if(execution.getVariable("dovoljnoClanarine")!="true") {
				execution.setVariable("dovoljnoClanarine", "false");
			}
		}
		
	}

}