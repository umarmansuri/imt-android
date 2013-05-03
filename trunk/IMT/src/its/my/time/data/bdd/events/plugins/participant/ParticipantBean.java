package its.my.time.data.bdd.events.plugins.participant;


public class ParticipantBean {

	private long eid;
	private long uid;
	private int idContactInfo;
	
	public long getUid() {
		return uid;
	}
	
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getEid() {
		return eid;
	}
	public void setEid(long eid) {
		this.eid = eid;
	}
	public int getIdContactInfo() {
		return idContactInfo;
	}
	public void setIdContactInfo(int idContactInfo) {
		this.idContactInfo = idContactInfo;
	}
}
