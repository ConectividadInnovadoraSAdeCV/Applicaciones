package mx.nanocode.puntojaliscoabierto;

import android.app.Activity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
//import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import mx.nanocode.puntojaliscoabierto.model.Ruta;
import mx.nanocode.puntojaliscoabierto.model.RutaLab;
//import org.osmdroid.views.MapController;
import mx.nanocode.util.*;
 
public class MainActivity extends Activity {
 
	public static final String TAG = "PJADebuj";
	private MapView myOpenMapView;
	public IMapController myMapController;
	Button myBt;
	Button myBtRuta;
	Button myConf;
	public ProgressBar pb;
	TextView tv;
	Spinner spinner;
	List<String> spinItems;
	static boolean executando;

    double latitude;
    double longitude;
    Ruta ruta;
	public static synchronized void SetStatus(boolean valor) {
        executando=valor;
    }
	@Override
	protected void onResume()
	{
		super.onResume();
		ruta=RutaLab.getInstance(getApplicationContext()).getRuta();
        spinner.setSelection(GetPosition(ruta));
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
		
        ruta=RutaLab.getInstance(getApplicationContext()).getRuta();
        spinner = (Spinner) findViewById(R.id.rutaspinner);
        loadSpinnerData();
        
        spinner.setSelection(GetPosition(ruta));
     // Spinner click listener
        spinner.setOnItemSelectedListener(spinnerSelector);
        //mapas
        myOpenMapView = (MapView)findViewById(R.id.mainopenmapview);
        myOpenMapView.setBuiltInZoomControls(true);
        myOpenMapView.setMultiTouchControls(true);
        
        myMapController = myOpenMapView.getController();
        myMapController.setZoom(10);
        myMapController.setCenter(new GeoPoint(20.870759, -103.573237));
        
        pb=(ProgressBar)findViewById(R.id.progressBarMain);
        
        myBt=(Button)findViewById(R.id.btactualiza);
        myBt.setOnClickListener(new OnClickListener() {
        	@Override
			public void onClick(View v) {
				GPSTracker gps = new GPSTracker(MainActivity.this);			 
	            // check if GPS enabled     
	            if(gps.canGetLocation()){                 
	                latitude = gps.getLatitude();
	                longitude = gps.getLongitude(); 
	                //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_SHORT).show();
	            }else{
	                gps.showSettingsAlert();
	            }
	            if(latitude!=0)
	            {
	            	pb.setVisibility(View.VISIBLE);
	            	new GetCamiones().execute();
	            }
			}
		});
        myBtRuta=(Button)findViewById(R.id.btruta);
        myBtRuta.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                Intent rutasIntent = new Intent().setClass(
                        MainActivity.this, myListActivity.class);
                startActivity(rutasIntent); 
				
			}
		});
        
        myConf=(Button)findViewById(R.id.btconf);
        myConf.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent confIntent; 
				//*
				confIntent = new Intent().setClass(
                        MainActivity.this, LoginActivity.class);
				/*/		
				confIntent = new Intent().setClass(
                        MainActivity.this, AvisoActivity.class);
                //*/
                startActivity(confIntent); 	
			}
		});
        
    } 
    void MuestraPos()
    {
    	
        //Toast.makeText(getApplicationContext(), "lat: "+latitude, Toast.LENGTH_SHORT).show();
    }
    //escucha los gestos
    OnItemGestureListener<OverlayItem> myOnItemGestureListener = new OnItemGestureListener<OverlayItem>() { 
    @Override
    public boolean onItemLongPress(int arg0, OverlayItem arg1) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean onItemSingleTapUp(int index, OverlayItem item) {
    	Toast.makeText(
    			getApplicationContext(),
    			item.getTitle() + "\n" + item.getSnippet() + "\n"
    					+ item.getPoint().getLatitudeE6() + " : "
    					+ item.getPoint().getLongitudeE6(),
    					Toast.LENGTH_LONG).show();

    	return true;
    }
    };
    //terminan los gestos
    void MuestraPunto(Iterator<String> ListCamiones)
    {
    	ArrayList<OverlayItem> anotherOverlayItemArray;
    	anotherOverlayItemArray = new ArrayList<OverlayItem>();
    	 
    	anotherOverlayItemArray.add(new OverlayItem("-1", "false", new GeoPoint(latitude,  longitude)));
    	
    	while(ListCamiones.hasNext())
    	 {
    		String value=(String)ListCamiones.next();
    		String[] camion=value.split("[\\x7C]");
    		Double lat,lon;
    		lat=Double.parseDouble(camion[0].replace(',','.'));
    		lon=Double.parseDouble(camion[1].replace(',','.'));
        	anotherOverlayItemArray.add(new OverlayItem(camion[2],camion[3], new GeoPoint(lat, lon)));
    	 }
    	 //clase de otra pagina
    	//*/
    	Context myContext=getApplicationContext();
    	ResourceProxy resourceProxy = new DefaultResourceProxyImpl(myContext);
    	MyItemizedIconOverlay anotherItemizedIconOverlay= new MyItemizedIconOverlay(anotherOverlayItemArray,myOnItemGestureListener,resourceProxy,myContext);
    	/*/    	
    	ItemizedOverlayWithFocus<OverlayItem> anotherItemizedIconOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this, anotherOverlayItemArray, myOnItemGestureListener);
    	anotherItemizedIconOverlay.setFocusItemsOnTap(true);
    	//*/
    	myMapController.setZoom(12);
    	myMapController.setCenter(anotherOverlayItemArray.get(0).getPoint());
    	//anotherItemizedIconOverlay.setFocus(anotherOverlayItemArray.get(0));
        myMapController.animateTo(anotherOverlayItemArray.get(0).getPoint());
    	//myMapController.setCenter(anotherOverlayItemArray.get(0).getPoint());
        myOpenMapView.getOverlays().clear();
    	myOpenMapView.getOverlays().add(anotherItemizedIconOverlay);
    	
    }
    private void loadSpinnerData() {
    	ArrayList<Ruta> childItems=(ArrayList<Ruta>) RutaLab.getInstance(getApplicationContext()).getRutas();
    	spinItems=new ArrayList<String>();
		for(Ruta truta : childItems) {
			spinItems.add(truta.toString());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, spinItems);
 
        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }
    private int GetPosition(Ruta r)
    {
    	int p=0;
    	for(String truta : spinItems) {
			if(truta==r.toString())
			{
				break;
			}
			p++;
        }
    	return p;
    }
    private AdapterView.OnItemSelectedListener spinnerSelector = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        	ruta=RutaLab.getInstance(getApplicationContext()).getRuta(spinner.getSelectedItem().toString());
        }
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public class GetCamiones extends AsyncTask <Void,Void,String>{
		@Override
		protected String doInBackground(Void... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			String Cad="http://weon.dynalias.com/MovileRestService/Service1.svc/GetCamiones/"+ruta.getId()+"%7C"+latitude+"%7C"+longitude+"";
			HttpGet httpGet = new HttpGet(Cad);
			//HttpGet httpGet = new HttpGet("http://www.webservicex.net/geoipservice.asmx/GetGeoIP?IPAddress=158.42.38.1");
			String text = null;
			try {
				HttpResponse response = httpClient.execute(httpGet, localContext);
				HttpEntity entity = response.getEntity();
				text = new Tools().getASCIIContentFromEntity(entity);
			} catch (Exception e) {
				return e.getLocalizedMessage();
			}
			return text;
		}
		
		@Override
		protected void onPostExecute(String results) {
			if (results!=null) {
				
				try {
					WebServiceParser wsp= new WebServiceParser();
					MuestraPos();
					MuestraPunto(wsp.GetListResult(results).iterator());
				} catch (Exception e) {
				}
				pb.setVisibility(View.GONE);
			}
		}
 	}
}
