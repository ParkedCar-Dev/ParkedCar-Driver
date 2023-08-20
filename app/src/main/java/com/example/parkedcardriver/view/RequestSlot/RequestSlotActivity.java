package com.example.parkedcardriver.view.RequestSlot;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkedcardriver.Adapters.SlotAdapter;
import com.example.parkedcardriver.Common.Common;
import com.example.parkedcardriver.Model.Event.SelectedPlaceEvent;
import com.example.parkedcardriver.Model.SlotModel;
import com.example.parkedcardriver.R;
import com.example.parkedcardriver.Repository.SearchSlotRepository;
import com.example.parkedcardriver.ViewModel.Remote.IGoogleAPI;
import com.example.parkedcardriver.ViewModel.Remote.RetrofitClient;
import com.example.parkedcardriver.ViewModel.SlotViewModel;
import com.example.parkedcardriver.databinding.ActivityRequestSlotBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.maps.android.ui.IconGenerator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class RequestSlotActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private SelectedPlaceEvent selectedPlaceEvent;

    // For fetching searched slot data
    ActivityRequestSlotBinding binding;
    SlotViewModel slotViewModel;

    SlotAdapter slotAdapter;
    // -------------------------------

    private View view;

    // Routes
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private IGoogleAPI iGoogleAPI;
    private Polyline blackPolyline, greyPolyline;
    private PolylineOptions polylineOptions, blackPolylineOptions;
    private List<LatLng> polylineList;

    private Marker originMarker, destinationMarker;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        slotViewModel.clearComposite();
        super.onStop();
        if(EventBus.getDefault().hasSubscriberForEvent(SelectedPlaceEvent.class)){
            EventBus.getDefault().removeStickyEvent(SelectedPlaceEvent.class);
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        slotViewModel.clearComposite();
        super.onDestroy();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onSelectedPlaceEvent(SelectedPlaceEvent event)
    {
        selectedPlaceEvent = event;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.activity_request_slot);

        init();
        bindingView();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SearchSlotRepository searchSlotRepository = new SearchSlotRepository();
        slotViewModel = new ViewModelProvider(this).get(SlotViewModel.class);

        slotAdapter = new SlotAdapter(RequestSlotActivity.this, new ArrayList<>());
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(RequestSlotActivity.this, R.anim.layout_animation);
        binding.searchedSlotsRecyclerView.setLayoutAnimation(controller);
        binding.searchedSlotsRecyclerView.setAdapter(slotAdapter);

        slotViewModel.setSearchSlotRepository(searchSlotRepository);
        slotViewModel.getSearchedSlots().observe(this, slots->{
            slotAdapter.setSlotModelArrayList(slots);
            slotAdapter.notifyDataSetChanged();

            for(SlotModel slot: slots){
                addParkHereMarker(new LatLng(slot.getLatitude(), slot.getLongitude()), slot.getAddress());
            }
        });

        initSlotViewModel();
    }


//    private void loadSlotData() {
//        slotViewModel.searchSlots().observe(this, slots->{
//            slotAdapter = new SlotAdapter(RequestSlotActivity.this, slots);
//            LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(RequestSlotActivity.this, R.anim.layout_animation);
//            binding.searchedSlotsRecyclerView.setLayoutAnimation(controller);
//            binding.searchedSlotsRecyclerView.setAdapter(slotAdapter);
//        });
//    }

    private void initSlotViewModel() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                String destination = selectedPlaceEvent.getDestinationString();
                Double latitude = Double.parseDouble(destination.split(",")[0]);
                Double longitude = Double.parseDouble(destination.split(",")[1]);
                slotViewModel.setLatitude(latitude);
                slotViewModel.setLongitude(longitude);

                // Get city name
                // Get the address from lat & lng
                String city = null;
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addressList;
                try {
                    addressList = geocoder.getFromLocation(latitude,
                            longitude, 1);
                    if (addressList != null) {
                        //String address = addressList.get(0).getAddressLine(0);
                        city = addressList.get(0).getLocality();
                        //String state = addressList.get(0).getAdminArea();
                        //String zip = addressList.get(0).getPostalCode();
                        //String country = addressList.get(0).getCountryName();

                        Log.d("Destination City",  city);
                    }
                } catch (IOException e) {

                }

                slotViewModel.setCity(city);
                slotViewModel.setLatitude(latitude);
                slotViewModel.setLongitude(longitude);
                Log.d("Request Slot", "Search Slots");
                slotViewModel.searchSlots();
                Log.d("Request Slot", "Search Slots after request");
//                loadSlotData();
            }
        }, 1000);
    }

    private void bindingView() {
        binding = ActivityRequestSlotBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.searchedSlotsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.searchedSlotsRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getString(R.string.permission_required), Toast.LENGTH_SHORT).show();
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedPlaceEvent.getOrigin(), 18f));
            }
        });

        drawPath(selectedPlaceEvent);

        View locationButton = ((View) findViewById(Integer.parseInt("1"))
                .getParent()).findViewById(Integer.parseInt("2"));

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // Right Bottom
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.setMargins(0, 350, 35, 0);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.uber_maps_style));
            if (!success) {
                Toast.makeText(this, "Load map style failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Resources.NotFoundException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void drawPath(SelectedPlaceEvent selectedPlaceEvent) {
        compositeDisposable.add(iGoogleAPI.getDirections("driving",
                "less_driving",
                selectedPlaceEvent.getOriginString(),
                selectedPlaceEvent.getDestinationString(),
                getString(R.string.google_maps_key))
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(returnResult -> {
                    try{
                        JSONObject jsonObject = new JSONObject(returnResult);
                        JSONArray jsonArray = jsonObject.getJSONArray("routes");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject route = jsonArray.getJSONObject(i);
                            JSONObject poly = route.getJSONObject("overview_polyline");
                            String polyline = poly.getString("points");
                            polylineList = Common.decodePoly(polyline);
                        }

                        polylineOptions = new PolylineOptions();
                        polylineOptions.color(Color.GRAY);
                        polylineOptions.width(12);
                        polylineOptions.startCap(new SquareCap());
                        polylineOptions.jointType(JointType.ROUND);
                        polylineOptions.addAll(polylineList);
                        greyPolyline = mMap.addPolyline(polylineOptions);

                        blackPolylineOptions = new PolylineOptions();
                        blackPolylineOptions.color(Color.BLACK);
                        blackPolylineOptions.width(5);
                        blackPolylineOptions.startCap(new SquareCap());
                        blackPolylineOptions.jointType(JointType.ROUND);
                        blackPolylineOptions.addAll(polylineList);
                        blackPolyline = mMap.addPolyline(blackPolylineOptions);

                        // Animator
                        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,1);
                        valueAnimator.setDuration(1100);
                        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
                        valueAnimator.setInterpolator(new LinearInterpolator());
                        valueAnimator.addUpdateListener(value -> {
                            List<LatLng> points = greyPolyline.getPoints();
                            int percentValue = (int)value.getAnimatedValue();
                            int size = points.size();
                            int newPoints = (int)(size * (percentValue/100f));
                            List<LatLng> p = points.subList(0, newPoints);
                            blackPolyline.setPoints(p);
                        });

                        valueAnimator.start();

                        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                .include(selectedPlaceEvent.getOrigin())
                                .include(selectedPlaceEvent.getDestination())
                                .build();

                        // Add Car icon at origin
                        JSONObject object = jsonArray.getJSONObject(0);
                        JSONArray legs = object.getJSONArray("legs");
                        JSONObject legsObjects = legs.getJSONObject(0);

                        JSONObject time = legsObjects.getJSONObject("duration");
                        String duration = time.getString("text");

                        String start_address = legsObjects.getString("start_address");
                        String end_address = legsObjects.getString("end_address");

                        addOriginMarker(duration, start_address);
                        addDestinationMarker(end_address);

                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 160));
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(mMap.getCameraPosition().zoom - 1));
                    }
                    catch (Exception e){
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }

    @SuppressLint("PotentialBehaviorOverride")
    private void addParkHereMarker(LatLng latlng, String address){
        View view = getLayoutInflater().inflate(R.layout.park_here, null);

        TextView txt_park_here = (TextView) view.findViewById(R.id.txt_park_here);

        txt_park_here.setText(address);

        // Create icon for marker
        IconGenerator generator = new IconGenerator(this);
        generator.setContentView(view);
        generator.setBackground(new ColorDrawable(Color.TRANSPARENT));
        Bitmap icon = generator.makeIcon();

        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                .position(latlng));

    }

    private void addDestinationMarker(String end_address) {
        View view = getLayoutInflater().inflate(R.layout.destination_info_box, null);

        TextView txt_destination = (TextView) view.findViewById(R.id.txt_destination);

        txt_destination.setText(Common.formatAddress(end_address));

        // Create icon for marker
        IconGenerator generator = new IconGenerator(this);
        generator.setContentView(view);
        generator.setBackground(new ColorDrawable(Color.TRANSPARENT));
        Bitmap icon = generator.makeIcon();

        destinationMarker = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                .position(selectedPlaceEvent.getDestination()));
    }

    private void addOriginMarker(String duration, String start_address) {
        View view = getLayoutInflater().inflate(R.layout.origin_info_box, null);

        TextView txt_time = (TextView) view.findViewById(R.id.txt_time);
        TextView txt_origin = (TextView) view.findViewById(R.id.txt_origin);

        txt_time.setText(Common.formatDuration(duration));
        txt_origin.setText(Common.formatAddress(start_address));

        // Create icon for marker
        IconGenerator generator = new IconGenerator(this);
        generator.setContentView(view);
        generator.setBackground(new ColorDrawable(Color.TRANSPARENT));
        Bitmap icon = generator.makeIcon();

        originMarker = mMap.addMarker(new MarkerOptions()
            .icon(BitmapDescriptorFactory.fromBitmap(icon))
            .position(selectedPlaceEvent.getOrigin()));
    }

    private void init()
    {
        iGoogleAPI = RetrofitClient.getInstance().create(IGoogleAPI.class);
    }
}