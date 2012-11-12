package cz.payas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GestureActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button prenosDat = (Button) findViewById(R.id.button1);
        
        prenosDat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), PrenosDat.class);
                startActivity(myIntent);
            }

        });
        
        Button training = (Button) findViewById(R.id.training);
        
        training.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), TrainingActivity.class);
                startActivity(myIntent);
            }

        });
        
        
        
    }
    
    
    
}