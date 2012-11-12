package cz.payas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class NastaveniActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nastaveni);
		Integer ipa1 = 0, ipa2,ipa3,ipa4;
		
		Intent data = getIntent();
		
		
		EditText ip1 = (EditText) findViewById(R.id.ip1);
		EditText ip2 = (EditText) findViewById(R.id.ip2);
		EditText ip3 = (EditText) findViewById(R.id.ip3);
		EditText ip4 = (EditText) findViewById(R.id.ip4);
		
		
		Bundle extras = getIntent().getExtras(); 

		if (extras != null) {
		    ipa1 = extras.getInt("ip1");
		    Log.i("neco prislo", "tady a ted");
		    Log.i("neco prislo", ipa1.toString());
		    // and get whatever type user account id is
		}else{
			Log.i("nic","neprislo");
		}
		//data.getIntExtra("ip1", ipa1);
		ip1.setText(1);
		/*ip2.setText(data.getExtras().getInt("ip2"));
		ip3.setText(data.getExtras().getInt("ip3"));
		ip4.setText(data.getExtras().getInt("ip4"));*/
				
		Button save = (Button) findViewById(R.id.save);
		
		ImageView yes = (ImageView) findViewById(R.id.yesIcon);
		ImageView no = (ImageView) findViewById(R.id.noIcon);
		
		//yes.setVisibility(1);
		no.setVisibility(0);
		
	}
}
