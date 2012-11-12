package cz.payas.db;

import java.util.ArrayList;
import java.util.List;

import cz.payas.model.Gesture;
import cz.payas.model.Move;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "gestures.db";

	// Contacts table name
	private static final String TABLE_GESTURE = "gesture";
	private static final String TABLE_MOVE = "move";

	private static final String GESTURE_ID = "id";
	private static final String GESTURE_NAME = "name";

	private static final String MOVE_ID = "id";
	private static final String MOVE_ORDER = "poradi";
	private static final String MOVE_DIRECTION = "direction";
	private static final String MOVE_SIZE = "size";
	private static final String MOVE_SIZE_NORMALIZE = "size_normalize";
	private static final String MOVE_AXIS = "axis";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_MOVE_TABLE = "CREATE TABLE " + TABLE_MOVE + "(" + MOVE_ID
				+ " INTEGER," + MOVE_ORDER
				+ " INTEGER," + MOVE_DIRECTION + " INTEGER," + MOVE_SIZE
				+ " REAL,"+ MOVE_SIZE_NORMALIZE + " INTEGER," + MOVE_AXIS + " INTEGER )";
		db.execSQL(CREATE_MOVE_TABLE);

		String CREATE_GESTURE_TABLE = "CREATE TABLE " + TABLE_GESTURE + "("
				+ GESTURE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ GESTURE_NAME + " TEXT )";
		db.execSQL(CREATE_GESTURE_TABLE);
		
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GESTURE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVE);

		// Create tables again
		onCreate(db);
	}

	// ================= CRUD operation ==============
	// ==================== GESTURE ===================
	// Adding new gesture
	public void addGesture(Gesture gesture) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(GESTURE_ID, gesture.getId());
		values.put(GESTURE_NAME, gesture.getName());

		// Inserting Row
		db.insert(TABLE_GESTURE, null, values);
		db.close(); // Closing database connection
	}

	// Getting single gesture
	public Gesture getGesture(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_GESTURE, new String[] { GESTURE_ID,
				GESTURE_NAME }, GESTURE_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		Gesture gesture = new Gesture(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1));
		// return contact
		return gesture;
	}

	// Getting All Gestures
	public List<Gesture> getAllGestures() {

		List<Gesture> gestureList = new ArrayList<Gesture>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_GESTURE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Gesture gesture = new Gesture();
				gesture.setId(Integer.parseInt(cursor.getString(0)));
				gesture.setName(cursor.getString(1));

				// Adding gesture to list
				gestureList.add(gesture);
			} while (cursor.moveToNext());
		}

		// return gesture list
		return gestureList;

	}

	// Getting gestures Count
	public Integer getGesturesCount() {
		String countQuery = "SELECT * FROM " + TABLE_GESTURE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int result = cursor.getCount();
		cursor.close();
		db.close();

		// return count
		return result;

	}

	// Updating single gesture
	public int updateGesture(Gesture gesture) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(GESTURE_NAME, gesture.getName());

		// updating row
		return db.update(TABLE_GESTURE, values, GESTURE_ID + " = ?",
				new String[] { String.valueOf(gesture.getId()) });
	}

	// Deleting single gesture
	public void deleteGesture(Gesture gesture) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_GESTURE, GESTURE_ID + " = ?",
				new String[] { String.valueOf(gesture.getId()) });
		db.close();
	}

	// ==================== MOVE ===================
	// ============================================

	// Adding new move
	public void addMove(Move move) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(MOVE_ID, move.getId());
		values.put(MOVE_ORDER, move.getOrder());
		values.put(MOVE_DIRECTION, move.getDirection());
		values.put(MOVE_SIZE, move.getSize());
		values.put(MOVE_SIZE_NORMALIZE, move.getSizeNormalize());
		values.put(MOVE_AXIS, move.getAxis());

		// Inserting Row
		db.insert(TABLE_MOVE, null, values);
		db.close(); // Closing database connection
	}

	// Getting single move
	public Move getMove(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_MOVE, new String[] { MOVE_ID,
				MOVE_ORDER, MOVE_DIRECTION, MOVE_SIZE, MOVE_SIZE_NORMALIZE, MOVE_AXIS }, MOVE_ID
				+ "=?", new String[] { String.valueOf(id) }, null, null, null,
				null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		
		Move move = new Move();

		move.setId(Integer.parseInt(cursor.getString(0)));
		move.setOrder(Integer.parseInt(cursor.getString(1)));
		move.setDirection(Integer.parseInt(cursor.getString(2)));
		move.setSize(Float.parseFloat(cursor.getString(3)));
		move.setSizeNormalize(Integer.parseInt(cursor.getString(4)));
		move.setAxis(Integer.parseInt(cursor.getString(5)));

		// return move
		return move;
	}
	
	// Getting single move
		public ArrayList<Move> getMoveByGesture(int id) {
			SQLiteDatabase db = this.getReadableDatabase();
			ArrayList<Move> moveList = new ArrayList<Move>();
			
			Cursor cursor = db.query(TABLE_MOVE, new String[] { MOVE_ID,
					MOVE_ORDER, MOVE_DIRECTION, MOVE_SIZE, MOVE_SIZE_NORMALIZE, MOVE_AXIS }, MOVE_ID
					+ "=?", new String[] { String.valueOf(id) }, null, null, MOVE_ORDER,
					null);
			if (cursor.moveToFirst()) {
				do {
					Move move = new Move();

					move.setId(Integer.parseInt(cursor.getString(0)));
					move.setOrder(Integer.parseInt(cursor.getString(1)));
					move.setDirection(Integer.parseInt(cursor.getString(2)));
					move.setSize(Float.parseFloat(cursor.getString(3)));
					move.setSizeNormalize(Integer.parseInt(cursor.getString(4)));
					move.setAxis(Integer.parseInt(cursor.getString(5)));

					// Adding move to list
					moveList.add(move);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();

			// return move
			return moveList;
		}

	// Getting All Moves
	public List<Move> getAllMoves() {

		List<Move> moveList = new ArrayList<Move>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_MOVE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Move move = new Move();

				move.setId(Integer.parseInt(cursor.getString(0)));
				move.setOrder(Integer.parseInt(cursor.getString(1)));
				move.setDirection(Integer.parseInt(cursor.getString(2)));
				move.setSize(Float.parseFloat(cursor.getString(3)));
				move.setSizeNormalize(Integer.parseInt(cursor.getString(4)));
				move.setAxis(Integer.parseInt(cursor.getString(5)));

				// Adding move to list
				moveList.add(move);
			} while (cursor.moveToNext());
		}

		// return move list
		return moveList;

	}
	
	// Getting moves Count
	public Integer getMovesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_MOVE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int result = cursor.getCount();
		cursor.close();

		// return count
		return result;

	}

	// Updating single move
	public int updateMove(Move move) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(MOVE_ORDER, move.getOrder());
		values.put(MOVE_DIRECTION, move.getDirection());
		values.put(MOVE_SIZE, move.getSize());
		values.put(MOVE_SIZE_NORMALIZE, move.getSizeNormalize());
		values.put(MOVE_AXIS, move.getAxis());
		

		// updating row
		return db.update(TABLE_MOVE, values, MOVE_ID + " = ?",
				new String[] { String.valueOf(move.getId()) });
	}

	// Deleting single move
	public void deleteMove(Move move) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MOVE, MOVE_ID + " = ?",
				new String[] { String.valueOf(move.getId()) });
		db.close();
	}

}
