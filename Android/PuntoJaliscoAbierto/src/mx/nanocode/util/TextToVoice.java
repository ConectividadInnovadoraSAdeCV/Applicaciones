package mx.nanocode.util;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;


public class TextToVoice implements TextToSpeech.OnInitListener{

	private TextToSpeech textToSpeech;
	Context context;
	int TextId;
	public TextToVoice(int TextId,Context context)
	{
		this.context=context;
		this.TextId=TextId;
		textToSpeech = new TextToSpeech(context, this);
	}
	public void Hablar()
	{
		textToSpeech.setLanguage( Locale.getDefault() );
		textToSpeech.speak( context.getText(TextId), TextToSpeech.QUEUE_FLUSH, null,null );
        textToSpeech.setSpeechRate( 0.0f );
        textToSpeech.setPitch( 0.0f );
	}
	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		
	}

}
