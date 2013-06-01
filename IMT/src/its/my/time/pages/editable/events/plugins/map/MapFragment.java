package its.my.time.pages.editable.events.plugins.map;

import its.my.time.R;
import its.my.time.pages.editable.events.meeting.MeetingActivity;
import its.my.time.pages.editable.events.plugins.BasePluginMapFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


public class MapFragment extends BasePluginMapFragment implements OnCheckedChangeListener, OnInfoWindowClickListener, OnMarkerClickListener{



	private PolylineOptions currentRouteOptions;
	private Polyline currentRoute;


	protected boolean isLocated;
	private LatLng destination;
	protected LatLng start;

	private String address;
	private LocationManager locationManager;
	private ToggleButton toggleButtonItineraire;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mapView = super.onCreateView(inflater, container, savedInstanceState);
		RelativeLayout root = (RelativeLayout)inflater.inflate(R.layout.activity_event_location,null);
		root.addView(mapView);
		toggleButtonItineraire = (ToggleButton)root.findViewById(R.id.toggleButtonItineraire);
		toggleButtonItineraire.setChecked(false);
		toggleButtonItineraire.setOnCheckedChangeListener(this);
		toggleButtonItineraire.bringToFront();
		root.invalidate();
		return root;
	}

	private LocationListener locationListener = new LocationListener() {
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

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		getMap().setMyLocationEnabled(true);
		getMap().getUiSettings().setAllGesturesEnabled(true);
		getMap().getUiSettings().setCompassEnabled(true);
		getMap().getUiSettings().setZoomControlsEnabled(true);
		getMap().setOnInfoWindowClickListener(this);
		getMap().setOnMarkerClickListener(this);
		getMap().setIndoorEnabled(true);

		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
				if(address.equals(null) || address.equals("")) {
					address = null;
					currentRoute = null;
					currentRouteOptions = null;
					toggleButtonItineraire.setTextOn(toggleButtonItineraire.getTextOff());
					getMap().clear();
				} else {
					new LoadRoute(address).execute();
				}
			}
		}
	}

	@Override
	public void onStop() {
		locationManager.removeUpdates(locationListener);
		super.onStop();
	}

	@Override
	public String getTitle() {
		return "Lieu";
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

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(address == null) {
			if(isChecked) {
				Toast.makeText(getActivity(), "Aucune adresse définie", Toast.LENGTH_SHORT).show();
				buttonView.setChecked(false);
			}
			return;
		}
		if(buttonView == toggleButtonItineraire) {
			if(isChecked) {
				if(currentRouteOptions != null) {
					currentRoute = getMap().addPolyline(currentRouteOptions);
				}
			} else {
				if(currentRoute != null) {
					currentRoute.remove();
				}
			}
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if(toggleButtonItineraire.isChecked()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" +destination.latitude+","+destination.longitude));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
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
				ArrayList<LatLng> points = loadRoute(start, destination);
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

		protected ArrayList<LatLng> loadRoute(LatLng start, final LatLng end) {
			GMapV2Direction md = new GMapV2Direction();
			Document doc = md.getDocument(start, end, GMapV2Direction.MODE_DRIVING);
			ArrayList<LatLng> directionPoint = md.getDirection(doc);
			currentRouteOptions = new PolylineOptions().width(8).color(Color.parseColor("#7f7eed"));

			for(int i = 0 ; i < directionPoint.size() ; i++) {          
				currentRouteOptions.add(directionPoint.get(i));
			}

			final String details = md.getDistanceText(doc) + " en " + md.getDurationText(doc); 
			getActivity().runOnUiThread(new Runnable() {


				@Override
				public void run() {
					if(toggleButtonItineraire.isChecked()) {
						currentRoute = getMap().addPolyline(currentRouteOptions);
					}
					toggleButtonItineraire.setTextOn(details);
					getMap().addMarker(
							new MarkerOptions()
							.position(end)
							.title(address)
							.snippet(details)
							);
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
