package gr.metaptixiako.forumergasia;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
                setContentView(R.layout.post_list);

                Intent intent = getIntent();
                topic_invisible = intent.getStringExtra("topic_invisible");
                System.out.println("topic_invisible " + topic_invisible);


                final Button new_topic_Button = findViewById(R.id.button_posts_new);
                new_topic_Button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        EditText new_post_EditText = (EditText) findViewById(R.id.posts_new);
                        String new_post = new_post_EditText.getText().toString();
                        SQLiteDatabase mydatabase = openOrCreateDatabase("ForumDB", MODE_PRIVATE, null);

                        mydatabase.execSQL("INSERT INTO posts (text , parent_id) VALUES ('"+new_post+"',"+topic_invisible+");");

                        Intent intent = getIntent();
                        overridePendingTransition(0, 0);//Για την εισαγωγή καινούριου τοπικ να κανει restart intent
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(intent);
                    }
                });

                DataList = new ArrayList<>();
                lv = (ListView) findViewById(R.id.list_posts);

                new gr.metaptixiako.forumergasia.PostActivity.GetData().execute();
            }

            private class GetData extends AsyncTask<Void, Void, Void> {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    Toast.makeText(gr.metaptixiako.forumergasia.PostActivity.this,"Φόρτωση Δεδομένων",Toast.LENGTH_LONG).show();

                }

                @Override
                protected Void doInBackground(Void... arg0) {

                    try {

                        SQLiteDatabase mydatabase = openOrCreateDatabase("ForumDB", MODE_PRIVATE, null);



                        DataList = new ArrayList<>();

                        int sumForum = 0;
                        String sumForumStr;

                        Cursor allPosts = mydatabase.rawQuery("SELECT * FROM posts WHERE parent_id = "+topic_invisible, null);


                        if (allPosts.moveToFirst()) {
                            while (allPosts.isAfterLast()==false) {
                                HashMap<String, String> list = new HashMap<>();

                                sumForum = sumForum+1;

                                sumForumStr = "" + sumForum;
                                String postname = allPosts.getString(allPosts.getColumnIndex("text"));

                                System.out.println("text " + postname);

                                list.put("name", postname);
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
                    ListAdapter adapter = new SimpleAdapter(gr.metaptixiako.forumergasia.PostActivity.this, DataList, R.layout.activity_post,new String[]{"sumForumStr","name"},new int[]{R.id.post_id,R.id.post_name});
                    lv.setAdapter(adapter);

                }

            }



        }