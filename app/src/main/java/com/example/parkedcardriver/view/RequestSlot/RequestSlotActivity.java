package com.example.parkedcardriver.view.RequestSlot;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
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
import com.example.parkedcardriver.view.auth.AuthActivity;
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
import com.google.android.material.slider.RangeSlider;
import com.google.maps.android.ui.IconGenerator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class RequestSlotActivity extends FragmentActivity implements OnMapReadyCallback, SlotAdapter.SlotItemClickListener {

    @BindView(R.id.selectSpotButton)
    Button selectSpotButton;
    @BindView(R.id.advanceSearchButton)
    Button advanceSearchButton;

    @BindView(R.id.spot_sort_button)
    ImageButton spot_sort_button;
    @BindView(R.id.spot_filter_button)
    ImageButton spot_filter_button;

    ConstraintLayout layout;


    private GoogleMap mMap;

    private SelectedPlaceEvent selectedPlaceEvent;

    SlotModel selectedSlot = null;

    // For fetching searched slot data
    ActivityRequestSlotBinding binding;
    SlotViewModel slotViewModel;

    SlotAdapter slotAdapter;

    ArrayList<SlotModel> listOfSearchedSlots = new ArrayList<>();
    ArrayList<Marker> listOfMarkers = new ArrayList<>();
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
    @RequiresApi(api = Build.VERSION_CODES.O)
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
        layout = findViewById(R.id.RequestSlotActivityConstraintLayout);

        SearchSlotRepository searchSlotRepository = SearchSlotRepository.getInstance();
        slotViewModel = new ViewModelProvider(this).get(SlotViewModel.class);

        slotAdapter = new SlotAdapter(RequestSlotActivity.this, new ArrayList<>());
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(RequestSlotActivity.this, R.anim.layout_animation);
        binding.searchedSlotsRecyclerView.setLayoutAnimation(controller);
        binding.searchedSlotsRecyclerView.setAdapter(slotAdapter);

        slotViewModel.setSearchSlotRepository(searchSlotRepository);
        // Common.quickSearchTime = Common.currentTime(); // Set Current time
        slotViewModel.getSearchedSlots().observe(this, slots->{
            if(slots == null){
//                go to auth activity
                Toast.makeText(getApplicationContext(), "Please Login to Continue", Toast.LENGTH_SHORT).show();
//                slots = new ArrayList<>();
                slotViewModel.setSearchedSlots(new ArrayList<>());
                Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                startActivity(intent);
                return;
            }


            slotAdapter.setSlotModelArrayList(slots);
            slotAdapter.notifyDataSetChanged();

            listOfSearchedSlots = slots;

            for(SlotModel slot: slots){
                addParkHereMarker(new LatLng(slot.getLatitude(), slot.getLongitude()), slot.getAddress());
            }

            slotAdapter.addItemClickListener(new SlotAdapter.SlotItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Toast.makeText(getApplicationContext(),"Click on item: " + position,Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getApplicationContext(), SlotDetailsActivity.class);
//                    intent.putExtra("Slot_Details", listOfSearchedSlots.get(position));
//                    startActivity(intent);
                    selectedSlot = listOfSearchedSlots.get(position);
                    selectSpotButton.setEnabled(true);
                    selectSpotButton.setBackgroundTintList(ColorStateList.valueOf(0xFF018786));

                    // EventBus.getDefault().postSticky(listOfSearchedSlots.get(position));
                }
            });
        });

        initSlotViewModel();

        selectSpotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SlotDetailsActivity.class);
                intent.putExtra("Slot_Details", selectedSlot);
                startActivity(intent);
            }
        });

        spot_sort_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateSortPopUpWindow();
            }
        });

        spot_filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateFilterPopUpWindow();
            }
        });
    }

    private void CreateFilterPopUpWindow() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = layoutInflater.inflate(R.layout.filter_slots_popup, null);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;

        PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);
        layout.post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            }
        });
        Button filter, cancel;
        filter = popUpView.findViewById(R.id.filter_slots_button);
        cancel = popUpView.findViewById(R.id.filter_cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popUpView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RangeSlider priceRangeSlider, distanceRangeSlider;
                CheckBox guard, indoor, cctv;

                priceRangeSlider = popUpView.findViewById(R.id.priceRangeSlider);
                distanceRangeSlider = popUpView.findViewById(R.id.distanceRangeSlider);
                guard = popUpView.findViewById(R.id.checkBoxGuard);
                indoor = popUpView.findViewById(R.id.checkBoxIndoor);
                cctv = popUpView.findViewById(R.id.checkBoxCCTV);

                ArrayList<SlotModel> newList = new ArrayList<>();
                // Toast.makeText(getApplicationContext(), guard.isChecked() + " " + indoor.isChecked() + " " + cctv.isChecked(), Toast.LENGTH_LONG).show();

                Log.d("PriceRange:" , priceRangeSlider.getValues().get(0) + " " + priceRangeSlider.getValues().get(1));
                Log.d("DistanceRange:" , distanceRangeSlider.getValues().get(0) + " " + distanceRangeSlider.getValues().get(1));

                for(SlotModel slot:listOfSearchedSlots){
                    Double distance = Double.valueOf(Common.df.format(slot.getDistance()/1000));
                    Log.d("PriceRange:" , slot.getPrice() + "");
                    Log.d("DistRange:" , distance + "");
                    if(priceRangeSlider.getValues().get(0) <= slot.getPrice() && slot.getPrice() <= priceRangeSlider.getValues().get(1)){
                        if(distanceRangeSlider.getValues().get(0) <= distance && distance <= distanceRangeSlider.getValues().get(1)){
                            String[] securities = slot.getSecurityMeasures().split("/");
                            // Log.d("Slot Security: " , Arrays.toString(securities));
                            boolean check = true;
//                            for(String security:securities){
//                                Log.d("Security", security);
//                                if(security.equals("cctv") && !cctv.isChecked()){
//                                    check = false;
//                                    break;
//                                }
//                                if(security.equals("guard") && !guard.isChecked()){
//                                    check = false;
//                                    break;
//                                }
//                                if(security.equals("indoor") && !indoor.isChecked()){
//                                    check = false;
//                                    break;
//                                }
//                            }
                            if(guard.isChecked() && !Arrays.asList(securities).contains("guard")){
                                check = false;
                            }
                            if(indoor.isChecked() && !Arrays.asList(securities).contains("indoor")){
                                check = false;
                            }
                            if(cctv.isChecked() && !Arrays.asList(securities).contains("cctv")){
                                check = false;
                            }
                            if(check){
                                newList.add(slot);
                            }
                        }
                    }
                }

                slotAdapter.setSlotModelArrayList(newList);
                slotAdapter.notifyDataSetChanged();

                popupWindow.dismiss();
            }
        });
    }

    private void CreateSortPopUpWindow() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = layoutInflater.inflate(R.layout.sort_slots_popup, null);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;

        PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);
        layout.post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            }
        });
        Button sort, cancel;
        sort = popUpView.findViewById(R.id.sort_slots_button);
        cancel = popUpView.findViewById(R.id.sort_cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popUpView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
        sort.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                RadioButton distance, fare, rating;
                RadioButton ascending, descending;
                distance = popUpView.findViewById(R.id.sort_by_distance);
                fare = popUpView.findViewById(R.id.sort_by_fare);
                rating = popUpView.findViewById(R.id.sort_by_rating);

                ascending = popUpView.findViewById(R.id.sort_by_ascending);
                descending = popUpView.findViewById(R.id.sort_by_descending);

                if(!distance.isChecked() && !fare.isChecked() && !rating.isChecked()){
                    Toast.makeText(getApplicationContext(), "Please select a sorting criteria!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!ascending.isChecked() && !descending.isChecked()){
                    Toast.makeText(getApplicationContext(), "Please select a sorting option!!", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    ArrayList<SlotModel> newList = new ArrayList<>();
                    newList = listOfSearchedSlots;

                    if(distance.isChecked()){
                        Collections.sort(newList, new Comparator<SlotModel>() {
                            @Override
                            public int compare(SlotModel o1, SlotModel o2) {
                                if(Objects.equals(o1.getDistance(), o2.getDistance())){
                                    return o1.getPrice().compareTo(o2.getPrice());
                                }
                                return o1.getDistance().compareTo(o2.getDistance());
                            }
                        });
                    }
                    else if(fare.isChecked()){
                        Collections.sort(newList, new Comparator<SlotModel>() {
                            @Override
                            public int compare(SlotModel o1, SlotModel o2) {
                                if(Objects.equals(o1.getPrice(), o2.getPrice())){
                                    return o1.getDistance().compareTo(o2.getDistance());
                                }
                                return o1.getPrice().compareTo(o2.getPrice());
                            }
                        });
                    }
                    else if(rating.isChecked()){
                        Collections.sort(newList, new Comparator<SlotModel>() {
                            @Override
                            public int compare(SlotModel o1, SlotModel o2) {
                                if(Objects.equals(o1.getRating(), o2.getRating())){
                                    return o1.getPrice().compareTo(o2.getPrice());
                                }
                                return o1.getRating().compareTo(o2.getRating());
                            }
                        });
                    }

                    if(ascending.isChecked()){
                        if(!distance.isChecked() && !fare.isChecked() && !rating.isChecked()){
                            Collections.sort(newList, new Comparator<SlotModel>() {
                                @Override
                                public int compare(SlotModel o1, SlotModel o2) {
                                    return o1.getPrice().compareTo(o2.getPrice());
                                }
                            });
                        }
                    }
                    if(descending.isChecked()){
                        Collections.reverse(newList);
                    }

                    slotAdapter.setSlotModelArrayList(newList);
                    slotAdapter.notifyDataSetChanged();

                    popupWindow.dismiss();
                }
            }
        });
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
        ButterKnife.bind(this, getWindow().getDecorView().getRootView());
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

        if(!selectedPlaceEvent.getOriginString().equals(selectedPlaceEvent.getDestinationString())){
            drawPath(selectedPlaceEvent);
        }
        else{
            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                    .include(selectedPlaceEvent.getOrigin())
                    .include(selectedPlaceEvent.getDestination())
                    .build();

            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 160));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(mMap.getCameraPosition().zoom - 1));
        }



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

        listOfMarkers.add(mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                .position(latlng)));

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

    @Override
    public void onItemClick(int position) {
        // Nothing to do
    }
}