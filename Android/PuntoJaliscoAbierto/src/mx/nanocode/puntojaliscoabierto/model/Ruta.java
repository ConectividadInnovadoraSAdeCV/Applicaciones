package mx.nanocode.puntojaliscoabierto.model;

import org.json.JSONException;
import org.json.JSONObject;


public class Ruta {

    private int id;
    private String ruta;
    private String salida1;
    private String salidan;
    private String duracion;
    private String base;    
    private boolean inicial;

    private static final String JSON_ID = "id";
    private static final String JSON_RUTA = "ruta";
    private static final String JSON_SALIDA1 = "salida1";
    private static final String JSON_SALIDAN = "salidan";
    private static final String JSON_DURACION = "duracion";
    private static final String JSON_BASE = "base";
    private static final String JSON_INICIAL = "inicial";

    public Ruta(JSONObject json) throws JSONException {
        this.id = json.getInt(JSON_ID);
        this.ruta=json.getString(JSON_RUTA);
        this.salida1=json.getString(JSON_SALIDA1);
        this.salidan=json.getString(JSON_SALIDAN);
        this.duracion=json.getString(JSON_DURACION);
        this.base=json.getString(JSON_BASE);
        this.inicial=json.getBoolean(JSON_INICIAL);
    }

    public Ruta() {
		// TODO Auto-generated constructor stub
	}

	@Override
    public String toString() {
        return ruta;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(JSON_ID, this.id);
        jsonObject.put(JSON_RUTA, this.ruta);
        jsonObject.put(JSON_SALIDA1, this.salida1);
        jsonObject.put(JSON_SALIDAN, this.salidan);
        jsonObject.put(JSON_DURACION, this.duracion);
        jsonObject.put(JSON_BASE, this.base);
        jsonObject.put(JSON_INICIAL, this.inicial);
        return jsonObject;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public String getSalida1() {
		return salida1;
	}

	public void setSalida1(String salida1) {
		this.salida1 = salida1;
	}

	public String getSalidan() {
		return salidan;
	}

	public void setSalidan(String salidan) {
		this.salidan = salidan;
	}

	public String getDuracion() {
		return duracion;
	}

	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public boolean isInicial() {
		return inicial;
	}

	public void setInicial(boolean inicial) {
		this.inicial = inicial;
	}    
}
