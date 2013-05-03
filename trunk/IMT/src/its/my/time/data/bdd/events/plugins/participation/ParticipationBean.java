package its.my.time.data.bdd.events.plugins.participation;


public class ParticipationBean {

	private long id;
	private long eid;
	private long uid;
	private long participation;
	
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUid() {
		return uid;
	}
	
	public void setUid(long uid) {
		this.uid = uid;
	}
	
	public long getParticipation() {
		return participation;
	}
	public void setParticipation(long participation) {
		this.participation = participation;
	}
	public long getEid() {
		return eid;
	}
	public void setEid(long eid) {
		this.eid = eid;
	}
}
