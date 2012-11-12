package cz.payas;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import cz.payas.db.DatabaseHandler;
import cz.payas.model.Gesture;
import cz.payas.model.Move;
import cz.payas.model.Vertex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PrenosDat extends Activity implements SensorEventListener {

	private SensorManager sensorManager;
	private boolean sensor = false;
	private int ipAddress1 = 192;
	private int ipAddress2 = 168;
	private int ipAddress3 = 2;
	private int ipAddress4 = 100;

	private DatabaseHandler db;

	private ArrayList<Move> movesAll = new ArrayList<Move>();
	private ArrayList<Vertex> verticies = new ArrayList<Vertex>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prenos);

		db = new DatabaseHandler(this);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);

		Button start = (Button) findViewById(R.id.start);

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

		Button zpracuj = (Button) findViewById(R.id.zpracujData);

		zpracuj.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				zpracujData();
			}
		});

		Button print = (Button) findViewById(R.id.print);

		print.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Log.i("poèet Moves", "" + db.getMovesCount());

				for (int i = 1; i < verticies.size(); i++) {
					Vertex v1 = verticies.get(i - 1);
					Vertex v2 = verticies.get(i);

					Log.d("print", ";" + i + ";" + v1.getX() + ";" + v2.getX() + ";" + Math.abs(v2.getX() - v1.getX()));
				}

			}
		});

		Button deleteMemory = (Button) findViewById(R.id.deleteMemory);

		deleteMemory.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				verticies = new ArrayList<Vertex>();
				movesAll = new ArrayList<Move>();
				makeToast("Data byla vymazána z pamìti");
			}
		});

		Button saveDB = (Button) findViewById(R.id.saveToDB);

		saveDB.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				db.addMove(new Move(1, 1, 0, 6.7421f, 3, 1));
				db.addMove(new Move(1, 2, 1, -9.00044f, 3, 1));
				db.addMove(new Move(1, 3, 0, 3.00044f, 1, 1));

				db.addMove(new Move(2, 1, 1, -8.3344f, 3, 1));
				db.addMove(new Move(2, 2, 0, 9.00044f, 3, 1));

				db.addMove(new Move(3, 1, 0, 6.7421f, 3, 1));
				db.addMove(new Move(3, 2, 1, -9.00044f, 3, 1));

			}
		});

	}

	public void zpracujData() {
		Integer direction = null;
		int iterMove = 1;

		for (int i = 1; i < verticies.size(); i++) {
			Vertex v1 = verticies.get(i - 1);		
			Vertex v2 = verticies.get(i);

			float diff = v2.getX() - v1.getX();

			if (Math.abs(diff) > 0.1) {
			
				direction = findOutDirection(diff);

				Move move = new Move(iterMove, direction, diff, 1);

				if (move.getSizeNormalize() > 1) {
					movesAll.add(move);
					iterMove++;
				}
			}
		}

		Log.i("INFO", "verticies=" + verticies.size() + " move= " + movesAll.size());

		ArrayList<Move> moves = createMoveList(); 
		
		Log.i("poèet Moves", "" + db.getMovesCount());

		ArrayList<Gesture> gesturesAll = new ArrayList<Gesture>();
		gesturesAll.add(new Gesture(1, "first"));
		gesturesAll.add(new Gesture(2, "levá"));
		gesturesAll.add(new Gesture(3, "pravá"));

		boolean match = false;

		// Gesture comparision
		for (Gesture gesture : gesturesAll) {

			ArrayList<Move> movesFromDB = db.getMoveByGesture(gesture.getId());

			Log.i("======================", "=============================");
			Log.i("gesture", gesture.getName());

			if (compareMoves(moves, movesFromDB)) {

				match = true;
				Log.d("=====================", "======================");
				Log.d("SHODA", "Yeah, je to tady");
				Log.d("Gesto=", gesture.getName());
				Log.d("=====================", "======================");
				break;
			}
		}

		if (!match) {
			Log.d("=====================", "======================");
			Log.d("žádná shoda", "žádná shoda");
			Log.d("=====================", "======================");
		}

	}

	private ArrayList<Move> createMoveList() {
		float directionSize = 0;
		int order = 0;
		int direction2 = movesAll.get(0).getDirection();
		
		ArrayList<Move> moveList = new ArrayList<Move>();
		
		for (int i = 0; i < movesAll.size(); i++) {
			Move move = movesAll.get(i);

			Log.i("move", move.toString());

			if (move.getDirection() == direction2) {
				directionSize += move.getSize();

			} else {
				Log.d("change in direction", ";" + "i= " + i + ";" + direction2 + ";" + directionSize);

				order++;
				moveList.add(new Move(order, direction2, directionSize, 1));
				direction2 = move.getDirection();
				directionSize = move.getSize();

			}

			if (movesAll.size() == i + 1) {
				order++;
				Log.d("end", ";" + "i= " + i + ";" + direction2 + ";" + directionSize);
				moveList.add(new Move(order, direction2, directionSize, 1));
			}
		}
		
		return moveList;
	}

	private Integer findOutDirection(float diff) {
		// -1 = - || 1 = +
		float sign = Math.signum(diff);
		int direction;
		
		if (sign >= 0) {
			direction = 0;
		} else {
			direction = 1;
		}
		return direction;
	}

	private boolean compareMoves(ArrayList<Move> moves, ArrayList<Move> fromDB) {

		int match = 0;

		for (int i = 0; i < moves.size(); i++) {
			Move move = moves.get(i);

			if ((i + 1) > fromDB.size()) {
				Log.d("break", "i > fromDB");
				break;
			}

			Move moveFromDB = fromDB.get(i);

			Log.d("move", move.toString());
			Log.d("moveFromDB", moveFromDB.toString());

			if (moveFromDB.getOrder() == move.getOrder() && move.getDirection() == moveFromDB.getDirection()) {

				Log.i("porovnání", "move size= " + move.getSize() + " from DB= " + moveFromDB.getSize());

				// vybere first order
				if (move.getSizeNormalize() == moveFromDB.getSizeNormalize()
						|| Math.abs(move.getSize() - moveFromDB.getSize()) < 0.5) {
					Log.e("beru - poradi=" + i, move.toString());
					match++;
					continue;
				} else {
					break;
				}
			}

		}

		Log.i("porovnání", "match= " + match + " move.size()= " + fromDB.size());
		if (match == fromDB.size() && match == moves.size()) {
			return true;
		} else {
			return false;
		}
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

			int xNorm = normalizuj(x1);
			int yNorm = normalizuj(y1);
			int zNorm = normalizuj(z1);
			
			
			x.setText("X: " + x1.toString() + "\t X norm:" + xNorm);
			y.setText("Y: " + y1.toString() + "\t Y norm:" + yNorm);
			z.setText("Z: " + z1.toString() + "\t Z norm:" + zNorm);

			if (x1 != null && y1 != null && z1 != null) {
				// sendToServer(x1,y1,z1);
				lowPass(new Vertex(x1, y1, z1));
			}

		}
	}
	
	

	private int normalizuj(Float x1) {
		float sizeAbs = Math.abs(x1);
		if(sizeAbs < 0.5){
			return 1;
		} else if (sizeAbs> 0.5 && sizeAbs < 1.4 ) {
			return 2;
		} else {
			return 3;
		}
	}



	boolean first = true;
	float previousLowX, previousLowY, previousLowZ;

	private void lowPass(Vertex vertex) {

		float x = vertex.getX();
		float y = vertex.getY();
		float z = vertex.getZ();

		if (first) {
			previousLowX = x;
			previousLowY = y;
			previousLowZ = z;

			first = false;
		}

		float filterFactor = 0.1f;

		float filterX = (x * filterFactor + (previousLowX * (1.0f - filterFactor)));
		float filterY = (y * filterFactor + (previousLowY * (1.0f - filterFactor)));
		float filterZ = (z * filterFactor + (previousLowZ * (1.0f - filterFactor)));

		System.out.println("xFl: " + x);
		System.out.println("filterX:  " + filterX);

		vertex.setX(filterX);
		vertex.setY(filterY);
		vertex.setZ(filterZ);

		previousLowX = x;
		previousLowY = y;
		previousLowZ = z;

		verticies.add(vertex);
	}

	private void sendToServer(Float x1, Float y1, Float z1) {

		InetAddress local = null;
		DatagramSocket s = null;
		int server_port = 12345;

		String xStr = x1.toString();
		String yStr = y1.toString();
		String zStr = z1.toString();

		byte[] xByte = xStr.getBytes();
		byte[] yByte = yStr.getBytes();
		byte[] zByte = zStr.getBytes();

		try {
			local = InetAddress.getByName(ipAddress1 + "." + ipAddress2 + "." + ipAddress3 + "." + ipAddress4);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		DatagramPacket p = new DatagramPacket(xByte, xStr.length(), local, server_port);
		DatagramPacket p1 = new DatagramPacket(yByte, yStr.length(), local, server_port);
		DatagramPacket p2 = new DatagramPacket(zByte, zStr.length(), local, server_port);

		try {
			s = new DatagramSocket();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		try {
			s.send(p);
			s.send(p1);
			s.send(p2);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void makeToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT);
	}
}
