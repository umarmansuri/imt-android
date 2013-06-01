package its.my.time.pages.editable.events.plugins.map;

import its.my.time.pages.editable.events.plugins.BasePluginMapFragment;

import java.util.ArrayList;

import org.w3c.dom.Document;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class MapFragment extends BasePluginMapFragment {

	protected boolean isLocated;

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
				LatLng start = new LatLng(location.getLatitude(), location.getLongitude());
				LatLng end = new LatLng(45.76001, 4.88581);
				ArrayList<LatLng> points = showRoute(start, end);
				if(!isLocated) {
					LatLngBounds.Builder builder = new LatLngBounds.Builder();
					for (LatLng latLng : points) {
						builder.include(latLng);
					}
					LatLngBounds bounds = builder.build();
					int padding = 10;
					CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
					getMap().animateCamera(cu);
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

	protected ArrayList<LatLng> showRoute(LatLng start, LatLng end) {
		getMap().clear();

		GMapV2Direction md = new GMapV2Direction();
		Document doc = md.getDocument(start, end, GMapV2Direction.MODE_DRIVING);
		ArrayList<LatLng> directionPoint = md.getDirection(doc);
		PolylineOptions rectLine = new PolylineOptions().width(8).color(Color.parseColor("#7f7eed"));

		for(int i = 0 ; i < directionPoint.size() ; i++) {          
			rectLine.add(directionPoint.get(i));
		}
		getMap().addPolyline(rectLine);
		getMap().addMarker(new MarkerOptions().position(end));

		return directionPoint;
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
