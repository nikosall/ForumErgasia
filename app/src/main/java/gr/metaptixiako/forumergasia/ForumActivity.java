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
    int forumVersion = 2;

    ArrayList<HashMap<String, String>> DataList; //Πίνακας με δύο στήλες

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_list);

        DataList = new ArrayList<>();

        new GetData().execute();
    }

    private class GetData extends AsyncTask<Void, Void, Void> { //μία κλάση που τρέχει στο παρασκήνιο
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ForumActivity.this,"Φόρτωση Δεδομένων",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                //σύνδεση και δημιουργία της βάσης δεδομένων

                SQLiteDatabase mydatabase = openOrCreateDatabase("ForumDB", MODE_PRIVATE, null);

                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS forums (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR , parent_id INTEGER);");
                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS topics (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR , parent_id INTEGER);");
                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS posts (id INTEGER PRIMARY KEY AUTOINCREMENT, text VARCHAR , parent_id INTEGER , author_id INTEGER);");

                Cursor test = mydatabase.rawQuery("Select * from forums ", null);

                if(forumVersion!=1 || test==null){  // forumVersion!=1 : Αυτό καθαρά για να βοηθήσει να διαγραφω τα πάντα και να βλέπω τις αλλαγές όταν θα
                                                     //ολοκληρωθεί το πρόγραμμα μπορεί να διαγαφεί

                    mydatabase.execSQL("DROP TABLE IF EXISTS forums;"); //διαγράφει τους πάνακες
                    mydatabase.execSQL("DROP TABLE IF EXISTS topics;");
                    mydatabase.execSQL("DROP TABLE IF EXISTS posts;");

                    //φτιάχνει τους πίνακες
                    mydatabase.execSQL("CREATE TABLE IF NOT EXISTS forums (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR , parent_id INTEGER);");
                    mydatabase.execSQL("CREATE TABLE IF NOT EXISTS topics (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR , parent_id INTEGER);");
                    mydatabase.execSQL("CREATE TABLE IF NOT EXISTS posts (id INTEGER PRIMARY KEY AUTOINCREMENT, text VARCHAR , parent_id INTEGER , author_id INTEGER);");

                    //περνάει τα δεδομένα από το QueryInsert.java
                    mydatabase.execSQL(new QueryInsert().QueryInsertForums());
                    mydatabase.execSQL(new QueryInsert().QueryInsertTopics());
                    mydatabase.execSQL(new QueryInsert().QueryInsertPosts());
                }

                DataList = new ArrayList<>();

                int sum = 0;
                String sumStr;

                Cursor allForums = mydatabase.rawQuery("Select * from forums ", null);

                if (allForums.moveToFirst()) { //κάνει loop σε όλα τα στοιχεία του πίνακα στην βάση δεδομένων
                    while (allForums.isAfterLast()==false) {

                        HashMap<String, String> list = new HashMap<>();//ορίζει μια λίστα

                        sum = sum+1;//για το Α/Α αριθμό της λίστας

                        sumStr = "" + sum;//τέχνασμα γιατί η λίστα θέλει string για να μετατραπεί από int σε string
                        String forumname = allForums.getString(allForums.getColumnIndex("name"));

                        int forum_invisible =allForums.getInt(allForums.getColumnIndex("id"));
                        //Τα περνάει όλα σε λίστα και μετά τα προσθέτε σε Datalist
                        list.put("forumname", forumname);
                        list.put("forum_invisible", ""+forum_invisible);
                        list.put("sumForumStr", sumStr);

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

            //Copy paste από το ίντερνετ να φτιαχτεί μια λίστα (ListAdapter). Στην ουσία πάει στο activity_forum.xml και περνάει
            //γραμμή γραμμή τα δεδομένα και τα πετάει στην λίστα.
            lv = (ListView) findViewById(R.id.list_forum);
            ListAdapter adapter = new SimpleAdapter(ForumActivity.this, DataList, R.layout.activity_forum,new String[]{"sumForumStr","forumname","forum_invisible"},new int[]{R.id.forum_id,R.id.forum_name,R.id.forum_invisible});
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new ListClickHandler()); //Κάνει την λίστα clickable.


        }

        public class ListClickHandler implements AdapterView.OnItemClickListener {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
                // TODO Auto-generated method stub
                TextView bird_id = (TextView) view.findViewById(R.id.forum_invisible);
                String forum_invisibleid = bird_id.getText().toString();
                Intent intent = new Intent(ForumActivity.this, TopicActivity.class);
                intent.putExtra("forum_invisible", forum_invisibleid); //Τεχνική να μπορούν να συνδεθούν τα intent μεταξύ τους με
                                                                            //το κλειδί στην συγγεκριμένη περίπτωση forum_invisibleid.
                                                                            //το οποίο με το intent.putExtra απλά το μεταφέρει στο επόμενο intent όπου θα το
                                                                            //ανακτήσουμε με intent.getStringExtra.
                startActivity(intent);

            }

        }

    }



}


