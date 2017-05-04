package projekt.sqlmulti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class View_Prof extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prof);

        String[] elementy_listy = { "Własna lista", "Drugi element", "Trzeci" };
        ListView prosta_lista = (ListView) findViewById(R.id.lv_prostalista);

        ArrayAdapter adapter_listy = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, elementy_listy);
        prosta_lista.setAdapter(adapter_listy);

        prosta_lista.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView arg0, View arg1, int pos,
                                    long arg3) {

                switch (pos) {
                    case 0:
                        Intent startActivityCustomList = new Intent(View_Prof.this,
                                MainActivity.class);
                        startActivity(startActivityCustomList);
                        break;
                }

            }

        });

    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_prof, menu);
        return true;
    }*/
}