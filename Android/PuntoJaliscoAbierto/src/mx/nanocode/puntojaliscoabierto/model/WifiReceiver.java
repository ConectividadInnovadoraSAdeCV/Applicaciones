package mx.nanocode.puntojaliscoabierto.model;

import mx.nanocode.puntojaliscoabierto.EncuestaActivity;
import mx.nanocode.puntojaliscoabierto.LoginActivity;
import mx.nanocode.puntojaliscoabierto.R;
import mx.nanocode.util.PlaySound;

import org.json.JSONException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

public class WifiReceiver extends BroadcastReceiver {

	private LogonData ld;

	public enum NotificationId {
		BIENVENIDA(1), SPOT(2), ENCUESTA(3);

		private final int value;

		private NotificationId(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	String NetId = "weon";
	Context context;

	@Override
	public void onReceive(Context context, Intent intent) {

		this.context = context;
		NetworkInfo info = intent
				.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
		if (info != null) {
			if (info.isConnected()) {
				// Do your work.

				// e.g. To check the Network Name or other info:
				WifiManager wifiManager = (WifiManager) context
						.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				String ssid = wifiInfo.getSSID();
				if (ssid.contains(NetId)) {
					
					if (IsRegistered()) {
						if(IsSoundActivate())
						{
							PlaySound ps= new PlaySound();
							ps.Play(context,R.raw.ruta368);
						}
						
						new SocketForPermisions(context, wifiInfo.getMacAddress()).Conectar();
						//the las line was change fo foufaith hardware
						/*int ip = wifiInfo.getIpAddress();
						@SuppressWarnings("deprecation")
						String ipAddress = Formatter.formatIpAddress(ip);
						Log.d("Hola", ipAddress);
						new SocketForPermisions(context, ipAddress).Conectar();*/
						SpotLab sl=SpotLab.getInstance(context);
						if(sl.getSPOTS_COUNT()>0)
						{
							Intent spotItent= new Intent(context,EncuestaActivity.class);
							spotItent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(spotItent);
						}
						else
						{
							sl.readSpotsFromServer(wifiInfo.getMacAddress().toString());
						}
					}
					else
					{
						Intent logintent= new Intent(context,LoginActivity.class);
						logintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						logintent.putExtra(mx.nanocode.puntojaliscoabierto.LoginActivity.FromBradcastReceiver, true);
						context.startActivity(logintent);
					}
				}
			}
		}
	}

	private boolean IsRegistered() {
		try {
			ld = new LogonData(context,
					mx.nanocode.puntojaliscoabierto.LoginActivity.LogonFileName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}
		if (ld.getConexiones() < 2||!ld.getDia().contains("dia"))
		{
			ld.setConexiones(ld.getConexiones()+1);
			ld.Guardar();
			return true;
		}
		return false;
	}
	private boolean IsSoundActivate()
	{
		try {
			ld = new LogonData(context,
					mx.nanocode.puntojaliscoabierto.LoginActivity.LogonFileName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}
		return ld.isAyuda_auditiva();
	}

}