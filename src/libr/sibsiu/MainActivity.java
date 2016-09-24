package libr.sibsiu;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	String[][] masBooks;
	String[] masIntent = new String [2];
	EditText txtSearch;
	Button btnLoadMore;
	Button btnSearch;
	ListView lv; 
	int viewCount;
	String keySearch;
	ArrayList<Map<String, Object>> data;
	String domen_name;
	String sort;
	Boolean key_author = true;
	Set<String> charectiristic = new HashSet<String>();
	String charBooks;
	SearchView sv;
	private static long back_pressed;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lv = getListView();
        lv.setCacheColorHint(0);
        //lv.setBackgroundResource(R.drawable.ic_launcher);
        
        txtSearch = new EditText(this);
        txtSearch.setHint(getString(R.string.txtHint));
        txtSearch.setMaxLines(1);
        //lv.addHeaderView(txtSearch);
            
        btnLoadMore = new Button(this);
        btnLoadMore.setText(getString(R.string.btLoad_more));
        
        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       	
            	new searchTask().execute(keySearch);          	
            }
        });         
             
        btnSearch = new Button(this);
        btnSearch.setText(getString(R.string.btSearch));
        //lv.addHeaderView(btnSearch);
          
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       	
            	try {
					keySearch=URLEncoder.encode(txtSearch.getText().toString(), "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} 
            	data = new ArrayList<Map<String, Object>>();
            	viewCount = 0;
            	new searchTask().execute(keySearch); 
            	finish();
            	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            	imm.hideSoftInputFromWindow(btnSearch.getWindowToken(), 0);
            }
        });         
        
        String[] Help = {getString(R.string.help_start)};  
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,	android.R.layout.simple_list_item_1, Help);      
        lv.setAdapter(adapter);   		
        lv.setOnItemLongClickListener(onListItemLongClick);   
        
    }
	
	@Override
	public void onBackPressed() {
		
		if (back_pressed + 2000 > System.currentTimeMillis())
			super.onBackPressed();	
		 else
		    Toast.makeText(getBaseContext(), getString(R.string.help_exit), Toast.LENGTH_SHORT).show();
		 back_pressed = System.currentTimeMillis();
	}
	
	@Override
	protected void onResume() {
		SharedPreferences SettingsActivity = PreferenceManager.getDefaultSharedPreferences(this);
		key_author = SettingsActivity.getBoolean(getString(R.string.pref_key_author), key_author);
		sort = SettingsActivity.getString(getString(R.string.pref_key_sort), "").trim();
		domen_name = SettingsActivity.getString(getString(R.string.pref_key_domen), getString(R.string.pref_domen_default)).trim();	
		charectiristic = SettingsActivity.getStringSet(getString(R.string.pref_key_charecteristic),	charectiristic);
		charBooks = "";
		if(charectiristic.contains("1"))
			charBooks += "1";
		if(charectiristic.contains("2"))
			charBooks += "2";
		if(charectiristic.contains("3"))
			charBooks += "3";
		if(charectiristic.contains("4"))
			charBooks += "4";
		if(charectiristic.contains("5"))
			charBooks += "5";
		if(charectiristic.contains("6"))
			charBooks += "6";
		
		final String Attribute_Name = "name";
        final String Attribute_Author = "author";   
		if (viewCount > 0) 
			if (key_author){
            	String[] from = {Attribute_Name, Attribute_Author};
            	int[] to = {android.R.id.text1, android.R.id.text2};          
            	SimpleAdapter sAdapter = new SimpleAdapter (MainActivity.this, data, R.layout.new_two_line_list_item, from, to); 
            	lv.setAdapter(sAdapter); 
            }
            else{
            	String[] from1 = {Attribute_Name};
            	int[] to1 = {android.R.id.text1}; 
            	SimpleAdapter sAdapter1 = new SimpleAdapter (MainActivity.this, data, R.layout.new_simple_list_item_1, from1, to1);                 
            	lv.setAdapter(sAdapter1); 
            }
		
		super.onResume();
	}
  
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.action_settings:
			if (Build.VERSION.SDK_INT >= 14){
				Intent intent = new Intent(); 
				intent.setClass(this, SettingsActivity.class); 
				startActivity(intent); 
			}
			else{
				Toast.makeText(getBaseContext(), "Настройки доступны с версии Android 4.0"+"\n"+"Включены стандартные настройки.", Toast.LENGTH_SHORT).show();
				key_author = true;
				sort = "dateBookAdded;descending";
				domen_name = getString(R.string.pref_domen_default);
				charBooks = "123456";
			}
				
			return true;	
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    MenuItem searchItem = menu.findItem(R.id.search);
	    sv = (SearchView) MenuItemCompat.getActionView(searchItem);
	    sv.setQueryHint(getString(R.string.searchHint));    
		sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
		
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				try {
					keySearch=URLEncoder.encode(query, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} 
            	data = new ArrayList<Map<String, Object>>();
            	viewCount = 0;
            	new searchTask().execute(keySearch);  
            	sv.setIconified(true);           	
            	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            	imm.hideSoftInputFromWindow(sv.getWindowToken(), 0);
				return false;				
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		return true;		
    }
   
    protected void onListItemClick(android.widget.ListView l, View v, int position, long id) {
    	if (viewCount > 0)
    		new viewTask().execute(masBooks[position][2],charBooks);	
    	
    };
    
    OnItemLongClickListener onListItemLongClick = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        	lv.setSelection(0);  
        	//new viewTask().execute("http://libr.sibsiu.ru/lib/item?id=chamo:55952&theme=mobile",charBooks);	
        	return true;
        
        }
    };
	
    //------------------Class task search books-------------------
    class searchTask extends AsyncTask<String, Void, String[][]> {
    	Document doc = null; 
    	String[][] result = new String[20][4];
    	ProgressDialog pDialog;
    	int vCount;
    	Boolean key;
    	
    	@Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(getString(R.string.pdPlease_wait));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
            vCount = viewCount;
        }
    	
        @Override
        protected String[][] doInBackground(String... params) {
                publishProgress(new Void[] {});
                try {
                	//Url + parametrs connect
                	doc = Jsoup.connect(domen_name+"&sort="+sort+"&term_1="+params[0]+"&pageNumber="+Integer.toString((vCount / 20)+1)).get();    
                	key = true;
                } catch (IOException e) {                	
                    e.printStackTrace();  
                    key = false;
                }
                if (key){
                	Element link_noResults = null;
                	link_noResults = doc.select("div[id=noResults]").first(); 
                	if (link_noResults != null)
                		key = false;
                }
                //Count books
                if (key){
                Element link_count = doc.select("div[class=resultCount] span").first(); 
                int count = Integer.parseInt(link_count.text().substring(link_count.text().lastIndexOf(" ")+1, link_count.text().length()))-vCount;
                result[0][3] = Integer.toString(count);
                if (count>20)
                	count = 20;
                
                for (int i = 0; i<=count-1; i++){
                	//Tag title book
                	Element link_title = doc.select("a[href][class=title]").get(i); 
                	result[i][0] = Integer.toString(vCount+i+1)+". "+link_title.text();
                	//Tag url book
                	result[i][2] = link_title.attr("abs:href"); 
                	
                	Element link_author = null;
                	//Tag author book
                	link_author = doc.select("a[href*="+ result[i][2].substring(result[i][2].indexOf("?"), result[i][2].length())+"][class=title] + a[href][class=author]").first(); 
                	if (link_author!=null)
                		result[i][1] = link_author.text(); 
                }    
                }
                return result;
        }

		@Override
        protected void onPostExecute(String[][] result) {
                super.onPostExecute(result);
                final String Attribute_Name = "name";
                final String Attribute_Author = "author";    
                Map<String, Object> m;
                
                if (key){
                int count = Integer.parseInt(result[0][3]);
                if (viewCount < 1)
                	masBooks = new String [count][3];                  
                if (count>20){
                	lv.removeFooterView(btnLoadMore);
                	lv.addFooterView(btnLoadMore);
                	count = 20;} 
                else{
                	lv.removeFooterView(btnLoadMore);}                                 

                for (int i = 0; i<=count-1; i++) {
                    m = new HashMap<String, Object>();
                    if (result[i][0].length()>5800)
                    	m.put(Attribute_Name, result[i][0].substring(0,(result[i][0].substring(0, 57)).lastIndexOf(" "))+"..");
                    else
                    	m.put(Attribute_Name, result[i][0]);  
                    if (result[i][1] != null)
                    	m.put(Attribute_Author, getString(R.string.author)+result[i][1]);
                    else
                    	m.put(Attribute_Author, getString(R.string.author)+getString(R.string.plaseholder));
                    data.add(m);
                    masBooks[i+viewCount][0] = result[i][0];
                    masBooks[i+viewCount][1] = result[i][1];
                    masBooks[i+viewCount][2] = result[i][2];
                }         
                
                if (key_author){
                	String[] from = {Attribute_Name, Attribute_Author};
                	int[] to = {android.R.id.text1, android.R.id.text2};          
                	SimpleAdapter sAdapter = new SimpleAdapter (MainActivity.this, data, R.layout.new_two_line_list_item, from, to); 
                	lv.setAdapter(sAdapter); 
                }
                else{
                	String[] from1 = {Attribute_Name};
                	int[] to1 = {android.R.id.text1}; 
                	SimpleAdapter sAdapter1 = new SimpleAdapter (MainActivity.this, data, R.layout.new_simple_list_item_1, from1, to1);                 
                	lv.setAdapter(sAdapter1); 
                }
                
                lv.setSelection(vCount); 
                viewCount = vCount + 20; 
                }
                else{
                	Toast.makeText(getBaseContext(), getString(R.string.error_internet), Toast.LENGTH_SHORT).show();	
                }
                pDialog.dismiss();  
        }  		
    }
    
    
  //------------------Class task read info on book------------
    class viewTask extends AsyncTask<String, Void, String[][]> {
    	Document doc = null; 
    	String[][] result = new String[8][2];
    	ProgressDialog pDialog;
    	String[] dump;
    	String sTemp = "";
    	String sResult = "";
    	String sCol = "1345";
    	Boolean sKey = false;
    	Boolean key;
    	int i;
    	int count;
    	
    	@Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(getString(R.string.pdPlease_wait));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    	
        @Override
        protected String[][] doInBackground(String... params) {
                publishProgress(new Void[] {});
                try {
                	doc = Jsoup.connect(params[0]).get();
                	key = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    key = false;
                }
                
                if (key){
                Element link = null;
                link = doc.select("h1[class=title]").first();
                if(link!=null)             
                	result[0][0] = link.text();          
                
                if (params[1].contains("1")){
                link = null;
            	link = doc.select("div[id=itemView] span").get(0);
            	if(link!=null)
            		result[1][0] = link.text();
            	result[1][1] = " ";
                }
                
                if (params[1].contains("2")){
                	link = null;
                	link = doc.select("div[id=itemView] span").get(1);
                	if(link!=null)
                		result[2][0] = link.text();
                	result[2][1] = " ";
                }
                
                if (params[1].contains("3")){
                	link = null;
                	link = doc.select("div[id=itemView] span").get(2);
                	if(link!=null)
                		result[3][0] = link.text();
                	result[3][1] = " ";               	
                }
                
                
                if (params[1].contains("4")){
                	sTemp = "";
                	sResult = "";
                	sKey = false;
                	i = 0;
                	count  = doc.select("div[id=tabContents-3] a[href*=field_1=a]").size();	    
                	if (count != 0)
                	do {               		
                		link = null; 
                		link = doc.select("div[id=tabContents-3] a[href*=field_1=a]").get(i);
            			if (link!=null)
            				sTemp = link.text();               			
            			if (sKey)
        					sResult = sResult + "<br><font color='#e5e5e5'>--------------------------</font><br>";
        				else
        					sKey = true;         				
        				if(i % 2 > 0)
        					sResult = sResult + "<font color='#444444'>"+sTemp+"</font>";
        				else
        					sResult = sResult + "<font color='#000000'>"+sTemp+"</font>";      	                  		
                    	i++;
                	} while(i<count);
                	if (count > 1)
                		result[4][0] = "<font color='#e5e5e5'>------------</font><br>"+sResult.trim()+"<br><font color='#e5e5e5'>------------</font>";
                	else
                		result[4][0] = sResult.trim();
                	result[4][1] = " ";
                	if (sTemp.trim() == "")
                		result[4][0] = getString(R.string.plaseholder);
                }
                
                if (params[1].contains("5")){
                	sTemp = "";
                	sResult = "";
                	sKey = false;
                	i = 0;
                	count  = doc.select("div[id=tabContents-3] a[href*=field_1=s]").size();	    
                	if (count != 0)
                	do {               		
                		link = null; 
                		link = doc.select("div[id=tabContents-3] a[href*=field_1=s]").get(i);
            			if (link!=null)
            				sTemp = link.text();               			
            			if (sKey)
        					sResult = sResult + "<br><font color='#e5e5e5'>--------------------------</font><br>";
        				else
        					sKey = true;         				
        				if(i % 2 > 0)
        					sResult = sResult + "<font color='#444444'>"+sTemp+"</font>";
        				else
        					sResult = sResult + "<font color='#000000'>"+sTemp+"</font>";       				
                    	i++;
                	} while(i<count);    
                	if (count > 1)
                		result[5][0] = "<font color='#e5e5e5'>------------</font><br>"+sResult.trim()+"<br><font color='#e5e5e5'>------------</font>"; 
                	else
                		result[5][0] = sResult.trim();
                	result[5][1] = " ";
                	if (sTemp.trim() == "")
                		result[5][0] = getString(R.string.plaseholder);
                }
                
                if (params[1].contains("6")){
                	link = null;
                	link = doc.select("div[id=tabContents-3] span").first();
                	if(link!=null)
                		result[6][0] = link.text();
                	result[6][1] = " ";
                	if (result[6][0] !=null)
                		result[6][0] = getString(R.string.plaseholder);
                }  
                
                int count_field = doc.select("div[id=tabContents-1] tr[class] th").size();		
            	count  = doc.select("div[id=tabContents-1] tbody tr[class]").size();	
                dump = new String[count];
                int i = 0;
            	for (int m = 0; m<count; m++){
            		sTemp = "";
            		sResult = "";
            		sKey = false;
            		for(int n = 0; n<count_field; n++){
            			link = null;          			
            			link = doc.select("div[id=tabContents-1] tbody tr[class] td").get(n);
            			if (link!=null)
            				sTemp = link.text();       			
            			if (sCol.contains(Integer.toString(n))){ 
            				if (sKey)
            					sResult = sResult + "<br><font color='#e5e5e5'>--------------------------</font><br>";
            				else
            					sKey = true;         				
            				if(i % 2 > 0)
            					sResult = sResult + "<font color='#333333'>"+sTemp+"</font>";
            				else
            					sResult = sResult + "<font color='#000000'>"+sTemp+"</font>";	
            				i++;
            			}
            			
            		}           		
            		dump[m] = "<font color='#e5e5e5'>------------</font><br>"+sResult.trim()+"<br><font color='#e5e5e5'>------------</font>"; 	    		
            	}   
                }            
                return result;
        }

        @Override
        protected void onPostExecute(String[][] result) {
                super.onPostExecute(result);  
                if (key){
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);         
                intent.putExtra("title", result[0][0]);                
                intent.putExtra("cipher", result[1][0]); 
                intent.putExtra("visible1", result[1][1]); 
            	intent.putExtra("publish", result[2][0]);
            	intent.putExtra("visible2", result[2][1]); 
            	intent.putExtra("page", result[3][0]); 
            	intent.putExtra("visible3", result[3][1]); 
            	intent.putExtra("authors", result[4][0]); 
            	intent.putExtra("visible4", result[4][1]); 
            	intent.putExtra("items", result[5][0]); 
            	intent.putExtra("visible5", result[5][1]); 
            	intent.putExtra("description", result[6][0]); 
            	intent.putExtra("visible6", result[6][1]);    
            	intent.putExtra("copies", dump);
                startActivity(intent);
                }
                else {
                	Toast.makeText(getBaseContext(), getString(R.string.error_internet), Toast.LENGTH_SHORT).show();	
                }
                pDialog.dismiss();    
        }  		
    }
                   
}
