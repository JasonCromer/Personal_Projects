package org.android.jasoncromer.coverme;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Name of table
    private static final String USER_TABLE = "user";

    private static EditText companyName;
    private static EditText companyLocation;
    private static EditText daysToCover;
    private static EditText hoursToCover;
    private static Button postButton;
    private static Button userPostListButton;
    private static Button deleteButton;
    private static TextView userInfoTextView;
    protected DatabaseHelper db;
    protected List<UserModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(getApplicationContext());

        companyName = (EditText) findViewById(R.id.companyNameEditText);
        companyLocation = (EditText) findViewById(R.id.companyLocationEditText);
        daysToCover = (EditText) findViewById(R.id.daysCoveredEditText);
        hoursToCover = (EditText) findViewById(R.id.hoursCoveredEditText);
        postButton = (Button) findViewById(R.id.postButton);
        userPostListButton = (Button) findViewById(R.id.userPostListButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        userInfoTextView = (TextView) findViewById(R.id.userInfoList);

        postButton.setOnClickListener(this);
        userPostListButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == findViewById(R.id.postButton)) {
            UserModel user = new UserModel();
            user.userCompanyName = companyName.getText().toString();
            user.userCompanyLocation = companyLocation.getText().toString();
            user.userDayCovered = daysToCover.getText().toString();
            user.userHoursCovered = hoursToCover.getText().toString();

            db.addUserDetails(user);
            list = db.getAllUsersList();
            printUserList(list);
        }
        if(v == findViewById(R.id.userPostListButton)) {
            list = db.getAllUsersList();
            printUserList(list);
        }
        if(v == findViewById(R.id.deleteButton)){
                long rowid = db.getOldestRowID();
                Toast.makeText(getApplicationContext(), Long.toString(rowid), Toast.LENGTH_LONG).show();
                db.deleteEntry(rowid);

        }
    }

    public void clearEditTexts() {
        companyName.setText("");
        companyLocation.setText("");
        daysToCover.setText("");
        hoursToCover.setText("");
    }

    private void printUserList(List<UserModel> list) {
        String value = "";
        for(UserModel user : list) {
            value = value + "id:" + user.userID + ", " + user.userCompanyName
                    + ", " + user.userCompanyLocation
                    + ", " + user.userDayCovered
                    + ", " + user.userHoursCovered + "\n";
        }
        userInfoTextView.setText(value);
    }
}
