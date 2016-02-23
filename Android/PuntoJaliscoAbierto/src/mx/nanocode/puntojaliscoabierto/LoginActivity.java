package mx.nanocode.puntojaliscoabierto;

import org.json.JSONException;

import mx.nanocode.puntojaliscoabierto.model.LogonData;
import mx.nanocode.puntojaliscoabierto.model.SocketForPermisions;
import mx.nanocode.util.Tools;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	LogonData datos;
	Spinner sd;
	Spinner sm;
	Spinner sa;
	RadioGroup rgs;
	Button myBt;
	CheckBox ckaviso;
	CheckBox avisual;
	boolean FromBroadcast;
	
	public static final String LogonFileName = "LogonFilePJA_2";
	public static final String FromBradcastReceiver="fbr";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		FromBroadcast=false;
		try
		{
			FromBroadcast=getIntent().getExtras().getBoolean(FromBradcastReceiver);
		}catch(Exception e){}
		if(FromBroadcast)
		{
			
			Ringtone r=RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
			r.play();
			Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
			 // Vibrate for 500 milliseconds
			 v.vibrate(500);
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_layout);
        
        try {
			datos= new LogonData(this,LogonFileName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d(mx.nanocode.puntojaliscoabierto.MainActivity.TAG,"excepcion al leer: "+e.getMessage());
		}
        
        Tools tool=new Tools();
        //el spiner de la fecha

        ckaviso= (CheckBox)findViewById(R.id.LgdAvisoAceptado);
        avisual=(CheckBox)findViewById(R.id.LgdAyudaAuditiva);
        sd = (Spinner) findViewById(R.id.lgdDiaSpin);
        sd.setSelection(tool.getSpinnerIndex(sd, datos.getDia()));
        
        sm=(Spinner) findViewById(R.id.lgdMesSpin);
        sm.setSelection(tool.getSpinnerIndex(sm, datos.getMes()));
        
        sa=(Spinner) findViewById(R.id.lgdAnoSpin);
        sa.setSelection(tool.getSpinnerIndex(sa, datos.getAno()));
        //muestro los datos del archivo
        //el sexo
        rgs= (RadioGroup) findViewById(R.id.LgdSexoRadio);
        if(datos.getSexo()=="0")
        {
        	rgs.check(R.id.rdbTwo);
        }
        
        ckaviso.setChecked(sd.getSelectedItemId()!=0);
        avisual.setChecked(datos.isAyuda_auditiva());
        
        myBt=(Button)findViewById(R.id.LgdEnviarBt);
        myBt.setOnClickListener(new OnClickListener() {
        	@Override
			public void onClick(View v) {
        		if(ValidarFecha() && ValidaAviso())
        		{
        			datos.setDia(sd.getSelectedItem().toString());
        			datos.setMes(sm.getSelectedItem().toString());
        			datos.setAno(sa.getSelectedItem().toString());
        			
        			String sex= rgs.getCheckedRadioButtonId()==R.id.rdbOne?"1":"0";
        			datos.setSexo(sex);
        			datos.setAyuda_auditiva(avisual.isChecked());
        			//guardar los nuevos datos
        			if(datos.Guardar())
        			{
        				datos.GuardarEnServer();
        			}
        			if(FromBroadcast)
        			{
        				new SocketForPermisions(getApplicationContext(), datos.getMac()).Conectar();
        			}
        			finish();
        		}
			}
		});
	}
	
	boolean ValidarFecha()
	{
		if(sd.getSelectedItemId()==0)
		{
			Toast.makeText(this, R.string.errordiaspin, Toast.LENGTH_SHORT).show();
			return false;
		}

		if(sm.getSelectedItemId()==0)
		{
			Toast.makeText(this, R.string.errordiaspin, Toast.LENGTH_SHORT).show();
			return false;
		}

		if(sa.getSelectedItemId()==0)
		{
			Toast.makeText(this, R.string.errordiaspin, Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	public void onCheckboxClicked(View view){
		Intent confIntent; 
		//*
		confIntent = new Intent().setClass(
				LoginActivity.this, AvisoActivity.class);
        startActivity(confIntent); 	
	}
	boolean ValidaAviso()
	{
		if(!ckaviso.isChecked())
		{
			Toast.makeText(this, R.string.erroraviso, Toast.LENGTH_SHORT).show();
		}
		return ckaviso.isChecked();
	}
	
}
