package gr.metaptixiako.forumergasia;

import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class PostActivity extends AppCompatActivity {

    private ListView lv;
    public String topic_invisible;

    ArrayList<HashMap<String, String>> DataList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();
        String topic_invisible = intent.getStringExtra("topic_invisible");
        System.out.println("topic_invisible " + topic_invisible);

        DataList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list_post);

        new GetData().execute();
    }

    private class GetData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(PostActivity.this,"Φόρτωση Δεδομένων",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {


            try {

                SQLiteDatabase mydatabase = openOrCreateDatabase("ForumDB", MODE_PRIVATE, null);



                DataList = new ArrayList<>();

                int sumForum = 0;
                String sumForumStr;

                Cursor allPosts = mydatabase.rawQuery("SELECT * FROM posts", null);


                if (allPosts.moveToFirst()) {
                    while (allPosts.isAfterLast()==false) {
                        HashMap<String, String> list = new HashMap<>();

                        sumForum = sumForum+1;

                        sumForumStr = "" + sumForum;
                        String postname = allPosts.getString(allPosts.getColumnIndex("text"));

                        //System.out.println("name " + topicname);

                        int post_invisible = allPosts.getInt(allPosts.getColumnIndex("parent_id"));
                        //System.out.println("id " + topic_invisible);

                        list.put("text", postname);
                        //list.put("post_invisible", ""+post_invisible);
                        list.put("sumForumStr", sumForumStr);
                        DataList.add(list);

                        allPosts.moveToNext();
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
            lv = (ListView) findViewById(R.id.list_post);
            ListAdapter adapter = new SimpleAdapter(PostActivity.this, DataList, R.layout.activity_post,new String[]{"sumForumStr","text"},new int[]{R.id.post_id,R.id.post_name});
            lv.setAdapter(adapter);

        }


    }



}





