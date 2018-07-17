package sg.edu.rp.webservices.c302_p08_secured_address_book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class ViewContactDetailsActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etMobile;
    private Button btnUpdate, btnDelete;
    private int contactId;

    private String apiKey;
    private String loginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact_details);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etMobile = findViewById(R.id.etMobile);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);


    }




    @Override
    protected void onResume() {
        super.onResume();

        // Code for step 1 start
        Intent intent = getIntent();
        contactId = intent.getIntExtra("contactId", -1);
        apiKey = intent.getStringExtra("apikey");
        loginId = intent.getStringExtra("loginId");

        String firstName = intent.getStringExtra("firstName");
        String lastName = intent.getStringExtra("lastName");
        String mobileNum = intent.getStringExtra("mobile");

        etFirstName.setText(firstName);
        etLastName.setText(lastName);
        etMobile.setText(mobileNum);


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://10.0.2.2/C302_P08_SecuredCloudAddressBook/updateContact.php";
                HttpRequest request = new HttpRequest(url);
                request.setOnHttpResponseListener(mHttpResponseListener);
                request.setMethod("POST");
                request.addData("id",contactId + "");
                request.addData("apikey",apiKey);
                request.addData("loginId",loginId);

                request.addData("FirstName", etFirstName.getText().toString());
                request.addData("LastName", etLastName.getText().toString());
                request.addData("Mobile", etMobile.getText().toString());
                request.execute();

            }
        });

//        btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = "http://10.0.2.2/C302_P08_SecuredCloudAddressBook/deleteContact.php?";
//                HttpRequest request = new HttpRequest(url);
//
//                request.setOnHttpResponseListener(mHttpResponseListener);
//                request.setMethod("POST");
//                request.addData("id",String.valueOf(contactId));
//
//                request.execute();
//                Intent intent = new Intent(ViewContactDetailsActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
    }

//    private HttpRequest.OnHttpResponseListener mHttpResponseListener =
//            new HttpRequest.OnHttpResponseListener() {
//                @Override
//                public void onResponse(String response){
//
//                    // process response here
//                    try {
//                        Log.i("JSON Results: ", response);
//                        JSONObject jsonObj = new JSONObject(response);
//                        Toast.makeText(getApplicationContext(), jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
//                    }
//                    catch(Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            };

    private HttpRequest.OnHttpResponseListener mHttpResponseListener =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response){

                    // process response here
                    try {
                        Log.i("JSON Results: ", response);
                        JSONObject jsonObj = new JSONObject(response);
                        Toast.makeText(getApplicationContext(), jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ViewContactDetailsActivity.this, MainActivity.class);
                        intent.putExtra("apikey",apiKey);
                        intent.putExtra("loginId",loginId);
                        startActivity(intent);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            };
}