package mx.nanocode.puntojaliscoabierto.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.nanocode.util.Tools;
import mx.nanocode.util.WebServiceParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class SpotLab {

    private static SpotLab instance;
    private Context context;
    private List<Spot> spots;
    public String mac;

    private static final String FILENAME = "spots.json";

    private int SPOTS_COUNT;
    private SpotLab(Context appContext) {
    	setSPOTS_COUNT(0);
        this.context = appContext;
        loadSpots();
    }

    private void loadSpots() {
        SpotIntentSerializer crimeSerializer = new SpotIntentSerializer(this.context, FILENAME);
        try {
            this.spots = crimeSerializer.loadSpots();
        	Log.d(mx.nanocode.puntojaliscoabierto.MainActivity.TAG,"Spots size: "+this.spots.size());
            if(this.spots.size()==0)
            {
            	setSPOTS_COUNT(0);
            }
            setSPOTS_COUNT(this.spots.size());
        } catch (Exception e) {
            spots = new ArrayList<Spot>();
        }
    }
    /*
    private void RutasDisponibles()
    {
    	//este metodo define las rutas que tenemos disponibles
    	Ruta r= new Ruta();
    	r.setId(1);
    	r.setBase("Nextipac");
    	r.setDuracion("01:11 hrs");
    	r.setInicial(true);
    	r.setRuta("RUTA 13");
    	r.setSalida1("05:50 am");
    	r.setSalidan("10:59 pm");
    	this.rutas.add(r);
    	
    	r= new Ruta();
    	r.setId(2);
    	r.setBase("Av. Periferico 385");
    	r.setDuracion("01:11 hrs");
    	r.setInicial(false);
    	r.setRuta("RUTA 380");
    	r.setSalida1("05:50 am");
    	r.setSalidan("10:59 pm");
    	this.rutas.add(r);
    	saveRutas();
    }
    */
    public void addSpot(Spot spot) {
        this.spots.add(spot);
        SPOTS_COUNT=SPOTS_COUNT+1;
    }

    public void deletSpot(Spot spot) {
        this.spots.remove(spot);
        SPOTS_COUNT=SPOTS_COUNT-1;
    }

    public static SpotLab getInstance(Context appContext) {
        if (instance == null) {
            instance = new SpotLab(appContext.getApplicationContext());
            
        }

        return instance;
    }

    public List<Spot> getSpots() {
        return spots;
    }


    public Spot getSpot(int id) {
        for(Spot spot : spots) {
            if (spot.getId()==id) {
                return spot;
            }
        }
        return null;
    }
    public Spot getSpot(String RutaText) {
        for(Spot spot : spots) {
            if (spot.toString()==RutaText) {
                return spot;
            }
        }
        return null;
    }
    public Spot getNextSpot() {
    	return spots.get(0);
    }
    public boolean saveSpots() {
        SpotIntentSerializer spotSerializer = new SpotIntentSerializer(this.context, FILENAME);
        try {
            Log.d(mx.nanocode.puntojaliscoabierto.MainActivity.TAG, "Saving spots...");
            spotSerializer.saveSpots(this.spots);
            return true;
        } catch (Exception e) {
            Log.d(mx.nanocode.puntojaliscoabierto.MainActivity.TAG, "There was an error saving the spots", e);
            return false;
        }
    }

	public int getSPOTS_COUNT() {
		return SPOTS_COUNT;
	}

	private void setSPOTS_COUNT(int sPOTS_COUNT) {
		SPOTS_COUNT = sPOTS_COUNT;
	}
	public void readSpotsFromServer(String mac)
	{
		this.mac=mac.replace(":", "!");
		new ReadSpots().execute();
	}
	public void llenaSpots(Iterator<String> ListSpots)
	{
		
    	while(ListSpots.hasNext())
    	 {
    		Spot sp=new Spot();
    		String value=(String)ListSpots.next();
    		String[] spot=value.split("[\\x7C]");
    		sp.setId(Integer.parseInt(spot[0]));
    		sp.setIntervalo(Integer.parseInt(spot[1]));
    		sp.setPregunta(spot[2]);
    		sp.setR1(spot[3]);
    		sp.setR2(spot[4]);
    		sp.setR3(spot[5]);
    		sp.setFondo(spot[6]);
    		sp.setLiga(spot[7]);
    		this.addSpot(sp);
    	 }
    	this.saveSpots();
	}
	public class ReadSpots extends AsyncTask <Void,Void,String>{
		@Override
		protected String doInBackground(Void... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			String Cad="http://weon.dynalias.com/MovileRestService/Service1.svc/GetSpots/"+mac+"";
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
				WebServiceParser wsp= new WebServiceParser();
				try
				{
					llenaSpots(wsp.GetListResult(results).iterator());				
				}catch(Exception e){
			    	Log.d(mx.nanocode.puntojaliscoabierto.MainActivity.TAG,"No hay encuestas: "+e.getMessage());
				}
			}
		}
 	}
}
