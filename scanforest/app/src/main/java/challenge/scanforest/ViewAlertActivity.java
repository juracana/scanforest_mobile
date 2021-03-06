package challenge.scanforest;

import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import challenge.scanforest.api.AlertService;
import challenge.scanforest.api.ApiManager;
import challenge.scanforest.models.Alert;
import challenge.scanforest.models.AlertImage;
import challenge.scanforest.utils.CLog;


public class ViewAlertActivity extends ActionBarActivity implements OnMapReadyCallback {

    private static final String TAG= ViewAlertActivity.class.getSimpleName();
    ImageView mAlertImage;
    TextView mLocation, mDescription, mArea, mMagnitude;
    MapFragment mapFragment;
    GoogleMap mGoogleMap;
    Alert alert;

    public static final String ALERT_IDENTIFIER="ALERT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_view_alert_landscape);
        }else{
            setContentView(R.layout.activity_view_alert);
        }
        mAlertImage = (ImageView) findViewById(R.id.alert_image);
//        mLocation = (TextView) findViewById(R.id.tv_location);
        mDescription = (TextView) findViewById(R.id.tv_description);
        mArea = (TextView) findViewById(R.id.tv_area);
        mMagnitude = (TextView) findViewById(R.id.tv_magnitude);
        mapFragment=(com.google.android.gms.maps.MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Bundle  bundle = getIntent().getExtras();
        try {
            alert= (Alert)bundle.get(ALERT_IDENTIFIER);
        }catch (Exception e){
            alert = new Alert();
        }


        mDescription.setText(alert.getDescription());
        mArea.setText(String.valueOf(alert.getArea()));
        mMagnitude.setText(String.valueOf(alert.getMagnitude()));
        try {
            CLog.i(TAG,alert.getImage());
            Picasso.with(this).load(ApiManager.getUrl()+alert.getImage())
                    .resize(200,2000)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher).into(mAlertImage);
        }catch (Exception ex){
            ex.getStackTrace();
        }


//        Geocoder gcd = new Geocoder(this, Locale.getDefault());
//        List<Address> addresses = null;
//        try {
//            addresses = gcd.getFromLocation(alert.getLatitud(), alert.getLongitud(), 1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (addresses.size() > 0){
//            mLocation.setText(addresses.get(0).getLocality());
//        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap=map;
        LatLng alerPosition = new LatLng(alert.getLatitud(), alert.getLongitud());
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(alerPosition, 13));

        map.addMarker(new MarkerOptions()
                .position(alerPosition));
    }
}
