package com.example.parkedcardriver.view.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkedcardriver.Common.Common;
import com.example.parkedcardriver.Model.Event.SelectedPlaceEvent;
import com.example.parkedcardriver.ViewModel.Remote.IGoogleAPI;
import com.example.parkedcardriver.ViewModel.Remote.RetrofitClient;
import com.example.parkedcardriver.view.RequestSlot.RequestSlotActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import com.example.parkedcardriver.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    // -----------------
    @BindView(R.id.whereToPage)
    SlidingUpPanelLayout slidingUpPanelLayout;
    @BindView(R.id.txt_welcome)
    TextView txt_welcome;
    @BindView(R.id.saved_work_address_maps)
    TextView txt_work_address;
    @BindView(R.id.saved_near_me_maps)
    TextView txt_near_me;
    @BindView(R.id.near_me_lat_lng)
    TextView near_me_lat_lng;

    private AutocompleteSupportFragment autocompleteSupportFragment;
    private View myView;
    // ----------------------------


    // Location
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private GoogleMap map;
    private SupportMapFragment mapFragment;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private IGoogleAPI iGoogleAPI;

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mapFragment != null)
            getActivity().getSupportFragmentManager().beginTransaction().remove(mapFragment).commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // Check Permissions
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            Snackbar.make(getView(), getString(R.string.permission_required), Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        map.setMyLocationEnabled(true);
                        map.getUiSettings().setMyLocationButtonEnabled(true);
                        map.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
                            @Override
                            public void onMyLocationClick(@NonNull Location location) {
                                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                        ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    Snackbar.make(getView(), getString(R.string.permission_required), Snackbar.LENGTH_SHORT).show();
                                    return;
                                }
                                fusedLocationProviderClient.getLastLocation()
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                                            @Override
                                            public void onSuccess(Location location) {
                                                LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 18f));
                                                System.out.println("eikhane ashche");
                                            }
                                        });
                            }
                        });

                        View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1"))
                                .getParent()).findViewById(Integer.parseInt("2"));

                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                        // Right Bottom
                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                        params.setMargins(0, 0, 35, 350);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(getContext(), "Permission " +
                                permissionDeniedResponse.getPermissionName() + " was denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();

        map.getUiSettings().setZoomControlsEnabled(true);
        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.uber_maps_style));
            if (!success) {
                Log.e("ERROR", "Style parsing error");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("ERROR", e.getMessage());
        }

//            LatLng sydney = new LatLng(-34, 151);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
//    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.myView = view;

        init();
        // ---------------
        initView(view);
        // ----------------

        mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    // --------------------------------
    private void initView(View view) {
        ButterKnife.bind(this, view);
        Common.setWelcomeMessage(txt_welcome);

        /**
         // Here, we are setting the address and LatLng for saved address(Work Address)
         // We will add functionality of adding a favourite place from profile section(optional). The new saved address will be shown in the saved places list.
         // Get the address from lat & lng
         */
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        TextView workLatLng = view.findViewById(R.id.work_address_lat_lng);
        String latLng = (String) workLatLng.getText();
        Double lat_work_address = Double.parseDouble(latLng.split(",")[0]);
        Double lng_work_address = Double.parseDouble(latLng.split(",")[1]);
        List<Address> workAddress = null;
        try {
            workAddress = geocoder.getFromLocation(lat_work_address,
                    lng_work_address, 1);
        } catch (IOException e) {
            Toast.makeText(getContext(), e.getMessage() + "", Toast.LENGTH_LONG).show();
        }
        if (workAddress != null) {
            String address = workAddress.get(0).getAddressLine(0);
            String city = workAddress.get(0).getLocality();
//            String state = workAddress.get(0).getAdminArea();
//            String zip = workAddress.get(0).getPostalCode();
            String country = workAddress.get(0).getCountryName();

            txt_work_address.setText(address + ", " + city + ", " + country);

            Log.d("Full Address", address + ", " + city + ", " + country);
        }

        LinearLayout workAddressLayout = view.findViewById(R.id.linearlayout_work_maps);
        workAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());
                        LatLng destination = new LatLng(lat_work_address, lng_work_address);
                        Common.origin = origin;
                        Common.destination = destination;
                        Common.isQuickSearch = true;
                        startActivity(new Intent(getContext(), RequestSlotActivity.class));
                        EventBus.getDefault().postSticky(new SelectedPlaceEvent(origin, destination));
                    }
                });
            }
        });
    }
    // --------------------------------

    private void setupQuickSearchInfo(){
        /**
         * For nearby me places
         */
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        TextView nearMeLatLng = myView.findViewById(R.id.near_me_lat_lng);
        String nearLatLng = (String) nearMeLatLng.getText();
        Log.d("Near me latlng", nearLatLng);
        Double lat_nearMe_address = Double.parseDouble(nearLatLng.split(",")[0]);
        Double lng_nearMe_address = Double.parseDouble(nearLatLng.split(",")[1]);
        List<Address> nearMeAddress = null;
        try {
            nearMeAddress = geocoder.getFromLocation(lat_nearMe_address,
                    lng_nearMe_address, 1);
        } catch (IOException e) {
            Toast.makeText(getContext(), e.getMessage() + "", Toast.LENGTH_LONG).show();
        }
        if (nearMeAddress != null) {
            String address = nearMeAddress.get(0).getAddressLine(0);
            String city = nearMeAddress.get(0).getLocality();
//            String state = nearMeAddress.get(0).getAdminArea();
//            String zip = nearMeAddress.get(0).getPostalCode();
            String country = nearMeAddress.get(0).getCountryName();

            txt_near_me.setText(address + ", " + city + ", " + country);

            Log.d("Full Address", address + ", " + city + ", " + country);
        }

        LinearLayout nearMeAddressLayout = myView.findViewById(R.id.linearlayout_near_me_maps);
        nearMeAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());
                        LatLng destination = new LatLng(lat_nearMe_address, lng_nearMe_address);
                        Common.origin = origin;
                        Common.destination = destination;
                        Common.isQuickSearch = true;
                        startActivity(new Intent(getContext(), RequestSlotActivity.class));
                        EventBus.getDefault().postSticky(new SelectedPlaceEvent(origin, destination));
                    }
                });
            }
        });
    }

    private void init() {

        iGoogleAPI = RetrofitClient.getInstance().create(IGoogleAPI.class);

        // --------------------------------------------------------------------
        Places.initialize(getContext(), getString(R.string.google_maps_key));
        autocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager()
                .findFragmentById(R.id.autocomplete_fragment);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.ADDRESS,
                Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteSupportFragment.setHint(getString(R.string.where_to_park));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(getContext(), "" + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // Toast.makeText(getContext(), ""+place.getLatLng(), Toast.LENGTH_LONG).show();
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), getString(R.string.permission_required), Toast.LENGTH_LONG).show();
                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());
                        LatLng destination = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                        Common.origin = origin;
                        Common.destination = destination;
                        Common.isQuickSearch = true;
                        startActivity(new Intent(getContext(), RequestSlotActivity.class));
                        EventBus.getDefault().postSticky(new SelectedPlaceEvent(origin, destination));
                    }
                });
            }
        });
        // --------------------------------------------------------------------


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setWaitForAccurateLocation(true)
                .setMinUpdateIntervalMillis(3000)
                .setMinUpdateDistanceMeters(10f)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                LatLng newPosition = new LatLng(locationResult.getLastLocation().getLatitude(),
                        locationResult.getLastLocation().getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 18f));

                // -----------------------
                setRestrictPlacesInCountry(locationResult.getLastLocation());
                // -----------------------

                // Get the address from lat & lng
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                List<Address> addressList;
                try {
                    addressList = geocoder.getFromLocation(locationResult.getLastLocation().getLatitude(),
                            locationResult.getLastLocation().getLongitude(), 1);
                    if (addressList != null) {
                        String address = addressList.get(0).getAddressLine(0);
                        String city = addressList.get(0).getLocality();
                        String state = addressList.get(0).getAdminArea();
                        String zip = addressList.get(0).getPostalCode();
                        String country = addressList.get(0).getCountryName();

                        Log.d("Full Address",  address + ", " + city + ", " + state + ", " + zip + ", " + country);
                    }
                } catch (IOException e) {
                    Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                }

                Log.d("LatAndLng", locationResult.getLastLocation().getLatitude() + " " + locationResult.getLastLocation().getLongitude());
                /**
                 * Quick Search setup
                 * */
                near_me_lat_lng.setText(String.valueOf(locationResult.getLastLocation().getLatitude()) + "," + String.valueOf(locationResult.getLastLocation().getLongitude()));
                setupQuickSearchInfo();
            }
        };

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

//        fusedLocationProviderClient.getLastLocation()
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnSuccessListener(new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // Get the address from lat & lng
//                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
//                        List<Address> addressList;
//                        try {
//                            addressList = geocoder.getFromLocation(location.getLatitude(),
//                                    location.getLongitude(), 1);
//                            if (addressList != null) {
//                                String address = addressList.get(0).getAddressLine(0);
//                                String city = addressList.get(0).getLocality();
//                                String state = addressList.get(0).getAdminArea();
//                                String zip = addressList.get(0).getPostalCode();
//                                String country = addressList.get(0).getCountryName();
//
//                                Log.d("Full Address",  address + ", " + city + ", " + state + ", " + zip + ", " + country);
//                            }
//                        } catch (IOException e) {
//                            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }

    private void setRestrictPlacesInCountry(Location location) {
        try{
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if(addressList.size() > 0){
                autocompleteSupportFragment.setCountries(addressList.get(0).getCountryCode());
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}