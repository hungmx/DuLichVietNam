package com.dhthuyloi.mxhung.dulichvietnam.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dhthuyloi.mxhung.dulichvietnam.model.DiaDanh;
import com.dhthuyloi.mxhung.dulichvietnam.model.Experience;
import com.dhthuyloi.mxhung.dulichvietnam.model.Hotel;
import com.dhthuyloi.mxhung.dulichvietnam.model.Introduce;
import com.dhthuyloi.mxhung.dulichvietnam.model.Place;
import com.dhthuyloi.mxhung.dulichvietnam.model.Restaurant;
import com.dhthuyloi.mxhung.dulichvietnam.model.Tour;
import com.dhthuyloi.mxhung.dulichvietnam.model.Vehicle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.R.id.list;

/**
 * Created by MXHung on 9/26/2016.
 */
public class DBManager extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "dulich.sqlite";
	public static final int DATABASE_VERSION = 1;
	public static final String TABLE_DIADANH = "diadanh";

	public static final String COLUMN_ID_DIADANH = "ID_DIADANH";
	public static final String COLUMN_NAME = "NAME";
	public static final String COLUMN_IMAGE = "IMAGE";
	public static final String COLUMN_IMAGE_INT = "IMAGE_INT";
	public static final String COLUMN_LATLNG = "LATLNG";
	public static final String COLUMN_REGIONS = "REGIONS";
	public static final String COLUMN_CITY = "CITY";
	public static final String COLUMN_FAVORITE = "FAVORITE";

	public static final String TABLE_PLACE = "diemdi";
	public static final String COLUMN_ID = "ID";
	public static final String COLUMN_DETAIL = "DETAIL";

	public static final String TABLE_INTRODUCE = "gioithieu";
	public static final String COLUMN_INTRODUCE = "INTRODUCE";

	public static final String TABLE_VEHICLE = "phuongtien";
	public static final String TABLE_HOTEL = "nhanghi";
	public static final String COLUMN_PHONE = "PHONE";
	public static final String COLUMN_PRICE = "PRICE";

	public static final String TABLE_RESTAURANT = "doan";
	public static final String TABLE_TOUR = "lichtrinh";
	public static final String TABLE_EXPERIENCE = "kinhnghiem";
	public static final String COLUMN_DAY = "DAY";

	private static final String DB_PATH_SUFFIX = "/databases/";

	static Context context;
//	String pathDatabase = "";
	public DBManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
//		pathDatabase = context.getFilesDir().getParent() + "/databases/" + DATABASE_NAME;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void CopyDataBaseFromAsset() throws IOException {

		InputStream myInput = context.getAssets().open(DATABASE_NAME);

		// Path to the just created empty db
		String outFileName = getDatabasePath();

		// if the path doesn't exist first, create it
		File f = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
		if (!f.exists())
			f.mkdir();

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	private static String getDatabasePath() {
		return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX
				+ DATABASE_NAME;
	}

	public SQLiteDatabase openDataBase() throws SQLException {
		File dbFile = context.getDatabasePath(DATABASE_NAME);
//		boolean deleted = dbFile.delete();
//		Log.d("delete", String.valueOf(deleted));
//		File db= context.getDatabasePath(DATABASE_NAME);
//		boolean delete = dbFile.exists();
//		Log.d("delete", String.valueOf(delete));
		if (!dbFile.exists()) {
			try {
				CopyDataBaseFromAsset();
				System.out.println("Copying sucess from Assets folder");
			} catch (IOException e) {
				throw new RuntimeException("Error creating source database", e);
			}
		}

		return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
	}

	public ArrayList<DiaDanh> getDiaDanh(){
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<DiaDanh> list = new ArrayList<>();
		String sql = "SELECT * FROM " + DBManager.TABLE_DIADANH;
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			DiaDanh diaDanh = new DiaDanh();
			diaDanh.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));
			diaDanh.setNameDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			diaDanh.setImDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			diaDanh.setImage_int(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE_INT)));
			diaDanh.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
			diaDanh.setCity(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_CITY)));
			diaDanh.setRegions(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_REGIONS))));
			diaDanh.setFavotite(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_FAVORITE))));

			list.add(diaDanh);
			cursor.moveToNext();
		}
		Log.d("ketqua", String.valueOf(list));
		return list;
	}
	public long editDiadanh (DiaDanh diaDanh){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBManager.COLUMN_IMAGE_INT, diaDanh.getImage_int());
//		Log.d("image", diaDanh.getImDiaDanh());
		Log.d("editD", String.valueOf(db.update(DBManager.TABLE_DIADANH,values,DBManager.COLUMN_ID_DIADANH + " =?", new String[]{String.valueOf(diaDanh.getIdDiaDanh())})));
		long ketqua = db.update(DBManager.TABLE_DIADANH,values,DBManager.COLUMN_ID_DIADANH + " =?", new String[]{String.valueOf(diaDanh.getIdDiaDanh())});

		Log.v("InsertDatabase","Ket qua :" + ketqua);

		return ketqua;
	}

	//chuc nang favorite dia danh
	public int editFavorite (DiaDanh diaDanh){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBManager.COLUMN_FAVORITE, diaDanh.getFavotite());
//		Log.d("image", diaDanh.getImDiaDanh());
		Log.d("--editFavotite", String.valueOf(db.update(DBManager.TABLE_DIADANH,values,DBManager.COLUMN_ID_DIADANH + " =?", new String[]{String.valueOf(diaDanh.getIdDiaDanh())})));
		return db.update(DBManager.TABLE_DIADANH,values,DBManager.COLUMN_ID_DIADANH + " =?", new String[]{String.valueOf(diaDanh.getIdDiaDanh())});
	}
	public ArrayList<DiaDanh> getDiaDanhID(int regions){
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<DiaDanh> list = new ArrayList<>();
		String sql = "SELECT * FROM " + DBManager.TABLE_DIADANH + " WHERE " + DBManager.COLUMN_REGIONS + " = " + regions + " ORDER BY " + COLUMN_NAME;
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			DiaDanh diaDanh = new DiaDanh();
			diaDanh.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));
			diaDanh.setNameDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			diaDanh.setImDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			diaDanh.setImage_int(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE_INT)));
			diaDanh.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
			diaDanh.setCity(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_CITY)));
			diaDanh.setRegions(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_REGIONS))));
			diaDanh.setFavotite(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_FAVORITE))));

			list.add(diaDanh);
			cursor.moveToNext();
		}
		Log.d("ketqua", String.valueOf(list));
		return list;
	}

	public ArrayList<DiaDanh> getFavorite(){
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<DiaDanh> list = new ArrayList<>();
		String sql = "SELECT * FROM " + DBManager.TABLE_DIADANH + " WHERE " + DBManager.COLUMN_FAVORITE + " = 1 "  + " ORDER BY " + COLUMN_NAME;
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			DiaDanh diaDanh = new DiaDanh();
			diaDanh.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));
			diaDanh.setNameDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			diaDanh.setImDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			diaDanh.setImage_int(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE_INT)));
			diaDanh.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
			diaDanh.setCity(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_CITY)));
			diaDanh.setRegions(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_REGIONS))));
			diaDanh.setFavotite(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_FAVORITE))));

			list.add(diaDanh);
			cursor.moveToNext();
		}
		Log.d("ketqua", String.valueOf(list));
		return list;
	}

	public ArrayList<DiaDanh> searchFavorite(String s, int favorite){
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<DiaDanh> list = new ArrayList<>();
//		String sql = "SELECT * FROM " + DBManager.TABLE_DIADANH;
		String sql = "SELECT " + COLUMN_ID_DIADANH + ", " + COLUMN_NAME + ", " + COLUMN_IMAGE + ", " + COLUMN_LATLNG + ", " + COLUMN_REGIONS + ", " + COLUMN_CITY + ", " + COLUMN_FAVORITE
				+ " FROM "+ TABLE_DIADANH + " WHERE " + COLUMN_NAME + " LIKE '%" + s + "%'" + " AND " + COLUMN_FAVORITE + " = " + favorite;
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			DiaDanh diaDanh = new DiaDanh();
			diaDanh.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));
			diaDanh.setNameDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			diaDanh.setImDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			diaDanh.setImage_int(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE_INT)));
			diaDanh.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
			diaDanh.setRegions(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_REGIONS))));
			diaDanh.setFavotite(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_FAVORITE))));
			diaDanh.setCity(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_CITY)));

			list.add(diaDanh);
			cursor.moveToNext();
		}
		Log.d("ketqua", String.valueOf(list));
		return list;
	}
	public DiaDanh getDiaDanhDetail(int id_diadanh){
		SQLiteDatabase db = this.getWritableDatabase();
		DiaDanh diaDanh = new DiaDanh();
		String sql = "SELECT * FROM " + DBManager.TABLE_DIADANH + " WHERE " + DBManager.COLUMN_ID_DIADANH + " = " + id_diadanh;
		Cursor cursor = db.rawQuery(sql,null);
		if (cursor.moveToFirst()){
			diaDanh.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));
			diaDanh.setNameDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			diaDanh.setImDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			diaDanh.setImage_int(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE_INT)));
			diaDanh.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
			diaDanh.setCity(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_CITY)));
			diaDanh.setRegions(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_REGIONS))));
			diaDanh.setFavotite(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_FAVORITE))));

		}
		Log.d("ketqua", String.valueOf(diaDanh));
		return diaDanh;
	}


	public ArrayList<DiaDanh> searchDiaDanh(String s, int region){
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<DiaDanh> list = new ArrayList<>();
//		String sql = "SELECT * FROM " + DBManager.TABLE_DIADANH;
		String sql = "SELECT " + COLUMN_ID_DIADANH + ", " + COLUMN_NAME + ", " + COLUMN_IMAGE + ", " + COLUMN_LATLNG + ", " + COLUMN_REGIONS + ", " + COLUMN_CITY + ", " + COLUMN_FAVORITE + ", " + COLUMN_IMAGE_INT
				+ " FROM "+ TABLE_DIADANH + " WHERE " + COLUMN_NAME + " LIKE '%" + s + "%'" + " AND " + COLUMN_REGIONS + " = " + region;
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			DiaDanh diaDanh = new DiaDanh();
			diaDanh.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));
			diaDanh.setNameDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			diaDanh.setImDiaDanh(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			diaDanh.setImage_int(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE_INT)));
			diaDanh.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
			diaDanh.setRegions(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_REGIONS))));
			diaDanh.setFavotite(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_FAVORITE))));
			diaDanh.setCity(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_CITY)));

			list.add(diaDanh);
			cursor.moveToNext();
		}
		Log.d("ketqua", String.valueOf(list));
		return list;
	}
	public ArrayList<Place> getPlace(){
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Place> list = new ArrayList<>();
		String sql = "SELECT * FROM " + DBManager.TABLE_PLACE;
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Place place = new Place();
			place.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
			place.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			place.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			place.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
			place.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
			place.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

			list.add(place);
			cursor.moveToNext();
		}
		Log.d("ketqua", String.valueOf(list));
		return list;
	}

	public ArrayList<Place> getPlaceId(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Place> list = new ArrayList<>();
		String sql = "SELECT * FROM " + DBManager.TABLE_PLACE + " WHERE " + DBManager.COLUMN_ID_DIADANH + " = " + id  + " ORDER BY " + COLUMN_NAME;
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Place place = new Place();
			place.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
			place.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			place.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			place.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
			place.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
			place.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

			list.add(place);
			cursor.moveToNext();
		}
		Log.d("ketqua", String.valueOf(list));
		return list;
	}
	public Place getPlaceDetail(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "SELECT * FROM " + DBManager.TABLE_PLACE + " WHERE " + DBManager.COLUMN_ID + " = " + id;
		Place place = new Place();
		Cursor cursor = db.rawQuery(sql,null);
		if (cursor.moveToFirst()){
			place.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
			place.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			place.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			place.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
			place.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
			place.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));
		}
		Log.d("ketqua", String.valueOf(place));
		return place;
	}

	public ArrayList<Place> searchPlace(String s, int id_diadanh){
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Place> list = new ArrayList<>();
		String sql = "SELECT " + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_IMAGE + ", " + COLUMN_LATLNG + ", " + COLUMN_DETAIL + ", " + COLUMN_ID_DIADANH
				+ " FROM "+ TABLE_PLACE + " WHERE " + COLUMN_NAME + " LIKE '%" + s + "%'"+ " AND " + COLUMN_ID_DIADANH + " = " + id_diadanh;
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Place place = new Place();
			place.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
			place.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			place.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			place.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
			place.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
			place.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

			list.add(place);
			cursor.moveToNext();
		}
		Log.d("ketqua", String.valueOf(list));
		return list;
	}
	public int editPlace (Place place){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBManager.COLUMN_IMAGE, place.getImage());
		Log.d("image", place.getImage());
		Log.d("editP", String.valueOf(db.update(DBManager.TABLE_PLACE,values,DBManager.COLUMN_ID + " =?", new String[]{String.valueOf(place.getId())})));
		return db.update(DBManager.TABLE_PLACE,values,DBManager.COLUMN_ID + " =?", new String[]{String.valueOf(place.getId())});
	}

	//vehicle


	public ArrayList<Vehicle> getVedicle(){
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Vehicle> list = new ArrayList<>();
		String sql = "SELECT * FROM " + DBManager.TABLE_VEHICLE;
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Vehicle vehicle = new Vehicle();
			vehicle.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
			vehicle.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			vehicle.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			vehicle.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
			vehicle.setId_diadanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

			list.add(vehicle);
			cursor.moveToNext();
		}
		Log.d("ketqua", String.valueOf(list));
		return list;
	}

	public ArrayList<Vehicle> getVedicleID(int id_diadanh){
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Vehicle> list = new ArrayList<>();
		String sql = "SELECT * FROM " + DBManager.TABLE_VEHICLE + " WHERE " + DBManager.COLUMN_ID_DIADANH + " = " + id_diadanh;
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Vehicle vehicle = new Vehicle();
			vehicle.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
			vehicle.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			vehicle.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			vehicle.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
			vehicle.setId_diadanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

			list.add(vehicle);
			cursor.moveToNext();
		}
		Log.d("ketqua", String.valueOf(list));
		return list;
	}

	public Vehicle getVedicleDetail(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "SELECT * FROM " + DBManager.TABLE_VEHICLE + " WHERE " + DBManager.COLUMN_ID + " = " + id;
		Cursor cursor = db.rawQuery(sql,null);
		Vehicle vehicle = new Vehicle();
		if (cursor.moveToFirst()){
			vehicle.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
			vehicle.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			vehicle.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			vehicle.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
			vehicle.setId_diadanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

		}
		Log.d("ketqua", String.valueOf(list));
		return vehicle;
	}


	public int editVehicle (Vehicle vehicle){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBManager.COLUMN_IMAGE, vehicle.getImage());
		Log.d("image", vehicle.getImage());
		Log.d("editV", String.valueOf(db.update(DBManager.TABLE_VEHICLE,values,DBManager.COLUMN_ID + " =?", new String[]{String.valueOf(vehicle.getId())})));
		return db.update(DBManager.TABLE_VEHICLE,values,DBManager.COLUMN_ID + " =?", new String[]{String.valueOf(vehicle.getId())});
	}

	//hotel

	public ArrayList<Hotel> getHotel(){
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Hotel> list = new ArrayList<>();
		String sql = "SELECT * FROM " + DBManager.TABLE_HOTEL;
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Hotel hotel = new Hotel();
			hotel.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
			hotel.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			hotel.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			hotel.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
			hotel.setPhone(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PHONE)));
			hotel.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
			hotel.setPrice(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PRICE)));
			hotel.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

			list.add(hotel);
			cursor.moveToNext();
		}
		Log.d("ketqua", String.valueOf(list));
		return list;
	}

	public ArrayList<Hotel> getHotelID(int id_diadanh){
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Hotel> list = new ArrayList<>();
		String sql = "SELECT * FROM " + DBManager.TABLE_HOTEL + " WHERE " + DBManager.COLUMN_ID_DIADANH + " = " + id_diadanh;
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Hotel hotel = new Hotel();
			hotel.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
			hotel.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			hotel.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			hotel.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
			hotel.setPhone(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PHONE)));
			hotel.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
			hotel.setPrice(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PRICE)));
			hotel.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

			list.add(hotel);
			cursor.moveToNext();
		}
		Log.d("ketqua", String.valueOf(list));
		return list;
	}

	public Hotel getHotelDetail(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "SELECT * FROM " + DBManager.TABLE_HOTEL + " WHERE " + DBManager.COLUMN_ID + " = " + id;
		Cursor cursor = db.rawQuery(sql,null);
		Hotel hotel = new Hotel();
		if (cursor.moveToFirst()){
			hotel.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
			hotel.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			hotel.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			hotel.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
			hotel.setPhone(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PHONE)));
			hotel.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
			hotel.setPrice(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PRICE)));
			hotel.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));
		}
		return hotel;
	}
	public int editHotel (Hotel hotel){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBManager.COLUMN_IMAGE, hotel.getImage());
		Log.d("image", hotel.getImage());
		Log.d("editH", String.valueOf(db.update(DBManager.TABLE_HOTEL,values,DBManager.COLUMN_ID + " =?", new String[]{String.valueOf(hotel.getId())})));
		return db.update(DBManager.TABLE_HOTEL,values,DBManager.COLUMN_ID + " =?", new String[]{String.valueOf(hotel.getId())});
	}

	public ArrayList<Hotel> searchHotel(String s, int id_diadanh){
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Hotel> list = new ArrayList<>();
		String sql = "SELECT " + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_IMAGE + ", " + COLUMN_LATLNG + ", " + COLUMN_PHONE + ", " + COLUMN_DETAIL + ", " + COLUMN_ID_DIADANH + ", " + COLUMN_PRICE
				+ " FROM "+ TABLE_HOTEL + " WHERE " + COLUMN_NAME + " LIKE '%" + s + "%'" + " AND " + COLUMN_ID_DIADANH + " = " + id_diadanh;
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Hotel hotel = new Hotel();
			hotel.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
			hotel.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			hotel.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			hotel.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
			hotel.setPhone(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PHONE)));
			hotel.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
			hotel.setPrice(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PRICE)));
			hotel.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

			list.add(hotel);
			cursor.moveToNext();
		}
		Log.d("ketqua", String.valueOf(list));
		return list;
	}
	//get resstaurant
	public ArrayList<Restaurant> getRestaurant(){
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Restaurant> list = new ArrayList<>();
		String sql = "SELECT * FROM " + DBManager.TABLE_RESTAURANT;
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Restaurant restaurant = new Restaurant();
			restaurant.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
			restaurant.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			restaurant.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			restaurant.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
			restaurant.setPhone(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PHONE)));
			restaurant.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
			restaurant.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

			list.add(restaurant);
			cursor.moveToNext();
		}
		Log.d("ketqua", String.valueOf(list));
		return list;
	}
	public ArrayList<Restaurant> getRestaurantId(int id_diadanh){
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Restaurant> list = new ArrayList<>();
		String sql = "SELECT * FROM " + DBManager.TABLE_RESTAURANT + " WHERE " + DBManager.COLUMN_ID_DIADANH + " = " + id_diadanh;
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Restaurant restaurant = new Restaurant();
			restaurant.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
			restaurant.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			restaurant.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			restaurant.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
			restaurant.setPhone(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PHONE)));
			restaurant.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
			restaurant.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

			list.add(restaurant);
			cursor.moveToNext();
		}
		Log.d("ketqua", String.valueOf(list));
		return list;
	}

	public Restaurant getRestaurantDetail(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "SELECT * FROM " + DBManager.TABLE_RESTAURANT + " WHERE " + DBManager.COLUMN_ID + " = " + id;
		Cursor cursor = db.rawQuery(sql,null);
		Restaurant restaurant = new Restaurant();
		if (cursor.moveToFirst()){
			restaurant.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
			restaurant.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			restaurant.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			restaurant.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
			restaurant.setPhone(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PHONE)));
			restaurant.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
			restaurant.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

		}
		return restaurant;
	}

	public ArrayList<Restaurant> searchRestaurant(String s, int id_diadanh){
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Restaurant> list = new ArrayList<>();
		String sql = "SELECT " + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_IMAGE + ", " + COLUMN_LATLNG + ", " + COLUMN_PHONE + ", " + COLUMN_DETAIL + ", " + COLUMN_ID_DIADANH
				+ " FROM "+ TABLE_RESTAURANT + " WHERE " + COLUMN_NAME + " LIKE '%" + s + "%'" + " AND " + COLUMN_ID_DIADANH + " = " + id_diadanh;
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Restaurant restaurant = new Restaurant();
			restaurant.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
			restaurant.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			restaurant.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			restaurant.setLatlng(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_LATLNG)));
			restaurant.setPhone(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PHONE)));
			restaurant.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
			restaurant.setIdDiaDanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

			list.add(restaurant);
			cursor.moveToNext();
		}
		Log.d("ketqua", String.valueOf(list));
		return list;
	}
	public int editRestaurant (Restaurant restaurant){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBManager.COLUMN_IMAGE, restaurant.getImage());
		Log.d("image", restaurant.getImage());
		Log.d("editR", String.valueOf(db.update(DBManager.TABLE_RESTAURANT,values,DBManager.COLUMN_ID + " =?", new String[]{String.valueOf(restaurant.getId())})));
		return db.update(DBManager.TABLE_RESTAURANT,values,DBManager.COLUMN_ID + " =?", new String[]{String.valueOf(restaurant.getId())});
	}
	//get lich trinh
	public ArrayList<Tour> getTourId(int id_diadanh){
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Tour> list = new ArrayList<>();
		String sql = "SELECT * FROM " + DBManager.TABLE_TOUR + " WHERE " + DBManager.COLUMN_ID_DIADANH + " = " + id_diadanh;
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Tour tour = new Tour();
			tour.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
			tour.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));
			tour.setDay(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DAY))));
			tour.setId_diadanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));

			list.add(tour);
			cursor.moveToNext();
		}
		Log.d("ketqua", String.valueOf(list));
		return list;
	}
	public Introduce getIntroID(int id_diadanh){
		SQLiteDatabase db = this.getWritableDatabase();
		Introduce introduce = new Introduce();
		String sql = "SELECT * FROM " + DBManager.TABLE_INTRODUCE + " WHERE " + DBManager.COLUMN_ID_DIADANH + " = " + id_diadanh;
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			introduce.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
			introduce.setIntro(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_INTRODUCE)));
			introduce.setId_diadanh(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID_DIADANH))));
			cursor.moveToNext();
		}
		Log.d("ketqua", String.valueOf(introduce));
		return introduce;
	}

	///kinh nghiemmmmm

	public ArrayList<Experience> getExperience(){
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Experience> list = new ArrayList<>();
		String sql = "SELECT * FROM " + DBManager.TABLE_EXPERIENCE;
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Experience experience = new Experience();
			experience.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
			experience.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			experience.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			experience.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));

			list.add(experience);
			cursor.moveToNext();
		}
		Log.d("ketqua", String.valueOf(list));
		return list;
	}

	public Experience getExperienceDetail(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Experience> list = new ArrayList<>();
		String sql = "SELECT * FROM " + DBManager.TABLE_EXPERIENCE + " WHERE " + COLUMN_ID + " = " + id;
		Cursor cursor = db.rawQuery(sql,null);
		Experience experience = new Experience();
		if (cursor.moveToFirst()){
			experience.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_ID))));
			experience.setName(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_NAME)));
			experience.setImage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_IMAGE)));
			experience.setDetail(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_DETAIL)));

		}
		return experience;
	}



}
