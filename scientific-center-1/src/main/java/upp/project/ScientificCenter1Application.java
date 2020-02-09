package upp.project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import upp.project.model.RegistredUser;

@SpringBootApplication
public class ScientificCenter1Application {

	@Autowired
	IdentityService identityService;
	
	public static void main(String[] args) {
		pdfFolders();
		SpringApplication.run(ScientificCenter1Application.class, args);
	}
	
	private static void pdfFolders() {
		try {
			if(Files.exists(Paths.get("upload-dir"))) {
				FileSystemUtils.deleteRecursively(Paths.get("upload-dir").toFile());
			}
			Files.createDirectory(Paths.get("upload-dir"));
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize storage!");
		}
	}
	
	
	@PostConstruct
	private void createUserGroup() {
		Group usersGroup=identityService.newGroup("korisnici");
		identityService.saveGroup(usersGroup);	
	
		Group recenzentiGroup=identityService.newGroup("recenzenti");
		identityService.saveGroup(recenzentiGroup);
		
		Group administratoriGroup = identityService.newGroup("administratori");
		identityService.saveGroup(administratoriGroup);
		
		Group uredniciGroup = identityService.newGroup("urednici");
		identityService.saveGroup(uredniciGroup);
		
		Group gostGroup = identityService.newGroup("gosti");
		identityService.saveGroup(gostGroup);
		
		registerInCamunda(new RegistredUser("gost", "gost", "gost", "gost", "gost@gmail.com"));
		identityService.createMembership("gost", "gosti");
		
	}

	private void registerInCamunda(RegistredUser newUser) {
		try {
			User camundaUser = identityService.newUser(newUser.getUsername());
			camundaUser.setPassword(newUser.getPassword());
			camundaUser.setFirstName(newUser.getName());
			camundaUser.setLastName(newUser.getLastName());
			camundaUser.setEmail(newUser.getEmail());
			identityService.saveUser(camundaUser);
		} catch (Exception e) {
			System.out.println("Korisnik vec postoji");
		}
	}
	
	@Bean
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}
}
