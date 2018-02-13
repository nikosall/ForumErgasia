package gr.metaptixiako.forumergasia;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);


        //με το κουμπί b2 θα πάρει όλα τα στοιχεία από την φόρμα εγγραφής και αφού περάσει
        //τους ελένχους ότι δεν είναι null και ότι το pass και το repass είναι ίδιοι κωδικοί
        //θα βάλει το username και το pass στην βάση.

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

                try {//το try catch το βάλαμε για έλενχο άν γίνει κάποιο σφάλμα να εντοπιστεί

                    //System.out.println(pass);
                    //System.out.println(repass);


                    if( !user.equals("")&& !user2.equals("") && !surnameid.equals("") && !pass.equals("") && !repass.equals("") && new String(pass).equals(repass)){ //έλενχος

                        SQLiteDatabase mydatabase = openOrCreateDatabase("Forum", MODE_PRIVATE, null); //δημιουργεί ή συνδέεται με την βάση Forum

                        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS forumLogin (Username VARCHAR,Password VARCHAR,Result VARCHAR);"); //φτιάχνει τον πίνακα forumLogin αν είναι ο πρώτος
                                                                                                                                        //χρήστης ή συνδέεται με αυτόν.

                        mydatabase.execSQL("INSERT INTO forumLogin VALUES('" + user + "','" + pass + "','ok');");   //βάζει στον πίνακα τα username kai ta passsword.
                        Toast.makeText(getApplicationContext(), "Μπράβο, εκτελέστηκε επιτυχώς.", Toast.LENGTH_SHORT).show(); //απλό μύνημα κειμένου που εκτυπώνεται στην οθόνη.
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class); //προετοιμάζει την μεταφορά σε καινούριο intent
                        startActivity(intent);

                    }else{

                        Toast.makeText(getApplicationContext(), "Λάθος Διαπιστευτήρια ή δεν έχετε συμπληρώσει όλα τα πεδία", Toast.LENGTH_SHORT).show();
                    }


                } catch (IndexOutOfBoundsException e) {
                    Toast.makeText(getApplicationContext(), "Λάθος Διαπιστευτήρια..", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}

