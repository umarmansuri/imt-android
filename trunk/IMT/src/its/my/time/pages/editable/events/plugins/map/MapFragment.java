package its.my.time.pages.editable.events.plugins.map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import its.my.time.pages.editable.events.plugins.BasePluginMapFragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;


public class MapFragment extends BasePluginMapFragment {

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
			private boolean isLocated;
			public void onLocationChanged(Location location) {
				if(!isLocated) {
					getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(
							new LatLng(location.getLatitude(), location.getLongitude()), 
							13f));
					isLocated = true;
				}

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

}
