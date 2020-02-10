package upp.project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.PostConstruct;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import upp.project.model.RegistredUser;

@EnableScheduling
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
			if (Files.exists(Paths.get("upload-dir"))) {
				FileSystemUtils.deleteRecursively(Paths.get("upload-dir").toFile());
			}
			Files.createDirectory(Paths.get("upload-dir"));
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize storage!");
		}
	}

	

	@PostConstruct
	private void createUserGroup() {

		List<Group> groups = identityService.createGroupQuery()
				.groupIdIn("korisnici", "recenzenti", "administratori", "urednici", "gosti").list();
		if (groups.isEmpty()) {

			Group usersGroup = identityService.newGroup("korisnici");
			identityService.saveGroup(usersGroup);

			Group recenzentiGroup = identityService.newGroup("recenzenti");
			identityService.saveGroup(recenzentiGroup);

			Group administratoriGroup = identityService.newGroup("administratori");
			identityService.saveGroup(administratoriGroup);

			Group uredniciGroup = identityService.newGroup("urednici");
			identityService.saveGroup(uredniciGroup);

			Group gostGroup = identityService.newGroup("gosti");
			identityService.saveGroup(gostGroup);
		}

		List<User> users = identityService.createUserQuery().userIdIn("gost", "urednik1", "urednik2", "recenzent1",
				"recenzent2", "recenzent3", "recenzent4", "korisnik", "admin").list();
		if (users.isEmpty()) {
			registerInCamunda(new RegistredUser("gost", "gost", "gost", "gost", "gost@gmail.com"));
			registerInCamunda(new RegistredUser("urednik1", "admin", "Urednik1", "Urdnikovic", "matkovskim@gmail.com"));
			registerInCamunda(new RegistredUser("urednik2", "admin", "Urednik2", "Urdnikovic", "matkovskim@gmail.com"));
			registerInCamunda(
					new RegistredUser("recenzent1", "admin", "Recenzent1", "Recenzic", "matkovskim@gmail.com"));
			registerInCamunda(
					new RegistredUser("recenzent2", "admin", "Recenzent2", "Recenzic", "matkovskim@gmail.com"));
			registerInCamunda(
					new RegistredUser("recenzent3", "admin", "Recenzent3", "Recenzic", "matkovskim@gmail.com"));
			registerInCamunda(
					new RegistredUser("recenzent4", "admin", "Recenzent4", "Recenzic", "matkovskim@gmail.com"));
			registerInCamunda(new RegistredUser("korisnik", "admin", "Korisnik", "Urdnikovic", "matkovskim@gmail.com"));
			registerInCamunda(new RegistredUser("admin", "admin", "Marijana", "Matkovski", "matkovskim@gmail.com"));

			identityService.createMembership("urednik1", "urednici");
			identityService.createMembership("urednik2", "urednici");
			identityService.createMembership("recenzent1", "recenzenti");
			identityService.createMembership("recenzent2", "recenzenti");
			identityService.createMembership("recenzent3", "recenzenti");
			identityService.createMembership("recenzent4", "recenzenti");
			identityService.createMembership("korisnik", "korisnici");
			identityService.createMembership("gost", "gosti");
			identityService.createMembership("admin", "administratori");
		}

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
	
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
