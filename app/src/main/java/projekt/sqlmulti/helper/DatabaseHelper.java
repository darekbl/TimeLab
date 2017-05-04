package projekt.sqlmulti.helper;

        import projekt.sqlmulti.FileUtils;
        import projekt.sqlmulti.MainActivity;
        import projekt.sqlmulti.model.Photo_model;
        import projekt.sqlmulti.model.TimesArray_model;
        import projekt.sqlmulti.model.User_model;
        import projekt.sqlmulti.model.Video_model;

        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;
        import java.util.Locale;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteException;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.os.Environment;
        import android.util.Log;
        import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    private static final String LOG_TIMES = DatabaseHelper.class.getName() + "_TimesArray";

    // Database Version
    private static final int DATABASE_VERSION = 16;

    // Database Name
    private static final String DATABASE_NAME = "ProfilesDatabase";


    //Tables names
    private static final String TABLE_VIDEO_PROFILES = "video_profiles";
    private static final String TABLE_PHOTO_PROFILES = "photo_profiles";
    private static final String TABLE_OF_TIMES = "times_table";
    private static final String TABLE_USER = "user";

    // Login Table Columns names
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    // GENERAL Table - column names
    private static final String KEY_NAME = "name";
    private static final String KEY_SPEED = "speed";
    private static final String KEY_DIRECTION = "direction";
    private static final String KEY_SHUTTER = "shutter";
    private static final String KEY_PROCESS_TIME = "process_time";
    private static final String KEY_ID = "_id";
    private static final String KEY_UID = "_uid";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_TIM = "TIM";
    private static final String KEY_TIM_ID = "times_array_id";
    private static final String KEY_HI_SP_MODE = "high_speed_mode";
    private static final String KEY_RETURN_MODE = "return_mode";

    // Table Create Statements
    private static final String CREATE_USER_TABLE = "CREATE TABLE "
            + TABLE_USER + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_UID + " INTEGER," +
            KEY_NAME + " TEXT," +
            KEY_EMAIL + " TEXT," +
            KEY_PASSWORD + " TEXT," +
            KEY_CREATED_AT + " DATETIME" +
            ")";

    private static final String CREATE_TABLE_VIDEO = "CREATE TABLE "
            + TABLE_VIDEO_PROFILES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_UID + " INTEGER," +
            KEY_NAME + " TEXT," +
            KEY_SPEED + " INTEGER," +
            KEY_DIRECTION + " INTEGER," +
            KEY_HI_SP_MODE + " INTEGER," +
            KEY_RETURN_MODE + " INTEGER," +
            KEY_CREATED_AT + " DATETIME" +
            ")";

    private static final String CREATE_TABLE_PHOTO = "CREATE TABLE "
            + TABLE_PHOTO_PROFILES + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_UID + " INTEGER," +
            KEY_NAME + " TEXT," +
            KEY_SPEED + " INTEGER," +
            KEY_SHUTTER + " INTEGER," +
            KEY_PROCESS_TIME + " INTEGER," +
            KEY_DIRECTION + " INTEGER," +
            KEY_TIM_ID + " INTEGER," +
            KEY_HI_SP_MODE + " INTEGER," +
            KEY_RETURN_MODE + " INTEGER," +
            KEY_CREATED_AT + " DATETIME" +
            ")";

    private static final String CREATE_TABLE_OF_TIMES = "CREATE TABLE "
            + TABLE_OF_TIMES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_UID + " INTEGER," +
            KEY_NAME + " TEXT," +
            "TIM1" + " INTEGER," +
            "TIM2" + " INTEGER," +
            "TIM3" + " INTEGER," +
            "TIM4" + " INTEGER," +
            "TIM5" + " INTEGER," +
            "TIM6" + " INTEGER," +
            "TIM7" + " INTEGER," +
            "TIM8" + " INTEGER," +
            "TIM9" + " INTEGER," +
            "TIM10" + " INTEGER," +
            KEY_CREATED_AT + " DATETIME" +
            ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_PHOTO);
        db.execSQL(CREATE_TABLE_VIDEO);
        db.execSQL(CREATE_TABLE_OF_TIMES);
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO_PROFILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEO_PROFILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OF_TIMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // create new tables
        onCreate(db);
    }


    //public static String DB_FILEPATH = "/data/data/{package_name}/databases/database.db";
    public static String DB_FILEPATH = Environment.getDataDirectory() + "/data/"+ "projekt.sqlmulti" + "/databases/"+ DATABASE_NAME + ".db";

    /**
     * Copies the database file at the specified location over the current
     * internal application database.
     * */
    public boolean importDatabase(String dbPath) throws IOException {

        // Close the SQLiteOpenHelper so it will commit the created empty
        // database to internal storage.
        close();
        File newDb = new File(dbPath);
        File oldDb = new File(DB_FILEPATH);
        if (newDb.exists()) {
            FileUtils.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
            // Access the copied database so SQLiteHelper will cache it and mark
            // it as created.
            getWritableDatabase().close();
            return true;
        }
        return false;
    }

    // ----------------------- "User" table methods ----------------//
    /**
     * Creating a user profiles
     */
    public long createUser(User_model um) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, um.getName());
        values.put(KEY_PASSWORD, um.getPassword());
        values.put(KEY_EMAIL, um.getEmail());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long user_id = db.insert(TABLE_USER, null, values);

        return user_id;
    }

    /**
     * get user profile
     */
    public User_model getUser(long user_id) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE "
                + KEY_ID + " = " + user_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        User_model um = new User_model();
        um.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        um.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        um.setPassword(c.getString(c.getColumnIndex(KEY_PASSWORD)));
        um.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
        um.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        c.close();

        return um;
    }

    public List<User_model> getAllUsers() {
        //List<User_model> Users = new ArrayList<User_model>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        //User_model[] Users = new User_model[];
        List<User_model> Users = new ArrayList<User_model>();

        int i = 0;
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                User_model um = new User_model();
                um.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                um.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                um.setPassword((c.getString(c.getColumnIndex(KEY_PASSWORD))));
                um.setEmail((c.getString(c.getColumnIndex(KEY_EMAIL))));
                um.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                Users.add(um);
            } while (c.moveToNext());
        }
        c.close();
        return Users;
    }

    public int getUsersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }



    // ------------------------ "video" table methods ----------------//

    /**
     * Creating a video profiles
     */
    public long createVid(Video_model video_model) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, video_model.getName());
        values.put(KEY_UID, video_model.getUId());
        values.put(KEY_SPEED, video_model.getSpeed());
        values.put(KEY_DIRECTION, video_model.getDirectionINT());
        //values.put(KEY_SHUTTER, video_model.getShutterINT());
        values.put(KEY_RETURN_MODE, video_model.getReturnModeINT());
        values.put(KEY_HI_SP_MODE, video_model.getHighSpeedINT());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long vid_id = db.insert(TABLE_VIDEO_PROFILES, null, values);

         return vid_id;
    }

    /**
     * get single video profile
     */

    public Video_model getVideo(long vid_id, long uid) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_VIDEO_PROFILES + " WHERE "
                + KEY_ID + " = " + vid_id + " AND " + KEY_UID + " = " + uid  ;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Video_model vm = new Video_model();
        vm.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        vm.setUId(c.getInt(c.getColumnIndex(KEY_UID)));
        vm.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        vm.setSpeed(c.getInt(c.getColumnIndex(KEY_SPEED)));
        //vm.setShutterByInt(c.getInt(c.getColumnIndex(KEY_SHUTTER)));
        vm.setHighSpeedModeByInt(c.getInt(c.getColumnIndex(KEY_HI_SP_MODE)));
        vm.setDirectionByInt(c.getInt(c.getColumnIndex(KEY_DIRECTION)));
        vm.setReturnModeByInt(c.getInt(c.getColumnIndex(KEY_RETURN_MODE)));
        vm.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        Log.i(LOG,vm.getSummaryString());
        c.close();
        return vm;
    }

    /**
     * getting all video profiles
     * */
    public List<Video_model> getAllVidProf() {
        List<Video_model> videos = new ArrayList<Video_model>();
        String selectQuery = "SELECT  * FROM " + TABLE_VIDEO_PROFILES;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Video_model vm = new Video_model();
                vm.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                vm.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                //
                //    GETY DO BOOLI !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //
                vm.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to todo list
                videos.add(vm);
            } while (c.moveToNext());
        }
        c.close();
        return videos;
    }

    /**
     * getting all video profiles under single tag ?????????????
     * */
//    public List<video_model> getAllToDosByTag(String tag_name) {
//        List<Todo> todos = new ArrayList<Todo>();
//
//        String selectQuery = "SELECT  * FROM " + TABLE_TODO + " td, "
//                + TABLE_TAG + " tg, " + TABLE_TODO_TAG + " tt WHERE tg."
//                + KEY_TAG_NAME + " = '" + tag_name + "'" + " AND tg." + KEY_ID
//                + " = " + "tt." + KEY_TAG_ID + " AND td." + KEY_ID + " = "
//                + "tt." + KEY_TODO_ID;
//
//        Log.e(LOG, selectQuery);
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor c = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (c.moveToFirst()) {
//            do {
//                Todo td = new Todo();
//                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
//                td.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
//                td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
//
//                // adding to todo list
//                todos.add(td);
//            } while (c.moveToNext());
//        }
//        c.close();
//        return todos;
//    }

    /**
     * getting video count
     */
    public int getVideoCount() {
        String countQuery = "SELECT  * FROM " + TABLE_VIDEO_PROFILES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a video profile
     */

    public int updateVideo(Video_model Video_model, long uid) {
        SQLiteDatabase db = this.getWritableDatabase();

        int direction = (Video_model.getDirection()) ? 1 : 0;
        //int shutter = (Video_model.getShutter()) ? 1 : 0;
        int return_mode = (Video_model.getReturnMode()) ? 1 : 0;
        int hsm = (Video_model.getHighSpeed()) ? 1 : 0;

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, Video_model.getName());
        values.put(KEY_SPEED, Video_model.getSpeed());
        values.put(KEY_DIRECTION, direction);
        //values.put(KEY_SHUTTER, shutter);
        values.put(KEY_RETURN_MODE, return_mode);
        values.put(KEY_HI_SP_MODE, hsm);

        // updating row

        return db.update(TABLE_VIDEO_PROFILES, values, KEY_ID + " = ? AND " + KEY_UID + " = ? ",
                new String[] { String.valueOf(Video_model.getId()), String.valueOf(uid) });    }

    public void deleteVideoByID(int id, int uid) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_VIDEO_PROFILES, KEY_ID + " = ? AND " + KEY_UID + " = ? ", new String[] { String.valueOf(id),String.valueOf(uid) });
    }

    /**
     * Deleting a video profile
     */
    public void deleteVideo(long vid_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VIDEO_PROFILES, KEY_ID + " = ?",
                new String[] { String.valueOf(vid_id) });
    }

    // ------------------------ "photo" table methods ----------------//

    /**
     * Creating photo profile
     */
    public long createPhoto(Photo_model photo_model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, photo_model.getName());
        values.put(KEY_UID, photo_model.getUId());
        values.put(KEY_SPEED, photo_model.getSpeed());

        values.put(KEY_DIRECTION, photo_model.getDirectionINT());
        values.put(KEY_HI_SP_MODE, photo_model.getHighSpeedINT());
        values.put(KEY_SHUTTER, photo_model.getShutterINT());
        values.put(KEY_RETURN_MODE, photo_model.getReturnINT());
        values.put(KEY_PROCESS_TIME, photo_model.getProcess_time());
        values.put(KEY_TIM_ID, photo_model.getTimes_id());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long photo_id = db.insert(TABLE_PHOTO_PROFILES, null, values);

        return photo_id;
    }

    /**
     * get single photo profile
     */
    public Photo_model getPhoto(long photo_id, long uid) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PHOTO_PROFILES + " WHERE "
                + KEY_ID + " = " + photo_id + " AND " + KEY_UID + " = " + uid  ;

        Log.i(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Photo_model pm = new Photo_model();
        pm.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pm.setUId(c.getInt(c.getColumnIndex(KEY_UID)));
        pm.setName((c.getString(c.getColumnIndex(KEY_NAME))));
        pm.setDirectionByInt(c.getInt(c.getColumnIndex(KEY_DIRECTION)));
        pm.setShutterByInt(c.getInt(c.getColumnIndex(KEY_SHUTTER)));
        //pm.setHighSpeedModeByInt(c.getInt(c.getColumnIndex(KEY_HI_SP_MODE)));
        pm.setReturnModeByInt(c.getInt(c.getColumnIndex(KEY_RETURN_MODE)));
        pm.setSpeed(c.getInt(c.getColumnIndex((KEY_SPEED))));
        pm.setProcess_time(c.getInt(c.getColumnIndex((KEY_PROCESS_TIME))));
        pm.setTimes_id(c.getInt(c.getColumnIndex((KEY_TIM_ID))));
        pm.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        c.close();
        return pm;
    }

    public int getPhotoIdByName(String photo_name, long uid) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PHOTO_PROFILES + " WHERE "
                + KEY_NAME + " ='" + photo_name + "'" + " AND " + KEY_UID + " = " + uid  ;

        Log.i(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        int id = c.getInt(c.getColumnIndex(KEY_ID));

        c.close();
        return id;
    }


    /**
     * getting all photo profiles
     * */
    public ArrayList<Photo_model> getAllUserPhotos(int uid) {
        ArrayList<Photo_model> photo_models = new ArrayList<Photo_model>();
        String selectQuery = "SELECT  * FROM " + TABLE_PHOTO_PROFILES + " WHERE " + KEY_UID + " = " + uid;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Photo_model pm = new Photo_model();
                pm.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                pm.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                pm.setDirectionByInt(c.getInt(c.getColumnIndex(KEY_DIRECTION)));
                pm.setShutterByInt(c.getInt(c.getColumnIndex(KEY_SHUTTER)));
                pm.setSpeed(c.getInt(c.getColumnIndex((KEY_SPEED))));
                pm.setProcess_time(c.getInt(c.getColumnIndex((KEY_PROCESS_TIME))));
                pm.setTimes_id(c.getInt(c.getColumnIndex((KEY_TIM_ID))));
                pm.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to tags list
                photo_models.add(pm);
            } while (c.moveToNext());
        }
        c.close();
        return photo_models;
    }

    /**
     * Updating a photo profile
     */
    public int updatePhoto(Photo_model photo_model, long uid) {
        SQLiteDatabase db = this.getWritableDatabase();

        int direction = (photo_model.getDirection()) ? 1 : 0;
        int shutter = (photo_model.getShutter()) ? 1 : 0;
        int return_mode = (photo_model.getReturnMode()) ? 1 : 0;

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, photo_model.getName());
        values.put(KEY_SPEED, photo_model.getSpeed());
        values.put(KEY_DIRECTION, direction);
        values.put(KEY_SHUTTER, shutter);
        values.put(KEY_RETURN_MODE, return_mode);
        values.put(KEY_PROCESS_TIME, photo_model.getProcess_time());
        values.put(KEY_TIM_ID, photo_model.getTimes_id());

        // updating row
        return db.update(TABLE_PHOTO_PROFILES, values, KEY_ID + " = ? AND " + KEY_UID + " = ? ",
                new String[] { String.valueOf(photo_model.getId()), String.valueOf(uid) });
    }

    /**
     * Deleting a photo profile
     */
    public void deletePhoto(Photo_model photo_model, long uid) {
        SQLiteDatabase db = this.getWritableDatabase();

        // before deleting tag
        // check if todos under this tag should also be deleted
        //if (should_delete_all_tag_todos) {
            // get all todos under this tag
      //      List<Photo_model> allTagToDos = getAllToDosByTag(photo_model.getTagName());

            // delete all todos
//            for (Todo todo : allTagToDos) {
//                // delete todo
//                deleteToDo(todo.getId());
//            }
        //}

        // now delete the tag
        db.delete(TABLE_PHOTO_PROFILES, KEY_ID + " = ?",
                new String[] { String.valueOf(photo_model.getId()), String.valueOf(uid) });
    }

    public void deletePhotoByID(int id, int uid) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_PHOTO_PROFILES, KEY_ID + " = ? AND " + KEY_UID + " = ? ", new String[] { String.valueOf(id),String.valueOf(uid) });
    }

    /**
     * getting photo count
     */
    public int getPhotoCount() {
        String countQuery = "SELECT * FROM " + TABLE_PHOTO_PROFILES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }


    public int getUserPhotoCount(int uid) {
        String countQuery = "SELECT * FROM " + TABLE_PHOTO_PROFILES + " WHERE " + KEY_UID + " = " + uid;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }


    // ------------------------ "Arrays of times" table methods ----------------//

    /**
     * Creating a times
     */
    public long createTimes(TimesArray_model timesArray_model) {
        SQLiteDatabase db = this.getWritableDatabase();
        long array_id = 0;
        Log.i(LOG_TIMES,"Creating new array .. started..");

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, timesArray_model.getName());
        values.put(KEY_UID, timesArray_model.getUId());

        for(int nr=0;nr<10;nr++) {
            int nrplus1 = nr+1;
            String timeid = (KEY_TIM + nrplus1);
            values.put(timeid, timesArray_model.getTimeFromArray(nr));
            Log.i(LOG_TIMES,"timeid="+timeid);
        }
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row

         try{
             array_id = db.insertOrThrow(TABLE_OF_TIMES, null, values);
             Log.i(LOG_TIMES,"INSERTING....");
             return array_id;
         }
        catch (SQLiteException e){
            e.printStackTrace();
        Log.e(LOG_TIMES,e.getMessage());
        }

        return array_id;
    }

    /**
     * get single times array
     */
    public TimesArray_model getArray(long array_id, long uid) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_OF_TIMES + " WHERE "
                + KEY_ID + " = " + array_id + " AND " + KEY_UID + " = " + uid;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        int[] timesArray = {0,0,0,0,0,0,0,0,0,0};
        TimesArray_model ta = new TimesArray_model();
        ta.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        ta.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        for(int nr=0;nr<10;nr++) {
            int nr1UP = nr+1;
            timesArray[nr] = c.getInt(c.getColumnIndex( KEY_TIM + nr1UP ));
        }
        ta.setTimes_array(timesArray);
        ta.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        c.close();
        return ta;
    }

    public String getArrayNameByID(long array_id, long uid) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_OF_TIMES + " WHERE "
                + KEY_ID + " = " + array_id + " AND " + KEY_UID + " = " + uid;

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();

        return c.getString(c.getColumnIndex(KEY_NAME));
    }

    public int getArrayIDByName(String array_name, long uid) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_OF_TIMES + " WHERE "
                + KEY_NAME + " = '" + array_name + "'" + " AND " + KEY_UID + " = " + uid;

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();

        return c.getInt(c.getColumnIndex(KEY_ID));
    }

    public int updateArray(TimesArray_model array_model, long uid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, array_model.getName());
        //values.put(KEY_UID, photo_model.getUId());

        for(int nr=0;nr<10;nr++) {
            int nrplus1 = nr+1;
            String timeid = (KEY_TIM + nrplus1);
            values.put(timeid, array_model.getTimeFromArray(nr));
            Log.i(LOG_TIMES,"timeid="+timeid);
        }

        // updating row
        return db.update(TABLE_OF_TIMES, values, KEY_ID + " = ? AND " + KEY_UID + " = ? ",
                new String[] { String.valueOf(array_model.getId()), String.valueOf(uid) });
    }


    /**
     * getting arrays count
     */
    public int getArraysCount() {
        String countQuery = "SELECT * FROM " + TABLE_OF_TIMES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getUserArraysCount(long uid) {
        String countQuery = "SELECT * FROM " + TABLE_OF_TIMES + " WHERE " + KEY_UID + " = " + uid;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }


    /**
     * Deleting an array
     */
    public void deleteArray(long array_id, long uid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OF_TIMES, KEY_ID + " = ? AND " + KEY_UID + " = ?",
                new String[] { String.valueOf(array_id), String.valueOf(uid) });
    }


    public void deleteTimesByID(int id, int uid) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_OF_TIMES, KEY_ID + " = ? AND " + KEY_UID + " = ? ", new String[] { String.valueOf(id),String.valueOf(uid) });
    }


    // ----------------------- "times arrays" names list -----------//

    public List<String> GetTimesArraysNotes (long uid){

        String selectQuery = "SELECT  * FROM " + TABLE_OF_TIMES + " WHERE " + KEY_UID + " = " + uid ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        List<String> TimesArrays = new ArrayList<>();
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                TimesArray_model tam = new TimesArray_model();
                tam.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                // adding to list
                TimesArrays.add(tam.getName());
            } while (c.moveToNext());
        }
        c.close();
        return TimesArrays;

    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


    /**
     * get Array of Notes
     */
    public List<String> GetPhotoProfilesNotes (long uid){

        String selectQuery = "SELECT  * FROM " + TABLE_PHOTO_PROFILES + " WHERE " + KEY_UID + " = " + uid;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        List<String> PhotoProfiles = new ArrayList<>();
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Photo_model pm = new Photo_model();
                pm.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                // adding to list
                PhotoProfiles.add(pm.getName());
            } while (c.moveToNext());
        }
        c.close();
        return PhotoProfiles;

    }

    public List<String> GetVideoProfilesNotes (long uid){

        String selectQuery = "SELECT  * FROM " + TABLE_VIDEO_PROFILES + " WHERE " + KEY_UID + " = " + uid;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        List<String> VideoProfiles = new ArrayList<>();
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Video_model vm = new Video_model();
                vm.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                // adding to list
                Log.e(LOG,"VideoName: " + vm.getName());
                VideoProfiles.add(vm.getName());
            } while (c.moveToNext());
        }
        c.close();
        return VideoProfiles;

    }

    public int getVideoIdByName(String video_name, long uid) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_VIDEO_PROFILES + " WHERE "
                + KEY_NAME + " ='" + video_name + "'" + " AND " + KEY_UID + " = " + uid  ;

        Log.i(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        int id = c.getInt(c.getColumnIndex(KEY_ID));

        c.close();
        return id;
    }

    public String isDbConnected(){
        return "Yes, database is connected.";
    }

    public String DbVersion() { return String.valueOf(DATABASE_VERSION); }

    public String getDbName() { return DATABASE_NAME; }
}
