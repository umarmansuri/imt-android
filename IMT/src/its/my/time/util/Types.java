package its.my.time.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Types {

	public static class Event {
		public static final int BASE = 0;
		public static final int TASK = 1;
		public static final int MEETING = 2;
		public static final int CALL = 3;

	public static String getLabelBy(int id) {
		if(id==0) return "event";
		else if (id == 1) return "task";
		else if (id == 2) return "metting";
		else if (id ==3) return "call";
		else return null;
	}
		
	}

	public static class Comptes {

		public final static Compte GOOGLE = new Compte(0, "Google");
		public final static Compte ERP1 = new Compte(1, "Erp 1");
		public final static Compte ERP2 = new Compte(2, "Erp 2");


		public final static Compte MYTIME = new Compte(3, "My Time");

		public static List<Compte> getAllCompte() {
			List<Compte> all = new ArrayList<Types.Comptes.Compte>();
			all.add(GOOGLE);
			all.add(ERP1);
			all.add(ERP2);
			all.add(MYTIME);
			return all;
		}


		public static List<Compte> getAllCompteEditable() {
			List<Compte> all = new ArrayList<Types.Comptes.Compte>();
			all.add(GOOGLE);
			all.add(ERP1);
			all.add(ERP2);
			return all;
		}

		public static Collection<? extends CharSequence> getAllLabels() {
			List<String> labels = new ArrayList<String>();
			List<Compte> all = getAllCompte();
			for (Compte compte : all) {
				labels.add(compte.label);
			}
			return labels;
		}

		public static Collection<? extends CharSequence> getAllLabelsEditable() {
			List<String> labels = new ArrayList<String>();
			List<Compte> all = getAllCompteEditable();
			for (Compte compte : all) {
				labels.add(compte.label);
			}
			return labels;
		}

		public static int getIdFromLabel(String label) {
			if(label == MYTIME.label) {return MYTIME.id;}
			if(label == GOOGLE.label) {return GOOGLE.id;}
			if(label == ERP1.label) {return ERP1.id;}
			if(label == ERP2.label) {return ERP2.id;}
			return 0;
		}

		public static class Compte {
			public int id;
			public String label;
			public Compte(int id, String label) {
				super();
				this.id = id;
				this.label = label;
			}
		}

	}
}