//package projekt.sqlmulti;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.widget.ListView;
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.AdapterView.OnItemClickListener;
//
//import java.util.ArrayList;
//
//import projekt.sqlmulti.model.TimesArray_model;
//import projekt.sqlmulti.model.Video_model;
//
//public class ImportExportActivity extends AppCompatActivity {
//
//     MyCustomAdapter dataAdapter = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_import_export);
//
//    //Generate list View from ArrayList
//        displayListView();
//
//        checkButtonClick();
//    }
//
//    private void displayListView() {
//        //Array list of countries
//
//        ArrayList<Video_model> countryList = new ArrayList<Video_model>();
//
//        //create an ArrayAdaptar from the String Array
//        dataAdapter = new MyCustomAdapter(this,
//                R.layout.text_and_check_listview, videoList);
//        ListView listView = (ListView) findViewById(R.id.);
//        // Assign adapter to ListView
//        listView.setAdapter(dataAdapter);
//
//
//        listView.setOnItemClickListener(new OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // When clicked, show a toast with the TextView text
//                Video_model videoElement = (Video_model) parent.getItemAtPosition(position);
//                Toast.makeText(getApplicationContext(),
//                        "Clicked on Row: " + Video_model.getName(),
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//
//
//    private class MyCustomAdapter<Video_model> extends ArrayAdapter<projekt.sqlmulti.model.Video_model> {
//
//        private ArrayList<Video_model> inputList;
//
//        public MyCustomAdapter(Context context, int textViewResourceId,
//                               ArrayList<Video_model> inputList) {
//            super(context, textViewResourceId, inputList);
//            this.inputList = new ArrayList<Video_model>();
//            this.inputList.addAll(inputList);
//        }
//
//        private class ViewHolder {
//            TextView code;
//            CheckBox name;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            ViewHolder holder = null;
//            Log.v("ConvertView", String.valueOf(position));
//
//            if (convertView == null) {
//                LayoutInflater vi = (LayoutInflater)getSystemService(
//                        Context.LAYOUT_INFLATER_SERVICE);
//                convertView = vi.inflate(R.layout.text_and_check_listview, null);
//
//                holder = new ViewHolder();
//                holder.code = (TextView) convertView.findViewById(R.id.descriptionTextToList);
//                holder.name = (CheckBox) convertView.findViewById(R.id.element);
//                convertView.setTag(holder);
//
//                holder.name.setOnClickListener( new View.OnClickListener() {
//                    public void onClick(View v) {
//                        CheckBox cb = (CheckBox) v ;
//                        <Video_model> element = cb.getTag();
//                        Toast.makeText(getApplicationContext(),
//                                "Clicked on Checkbox: " + cb.getText() +
//                                        " is " + cb.isChecked(),
//                                Toast.LENGTH_LONG).show();
//                        country.setSelected(cb.isChecked());
//                    }
//                });
//            }
//            else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//
//            projekt.sqlmulti.model.Video_model country = countryList.get(position);
//            holder.code.setText(" (" +  country.getCode() + ")");
//            holder.name.setText(country.getName());
//            holder.name.setChecked(country.isSelected());
//            holder.name.setTag(country);
//
//            return convertView;
//
//        }
//
//    }
//
//    private void checkButtonClick() {
//
//
//        Button myButton = (Button) findViewById(R.id.findSelected);
//        myButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                StringBuffer responseText = new StringBuffer();
//                responseText.append("The following were selected...\n");
//
//                ArrayList<Country> countryList = dataAdapter.countryList;
//                for(int i=0;i<countryList.size();i++){
//                    Country country = countryList.get(i);
//                    if(country.isSelected()){
//                        responseText.append("\n" + country.getName());
//                    }
//                }
//
//                Toast.makeText(getApplicationContext(),
//                        responseText, Toast.LENGTH_LONG).show();
//
//            }
//        });
//
//    }
//
//}
