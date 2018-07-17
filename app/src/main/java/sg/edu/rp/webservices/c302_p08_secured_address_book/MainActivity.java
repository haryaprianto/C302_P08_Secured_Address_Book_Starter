package sg.edu.rp.webservices.c302_p08_secured_address_book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView lvContact;
    private ArrayList<Contact> alContact;
    private ArrayAdapter<Contact> aaContact;
    ContactAdapter ca;
    // TODO (3) Declare loginId and apikey
    private String apiKey;
    private String loginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvContact = (ListView) findViewById(R.id.listViewContact);
        alContact = new ArrayList<Contact>();

        aaContact = new ContactAdapter(this, R.layout.contact_row, alContact);
        lvContact.setAdapter(aaContact);

        // TODO (4) Get loginId and apikey from the previous Intent
        Intent IDRecieve = getIntent();
        apiKey = IDRecieve.getStringExtra("apikey");
        loginId = IDRecieve.getStringExtra("loginId");



        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO (7) When a contact is selected, create an Intent to View Contact Details
                // Put the following into intent:- contact_id, loginId, apikey
                Intent intent = new Intent(MainActivity.this, ViewContactDetailsActivity.class);
                Contact selectedContact = alContact.get(position);
                int contactId = selectedContact.getContactId();
                String firstName = selectedContact.getFirstName();
                String lastName = selectedContact.getLastName();
                String mobileNum = selectedContact.getMobile();
                intent.putExtra("contactId", contactId);
                intent.putExtra("lastName", lastName);
                intent.putExtra("firstName", firstName);
                intent.putExtra("mobile", mobileNum);

                intent.putExtra("apikey", apiKey);
                intent.putExtra("loginId", loginId);

                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        alContact.clear();



        // Code for step 1 start
        HttpRequest request = new HttpRequest
                ("http://10.0.2.2/C302_P08_SecuredCloudAddressBook/getListOfContacts.php");
        request.setOnHttpResponseListener(mHttpResponseListener);
        request.setMethod("POST");
        request.addData("apikey", apiKey);
        request.addData("loginId", loginId);
        request.execute();
        alContact = new ArrayList<Contact>();
        aaContact = new ContactAdapter(this,R.layout.contact_row,alContact);
        lvContact.setAdapter(aaContact);
        Intent IDRecieve = getIntent();
        apiKey = IDRecieve.getStringExtra("apikey");
        loginId = IDRecieve.getStringExtra("loginid");

    }

    // TODO (6) In the HttpResponseListener for getListOfContacts.php, get all contacts from the results and show in the list
    private HttpRequest.OnHttpResponseListener mHttpResponseListener =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response){
                    // process response here
                    try {

                        JSONArray jsonArray = new JSONArray(response);

                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject jsonObj = jsonArray.getJSONObject(i);

                            String contactId = jsonObj.getString("id");
                            String firstName = jsonObj.getString("firstname");
                            String lastName = jsonObj.getString("lastname");
                            String mobile = jsonObj.getString("mobile");
                            alContact.add(new Contact(Integer.parseInt(contactId),firstName,lastName,mobile));
                            Log.v("found","item");
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    aaContact.notifyDataSetChanged();

                }
            };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_add) {

            // TODO (8) Create an Intent to Create Contact
            // Put the following into intent:- loginId, apikey
            Intent intent = new Intent(getBaseContext(),CreateContactActivity.class);
            intent.putExtra("apikey", apiKey);
            intent.putExtra("loginId", loginId);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}
