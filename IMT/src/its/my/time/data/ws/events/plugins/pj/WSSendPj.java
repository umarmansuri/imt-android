package its.my.time.data.ws.events.plugins.pj;

import its.my.time.data.bdd.base.BaseRepository;
import its.my.time.data.bdd.events.event.EventBaseRepository;
import its.my.time.data.bdd.events.plugins.pj.PjBean;
import its.my.time.data.bdd.events.plugins.pj.PjRepository;
import its.my.time.data.ws.WSPostBase;
import its.my.time.util.PreferencesUtil;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

public class WSSendPj extends WSPostBase<PjBean>{

	public WSSendPj(Context context, PjBean pj, PostCallback<PjBean> callBack) {
		super(context, pj, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/attachment.json";
	}
	
	@Override
	public BaseRepository<PjBean> getRepository() {
		return new PjRepository(getContext());
	}

	@Override
	public List<NameValuePair> intitialiseParams(List<NameValuePair> nameValuePairs) {
        nameValuePairs.add(new BasicNameValuePair("attachment_id", String.valueOf(getObject().getIdDistant())));
        nameValuePairs.add(new BasicNameValuePair("attachment_name", String.valueOf(getObject().getName())));
        nameValuePairs.add(new BasicNameValuePair("attachment_mime", String.valueOf(getObject().getMime())));
        nameValuePairs.add(new BasicNameValuePair("attachment_base64", String.valueOf(getObject().getBase64())));
        nameValuePairs.add(new BasicNameValuePair("attachment_extension", String.valueOf(getObject().getExtension())));
        nameValuePairs.add(new BasicNameValuePair("attachment_user", String.valueOf(PreferencesUtil.getCurrentUid())));
        nameValuePairs.add(new BasicNameValuePair("attachment_event", String.valueOf(new EventBaseRepository(getContext()).getById(getObject().getEid()).getId())));

        return nameValuePairs;
	}

}
