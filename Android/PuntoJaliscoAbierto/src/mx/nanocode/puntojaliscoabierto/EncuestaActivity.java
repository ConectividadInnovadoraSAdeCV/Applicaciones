package mx.nanocode.puntojaliscoabierto;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import mx.nanocode.puntojaliscoabierto.model.Spot;
import mx.nanocode.puntojaliscoabierto.model.SpotLab;
import mx.nanocode.util.Tools;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class EncuestaActivity extends Activity {
	
	WebView webview;
	public static int webwidht;
	int idEncuesta;
	int respuesta;
	Spot sp;
	public ProgressBar pb;
	@SuppressLint("SetJavaScriptEnabled") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.encuesta_layout);
        //lineas para leer la encuesta a mostrar
        idEncuesta=0;
		try
		{
			idEncuesta=SpotLab.getInstance(this).getNextSpot().getId();
		}catch(Exception e){
			webview = (WebView)findViewById(R.id.encuestaImagen);
	        webview.getSettings().setJavaScriptEnabled(true);
	        webview.loadUrl("http://weon.dynalias.com/EncuestasFondos/ejfoto.png");   
		}
		sp= SpotLab.getInstance(this).getSpot(idEncuesta);
		webview = (WebView)findViewById(R.id.encuestaImagen);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(sp.getFondo());
        
        TextView pregunta= (TextView)findViewById(R.id.encuestaPregunta);
        pregunta.setText(sp.getPregunta());
        
        Button r1=(Button)findViewById(R.id.encuestaRespuesta1Bt);
        Log.d(mx.nanocode.puntojaliscoabierto.MainActivity.TAG,"r1: |"+sp.getR1().length()+"|");
        if(sp.getR1().length()==0)
        {
        	r1.setVisibility(View.GONE);
        }
        else
        {
        	r1.setText(sp.getR1());
        }
        

        Button r2=(Button)findViewById(R.id.encuestaRespuesta2Bt);
        if(sp.getR2().length()==0)
        {
        	r2.setVisibility(View.GONE);
        }
        else
        {
        	r2.setText(sp.getR2());
        }
        

        Button r3=(Button)findViewById(R.id.encuestaRespuesta3Bt);
        if(sp.getR3().length()==0)
        {
        	r3.setVisibility(View.GONE);
        }
        else
        {
        	r3.setText(sp.getR3());
        }
        
        webwidht=0;     
		Scale();
		/************** programo los botones ***************/
		pb=(ProgressBar)findViewById(R.id.progressBarEncuesta);
		r1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Responder(1);
			}
		});

		r2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Responder(2);
			}
		});

		r3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Responder(3);
			}
		});
	}
	private void Responder(int Respuesta)
	{
		this.respuesta=Respuesta;
		pb.setVisibility(View.VISIBLE);
    	new ResponerSpot().execute();
	}
	public void EliminarSpot()
	{
		pb.setVisibility(View.GONE);
		if(sp.getLiga()!="")
		{
			AbrirBrowser();
		}
		SpotLab.getInstance(this).deletSpot(sp);
		SpotLab.getInstance(this).saveSpots();
		
		finish();
	}
	void AbrirBrowser()
	{
		try {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sp.getLiga()));
			startActivity(browserIntent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, R.string.errordebrowser, Toast.LENGTH_SHORT).show();
		}
	}

	private void Scale() {
		webview.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						int FotoWidth = 231;
						//int FotoHeight=155;
						if (webwidht < 1) {
							webwidht = webview.getWidth();
							if (webwidht > 0) {
								webview.setInitialScale(100 * webwidht
										/ FotoWidth);
								
							}
						}
					}
				});
	}
	public class ResponerSpot extends AsyncTask <Void,Void,String>{
		@Override
		protected String doInBackground(Void... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			String Cad="http://weon.dynalias.com/MovileRestService/Service1.svc/SetAnswers/"+sp.getId()+"%7C"+respuesta+"";
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
				EliminarSpot();
			}
		}
 	}
}
