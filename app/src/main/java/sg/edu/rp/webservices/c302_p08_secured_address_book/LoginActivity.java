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

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.editTextUsername);
        etPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "http://10.0.2.2/C302_P08_SecuredCloudAddressBook/doLogin.php";

                HttpRequest request = new HttpRequest(url);

                request.setOnHttpResponseListener(mHttpResponseListener);
                request.setMethod("POST");
                request.addData("username", etUsername.getText().toString());
                request.addData("password", etPassword.getText().toString());
                request.execute();





            }
        });
    }


    // TODO (2) In the HttpResponseListener, check if the user has been authenticated successfully
    // If the user can log in, extract the id and API Key from the JSON object, set them into Intent and start MainActivity Intent.
    // If the user cannot log in, display a toast to inform user that login has failed.
    private HttpRequest.OnHttpResponseListener mHttpResponseListener =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response){

                    // process response here
                    try {
                        Log.i("JSON Results: ", response);

                        JSONObject jsonObj = new JSONObject(response);
                        boolean authentication = jsonObj.getBoolean("authenticated");
                        String apiKey = jsonObj.getString("apikey");
                        String loginId = jsonObj.getString("id");

                        if (authentication == true){
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("apikey", apiKey);
                            intent.putExtra("loginId", loginId);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Login Failed. Please check your username and password", Toast.LENGTH_SHORT).show();
                        }

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            };
}