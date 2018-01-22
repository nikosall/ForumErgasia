package gr.metaptixiako.forumergasia;

import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class ForumActivity extends AppCompatActivity {

    private ListView lv;
    int forumVersion = 1;

    ArrayList<HashMap<String, String>> DataList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_list);

        DataList = new ArrayList<>();
        //lv = (ListView) findViewById(R.id.list_forum);

        new GetData().execute();
    }

    private class GetData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ForumActivity.this,"Φόρτωση Δεδομένων",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                SQLiteDatabase mydatabase = openOrCreateDatabase("ForumDB", MODE_PRIVATE, null);

                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS forums (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR , parent_id INTEGER);");
                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS topics (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR , parent_id INTEGER);");
                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS posts (id INTEGER PRIMARY KEY AUTOINCREMENT, text VARCHAR , parent_id INTEGER , author_id INTEGER);");



                //mydatabase.rawQuery("SELECT f.name, f.id,COUNT(f.id) AS topic_count, f.parent_id FROM forums AS f LEFT JOIN topics AS t ON f.id = t.parent_id GROUP BY f.id", null);
                Cursor test = mydatabase.rawQuery("Select * from forums ", null);

                if(forumVersion!=1 || test==null){  // Αυτό καθαρά για να βοηθήσει να διαγραφω τα πάντα και να βλέπω τις αλλαγές όταν θα
                                        //ολοκληρωθεί το πρόγραμμα μπορεί να διαγαφεί

                    mydatabase.execSQL("DROP TABLE IF EXISTS forums;");
                    mydatabase.execSQL("DROP TABLE IF EXISTS topics;");
                    mydatabase.execSQL("DROP TABLE IF EXISTS posts;");


                    mydatabase.execSQL("CREATE TABLE IF NOT EXISTS forums (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR , parent_id INTEGER);");
                    mydatabase.execSQL("CREATE TABLE IF NOT EXISTS topics (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR , parent_id INTEGER);");
                    mydatabase.execSQL("CREATE TABLE IF NOT EXISTS posts (id INTEGER PRIMARY KEY AUTOINCREMENT, text VARCHAR , parent_id INTEGER , author_id INTEGER);");

                    mydatabase.execSQL(new QueryInsert().QueryInsertForums());
                    mydatabase.execSQL(new QueryInsert().QueryInsertTopics());
                    mydatabase.execSQL(new QueryInsert().QueryInsertPosts());
                }

                DataList = new ArrayList<>();

                int sumForum = 0;
                String sumForumStr;

                Cursor allForums = mydatabase.rawQuery("Select * from forums ", null);

                if (allForums.moveToFirst()) {
                    while (allForums.isAfterLast()==false) {
                        HashMap<String, String> list = new HashMap<>();

                        sumForum = sumForum+1;

                        sumForumStr = "" + sumForum;
                        String forumname = allForums.getString(allForums.getColumnIndex("name"));

                        System.out.println("name "+forumname);

                        int forum_invisible =allForums.getInt(allForums.getColumnIndex("id"));
                        System.out.println("id "+forum_invisible);

                        list.put("forumname", forumname);
                        list.put("forum_invisible", ""+forum_invisible);
                        list.put("sumForumStr", sumForumStr);
                        DataList.add(list);

                        allForums.moveToNext();
                    }
                }

            } catch (IndexOutOfBoundsException e) {

                Toast.makeText(getApplicationContext(), "Ουπς κάτι πήγε στραβά... Παμε πάλι αργότερα", Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            lv = (ListView) findViewById(R.id.list_forum);
            ListAdapter adapter = new SimpleAdapter(ForumActivity.this, DataList, R.layout.activity_forum,new String[]{"sumForumStr","forumname","forum_invisible"},new int[]{R.id.forum_id,R.id.forum_name,R.id.forum_invisible});
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new ListClickHandler());


        }

        public class ListClickHandler implements AdapterView.OnItemClickListener {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
                // TODO Auto-generated method stub
                TextView bird_id = (TextView) view.findViewById(R.id.forum_invisible);
                String forum_invisibleid = bird_id.getText().toString();
                Intent intent = new Intent(ForumActivity.this, TopicActivity.class);
                intent.putExtra("forum_invisible", forum_invisibleid);
                startActivity(intent);

            }

        }

    }



}


