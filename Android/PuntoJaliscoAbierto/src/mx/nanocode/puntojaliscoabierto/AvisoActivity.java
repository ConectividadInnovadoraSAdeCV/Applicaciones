package mx.nanocode.puntojaliscoabierto;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

public class AvisoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aviso_layout);
		WebView wv= (WebView)findViewById(R.id.aviso_webview);
		wv.loadUrl("file:///android_asset/aviso_privacidad_html.htm");
		
		Button btAceptar=(Button) findViewById(R.id.aviso_aceptar);
		btAceptar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
