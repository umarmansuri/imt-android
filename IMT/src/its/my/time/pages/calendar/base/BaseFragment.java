package its.my.time.pages.calendar.base;

import its.my.time.data.bdd.base.BaseRepository.OnObjectChangedListener;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.bdd.events.eventBase.EventBaseRepository;
import its.my.time.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.actionbarsherlock.app.SherlockFragment;

public abstract class BaseFragment extends SherlockFragment{

	private FrameLayout frame;
	private BaseView baseView;

	private CompteRepository compteRepo;
	private EventBaseRepository eventBaseRepo;

	private SparseArray<CompteBean> comptes;
	private SparseArray<EventBaseBean> events;
	private SparseArray<View> eventViews = new SparseArray<View>();

	private Calendar calDeb;
	private Calendar calFin;

	public BaseFragment(Calendar calDeb, Calendar calFin) {
		super();
		this.calDeb = calDeb;
		this.calFin = calFin;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		frame = new FrameLayout(getSherlockActivity());
		return frame;
	}




	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					if(isVisible()) {
						new CreateView().execute();
					}
				}
			}, 400);
		}
	}

	protected abstract BaseView createView(OnViewCreated onViewCreated);

	public class CreateView extends AsyncTask<Void, Void, View> {

		private List<CompteBean> listComptes;
		@Override
		protected View doInBackground(Void... params) {
			baseView = createView(new OnViewCreated() {

				@Override
				public void onViewCreated(final BaseView v) {
					if(eventBaseRepo == null) {
						eventBaseRepo = new EventBaseRepository(getSherlockActivity());
					}
					eventBaseRepo.addOnObjectChangedListener(onEventChangedListener);
					if(compteRepo == null) {
						compteRepo = new CompteRepository(getSherlockActivity());
					}
					compteRepo.addOnObjectChangedListener(onCompteChangedListener);
					listComptes = compteRepo.getAllByUid(PreferencesUtil.getCurrentUid());

					if(comptes == null) {
						comptes = new SparseArray<CompteBean>();
						for (CompteBean compteBean : listComptes) {
							comptes.put(compteBean.getId(), compteBean);
						}
					}

					List<EventBaseBean> res = eventBaseRepo.getAllEvents(calDeb, calFin);
					events = new SparseArray<EventBaseBean>();
					for (EventBaseBean eventBaseBean : res) {
						events.put(eventBaseBean.getId(), eventBaseBean);
						CompteBean compte = comptes.get(eventBaseBean.getCid());
						eventViews.put(eventBaseBean.getId(), v.addEvent(eventBaseBean, compte.getColor(), View.INVISIBLE));
					}

					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							v.setVisibility(View.INVISIBLE);
							frame.addView(v);
							Animation anim = new AlphaAnimation(0, 1);
							anim.setDuration(1000);
							anim.setFillAfter(true);
							anim.setInterpolator(new LinearInterpolator());
							v.startAnimation(anim);
							for (CompteBean compteBean : listComptes) {
								if(compteBean.isShowed()) {
									onCompteChangedListener.onObjectUpdated(compteBean);
								}
							}
						}
					});
				}
			});



			return null;
		}
	}


	private OnObjectChangedListener<CompteBean> onCompteChangedListener = new OnObjectChangedListener<CompteBean>() {
		@Override public void onObjectAdded(CompteBean object) {}
		@Override public void onObjectDeleted(CompteBean object) {}
		@Override public void onObjectUpdated(final CompteBean object) {
			List<View> alreadyDone = new ArrayList<View>();

			for (int i = 0; i < eventViews.size(); i++) {
				int eventId = eventViews.keyAt(i);
				if(events.get(eventId).getCid() == object.getId()) {
					final View v = eventViews.get(eventId);
					if(!alreadyDone.contains(v)) {
						alreadyDone.add(v);
						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								if(object.isShowed()) {
									showView(v, true);
								} else {
									hideView(v, true);
								}						
							}
						}, 600* (alreadyDone.size()-1));
					}
				}
			}
		}
	};

	private void showView(View v, boolean withAnim) {
		if(withAnim) {
			AlphaAnimation animIn = new AlphaAnimation(0,1);
			animIn.setDuration(1000);
			animIn.setFillAfter(true);
			v.startAnimation(animIn);
		} else {
			v.setVisibility(View.VISIBLE);
		}
	}

	private void hideView(View v, boolean withAnim) {
		if(withAnim) {			
			AlphaAnimation animIn = new AlphaAnimation(1,0);
			animIn.setDuration(1000);
			animIn.setFillAfter(true);
			v.startAnimation(animIn);
		} else {
			v.setVisibility(View.INVISIBLE);
		}
	}


	private OnObjectChangedListener<EventBaseBean> onEventChangedListener = new OnObjectChangedListener<EventBaseBean>() {
		@Override public void onObjectAdded(EventBaseBean object) {
			CompteBean compte = comptes.get(object.getCid());
			final View v = baseView.addEvent(object, compte.getColor(), View.INVISIBLE);
			eventViews.put(object.getId(), v);
			if(compte.isShowed()) {
				showView(v, true);
			}
		}
		@Override public void onObjectDeleted(EventBaseBean object) {
			View v = eventViews.get(object.getId());
			if(v != null) {
				hideView(v, true);
			}
		}
		@Override public void onObjectUpdated(final EventBaseBean object) {
			View lastView = eventViews.get(object.getId());
			if(lastView != null) {
				hideView(lastView, false);
			}
			View v = baseView.addEvent(object, comptes.get(object.getCid()).getColor(), View.INVISIBLE);
			eventViews.put(object.getId(), v);
			showView(v, true);
		}
	};


	public interface OnViewCreated {
		public void onViewCreated(BaseView v);
	}
}
