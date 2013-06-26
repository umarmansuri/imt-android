package its.my.time.pages.calendar.base;

import its.my.time.data.bdd.base.BaseRepository.OnObjectChangedListener;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.data.bdd.events.event.EventBaseRepository;
import its.my.time.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
		this.calDeb = (Calendar) calDeb.clone();
		this.calFin = (Calendar) calFin.clone();
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
			if(task == null) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if(isVisible()) {
							task = new CreateView();
							task.execute();
						}
					}
				}, 400);
			}
		}
	}

	@Override
	public void onDestroy() {
		if(task != null) {
			task.cancel(true);
		}
		super.onDestroy();
	}

	private CreateView task;
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

					activity.runOnUiThread(new Runnable() {

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

		@Override
		protected void onCancelled() {

		}

		@Override
		protected void onCancelled(View result) {

		}
	}


	private OnObjectChangedListener<CompteBean> onCompteChangedListener = new OnObjectChangedListener<CompteBean>() {
		@Override public void onObjectAdded(CompteBean object) {
			comptes.put(object.getId(), object);
		}
		@Override public void onObjectDeleted(CompteBean object) {
			comptes.remove(object.getId());
		}
		@Override public void onObjectUpdated(final CompteBean object) {
			final List<View> alreadyDone = new ArrayList<View>();

			for (int i = 0; i < eventViews.size(); i++) {
				int eventId = eventViews.keyAt(i);
				if(events.get(eventId).getCid() == object.getId()) {
					final View v = eventViews.get(eventId);
					if(!alreadyDone.contains(v)) {
						alreadyDone.add(v);
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								new Handler().postDelayed(new Runnable() {

									@Override
									public void run() {
										if(object.isShowed()) {
											showView(v, true);
										} else {
											hideView(v, true);
										}						
									}
								}, 200* (alreadyDone.size()-1));								
							}
						});
					}
				}
			}
		}
	};
	private Activity activity;

	private void showView(View v, boolean withAnim) {
		if(v == null) {
			return;
		}
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

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	private OnObjectChangedListener<EventBaseBean> onEventChangedListener = new OnObjectChangedListener<EventBaseBean>() {
		@Override public void onObjectAdded(final EventBaseBean object) {
			if(object.gethDeb().before(calFin) || object.gethFin().after(calDeb)) {
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							CompteBean compte = comptes.get(object.getCid());
							int visibility = compte.isShowed() ? View.VISIBLE : View.INVISIBLE;
							final View v = baseView.addEvent(object, compte.getColor(), visibility 	);
							events.put(object.getId(), object);
							eventViews.put(object.getId(), v);
							if(compte.isShowed()) {
								showView(v, true);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
		@Override public void onObjectDeleted(final EventBaseBean object) {
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					View v = eventViews.get(object.getId());
					if(v != null) {
						hideView(v, true);
					}
				}
			});
		}
		@Override public void onObjectUpdated(final EventBaseBean object) {
			if(object.gethDeb().before(calFin) || object.gethFin().after(calDeb)) {

				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						View lastView = eventViews.get(object.getId());
						if(lastView != null) {
							hideView(lastView, false);
						}
						View v = baseView.addEvent(object, comptes.get(object.getCid()).getColor(), View.INVISIBLE);
						eventViews.put(object.getId(), v);
						showView(v, true);
					}
				});

			}
		}
	};

	public interface OnViewCreated {
		public void onViewCreated(BaseView v);
	}
}
