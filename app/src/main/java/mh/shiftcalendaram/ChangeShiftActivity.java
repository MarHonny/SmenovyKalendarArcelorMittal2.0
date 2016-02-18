package mh.shiftcalendaram;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import mh.shiftcalendaram.adapters.ShiftListViewAdapter;
import mh.shiftcalendaram.templates.ListTemplates;
import mh.shiftcalendaram.templates.ShiftSymbolTemplates;

public class ChangeShiftActivity extends AppCompatActivity {

    Database database;
    ListView listView;
    String color;
    LinearLayout shiftLayout, icon;
    ImageView deleteShift;

    ShiftListViewAdapter adapter;
    ArrayList<ShiftSymbolTemplates> list;
    ShiftSymbolTemplates symbol;
    int positionOfSymbol;
    EditText note;

    TextView changeShiftTitle, iconText, date;
    Button deleteNote;
    SharedPreferences pref;
    String day,month,year;

    int positionOfCustom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_shift);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database= new Database(ChangeShiftActivity.this);
        getSymbolsFromDatabase();

        color = "#ff9800";
        positionOfSymbol = -1;

        shiftLayout = (LinearLayout)findViewById(R.id.linearLayout_change_shift);
        changeShiftTitle = (TextView)findViewById(R.id.textView_change_shift_title);
        iconText = (TextView)findViewById(R.id.textView_change_shift_icon_text);
        icon = (LinearLayout) findViewById(R.id.linearLayout_change_shift_circle);
        date = (TextView)findViewById(R.id.textView_change_shift_date);
        deleteShift = (ImageView)findViewById(R.id.imageView_change_shift_delete_shift);

        note = (EditText)findViewById(R.id.editext_change_snift_note);
        deleteNote = (Button)findViewById(R.id.button_change_shift_delete_text);


        pref = getSharedPreferences("shift", Context.MODE_PRIVATE);
        positionOfCustom = pref.getInt("positionOfCustom", -1);

        Intent intent = getIntent();
        day = intent.getStringExtra("day");
        month = intent.getStringExtra("month");
        year = intent.getStringExtra("year");
        date.setText(day + ". " + month + ". " + year);


        deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.setText("");
                Log.v("ddw", "bla");
                //   if
            }
        });

        shiftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeShiftActivity.this);

                View view =  getLayoutInflater().inflate(R.layout.fragment_shift, null);

              //  LinearLayout lin = (LinearLayout)view.findViewById(R.id.linearLayout_nocalendar);
                listView = (ListView) view.findViewById(R.id.listView_shift);
                listView.setAdapter(adapter);

                builder.setTitle("Vyberte směnu");
                builder.setView(view);
                final AlertDialog alert = builder.create();
                alert.show();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0,
                                            View arg1, int arg2, long arg3) {

                        positionOfSymbol = arg2;

                        changeShiftTitle.setText(list.get(positionOfSymbol).getTitle());
                        iconText.setText(list.get(positionOfSymbol).getShortTitle());
                        GradientDrawable background = (GradientDrawable) icon.getBackground();
                        background.setColor(list.get(positionOfSymbol).getColor());
                        alert.dismiss();

                    }

                });


            }
        });



    }

    public void getSymbolsFromDatabase()
    {
        list = database.getSymbols();
        ArrayList<ListTemplates> adapterList = (ArrayList<ListTemplates>) list.clone();
        adapter = new ShiftListViewAdapter(ChangeShiftActivity.this, adapterList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_creating_shift, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.ic_accept) {
            if (positionOfSymbol != -1)
            {
               database.insertAlternative("P", Integer.parseInt(day),month,year,0, "#FF0000");
            }


            if(note.getText().length() > 0)
            {
                database.insertNote(15, month,year, note.getText().toString(),positionOfCustom);
            }
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}