package upp.project.aasecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import upp.project.aasecurity.JwtProvider;
import upp.project.aasecurity.authentication.JwtAuthenticationEntryPoint;
import upp.project.aasecurity.authentication.JwtAuthenticationTokenFilter;
import upp.project.services.UserCustomService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserCustomService userCustomService;

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	JwtProvider jwtProvider;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userCustomService).passwordEncoder(passwordEncoder());
	}

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
		.authorizeRequests().antMatchers(HttpMethod.GET, "/", "/auth/**", "/webjars/**", "/*.html", "/favicon.ico", "/**/*.html",
						"/**/*.css", "/**/*.js").permitAll()
		.antMatchers("/welcome/**").permitAll()
		.anyRequest().permitAll().and()
		//.anyRequest().authenticated().and()
		.addFilterBefore(new JwtAuthenticationTokenFilter(jwtProvider, userCustomService),BasicAuthenticationFilter.class);

		http.csrf().disable();
	}
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        // TokenAuthenticationFilter will ignore the below paths
        web.ignoring().antMatchers(
                HttpMethod.POST,
                "/welcome/**"
        );
        web.ignoring().antMatchers(
                HttpMethod.GET,
                "/welcome/**"
        );
        
        web.ignoring().antMatchers(
                HttpMethod.GET,
                "/",
                "/webjars/**",
                "/*.html",
                "/favicon.ico",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js"
            );

    }

}
