package its.my.time.pages.editable.events.plugins.map;

import its.my.time.pages.editable.events.meeting.MeetingActivity;
import its.my.time.pages.editable.events.plugins.BasePluginMapFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class MapFragment extends BasePluginMapFragment {

	protected boolean isLocated;
	private LatLng destination;
	protected LatLng start;

	private String address;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		getMap().setMyLocationEnabled(true);
		getMap().getUiSettings().setAllGesturesEnabled(true);
		getMap().getUiSettings().setCompassEnabled(true);
		getMap().getUiSettings().setZoomControlsEnabled(true);


		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				start = new LatLng(location.getLatitude(), location.getLongitude());
				if(!isLocated) {
					getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(start,13f));
				}
				isLocated = true;
			}
			public void onStatusChanged(String provider, int status, Bundle extras) {}
			public void onProviderEnabled(String provider) {}
			public void onProviderDisabled(String provider) {}
		};

		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100, locationListener);
		}
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 100, locationListener);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		if(isVisibleToUser) {
			String newAddress = ((MeetingActivity)getParentActivity()).getMeetingDetails().getAddress();
			if(!newAddress.equals(address)) {
				address = newAddress;
				new LoadRoute(address).execute();
			}
		}
	}

	@Override
	public String getTitle() {
		return "Adresse";
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public boolean isCancelable() {
		return false;
	}

	@Override
	public boolean isSavable() {
		return false;
	}

	private class LoadRoute extends AsyncTask<Void, Void, CameraUpdate> {

		public LoadRoute(String address) {
			MapFragment.this.address = address;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			getMap().clear();
		}

		@Override
		protected CameraUpdate doInBackground(Void... params) {
			destination = retreiveCoordinate(address);
			CameraUpdate cu = null;
			if(destination != null) {
				ArrayList<LatLng> points = showRoute(start, destination);
				if(!isLocated) {
					LatLngBounds.Builder builder = new LatLngBounds.Builder();
					for (LatLng latLng : points) {
						builder.include(latLng);
					}
					LatLngBounds bounds = builder.build();
					int padding = 10;
					cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
				}
			}
			return cu;
		}

		protected ArrayList<LatLng> showRoute(LatLng start, final LatLng end) {
			GMapV2Direction md = new GMapV2Direction();
			Document doc = md.getDocument(start, end, GMapV2Direction.MODE_DRIVING);
			ArrayList<LatLng> directionPoint = md.getDirection(doc);
			final PolylineOptions rectLine = new PolylineOptions().width(8).color(Color.parseColor("#7f7eed"));

			for(int i = 0 ; i < directionPoint.size() ; i++) {          
				rectLine.add(directionPoint.get(i));
			}

			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					getMap().addPolyline(rectLine);
					getMap().addMarker(new MarkerOptions().position(end));
				}
			});

			return directionPoint;
		}

		private LatLng retreiveCoordinate(String address) {
			Geocoder geocoder = new Geocoder(getActivity());  
			List<Address> addresses;
			LatLng result = null;

			try {
				addresses = geocoder.getFromLocationName(address, 1);
				if(addresses.size() > 0) {
					double latitude= addresses.get(0).getLatitude();
					double longitude= addresses.get(0).getLongitude();
					result = new LatLng(latitude, longitude);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onPostExecute(CameraUpdate result) {
			if(result != null) {
				getMap().animateCamera(result);
			}
		}
	}
}
