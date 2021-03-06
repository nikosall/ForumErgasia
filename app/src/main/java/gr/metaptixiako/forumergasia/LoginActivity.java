package gr.metaptixiako.forumergasia;

import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {


    EditText username,password;
    TextView textViewReg;
    String user,pass;
    String result;
    Button b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        textViewReg =(TextView)findViewById(R.id.textView_register); //στην activity_login.xml υπάρχει ένα text view για register
                                                                    // εδώ οριζω μια μεταβλητή τύπου text view να
                                                                    //να γίνει clickable ώστε να μπορέσω να μεταφερθώ αν
                                                                    //θέλω από το login στο gegister να κάνω εγγραφή.

        textViewReg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });


        b2 = (Button)findViewById(R.id.button_login);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = (EditText) findViewById(R.id.username);
                user = username.getText().toString();
                password = (EditText) findViewById(R.id.password);
                pass = password.getText().toString();
                System.out.println("pass " + pass);
                System.out.println("user " + user);

                if (user.equals("") || pass.equals("")) {

                    Toast.makeText(getApplicationContext(), "Συμπληρώστε όλα τα πεδία.", Toast.LENGTH_SHORT).show();

                }


                //συνδέεται με την βάση απλά για να κάνει login
                SQLiteDatabase mydatabase = openOrCreateDatabase("Forum", MODE_PRIVATE, null);

                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS forumLogin (Username VARCHAR,Password VARCHAR,Result VARCHAR);");

                try {
                    //έλεγχος οτι υπάρχει ο χρήστης. Με το select * from .... φέρνει όλα τα στοιχεία του πίνακα.
                    Cursor resultSet = mydatabase.rawQuery("Select * from forumLogin WHERE Username ='" + user + "' AND Password = '" + pass + "'", null);
                    //όταν κάνει εγγραφή βάζει στην βάση μία μεταβλητή "ok". Αυτό το κάνει για να ελέγξει μετά ότι υπάχει ο χρήστης με το  if ("ok".equals(result)).

                        resultSet.moveToFirst(); //Η πρώτη τιμή του ερωτήματος

                            result = resultSet.getString(resultSet.getColumnIndex("Result"));
                            System.out.println("Result " + result);

                            if ("ok".equals(result)) {

                                Toast.makeText(getApplicationContext(),
                                        "Συνδεθήκατε Επιτυχώς", Toast.LENGTH_SHORT).show();


                                Intent in = new Intent(LoginActivity.this, ForumActivity.class);
                                startActivity(in);

                            } else {

                                Toast.makeText(getApplicationContext(), "Λάθος Διαπιστευτήρια", Toast.LENGTH_SHORT).show();

                            }


                } catch (IndexOutOfBoundsException e) {
                    Toast.makeText(getApplicationContext(), "Λάθος Διαπιστευτήρια...", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}

