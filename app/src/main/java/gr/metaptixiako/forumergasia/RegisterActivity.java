package gr.metaptixiako.forumergasia;

import android.content.Intent;
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

                username2 = (EditText) findViewById(R.id.name);
                user2 = username2.getText().toString();
                System.out.println(user2);
                surname = (EditText) findViewById(R.id.surname);
                surnameid = surname.getText().toString();
                System.out.println(surnameid);
                username = (EditText) findViewById(R.id.username);
                user = username.getText().toString();
                System.out.println(user);
                password = (EditText) findViewById(R.id.pass);
                pass = password.getText().toString();
                repassword = (EditText) findViewById(R.id.repassw);
                repass = repassword.getText().toString();

                try {

                    System.out.println(pass);
                    System.out.println(repass);


                    if(pass==repass){

                        SQLiteDatabase mydatabase = openOrCreateDatabase("Forum", MODE_PRIVATE, null);

                        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS forumLogin (Username VARCHAR,Password VARCHAR,Result VARCHAR);");

                        mydatabase.execSQL("INSERT INTO forumLogin VALUES('" + user + "','" + pass + "','ok');");
                        Toast.makeText(getApplicationContext(), "Μπράβο, εκτελέστηκε επιτυχώς.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);

                    }else{

                        Toast.makeText(getApplicationContext(), "Λάθος Διαπιστευτήρια ή δεν έχετε συμπληρώσει όλα τα πεδία", Toast.LENGTH_SHORT).show();
                    }


                } catch (IndexOutOfBoundsException e) {
                    Toast.makeText(getApplicationContext(), "Λάθος Διαπιστευτήρια...........", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}

