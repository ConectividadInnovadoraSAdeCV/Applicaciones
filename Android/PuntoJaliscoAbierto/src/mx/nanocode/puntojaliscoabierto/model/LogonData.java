package mx.nanocode.puntojaliscoabierto.model;

import mx.nanocode.util.JSONFileManager;
import mx.nanocode.util.Tools;
import mx.nanocode.util.WebServiceParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;


public class LogonData {

    private int id;
    private int conexiones;
    private boolean registrado;
    private boolean ayuda_auditiva;
    public boolean isAyuda_auditiva() {
		return ayuda_auditiva;
	}

	public void setAyuda_auditiva(boolean ayuda_auditiva) {
		this.ayuda_auditiva = ayuda_auditiva;
	}
	private String sexo;
    private String dia;
	private String mes;    
    private String ano;
    private String mac;
    
    Context context;
    String filename;

    private static final String JSON_ID = "id";
    private static final String JSON_CONEXIONES = "conexiones";
    private static final String JSON_REGISTRADO = "registrado";
    private static final String JSON_AVISUAL="ayuda_auditiva";
    private static final String JSON_SEXO = "sexo";
    private static final String JSON_DIA = "dia";
    private static final String JSON_MES = "mes";
    private static final String JSON_ANO = "ano";

    private void SetValues(JSONObject json) throws JSONException {
        this.id = json.getInt(JSON_ID);
        this.conexiones=json.getInt(JSON_CONEXIONES);
        this.registrado=json.getBoolean(JSON_REGISTRADO);
        this.ayuda_auditiva=json.getBoolean(JSON_AVISUAL);
        this.sexo=json.getString(JSON_SEXO);
        this.dia=json.getString(JSON_DIA);
        this.mes=json.getString(JSON_MES);
        this.ano=json.getString(JSON_ANO);
    }

    public LogonData(Context context,String filename) throws JSONException {
		// TODO Auto-generated constructor stub
    	this.context=context;
    	this.filename=filename;
    	JSONFileManager jfm = new JSONFileManager(context,filename);
    	
    	JSONObject datos=null;
    	datos=jfm.GetDatos();

    	if(datos==null)
    	{
			id = 0;
			conexiones = 0;
			registrado = false;
			ayuda_auditiva=false;
			sexo = "h";
			dia = "dia";
			mes = "mes";
			ano = "ano";
			jfm.Guardar(this.toJSON());
		}
    	else
    	{
    		SetValues(datos);
    	}

		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		setMac(wifiInfo.getMacAddress());
	}

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(JSON_ID, this.id);
        jsonObject.put(JSON_CONEXIONES, this.conexiones);
        jsonObject.put(JSON_REGISTRADO, this.registrado);
        jsonObject.put(JSON_AVISUAL, this.ayuda_auditiva);
        jsonObject.put(JSON_SEXO, this.sexo);
        jsonObject.put(JSON_DIA, this.dia);
        jsonObject.put(JSON_MES, this.mes);
        jsonObject.put(JSON_ANO, this.ano);
        
        return jsonObject;
    }
    
    public boolean Guardar()
    {

		Log.d(mx.nanocode.puntojaliscoabierto.MainActivity.TAG,"Estoy guardando: "+isRegistrado());
		try {
			new JSONFileManager(context, filename).Guardar(toJSON());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d(mx.nanocode.puntojaliscoabierto.MainActivity.TAG,"excepcion al guardar datos: "+e.getMessage());
			return false;
		}
		return true;
    }
    public void GuardarEnServer()
    {
    	new SaveOnServer().execute();
    }
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getConexiones() {
		return conexiones;
	}

	public void setConexiones(int conexiones) {
		this.conexiones = conexiones;
	}

	public boolean isRegistrado() {
		return registrado;
	}

	public void setRegistrado(boolean registrado) {
		this.registrado = registrado;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

    public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
	public class SaveOnServer extends AsyncTask<Void, Void, String> {
		
		private String GetParametros()
		{
			String Parametros="";
			Parametros=getMac().replace(":", "!")+"%7C"+getSexo()+"%7C"+getAno()+getMes()+getDia();
			return Parametros;
		}

		@Override
		protected String doInBackground(Void... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			String Cad = "http://weon.dynalias.com/MovileRestService/Service1.svc/AltaUsuario/"+GetParametros();
			Log.d(mx.nanocode.puntojaliscoabierto.MainActivity.TAG,Cad);
			HttpGet httpGet = new HttpGet(Cad);
			String text = null;
			try {
				HttpResponse response = httpClient.execute(httpGet,
						localContext);
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
				String valor=wsp.GetResultado(results);
				if(valor.contains("si"))
				{
					setRegistrado(true);
					Guardar();
				}
			}
		}
	}
}
