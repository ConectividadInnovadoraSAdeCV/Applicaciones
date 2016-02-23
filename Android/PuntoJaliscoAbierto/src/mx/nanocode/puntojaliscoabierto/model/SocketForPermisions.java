package mx.nanocode.puntojaliscoabierto.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONException;

import mx.nanocode.puntojaliscoabierto.MainActivity;
import mx.nanocode.puntojaliscoabierto.R;
import mx.nanocode.puntojaliscoabierto.model.WifiReceiver.NotificationId;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class SocketForPermisions {
	
	Context context;
	String macadress;
	public SocketForPermisions(Context context,String macadress)
	{
		this.context=context;
		this.macadress=macadress;
	}
	public void Conectar()
	{
		new ObtienePermisos().execute(macadress);
	}
	private boolean IsSoundActivate()
	{
		LogonData ld;
		try {
			ld = new LogonData(context,
					mx.nanocode.puntojaliscoabierto.LoginActivity.LogonFileName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}
		return ld.isAyuda_auditiva();
	}
	void displayNotificationOne()
	{
		Log.d(mx.nanocode.puntojaliscoabierto.MainActivity.TAG,"Entrando a la notificacion");
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setContentTitle(context.getString(R.string.notifytext));
		mBuilder.setContentText(context.getString(R.string.notifytext));
		mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		mBuilder.setAutoCancel(true);
		
		Intent resultIntent = new Intent(context,MainActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(MainActivity.class);

		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		
		NotificationManager mNotificationManager =
			    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			    
			// notificationID allows you to update the notification later on.
			mNotificationManager.notify(NotificationId.BIENVENIDA.getValue(), mBuilder.build());
			Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
			 // Vibrate for 500 milliseconds
			 v.vibrate(500);
			 if(IsSoundActivate())
			 {
				 v.vibrate(500);
			 }
			 CheckIfLogedOnServer();
		
	}
	private void CheckIfLogedOnServer() {
		LogonData ld=null;
		try {
			ld= new LogonData(context,mx.nanocode.puntojaliscoabierto.LoginActivity.LogonFileName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return;
		}		

		Log.d(mx.nanocode.puntojaliscoabierto.MainActivity.TAG,"Registrado: "+ld.isRegistrado()+"Dia: "+ld.getDia());
		if(!ld.isRegistrado()&&ld.getDia()!="dia")
		{
			ld.GuardarEnServer();
		}
		
	}
	public class ObtienePermisos extends AsyncTask <String, Void,String> {

		protected String doInBackground(String... macadress) {
			
			String ip = "192.168.1.2";
			int puerto = 7000;
			String respuesta="";
			String solicitud="weon|"+macadress[0]+"|@";
			try {
				Socket sk = new Socket(ip, puerto);
				BufferedReader entrada = new BufferedReader(
						new InputStreamReader(sk.getInputStream()));
				PrintWriter salida = new PrintWriter(new OutputStreamWriter(
						sk.getOutputStream()), true);
				Log.d(mx.nanocode.puntojaliscoabierto.MainActivity.TAG,"Solicitud: "+solicitud);
				salida.println(solicitud);
				respuesta=entrada.readLine();
				Log.d(mx.nanocode.puntojaliscoabierto.MainActivity.TAG,"Respuesta:" + respuesta);
				sk.close();
			} catch (Exception e) {
				Log.d(mx.nanocode.puntojaliscoabierto.MainActivity.TAG,"error: " + e.toString());
			}
			return respuesta;
		}

		@Override
		protected void onPostExecute(String results) {
			if (results != null) {
				displayNotificationOne();
			}
		}
	}
}
