package com.example.gift_and_go;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Localisation> listLocalisations = new ArrayList<>();
    private String action,nameChasse;
    private Integer idChasse;
    DBBDao BDD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //On récupere la valeur passé en paramétre dans le intent précedent
        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        nameChasse = intent.getStringExtra("name");
        idChasse = intent.getIntExtra("id",0);

        //On récupere les points de Localisation se trouvant en BDD
        BDD = new DBBDao(this);
        BDD.open();
        listLocalisations = BDD.getLocalisationsByIdChasse(idChasse);
        BDD.close();

        //On prépare la map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //On vérifie qu'on a bien accès à la position de l'utilisateur
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            //On affiche le point de l'utilisateur sur la MAP
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);



            //On zoom sur la position de l'utilisateur
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                mMap.animateCamera(cameraUpdate);
            }
        }

        ajoutPointMap(mMap);

        if(action.equals("play")){
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    Boolean updateMap = false;
                    for (Localisation laLocalisation : listLocalisations) {
                        LatLng point = laLocalisation.getLocalisation();
                        if(!laLocalisation.getEtat() && checkIfMarkerReached(point)) {
                            updateMap=true;
                            laLocalisation.setEtat(true);
                            BDD.open();
                            BDD.modifierLocalisation(laLocalisation);
                            BDD.close();
                            Intent camera = new Intent(getApplicationContext(), CameraActivity.class);
                            camera.putExtra("image",laLocalisation.getImage());
                            startActivity(camera);
                        }
                    }
                    if(updateMap){
                        ajoutPointMap(mMap);
                    }
                }
            });
        }else{
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    BDD.open();
                    BDD.supprimerLocalisation((int) marker.getTag());
                    listLocalisations = BDD.getLocalisationsByIdChasse(idChasse);
                    BDD.close();
                    ajoutPointMap(mMap);
                    return false;
                }
            });
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    try {
                        String result = new PostTask().execute().get();
                        try {
                            JSONObject json = new JSONObject(result);
                            JSONArray data = json.getJSONArray("data");
                            String imageUrl = data.getJSONObject(0).getString("url");

                            Glide.with(getApplicationContext())
                                    .asBitmap()
                                    .load(imageUrl)
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            Localisation maNouvelleLocalisation= new Localisation();
                                            maNouvelleLocalisation.setLocalisation(latLng);

                                            String imageBase64 = getBase64FromBitmap(makeTransparent(resource));
                                            maNouvelleLocalisation.setIdChasse(idChasse);
                                            maNouvelleLocalisation.setImage(imageBase64);
                                            maNouvelleLocalisation.setEtat(false);
                                            BDD.open();
                                            maNouvelleLocalisation.setId((int) BDD.ajouterLocalisation(maNouvelleLocalisation));
                                            BDD.close();
                                            listLocalisations.add(maNouvelleLocalisation);
                                            ajoutPointMap(mMap);
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                            // Afficher une erreur si l'image n'a pas pu être chargée
                                            Toast.makeText(getApplicationContext(), "Error loading image", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch ( ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    mMap.addMarker(new MarkerOptions().position(latLng));
                }
            });


        }

    }

    private String getBase64FromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }


    public void ajoutPointMap(GoogleMap mMap){
        mMap.clear();
        for (Localisation localisation : listLocalisations) {
            MarkerOptions marqueur =  new MarkerOptions();
            marqueur.position(localisation.getLocalisation());
            //Si le point à déjà été validé, on l'affiche en vert.
            if(localisation.getEtat()){
                BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                marqueur.icon(icon);
            }
            //On ajoute le marqueur à la map
            Marker marker =mMap.addMarker(marqueur);
            marker.setTag(localisation.getId());
        }
    }

    public static Bitmap makeTransparent(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap transparentBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(transparentBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = transparentBitmap.getPixel(x, y);
                int r = (color >> 16) & 0xff;
                int g = (color >> 8) & 0xff;
                int b = color & 0xff;
                if (r > 250 && g > 250 && b > 250) {
                    // teinte blanche, on rend le pixel transparent
                    color = 0x00000000 | (color & 0x00ffffff);
                    transparentBitmap.setPixel(x, y, color);
                }
            }
        }
        return transparentBitmap;
    }


    public Boolean checkIfMarkerReached(LatLng markerLatLng) {
        Location markerLocation = new Location("Marker");
        markerLocation.setLatitude(markerLatLng.latitude);
        markerLocation.setLongitude(markerLatLng.longitude);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        Location deviceLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(deviceLocation !=null){
            float distance = deviceLocation.distanceTo(markerLocation);
            if (distance < 10) {
                return true;
            }
        }
        return false;
    }

}
