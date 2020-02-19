package upp.project.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UserOrder {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "uuid")
	private String uuid;
	
	@Column
	private String email;
	
	@Column(name = "orderStatus")
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	
	@Column(name = "paymentAmount")
	private double paymentAmount;
	
	@Column(name = "paymentCurrency")
	private String paymentCurrency;
	
	@ManyToOne
	private RegistredUser buyer;
	
	@ManyToOne
	private Magazine magazine;

	public UserOrder() {
		super();
	}

	public UserOrder(String uuid, Date expirationDate, OrderStatus orderStatus, double paymentAmount,
			String paymentCurrency, String successUrl, String errorUrl, String failedUrl, RegistredUser buyer) {
		super();
		this.uuid = uuid;
		this.orderStatus = orderStatus;
		this.paymentAmount = paymentAmount;
		this.paymentCurrency = paymentCurrency;
		this.buyer = buyer;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getPaymentCurrency() {
		return paymentCurrency;
	}

	public void setPaymentCurrency(String paymentCurrency) {
		this.paymentCurrency = paymentCurrency;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public RegistredUser getBuyer() {
		return buyer;
	}

	public void setBuyer(RegistredUser buyer) {
		this.buyer = buyer;
	}

	public Magazine getMagazine() {
		return magazine;
	}

	public void setMagazine(Magazine magazine) {
		this.magazine = magazine;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
