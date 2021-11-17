package video.rental.demo.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "VIDEO", uniqueConstraints = { @UniqueConstraint(columnNames = { "title" }) })
public class Video {
	@Id
	private String title;
	private Rating videoRating;
	private int priceCode;

	public static final int REGULAR = 1;
	public static final int NEW_RELEASE = 2;
	public static final int CHILDREN = 3;

	private int videoType;
	public static final int VHS = 1;
	public static final int CD = 2;
	public static final int DVD = 3;

	private LocalDate registeredDate;
	private boolean rented;

	public Video() {
	} // for hibernate

	public Video(String title, int videoType, int priceCode, Rating videoRating, LocalDate registeredDate) {
		this.title = title;
		this.videoType = videoType;
		this.priceCode = priceCode;
		this.videoRating = videoRating;
		this.registeredDate = registeredDate;
		this.rented = false;
	}

	public int getLateReturnPointPenalty() {
		int pentalty = 0;
		switch (videoType) {
		case VHS:
			pentalty = 1;
			break;
		case CD:
			pentalty = 2;
			break;
		case DVD:
			pentalty = 3;
			break;
		}
		return pentalty;
	}

	public int getPriceCode() {
		return priceCode;
	}

	public void setPriceCode(int priceCode) {
		this.priceCode = priceCode;
	}

	public String getTitle() {
		return title;
	}

	public Rating getVideoRating() {
		return videoRating;
	}

	public boolean isRented() {
		return rented;
	}

	public void setRented(boolean rented) {
		this.rented = rented;
	}

	public LocalDate getRegisteredDate() {
		return registeredDate;
	}

	public int getVideoType() {
		return videoType;
	}

	public boolean rentFor(Customer customer) {
		if (!isUnderAge(customer)) {
			setRented(true);
			Rental rental = new Rental(this);			
			customer.addRental(rental);
			return true;
		} else {
			return false;
		}
	}

	public boolean isUnderAge(Customer customer) {
		return videoRating.isUnderAge(customer.getDateOfBirth());	
	}
}
