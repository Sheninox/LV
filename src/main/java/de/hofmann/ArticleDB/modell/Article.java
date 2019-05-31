package de.hofmann.ArticleDB.modell;

import org.jetbrains.annotations.Contract;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;


@Entity
@SuppressWarnings("unused")
public class Article {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ID;

	private String name;
	private Long EAN;

	public Long getEAN() {
		return EAN;
	}

	public void setEAN(Long EAN) {
		this.EAN = EAN;
	}

	private Date adDate;
	private int duration;

	@Contract(pure = true)
	public Article(){}

	public Long getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getAdDate() {
		return adDate;
	}

	public void setAdDate(Date adDate) {
		this.adDate = adDate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}
