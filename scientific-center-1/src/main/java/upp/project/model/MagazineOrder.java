package upp.project.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class MagazineOrder extends UserOrder {

	@ManyToOne
	private Magazine magazine;

	public MagazineOrder(Magazine magazine) {
		super();
		this.magazine = magazine;
	}
	
	public MagazineOrder() {
		
	}

	public Magazine getMagazine() {
		return magazine;
	}

	public void setMagazine(Magazine magazine) {
		this.magazine = magazine;
	}
	
}
