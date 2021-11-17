package video.rental.demo.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

public enum Rating {
	TWELVE(12), FIFTEEN(15), EIGHTEEN(18);
	
	int videoAge;
	
	Rating(int age) {
		this.videoAge = age;
	}
	
	public boolean isUnderAge(LocalDate birth) {
		
		Calendar calDateOfBirth = Calendar.getInstance();
		try {
			calDateOfBirth.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(birth.toString()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// get current date
		Calendar calNow = Calendar.getInstance();
		calNow.setTime(new java.util.Date());

		// calculate age different in years and months
		int ageYr = (calNow.get(Calendar.YEAR) - calDateOfBirth.get(Calendar.YEAR));
		int ageMo = (calNow.get(Calendar.MONTH) - calDateOfBirth.get(Calendar.MONTH));

		// decrement age in years if month difference is negative
		if (ageMo < 0) {
			ageYr--;
		}
		int age = ageYr;
		
		return age < videoAge;
	}
}
