package com.journaldev.androidrealmdatabase;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnAdd, btnRead, btnUpdate, btnDelete;
    EditText text_productName, text_desc, text_category,text_price,text_marketPrice;
    TextView textView, txtFilterByAge;
    Realm mRealm;
    Spinner spinner_language;
    private ArrayAdapter mAdapter;
    private TextToSpeech convert;
    String translated=null;
    private String mLanguageCode = "hi";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        mRealm = Realm.getDefaultInstance();
    }

    private void initViews() {
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnRead = findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);
        textView = findViewById(R.id.textViewEmployees);
        text_productName = findViewById( R.id.text_productName );
        text_desc = findViewById( R.id.text_desc );
        text_category = findViewById( R.id.text_category );
        text_price = findViewById( R.id.text_price );
        text_marketPrice = findViewById( R.id.text_marketPrice );
        spinner_language = findViewById( R.id.spinner_language );
        List<String> language = new ArrayList<>();
        language.add("English");
        language.add("Hindi");
        mAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,language);
        mAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_language.setAdapter(mAdapter);
        spinner_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItemText = (String) adapterView.getItemAtPosition(i);
                if(selectedItemText.equals( "Hindi" )){
                    onselectedProductName();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                addEmployee();
                break;
            case R.id.btnRead:
                readEmployeeRecords();
                break;
            case R.id.btnUpdate:
                updateEmployeeRecords();
                break;
            case R.id.btnDelete:
                deleteEmployeeRecord();
                break;

        }
    }

    private void addEmployee() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {
                        if (!text_productName.getText().toString().trim().isEmpty()) {
                            Employee employee = new Employee();
                            employee.text_productName = text_productName.getText().toString().trim();
                            if (!text_desc.getText().toString().trim().isEmpty())
                                employee.text_desc = text_desc.getText().toString().trim();
                            if (!text_category.getText().toString().trim().isEmpty())
                                employee.text_category = text_category.getText().toString().trim();
                            if (!text_price.getText().toString().trim().isEmpty())
                                employee.text_price = text_price.getText().toString().trim();
                            if (!text_marketPrice.getText().toString().trim().isEmpty())
                                employee.text_marketPrice = text_marketPrice.getText().toString().trim();
                                realm.copyToRealm(employee);
                        }

                    } catch (RealmPrimaryKeyConstraintException e) {
                        Toast.makeText(getApplicationContext(), "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }


    private void readEmployeeRecords() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Employee> results = realm.where(Employee.class).findAll();
                textView.setText("");
                for (Employee employee : results) {
                    textView.append("Product Name: "+employee.text_productName+"\n"+ "Product Description: " + employee.text_desc+"\n" + "Product Category: " + employee.text_category+"\n"+ "Product Price: "+employee.text_price+"\n"+ "Product Market Price:"+employee.text_marketPrice+"\n\n");
                }
            }
        });


    }

    private void updateEmployeeRecords() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (!text_productName.getText().toString().trim().isEmpty()) {
                    Employee employee = realm.where(Employee.class).equalTo(Employee.PROPERTY_NAME, text_productName.getText().toString()).findFirst();
                    if (employee == null) {
                        employee = realm.createObject(Employee.class, text_productName.getText().toString().trim());
                    }
                    if (!text_desc.getText().toString().trim().isEmpty())
                        employee.text_desc = text_desc.getText().toString().trim();

                    if (!text_category.getText().toString().trim().isEmpty())
                        employee.text_category = text_category.getText().toString().trim();

                    if (!text_price.getText().toString().trim().isEmpty())
                        employee.text_price = text_price.getText().toString().trim();

                    if (!text_marketPrice.getText().toString().trim().isEmpty())
                        employee.text_marketPrice = text_marketPrice.getText().toString().trim();


                }
            }
        });
    }

    private void deleteEmployeeRecord() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Employee employee = realm.where(Employee.class).equalTo(Employee.PROPERTY_NAME, text_productName.getText().toString()).findFirst();
                if (employee != null) {
                    employee.deleteFromRealm();
                }
            }
        });
    }
    private void onselectedProductName() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Employee> results = realm.where(Employee.class).findAll();
                textView.setText("");
                for (final Employee employee : results) {
                    textView.append("Product Name: "+employee.text_productName+"\n");
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRealm != null) {
            mRealm.close();
        }
    }
}
