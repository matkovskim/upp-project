package upp.project.model;

public class RegisterEditorDTO {

private String username;
	
	private String name;

	private String lastName;

	private String city;

	private String state;

	private String email;

	private String password;
	
	private String title;
	
	private String scientificAreas;
	
	public RegisterEditorDTO() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getScientificAreas() {
		return scientificAreas;
	}

	public void setScientificAreas(String scientificAreas) {
		this.scientificAreas = scientificAreas;
	}
	
}
