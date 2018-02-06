package gr.metaptixiako.forumergasia;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText username,password,repassword,username2,surname;
    String user,pass,repass,user2,surnameid;
    int counter = 3;
    Button b2;
    private String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        b2 = (Button)findViewById(R.id.egrafi);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = (EditText) findViewById(R.id.editText5);
                user = username.getText().toString();
                password = (EditText) findViewById(R.id.pass);
                pass = password.getText().toString();
                repassword = (EditText) findViewById(R.id.repass);
                repass = repassword.getText().toString();
                username2 = (EditText) findViewById(R.id.name);
                user2 = username2.getText().toString();
                surname = (EditText) findViewById(R.id.surname);
                surnameid = surname.getText().toString();

                SQLiteDatabase mydatabase = openOrCreateDatabase("Forum", MODE_PRIVATE, null);

                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS forumLogin (Username VARCHAR,Password VARCHAR,Result VARCHAR);");

                try {

                    if(pass==repass && user!=null && user2!=null && surnameid!=null ) {

                        mydatabase.execSQL("INSERT INTO forumLogin VALUES('" + user + "','" + pass + "','ok');");
                        Toast.makeText(getApplicationContext(), "Μπράβο, εκτελέστηκε επιτυχώς.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);

                    }else{

                        Toast.makeText(getApplicationContext(), "Λάθος Διαπιστευτήρια ή δεν έχετε συμπληρώσει όλα τα πεδία", Toast.LENGTH_SHORT).show();
                    }


                } catch (IndexOutOfBoundsException e) {
                    Toast.makeText(getApplicationContext(), "Λάθος Διαπιστευτήρια", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}

