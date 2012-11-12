package cz.payas;

import java.util.ArrayList;

import cz.payas.db.DatabaseHandler;
import cz.payas.model.Move;
import cz.payas.model.Vertex;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TrainingActivity extends Activity implements SensorEventListener {

	private SensorManager sensorManager;
	private boolean sensor = false;
	
	private ArrayList<Move> moves = new ArrayList<Move>();
	private ArrayList<Vertex> verticies = new ArrayList<Vertex>();

	private DatabaseHandler db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.training);

		db = new DatabaseHandler(this);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);

		Button start = (Button) findViewById(R.id.startTraining);

		start.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View arg0, MotionEvent me) {

				switch (me.getAction()) {
				case MotionEvent.ACTION_DOWN:
					sensor = true;
					break;
				case MotionEvent.ACTION_UP:
					sensor = false;
					break;
				}

				return true;
			}
		});
		
		TextView name = (TextView) findViewById(R.id.gestureName);
		name.setText("První gesto");
		
		TextView description = (TextView) findViewById(R.id.gestureDescription);
		name.setText("Popis prvního gesta - musíme hnout telefonem vpravo");
		
		TextView numberOfAttempt = (TextView) findViewById(R.id.numberOfAttempts);
		name.setText("Poèet pokusù:");
		

	}

	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	public void onSensorChanged(SensorEvent e) {
		if (sensor) {
			TextView x = (TextView) findViewById(R.id.xTView);
			TextView y = (TextView) findViewById(R.id.yTView);
			TextView z = (TextView) findViewById(R.id.zTView);

			Float x1 = e.values[0];
			Float y1 = e.values[1];
			Float z1 = e.values[2];

			x.setText("X: " + x1.toString());
			y.setText("Y: " + y1.toString());
			z.setText("Z: " + z1.toString());

			if (x1 != null && y1 != null && z1 != null) {
				// sendToServer(x1,y1,z1);
				verticies.add(new Vertex(x1, y1, z1));

			}

		}
	}

}