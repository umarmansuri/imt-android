package its.my.time.util;

import its.my.time.data.bdd.events.eventBase.EventBaseBean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtil {

	public static final int ALTERNATIVEDATESELECTOR_ID = 1;
	public static final int CUSTOMDATESELECTOR_ID = 2;
	public static final int DATETIMESELECTOR_ID = 5;
	public static final int DEFAULTDATESELECTOR_ID = 0;
	public static final int FIRST_DAY = 2;
	public static String FORMAT_DATE_ISO = "yyyy-MM-dd HH:mm:ss";

	public static final int MONTHYEARDATESELECTOR_ID = 3;

	public static final int TIMESELECTOR_ID = 4;

	public static Calendar addMonth(Calendar date, int numMonth) {
		date.add(Calendar.MONTH, numMonth);
		return date;
	}

	public static String getDate(Calendar cal) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(cal.getTime());
	}

	public static GregorianCalendar getDateFromISO(String strDate) {
		final DateFormat f = new SimpleDateFormat(FORMAT_DATE_ISO);
		final GregorianCalendar res = new GregorianCalendar();
		try {
			res.setTime(f.parse(strDate));
		} catch (final ParseException e) {
		}
		return res;
	}

	public static String getDay(Calendar cal) {

		final SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMMM yyyy");

		final String res = sdf.format(cal.getTime());
		final char[] chars = res.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);

		return new String(chars);
	}

	/**
	 * 
	 * @param day
	 *            de type Entier (utiliser Calendar.Monday ...
	 * @return Retourne, le jour sous la forme 'JJJ' (ex: "Lun", "Mar")
	 * 
	 */
	public static String getDay(int day) {
		final GregorianCalendar leJour = new GregorianCalendar();
		leJour.set(Calendar.DAY_OF_WEEK, day);
		final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE");
		return dateFormat.format(leJour.getTime());
	}

	/**
	 * 
	 * @param cal
	 *            La date a retourner
	 * @return Un string sous la forme "3.4.2012 12h30"
	 */
	public static String getDayHour(Calendar cal) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("d.M.y H:m");
		return dateFormat.format(cal.getTime());
	}

	public static String getDayHourFrench(Calendar cal) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		return dateFormat.format(cal.getTime());
	}

	public static CharSequence getHourLabelLong(Calendar cal) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("à HH'h'mm");
		return dateFormat.format(cal.getTime());
	}
	
	public static CharSequence getHourLabel(Calendar cal) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		return dateFormat.format(cal.getTime());
	}

	public static CharSequence getHourLabel(Calendar start, Calendar end) {
		final StringBuilder hourLabel = new StringBuilder();
		hourLabel.append(DateUtil.getHourLabel(start));
		if (end != null) {
			hourLabel.append(" - " + DateUtil.getHourLabel(end));
		}
		return hourLabel.toString();
	}

	public static CharSequence getLittleDate(Calendar cal) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
		return dateFormat.format(cal.getTime());
	}

	/**
	 * 
	 * @param cal
	 *            La date a retourner
	 * @return Un string sous la forme "Mar. 3 Avril 2012"
	 */
	public static String getLongDate(Calendar cal) {
		String leJour = getDay(cal.get(Calendar.DAY_OF_WEEK));
		leJour = Character.toUpperCase(leJour.charAt(0))
				+ leJour.substring(1, leJour.length());
		final String leMois = getMonth(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH));
		final String res = leJour + " " + cal.get(Calendar.DAY_OF_MONTH) + " "
				+ leMois;
		return res;
	}

	/**
	 * 
	 * @param cal
	 *            La date a retourner
	 * @return Un string sous la forme "Mar. 3 Avril 2012"
	 */
	public static String getLongDateTime(Calendar cal) {
		String leJour = getDay(cal.get(Calendar.DAY_OF_WEEK));
		leJour = Character.toUpperCase(leJour.charAt(0))
				+ leJour.substring(1, leJour.length());
		final String leMois = getMonth(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH));
		final String res = leJour + " " + cal.get(Calendar.DAY_OF_MONTH) + " "
				+ leMois + " à " + cal.get(Calendar.HOUR_OF_DAY) + ":"
				+ cal.get(Calendar.MINUTE);
		return res;
	}

	/**
	 * 
	 * @param numYear
	 * @param numMonth
	 * @return Renvoi le mois sous la forme Mois Anne (ex: "Mars 2012")
	 */
	public static String getMonth(int numYear, int numMonth) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
		final String res = dateFormat.format(new GregorianCalendar(numYear,
				numMonth, 1).getTime());
		final char[] chars = res.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		return new String(chars);
	}

	public static float getNbHeure(Calendar hDeb, Calendar hFin, Calendar day) {
		long millisecondsDeb;
		long millisecondsFin;

		final GregorianCalendar dayDeb = new GregorianCalendar(
				day.get(Calendar.YEAR), day.get(Calendar.MONTH),
				day.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

		final GregorianCalendar dayFin = new GregorianCalendar(
				day.get(Calendar.YEAR), day.get(Calendar.MONTH),
				day.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		dayFin.add(Calendar.DAY_OF_MONTH, 1);

		if (hDeb.before(dayDeb) && hFin.after(dayFin)) {
			return 24;
		}
		if (hDeb.before(dayDeb)) {
			millisecondsDeb = dayDeb.getTimeInMillis();
			millisecondsFin = hFin.getTimeInMillis();
		} else if (hFin.after(dayFin)) {
			millisecondsDeb = hDeb.getTimeInMillis();
			millisecondsFin = dayFin.getTimeInMillis();
		} else {
			millisecondsDeb = hDeb.getTimeInMillis();
			millisecondsFin = hFin.getTimeInMillis();
		}
		final long diff = millisecondsFin - millisecondsDeb;
		return (float) diff / (60 * 60 * 1000);
	}

	public static String getTime(Calendar cal) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		return dateFormat.format(cal.getTime());
	}

	public static String getTimeInIso(Calendar cal) {
		if (cal == null) {
			return "";
		} else {
			return getTimeInIso(cal, FORMAT_DATE_ISO, TimeZone.getDefault());
		}
	}

	public static String getTimeInIso(Calendar cal, String format, TimeZone tz) {
		if (format == null) {
			format = FORMAT_DATE_ISO;
		}
		if (tz == null) {
			tz = TimeZone.getDefault();
		}
		final DateFormat f = new SimpleDateFormat(format);
		f.setTimeZone(tz);
		return f.format(cal.getTime());
	}

	public static boolean isInDay(Calendar gethDeb, Calendar cal) {
		final GregorianCalendar calBef = new GregorianCalendar(
				cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		final GregorianCalendar calAft = new GregorianCalendar(
				cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		calAft.add(Calendar.DAY_OF_MONTH, 1);
		return calBef.before(gethDeb) && calAft.after(gethDeb);
	}

	/**
	 * Regarde si l'evenement est dans le jour
	 * 
	 * @param event
	 *            l'evenement en question
	 * @param calDay
	 *            Le jour
	 * @return true si l'evenement est dans le jour, sinon false
	 */
	public static boolean isInDay(EventBaseBean event, Calendar calDay) {
		final GregorianCalendar calDayDeb = new GregorianCalendar(
				calDay.get(Calendar.YEAR), calDay.get(Calendar.MONTH),
				calDay.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		final GregorianCalendar calDayFin = new GregorianCalendar(
				calDay.get(Calendar.YEAR), calDay.get(Calendar.MONTH),
				calDay.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		calDayFin.add(Calendar.DAY_OF_MONTH, 1);
		return (event.gethDeb().before(calDayFin) && event.gethFin().after(
				calDayDeb));
	}
	
	/**
	 * Donne le nombre d'années entre une date et la date courante
	 * 
	 * @param Calendar
	 *            la date d'anniversaire par exemple
	 * @return le nombre d'années (l'âge)
	 */
	public static int getNbYears(Calendar d)
	{
	  Calendar curr = Calendar.getInstance();
	  Calendar birth = d;
	  int yeardiff = curr.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
	  curr.add(Calendar.YEAR,-yeardiff);
	  if(birth.after(curr))
	  {
	    yeardiff = yeardiff - 1;
	  }
	  return yeardiff;
	}
	
	
	/**
	 * 
	 * @return une nouvelle instance d'un Calendar, initialisé au 1 janvier 1970 à 0h00:00
	 */
	public static Calendar createCalendar() {
		return new GregorianCalendar(1970, 0, 1,0,0,0);
	}
}
