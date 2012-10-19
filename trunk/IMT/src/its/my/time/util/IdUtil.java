package its.my.time.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class IdUtil {

	public static final String KEY_DAY = "KEY_DAY";
	public static final String KEY_MONTH = "KEY_MONTH";
	public static final String KEY_WEEK = "KEY_WEEK";
	public static final String KEY_YEAR = "KEY_YEAR";


	public static final String STRING_DAY = "day_";
	public static final String STRING_MONTH = "month_";

	public static final String STRING_WEEK = "week_";
	public static final String STRING_YEAR = "year_";
	public static final int YEAR_END = 2100;
	public static final int YEAR_START = 2000;

	/**
	 * 
	 * @param id 
	 * L'id a analysé
	 * 
	 * @return Si le jour est trouve, une TreeMap avec, comme clé, les constante KEY de cette classe (YEAR, MONTH et WEEK)
	 * 
	 * Sinon, si le jour n'est pas trouvé, retourne null
	 */
	public static GregorianCalendar getDayFromId(int id){
		for (int k = YEAR_START; k <= YEAR_END; k++){
			for (int i = 0; i <= 11; i++){
				for (int j = 1; j <= 31; j++){
					if (id == k * STRING_YEAR.hashCode() + i * STRING_MONTH.hashCode() + j * STRING_DAY.hashCode()){
						GregorianCalendar cal = new GregorianCalendar(k, i, j, 0, 0, 0);
						return cal;
					}
				}
			}
		}
		return null;
	}


	/**
	 * 
	 * @param year L'année
	 * MonthDisplayHelper.getYear() retourne cette valeur...
	 *  
	 * @param month De 0 à 11 -> MonthDisplayHelper.getMonth() retourne cette valeur...
	 * 
	 * @param day de 1 à 31
	 * 
	 * @return Retourne un ID composé de l'année, du mois et du jour
	 */
	public static int getDayId(int year, int month, int day){
		return (year) * STRING_YEAR.hashCode() + (month) * STRING_MONTH.hashCode() + day * STRING_DAY.hashCode();
	}


	/**
	 * 
	 * @param id 
	 * L'id a analysé
	 * 
	 * @return Si la semaine est trouve, une TreeMap avec, comme clé, les constante KEY de cette classe (YEAR et WEEK)
	 * 
	 * Sinon, si le jour n'est pas trouvé, retourne null
	 */
	public static GregorianCalendar getWeekFromId(int id){
		for (int k = YEAR_START; k <= YEAR_END; k++){
			for (int i = 1; i <= 53; i++){
				if (id == k * STRING_YEAR.hashCode() + i * STRING_WEEK.hashCode()){
					GregorianCalendar cal = new GregorianCalendar();
					cal.set(Calendar.YEAR,k);
					cal.set(Calendar.WEEK_OF_YEAR,i);
					return cal;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param year L'année
	 * MonthDisplayHelper.getYear() retourne cette valeur...
	 *  
	 * @param week
	 * 
	 * @return Retourne un ID composé de l'année et du numéro de semaine
	 */
	public static int getWeekId(int year, int week){
		return year * STRING_YEAR.hashCode() + week * STRING_WEEK.hashCode();
	}
}
