
package com.bcbst.benefitchange.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * String tools.
 * 
 * @author s11376y
 */
public class StringTool {
	public static final Logger logger = LogManager.getLogger(StringTool.class.getName());

    private StringTool() {}
    
    public static String toUSDateString(Date date) {
        SimpleDateFormat usDateFormat = new SimpleDateFormat("MM/dd/yyyy");

        if(date == null)
        {
            return null;
        }

        return usDateFormat.format(date);
    }

    /**
     * Get date from string like MM/DD/YYYY
     * 
     * @param usDateString String in MM/DD/YYYY format or null.
     * @return Date, or null if param is null.
     * @throws RuntimeException if can not get date from param.
     */
    public static Date toUSDate(String usDateString)
    {
        SimpleDateFormat usDateFormat = new SimpleDateFormat("MM/dd/yyyy");

        if(usDateString == null || usDateString.length() == 0)
        {
            return null;
        }

        try
        {
            return usDateFormat.parse(usDateString);
        }
        catch(Exception e)
        {
            throw new BenefitChangeException("Date string can not be parsed " + usDateString);
        }
    }

    /**
     * Get date from string.
     * 
     * @param dateString
     * @param format
     * @return Return null if {@code dateString} is null.
     */
    public static Date toDate(String dateString, String format)
    {
        if(dateString == null || dateString.length() == 0)
        {
            return null;
        }

        try
        {
            return new SimpleDateFormat(format).parse(dateString);
        }
        catch(Exception e)
        {
            throw new BenefitChangeException("Date string can not be parsed " + dateString);
        }
    }

    /**
     * Get int from String.
     * 
     * @param v
     * @return
     */
    public static int toInt(String v)
    {
        if(v == null || v.length() == 0)
        {
            throw new IllegalArgumentException();
        }

        return Integer.parseInt(v);
    }

    /**
     * Get Integer from String
     */
    public static Integer toInteger(String v)
    {
        if(v == null || v.length() == 0)
        {
            return null;
        }

        return Integer.parseInt(v);
    }

    /**
     * Get double from String
     */
    public static Double toDouble(String v)
    {
        if(v == null || v.length() == 0)
        {
            return null;
        }

        return Double.parseDouble(v);
    }

    /**
     * Get character from String.
     */
    public static Character toCharacter(String v)
    {
        if(v == null || v.length() == 0)
        {
            return null;
        }

        return Character.valueOf(v.charAt(0));
    }

    /**
     * SQL quote a String
     */
    public static String sqlQuote(String s)
    {
        if (s == null)
        {
            return null;
        }

        int length = s.length();
        char c = 0;

        StringBuilder buffer = new StringBuilder(length + 16);
        buffer.append('\'');

        for(int i = 0; i < length; i++)
        {
            c = s.charAt(i);

            if(c == '\'')
            {
                buffer.append('\'');
            }

            buffer.append(c);
        }

        buffer.append('\'');

        return buffer.toString();
    }

    /**
     * SQL quote a char
     */
    public static String sqlQuote(char c)
    {
        return new StringBuilder(3)
                .append('\'')
                .append(c)
                .append('\'')
                .toString();
    }

    /**
     * SQL quote a date
     */
    public static String sqlQuote(java.sql.Date date)
    {
        if (date == null)
        {
            return null;
        }

        return "'" + date + "'";
    }
    
    /**
     * SQL quote a time
     */
    public static String sqlQuote(java.sql.Time time)
    {
        if (time == null)
        {
            return null;
        }

        return "'" + time + "'";
    }

    /**
     * SQL quote a time stamp
     */
    public static String sqlQuote(java.sql.Timestamp timestamp)
    {
        if (timestamp == null)
        {
            return null;
        }

        return "'" + timestamp + "'";
    }

    /**
     * Join a list into a string.
     * joint({"A", "B"}, "-") = "A-B"
     * 
     * @param items items to be joined
     * @param joinedBy use it to join.
     * @return String or null if param items is null.
     */
    public static String join(List<String> items, String joinedBy)
    {
        if(items == null)
        {
            return null;
        }

        int size = items.size();

        if(size == 0)
        {
            return "";
        }

        StringBuilder bd = new StringBuilder();
        bd.append(items.get(0));

        for(int i = 1; i < size; i++)
        {
            bd.append(joinedBy).append(items.get(i));
        }

        return bd.toString();
    }
    
    
 
	/**
	 * @objective converts date to string
	 * @param date
	 * @param dateFormatPattern
	 * @return
	 * @author v82473n
	 */
	public static String getDateString(Date date, String dateFormatPattern) {
		String dateAsString = null;
		if (date == null || dateFormatPattern == null)
			return null;
		try{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatPattern);
			dateAsString = simpleDateFormat.format(date);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return dateAsString;
	}
	
	
	/**
	 *  @objective converts string to date
	 * @param dateString
	 * @param dateFormatPattern
	 * @return
	 * @author v82473n
	 */
	public static Date getDate(String dateString, String dateFormatPattern) {
		Date validDate = null;
		if (dateString == null || dateFormatPattern == null)
			return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		try {
			dateFormat.applyPattern(dateFormatPattern);
			dateFormat.setLenient(false);
			validDate = dateFormat.parse(dateString);
		} catch (Exception e) {
			logger.error("", e);
		}
		return validDate;
	}
	
	/**
	 * @objective converts XMLGregorianCalendar to the give dateformat string.
	 * @param xmlGregorianCalendar
	 * @param dateFormatPattern
	 * @return
	 * @author v82473n
	 */
	public static String getDateStringFromXMLGregorianCalendar(XMLGregorianCalendar xmlGregorianCalendar, String dateFormatPattern){
		Date convertedDate = null;
		try{
			convertedDate = xmlGregorianCalendar.toGregorianCalendar().getTime();
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return getDateString(convertedDate, dateFormatPattern);		
	}
	
	public static int compareDates(String dateOne, String dateTwo, String format) {
			
			Date firstDate = getDate(dateOne, format);
			Date secondDate = getDate(dateTwo, format);
			
			Calendar calOne = Calendar.getInstance();
			calOne.setTime(firstDate);
	
			Calendar calTwo = Calendar.getInstance();
			calTwo.setTime(secondDate);
			
			if(calOne.after(calTwo)){
				return 1;
			}else if (calOne.before(calTwo)){
				return -1;
			}else{
				return 0;
			}
		}
	
	/**
	 * @objective This method validates given dates if they are in 2 year date range.
	 * @param dateOne
	 * @param dateTwo
	 * @param format
	 * @return -1 = Dates are not in 2 years date range. 5 = Invalid Dates Entered.
	 */
	public static int validate2YearDateRange(XMLGregorianCalendar dateOne,
			XMLGregorianCalendar dateTwo) {
		try {
			GregorianCalendar startDate = new GregorianCalendar();
			startDate.setTime(dateOne.toGregorianCalendar().getTime());

			GregorianCalendar endDate = new GregorianCalendar();
			endDate.setTime(dateTwo.toGregorianCalendar().getTime());

			startDate.add(Calendar.YEAR, 2);
			if (startDate.after(endDate)) {
				return 1;
			} else if (startDate.before(endDate)) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return 5;// return invalid code
		}
	}

	/**
	 * @objective This method returns claim status description based upon codes
	 *            for input parameters.
	 * @Note ESI Claim Status Descriptions are different from CBDW Claim Status.
	 *       ESI will not recognize CBDW Claim Status like CAPTURED etc.
	 * @param statusCode
	 * @return
	 * @author v82473n
	 */
	public static String getClaimStatus(int statusCode) {
		String claimsStatus = null;
		switch (statusCode) {
				case 1:
					claimsStatus = "PAID";
					break;
				case 2:
					claimsStatus = "REJECTED";
					break;
				case 3:
					claimsStatus = "REVERSED";
					break;
				case 4:
					claimsStatus = "ADJUSTED";
					break;				
				default:
					claimsStatus = "";
		}
		return claimsStatus;
	}

	/**
	 * @objective This method returns claim status description based upon codes
	 *            for CBDW Claim Status Codes.
	 * @Note ESI Claim Status Descriptions are different from CBDW Claim Status.
	 *       ESI will not recognize CBDW Claim Status like CAPTURED etc.
	 * @param statusCode
	 * @return
	 * @author v82473n
	 */
	public static String getCBDWClaimStatusDescr(int statusCode) {
		String claimsStatus = null;
		switch (statusCode) {
				case 1:
					claimsStatus = "PAID";
					break;
				case 2:
					claimsStatus = "REJECTED";
					break;
				case 3:
					claimsStatus = "REVERSED";
					break;
				case 4:
					claimsStatus = "ADJUSTED";
					break;
				case 5:
					claimsStatus = "CAPTURED";
					break;
				case 6:
					claimsStatus = "REVERSE CAPTURED";
					break;
				default:
					claimsStatus = "";
		}
		return claimsStatus;
	}
	

	
	/**
	 * @objective This method will trim the spaces if not null.
	 * @param string
	 * @return
	 * @author v82473n
	 */
	public static String trimSpaces(String string) {
		return string != null ? string.trim() : string;
	}
	
	/**
	 * @objective This method will check if the string is null or empty.
	 * @param string
	 * @return true if empty
	 * @author a39663c
	 */
	public static boolean isEmpty(String string) {
		return (string == null || (string.trim().equalsIgnoreCase("")));
	}
	
	
	/**
	 * @objective This method will add 0 in front of given string untill final String length is equal to specified in input Param.
	 * @param string
	 * @return String padded with 0s on Left
	 * @author v45716m
	 */
	public static String leftPadWithZeros(String inpString, char padChar, int finaLength) {
		if(inpString!=null) {
			int padCharsLength = finaLength-inpString.length();
			
			if(padCharsLength>0) {
				char[] padChars = new char[padCharsLength];
				
				Arrays.fill(padChars, padChar);
				
				return new String(padChars) + inpString;		
			}
		}

		return inpString;
	}
	

	/**
	 * @objective Thsi method calculates age as of the given date. 
	 * @param calendar
	 * @param asofDate
	 * @return
	 * @author v82473n
	 */
	public static int getAge(XMLGregorianCalendar calendar, Calendar asofDate) {
		int age = 0;
		if (calendar == null || asofDate == null) {
			return age;
		}
		try {
			Calendar dob = Calendar.getInstance();
			dob.setTime(calendar.toGregorianCalendar().getTime());
			age = asofDate.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
			if (asofDate.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR))
				age--;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return age;
	}

	public static java.sql.Date getSQLDate(String dateString, String dateFormatPattern) {
		Date validDate = null;
		if (dateString == null || dateFormatPattern == null)
			return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		try {
			dateFormat.applyPattern(dateFormatPattern);
			dateFormat.setLenient(false);
			validDate = dateFormat.parse(dateString);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);

			return null;
		}
		
		return new java.sql.Date(validDate.getTime());
	}
}
