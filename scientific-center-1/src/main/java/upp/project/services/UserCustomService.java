package upp.project.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import upp.project.model.RegistredUser;
import upp.project.repository.RegistredUserRepository;

@Service
public class UserCustomService implements UserDetailsService{

	protected final Log LOGGER = LogFactory.getLog(getClass());
	
	@Autowired
	private RegistredUserRepository registredUserRepository;
		
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		RegistredUser user = registredUserRepository.findByUsername(username);
		if(user==null) {
			throw new UsernameNotFoundException("User Not Found with -> username or email : " + username);
		}
		return user;	
	}
	
}
