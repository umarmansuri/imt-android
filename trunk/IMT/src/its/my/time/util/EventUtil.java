package its.my.time.util;

import its.my.time.data.bdd.events.event.EventBaseBean;

public class EventUtil {

	/**
	 * 
	 * @param ev1
	 * @param ev2
	 * @return true si les evenement se déroule à un moment ou l'autre en même
	 *         temps
	 */
	public static boolean isAtSameTime(EventBaseBean ev1, EventBaseBean ev2) {
		return ((ev1.gethFin().after(ev2.gethDeb()) && ev1.gethDeb().before(
				ev2.gethFin())));

	}

	/**
	 * 
	 * @param ev1
	 * @param ev2
	 * @return return true si ev1 commence après ev2
	 */
	public static boolean isUnder(EventBaseBean ev1, EventBaseBean ev2) {
		return ev1.gethDeb().after(ev2.gethFin())
				|| ev1.gethDeb().equals(ev2.gethFin());
	}
}
