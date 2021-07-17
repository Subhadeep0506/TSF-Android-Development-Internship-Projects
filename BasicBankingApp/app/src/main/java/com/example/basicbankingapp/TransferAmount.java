package com.example.basicbankingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

public class TransferAmount extends AppCompatActivity {

    CustomerDatabase myDB;
    TextView tvCustomerName, tvCustomerBalance;
    Button btnTransfer, btnCancel;
    EditText etTransferAmount;
    Spinner spinner;

    Cursor itemData, cursor, data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_screen);

        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerBalance = findViewById(R.id.tvCustomerBalance);
        btnTransfer = findViewById(R.id.btnTransfer);
        btnCancel = findViewById(R.id.btnCancel);
        etTransferAmount = findViewById(R.id.etTransferAmount);
        spinner = findViewById(R.id.spinner);

        myDB = new CustomerDatabase(this);

        int id = getIntent().getIntExtra("ID", 0);
        cursor = myDB.getDataFromListExcept(id);
        data = myDB.getDataFromList(id);

        data.moveToFirst();
        String name = data.getString(data.getColumnIndex("NAME"));
        long balance = Long.parseLong( data.getString(data.getColumnIndex("BALANCE")));

        String balanceText = "" + balance;
        tvCustomerName.setText(name);
        tvCustomerBalance.setText(balanceText);

        List<String> customerList = new ArrayList<>();
        //customerList.add(0, "\"Select\"");
        if(cursor.getCount() == 0){
            Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(cursor.moveToNext()){
                customerList.add(cursor.getString(1));
            }
        }

        cursor.moveToFirst();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                                R.layout.spinner_item, customerList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int itemId = 0;
                while(itemId != position){
                    cursor.moveToNext();
                    itemId++;
                }

                //Toast.makeText(TransferAmount.this, cursor.getString(0), Toast.LENGTH_SHORT).show();
                itemData = myDB.getDataFromList(Integer.parseInt(cursor.getString(0)));
                cursor.moveToFirst();
                itemId = 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnTransfer.setOnClickListener(v -> {

            if ((etTransferAmount.getText().toString()).equals("")){
                Toast.makeText(TransferAmount.this, "enter some amount", Toast.LENGTH_SHORT).show();
            }
            else {
                long transferAmount = Long.parseLong(etTransferAmount.getText().toString().trim());

                if (myDB.updateBalance(data, itemData, transferAmount)) {

                    Intent intent = new Intent(TransferAmount.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    TransferAmount.this.finish();
                }
                else{
                    Toast.makeText(this, "Not Enough balance", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(v -> {

            Intent intent = new Intent(TransferAmount.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            TransferAmount.this.finish();
        });
    }
}