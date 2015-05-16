package com.example.DTUApp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.example.DTUApp.R;
import com.example.DTUApp.global.constants;
import com.example.DTUApp.global.global_app;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.location.QBLocations;
import com.quickblox.location.model.QBLocation;
import com.quickblox.location.request.QBLocationRequestBuilder;

import java.util.ArrayList;
import java.util.List;

public class map_frag extends base_frag implements LocationListener {

    private Context context;
    //private Resources resources;
    private GoogleMap googleMap;
    private Location lastLocation;
    //private Map<Marker, data> storageMap = new HashMap<Marker, data>();
    //private Marker myMarker;
    private DialogInterface.OnClickListener checkInPositiveButton;
    private DialogInterface.OnClickListener checkInNegativeButton;

    private static boolean bFirstTime = true;
    private Circle mCircle = null;
    private Marker mMarker = null;
    private static double dWalkedDistanceKM = 0;
    private static Location mLocationStart = null;
    private double mRadiusInKM = 2.0;
    private TextView mtv = null;
    private static boolean m_bKeepGoing = false;
    private static float zoomLvl = 15;
    private LocationManager mLocationManager = null;
    private static LatLng mStartPos = null;
    private boolean mIsPopupShowing = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //new GoogleApiClient().Builder
        // fued location provider

        Log.d("GVE", "Create MAP fragment");

        View v = inflater.inflate(R.layout.map_frag,
                container, false);

        // create map
        googleMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map_fragment)).getMap();
        mtv = (TextView) v.findViewById(R.id.txtStatus);
        mRadiusInKM = global_app.GetPref().getInt(constants.KM_TO_WALK, 3);

        initGooglePlayStatus();
        initLocationRequestBuilder();

        global_app.getInstance().AddSubscriber(this);
        return v;
    }

    public void Notify() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
            mLocationManager = null;
        }
        initLocationManager();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("GVE", "Destroy MAP fragment");

        global_app.getInstance().RemoveSubscriber(this);

        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
            mLocationManager = null;
        }
        googleMap = null;
        //resources = null;
        //bFirstTime = true;

        try {
            SupportMapFragment fragment = (SupportMapFragment) getActivity()
                    .getSupportFragmentManager().findFragmentById(
                            R.id.map_fragment);
            if (fragment != null) getFragmentManager().beginTransaction().remove(fragment).commit();

        } catch (IllegalStateException e) {
            //handle this situation because you are necessary will get
            //an exception here :-(
        }
    }

    private void initGooglePlayStatus() {
        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getBaseContext());

        // Showing status
        if (status != ConnectionResult.SUCCESS) {
            // Google Play Services are not available
            int requestCode = constants.PLAY_SERVICE_REQUEST_CODE;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this.getActivity(), requestCode);
            dialog.show();
        } else {
            // Google Play Services are available
            // Init Map
            setUpMapIfNeeded();
            initLocationManager();
        }
    }

    private void initLocationRequestBuilder() {
        // Retrieve other users' locations from QuickBlox
        //
        QBLocationRequestBuilder getLocationsBuilder = new QBLocationRequestBuilder();
        getLocationsBuilder.setPerPage(constants.LOCATION_PER_PAGE);
        getLocationsBuilder.setLastOnly();

        QBLocations.getLocations(getLocationsBuilder, new QBEntityCallbackImpl<ArrayList<QBLocation>>() {
            @Override
            public void onSuccess(ArrayList<QBLocation> qbLocations, Bundle bundle) {
                // show all locations on the map
                //
                for (QBLocation location : qbLocations) {
                    Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(
                            location.getLatitude(), location.getLongitude())).icon(
                            BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));

                    //Data data = new Data(location.getUser().getLogin(), location.getStatus());
                    //storageMap.put(marker, data);
                }
            }

            @Override
            public void onError(List<String> errors) {
                //DialogUtils.showLong(context, resources.getString(R.string.dlg_location_error) + errors);
            }
        });
    }

    private void setUpMapIfNeeded() {
        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map_fragment)).getMap();
            if (googleMap != null) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        return false;
                    }
                });
            }
        }
    }

    private void initLocationManager() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
            mLocationManager = null;
        }
        mLocationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        Location location = null;
        String provider = null;

        if (global_app.GetPref().getBoolean(constants.USEGPS, false)) {
            provider = LocationManager.GPS_PROVIDER;
            Log.d("GVE", "initLocationManager GPS_PROVIDER-----------------------------------------");
        } else {
            provider = LocationManager.NETWORK_PROVIDER;
            Log.d("GVE", "initLocationManager NETWORK_PROVIDER-------------------------------------");
        }

        if (global_app.GetPref().getBoolean(constants.USELASTKNOWNLOCATION, false)){
            location = mLocationManager.getLastKnownLocation(provider);
            Log.d("GVE", "Using last known location-------------------------------------");

            if (location != null) {
                onLocationChanged(location);
            }
        }

        mLocationManager.requestLocationUpdates(provider, constants.LOCATION_MIN_TIME, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        if (bFirstTime) {
            mLocationStart = new Location(location);
            mStartPos = latLng;
            googleMap.setMyLocationEnabled(true);
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            if (mCircle == null || mMarker == null) {
            } else {
                //updateMarkerWithCircle(latLng);
            }

            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            zoomLvl = calculateZoomLevel(dm.widthPixels);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoomLvl));

            bFirstTime = false;
        }

        dWalkedDistanceKM = calc_distance_km(mLocationStart, location);

        if (dWalkedDistanceKM < 1.0) {
            // in meters
            mtv.setText(String.format("Gået afstand %1.0f m\nDu skal gå minimum %1.0f m", 1000 * dWalkedDistanceKM, 1000 * mRadiusInKM));
        } else {
            // in km
            mtv.setText(String.format("Gået afstand %1.1f km\nDu skal gå minimum %1.0f km", dWalkedDistanceKM, mRadiusInKM));
        }

        // clear
        googleMap.clear();

        // start point in green
        MarkerOptions markerOptions = new MarkerOptions().position(mStartPos).icon(
                BitmapDescriptorFactory.fromResource(android.R.drawable.presence_online));
        mMarker = googleMap.addMarker(markerOptions);

        // always redraw this one (circle + blue marker)
        drawMarkerWithCircle(mStartPos);

        // blue marker, marks current position

        Log.d("GVE", "Walked: " + dWalkedDistanceKM + ", needed: " + mRadiusInKM);

        if (dWalkedDistanceKM > mRadiusInKM) {
            if (!m_bKeepGoing) {
                PopupDialog();
            }
        }
    }

    private void PopupDialog() {
        if (!mIsPopupShowing)
        {
            mIsPopupShowing = true;
            new AlertDialog.Builder(this.getActivity())
                    .setTitle("Super!")
                    .setMessage("Du har gået den krævede afstand.\nVil du til det næste skærm?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            m_bKeepGoing = false;
                            ToNextFragment();
                            mIsPopupShowing = false;
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            m_bKeepGoing = true;
                            mIsPopupShowing = false;
                        }
                    })
                    .show();
            {}
            ;
        }
    }

    private double calc_distance_km(Location loc1, Location loc2) {
        return loc1.distanceTo(loc2) / 1000; // km
    }

    private void updateMarkerWithCircle(LatLng position) {
        mCircle.setCenter(position);
        mMarker.setPosition(position);
    }

    private void drawMarkerWithCircle(LatLng position) {
        int strokeColor = 0x200000ff; // outline
        int shadeColor = 0x100000ff; //opaque fill

        CircleOptions circleOptions = new CircleOptions().center(position).radius(1000 * mRadiusInKM).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(1);
        mCircle = googleMap.addCircle(circleOptions);
    }

    private float calculateZoomLevel(int screenWidth) {
        float equatorLength = 40075004 / 2; // in meters, zoom lvl=1, about 1/2 of the world fits on the screen

        // we want to be able to see: 2 * 1000 * mRadiusInKM meters in our screenWidth
        zoomLvl = logX(equatorLength / (2 * 1000 * (float) mRadiusInKM), 2);

        Log.i("GVE", "zoom level = " + zoomLvl);
        return zoomLvl;
    }

    private float logX(float x, int base) {
        return (float) (Math.log(x) / Math.log(base));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

/*    public void onClickButtons(View view) {
*//*        switch (view.getId()) {
            case R.id.check_in_button:

                final EditText input = new EditText(this);
                //input.setTextColor(getResources().getColor(R.color.white));
                initAlertListeners(input);

*//**//*                final Dialog checkInAlert = DialogUtils.createDialog(context, R.string.dlg_check_in,
                        R.string.dlg_enter_message, input, checkInPositiveButton, checkInNegativeButton);*//**//*

                initAlertListeners(input);
                //checkInAlert.show();
        }*//*
    }*/

    private void initAlertListeners(final EditText input) {
        checkInPositiveButton = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // check in
                double lat = lastLocation.getLatitude();
                double lng = lastLocation.getLongitude();

                // ================= QuickBlox ====================
                // Share own location
                QBLocation location = new QBLocation(lat, lng, input.getText().toString());
                QBLocations.createLocation(location, new QBEntityCallbackImpl<QBLocation>() {
                    @Override
                    public void onSuccess(QBLocation qbLocation, Bundle bundle) {
                        //DialogUtils.showLong(context, resources.getString(R.string.dlg_check_in_success));
                    }

                    @Override
                    public void onError(List<String> errors) {
                        //DialogUtils.showLong(context, resources.getString(R.string.dlg_location_error) + errors);
                    }
                });
            }
        };

        checkInNegativeButton = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelled.
            }
        };
    }
}