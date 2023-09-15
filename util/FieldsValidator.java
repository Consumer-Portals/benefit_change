package com.bcbst.benefitchange.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @objective additional validations for the fields.
 * @author v82473n
 *
 */
public class FieldsValidator {
	
	private FieldsValidator() {}
	
	public static boolean isValidUserName(String string){
		boolean result = false;
		Pattern p = Pattern.compile("^[a-zA-Z0-9_]+$");
		Matcher m = p.matcher(string);
		if (m.find())
			result = true;

		return result;
	}
	
	
	//This method checks for bad characters. It's the lowest/default validation
	public static boolean isValid(String input){
		Pattern p = Pattern.compile("<|\"|>|;");
		Matcher m = p.matcher(input);
		return !m.find();
	}
	
	public static boolean isValidDate(String date) {
		if (date == null || date.trim().length() == 0)
			return false;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date testDate = null;

		// we will now try to parse the string into date form
		try {
			testDate = sdf.parse(date);
		}

		// if the format of the string provided doesn't match the format we
		// declared in SimpleDateFormat() we will get an exception

		catch (ParseException e) {
			return false;
		}

		// dateformat.parse will accept any date as long as it's in the format
		// you defined, it simply rolls dates over, for example, december 32
		// becomes jan 1 and december 0 becomes november 30
		// This statement will make sure that once the string
		// has been checked for proper formatting that the date is still the
		// date that was entered, if it's not, we assume that the date is
		// invalid

		return sdf.format(testDate).equals(date);
	} 
	
	
	public static boolean isValidString(String... fields) {
		boolean isValid = false;
		for (String string : fields) {
			Pattern pattern = Pattern.compile("^[a-zA-Z0-9_*\\-\\s]+$");
			Matcher matcher = pattern.matcher(string);
			if (matcher.find()) {
				isValid = true;
			} else {
				return false;
			}
		}
		return isValid;
	}
	
	
	public static boolean isAllLetters(String input) {
		boolean result = false;
		Pattern p = Pattern.compile("^[a-zA-Z]+$");
		Matcher m = p.matcher(input);
		if (m.find())
			result = true;

		return result;
	}

	public static boolean isAllNumbers(String numbers) {
		boolean result = false;
		Pattern p = Pattern.compile("^\\d+$");
		Matcher m = p.matcher(numbers);
		if (m.find())
			result = true;

		return result;
	}
	
	public static boolean isAlphaNumeric(String string){
		boolean result = false;
		Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");
		Matcher m = p.matcher(string);
		if (m.find())
			result = true;

		return result;
	}

	public static boolean isNotEmpty(String string) {
		return (string != null && string.trim().length() > 0);
	}
	
	public static boolean isEmpty(String string){
		return (string == null || string.trim().equals(""));
	}
	
	public static void validateString(StringBuilder invalidFields, String field) {
		if (field!= null && !FieldsValidator.isValidString(field)){
			invalidFields.append(field);
		}
	}

	public static void validate(StringBuilder invalidFields, String field) {
		if (field!= null && !FieldsValidator.isValid(field)){
			invalidFields.append(field);
		}
	}

}