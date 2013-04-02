package its.my.time.pages.editable.events.plugins.note;

import fr.adrienhugon.richedit.RichEditText;
import its.my.time.data.bdd.events.plugins.note.NoteBean;
import its.my.time.data.bdd.events.plugins.note.NoteRepository;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.util.PreferencesUtil;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NoteFragment extends BasePluginFragment {


	private RichEditText mRichEditText;
	private NoteBean noteBean;
	private NoteRepository noteRepo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mRichEditText = new RichEditText(getActivity());
		mRichEditText.setEnabled(false);
		
		int eid = getParentActivity().getEvent().getId();
		long uid = PreferencesUtil.getCurrentUid(getActivity());
		
		noteRepo = new NoteRepository(getActivity());
		noteBean = noteRepo.getAllByUidEid(eid, uid);
		
		if(noteBean == null) {
			noteBean = new NoteBean();
			noteBean.setEid(eid);
			noteBean.setName("Note");
			noteBean.setUid(uid);
			noteBean.setHtml("");
		}

		mRichEditText.setText(Html.fromHtml(noteBean.getHtml()));
		
		return mRichEditText;
	}

	@Override
	public String getTitle() {
		return "Notes";
	}

	@Override
	public void launchEdit() {
		mRichEditText.setEnabled(true);
		super.launchEdit();
	}

	@Override
	public void launchSave() {
		noteBean.setHtml(mRichEditText.getTextHtml());
		if(noteBean.getId() > -1) {
			noteRepo.updateNote(noteBean);	
		} else {
			noteRepo.insertnote(noteBean);
		}
		mRichEditText.setEnabled(false);
		super.launchSave();
	}

	@Override
	public void launchCancel() {
		mRichEditText.setText(Html.fromHtml(noteBean.getHtml()));
		mRichEditText.setEnabled(false);
		super.launchCancel();
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public boolean isCancelable() {
		return true;
	}

	@Override
	public boolean isSavable() {
		return true;
	}
}
