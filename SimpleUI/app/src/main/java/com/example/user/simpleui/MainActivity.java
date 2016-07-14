package com.example.user.simpleui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final  int REQUEST_CODE_DRINK_MENU_ACTIVITY =0;

    // 創造物件
    TextView textView;
    EditText editText;
    RadioGroup radioGroup;
    ListView listView;
    Spinner spinner;
    String selectedTea = "Black Tea";
    List<Order> orders = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         //讓程式取找到物件
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        listView = (ListView) findViewById(R.id.listView);
        spinner = (Spinner) findViewById(R.id.spinner);


        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    submit(v);
                    return true;
                }
                return false;
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                selectedTea = radioButton.getText().toString();
            }

        });
        setUpListView();
        setupSpinner();
        Log.d("Debug","Main Activity OnCreate");
    }




    //定義物件

    public void setupSpinner()
    {
        String[] data = getResources().getStringArray(R.array.storeInfos);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,data); //this represent main activity
        spinner.setAdapter(adapter);
    }


    public void setUpListView()
    {
        OrderAdapter adapter =new OrderAdapter(this,orders);
        listView.setAdapter(adapter);
    }


    public void submit(View view) {
        String text = editText.getText().toString();

        Order order = new Order();
        order.note = text;
        order.drinkName = selectedTea;
        order.storeInfo = (String)spinner.getSelectedItem();

        orders.add(order);
        setUpListView();

        textView.setText(text);
        editText.setText("");
    }

    public  void  goToMenu(View view)
    {
        Intent intent = new Intent();
        intent.setClass(this,DrinkMenuActivity.class);
        startActivityForResult(intent, REQUEST_CODE_DRINK_MENU_ACTIVITY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==REQUEST_CODE_DRINK_MENU_ACTIVITY)
        {
            if (resultCode ==RESULT_OK)
            {
                Toast.makeText(this,"完成菜單",Toast.LENGTH_SHORT).show();
                textView.setText(data.getStringExtra("results"));
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Debug", "Main Activity OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Debug","Main Activity OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Debug","Main Activity OnPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Debug","Main Activity OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Debug","Main Activity OnDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Debug","Main Activity OnRestart");
    }
}

