package gr.metaptixiako.forumergasia;

import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;



public class LoginActivity extends AppCompatActivity {


    EditText username,password;
    String user,pass;
    int counter = 3;
    Button b2;
    private String TAG = LoginActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        b2 = (Button)findViewById(R.id.button_login);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = "";
                pass = "";

                username = (EditText) findViewById(R.id.username);
                user = username.getText().toString();
                password = (EditText) findViewById(R.id.password);
                pass = password.getText().toString();
               // System.out.println("pass " + pass);


                SQLiteDatabase mydatabase = openOrCreateDatabase("Forum", MODE_PRIVATE, null);

                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS forumLogin (Username VARCHAR,Password VARCHAR,Result VARCHAR);");

                mydatabase.execSQL("INSERT INTO forumLogin VALUES('admin','admin','ok');");
                try {
                    user = "admin";
                    pass = "admin";
                    Cursor resultSet = mydatabase.rawQuery("Select * from forumLogin WHERE Username ='" + user + "' AND Password = '" + pass + "'", null);
                    if (resultSet == null){
                        Toast.makeText(getApplicationContext(),
                                "Συνδεθήκατε Επιτυχώς", Toast.LENGTH_SHORT).show();
                    }else{
                    resultSet.moveToFirst();}
                    //String username = resultSet.getString(resultSet.getColumnIndex("Username"));
                    // String password = resultSet.getString(resultSet.getColumnIndex("Password"));
                    String result = resultSet.getString(resultSet.getColumnIndex("Result"));

                    System.out.println("Result " + result);

                    if ("ok".equals(result)) {

                        Toast.makeText(getApplicationContext(),
                                "Συνδεθήκατε Επιτυχώς", Toast.LENGTH_SHORT).show();


                        Intent in = new Intent(LoginActivity.this, ForumActivity.class);
                        startActivity(in);

                    } else if (user == null || pass == null) {

                        Toast.makeText(getApplicationContext(), "Λάθος Διαπιστευτήρια", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getApplicationContext(), "Λάθος Διαπιστευτήρια", Toast.LENGTH_SHORT).show();
                        counter--;

                        if (counter == 0) {
                            Toast.makeText(getApplicationContext(), "Προσπαθήστε Αργότερα", Toast.LENGTH_SHORT).show();
                            b2.setEnabled(false);
                        }

                    }
                } catch (IndexOutOfBoundsException e) {
                    Toast.makeText(getApplicationContext(), "Λάθος Διαπιστευτήρια", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}

