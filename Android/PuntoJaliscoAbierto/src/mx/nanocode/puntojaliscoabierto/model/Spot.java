package mx.nanocode.puntojaliscoabierto.model;

import org.json.JSONException;
import org.json.JSONObject;


public class Spot {

    private int id;
    private int intervalo;
    private String pregunta;
    private String r1;
	private String r2;
    private String r3;  
    private String fondo;  
    private String liga;  
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIntervalo() {
		return intervalo;
	}

	public void setIntervalo(int intervalo) {
		this.intervalo = intervalo;
	}

	public String getPregunta() {
		return pregunta;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

	public String getR1() {
		return r1;
	}

	public void setR1(String r1) {
		this.r1 = r1;
	}

	public String getR2() {
		return r2;
	}

	public void setR2(String r2) {
		this.r2 = r2;
	}

	public String getR3() {
		return r3;
	}

	public void setR3(String r3) {
		this.r3 = r3;
	}

	public String getFondo() {
		return fondo;
	}

	public void setFondo(String fondo) {
		this.fondo = fondo;
	}

	public String getLiga() {
		return liga;
	}

	public void setLiga(String liga) {
		this.liga = liga;
	}
  

    private static final String JSON_ID = "id";
    private static final String JSON_INTERVALO = "intervalo";
    private static final String JSON_PREGUNTA = "pregunta";
    private static final String JSON_R1 = "r1";
    private static final String JSON_R2 = "r2";
    private static final String JSON_R3 = "r3";
    private static final String JSON_FONDO = "fondo";
    private static final String JSON_LIGA = "liga";

    public Spot(JSONObject json) throws JSONException {
        this.id = json.getInt(JSON_ID);
        this.intervalo = json.getInt(JSON_INTERVALO);
        this.pregunta=json.getString(JSON_PREGUNTA);
        this.r1=json.getString(JSON_R1);
        this.r2=json.getString(JSON_R2);
        this.r3=json.getString(JSON_R3);
        this.fondo=json.getString(JSON_FONDO);
        this.liga=json.getString(JSON_LIGA);
    }

    public Spot() {
		// TODO Auto-generated constructor stub
	}

	@Override
    public String toString() {
        return pregunta;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(JSON_ID, this.id);
        jsonObject.put(JSON_INTERVALO, this.intervalo);
        jsonObject.put(JSON_PREGUNTA, this.pregunta);
        jsonObject.put(JSON_R1, this.r1);
        jsonObject.put(JSON_R2, this.r2);
        jsonObject.put(JSON_R3, this.r3);
        jsonObject.put(JSON_FONDO, this.fondo);
        jsonObject.put(JSON_LIGA, this.liga);
        return jsonObject;
    }
}
