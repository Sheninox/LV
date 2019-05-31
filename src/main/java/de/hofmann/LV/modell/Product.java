package de.hofmann.LV.modell;

import org.jetbrains.annotations.Contract;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;


@Entity
@SuppressWarnings("unused")
public class Product {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ID;

	private String name;
	private int quantity;
	private Date adDate;
	private Date duration;

	@Contract(pure = true)
	public Product(){}

	public Long getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Date getAdDate() {
		return adDate;
	}

	public void setAdDate(Date adDate) {
		this.adDate = adDate;
	}

	public Date getDuration() {
		return duration;
	}

	public void setDuration(Date duration) {
		this.duration = duration;
	}
}
