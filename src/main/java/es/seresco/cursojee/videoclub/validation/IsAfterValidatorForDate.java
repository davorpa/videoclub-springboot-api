package es.seresco.cursojee.videoclub.validation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintDefinitionException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsAfterValidatorForDate implements ConstraintValidator<IsAfter, Date> {

	private Date date;

	@Override
	public void initialize(IsAfter constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
		String dateString = constraintAnnotation.date();
		String pattern    = constraintAnnotation.pattern();

		try {
			DateFormat df = new SimpleDateFormat(pattern);
			df.setLenient(false); // stritct parse

			date = df.parse(dateString);

		} catch (IllegalArgumentException | ParseException e) {
			throw new ConstraintDefinitionException(e);
		}
	}

	@Override
	public boolean isValid(
			final Date value, final ConstraintValidatorContext context)
	{
		// null values are valid
		if (value == null) {
			return true;
		}

		return value.after(date);
	}

}
