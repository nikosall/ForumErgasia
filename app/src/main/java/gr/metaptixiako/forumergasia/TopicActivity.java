package gr.metaptixiako.forumergasia;

import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class TopicActivity extends AppCompatActivity {

    private ListView lv;
    public String forum_invisible;

    ArrayList<HashMap<String, String>> DataList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_list);

        Intent intent = getIntent();
        forum_invisible = intent.getStringExtra("forum_invisible");
        System.out.println("forum_invisible " + forum_invisible);



        final Button map_button = findViewById(R.id.mapsButton);
        map_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(TopicActivity.this, MapsActivity.class);

                startActivity(intent);

            }
        });



        final Button new_topic_Button = findViewById(R.id.button_topic_new);
        new_topic_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText new_topic_EditText = (EditText) findViewById(R.id.topic_new);
                String new_topic = new_topic_EditText.getText().toString();
                System.out.println("new_topic "+new_topic);
                System.out.println("forum_invisible "+forum_invisible);

                SQLiteDatabase mydatabase = openOrCreateDatabase("ForumDB", MODE_PRIVATE, null);

                mydatabase.execSQL("INSERT INTO topics (name , parent_id ) VALUES ('"+new_topic+"',"+forum_invisible+");");

                Intent intent = getIntent();
                overridePendingTransition(0, 0);//Για την εισαγωγή καινούριου τοπικ να κανει restart intent
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        });

        DataList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list_post);

        new GetData().execute();
    }

    private class GetData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(TopicActivity.this,"Φόρτωση Δεδομένων",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                SQLiteDatabase mydatabase = openOrCreateDatabase("ForumDB", MODE_PRIVATE, null);



                DataList = new ArrayList<>();

                int sumForum = 0;
                String sumForumStr;

                Cursor allTopics = mydatabase.rawQuery("SELECT * FROM topics WHERE parent_id = "+forum_invisible, null);


                if (allTopics.moveToFirst()) {
                    while (allTopics.isAfterLast()==false) {
                        HashMap<String, String> list = new HashMap<>();

                        sumForum = sumForum+1;

                        sumForumStr = "" + sumForum;
                        String topicname = allTopics.getString(allTopics.getColumnIndex("name"));

                        System.out.println("name " + topicname);

                        int topic_invisible = allTopics.getInt(allTopics.getColumnIndex("id"));
                        System.out.println("id " + topic_invisible);

                        list.put("name", topicname);
                        list.put("topic_invisible", ""+topic_invisible);
                        list.put("sumForumStr", sumForumStr);
                        DataList.add(list);

                        allTopics.moveToNext();
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
            ListAdapter adapter = new SimpleAdapter(TopicActivity.this, DataList, R.layout.activity_topic,new String[]{"sumForumStr","name"},new int[]{R.id.topic_id,R.id.topic_name});
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new ListClickHandler());


        }

        public class ListClickHandler implements AdapterView.OnItemClickListener {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
                // TODO Auto-generated method stub
                TextView forum_id = (TextView) view.findViewById(R.id.topic_parent_id);
                String topic_invisibleid = forum_id.getText().toString();
                Intent intent = new Intent(TopicActivity.this, PostActivity.class);
                intent.putExtra("topic_invisible", topic_invisibleid);
                startActivity(intent);

            }

        }

    }



}