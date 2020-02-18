package upp.project.model;

public class RegistrationDTO {
	private String email;
    private String name;
    private String confirmationLink;
    
    public RegistrationDTO() {
    	
	}
    
	public RegistrationDTO(String email, String name, String confirmationLink) {
		this.email = email;
		this.name = name;
		this.confirmationLink = confirmationLink;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConfirmationLink() {
		return confirmationLink;
	}

	public void setConfirmationLink(String confirmationLink) {
		this.confirmationLink = confirmationLink;
	}
       
}
