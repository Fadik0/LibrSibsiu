package libr.sibsiu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.os.Bundle;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class SecondActivity extends ExpandableListActivity {

	ExpandableListView lv;
	String sTemp;

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		lv = getExpandableListView();	
		final String attribute = "attribute";
 
		List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
		
		List<List<Map<String, String>>> listOfChildGroups = new ArrayList<List<Map<String, String>>>();
		
		//-------------------add group title-------------------
		Map<String, String> groupMap0 = new HashMap<String, String>();
		groupData.add(groupMap0);
		groupMap0.put(attribute, getString(R.string.char_title) );
		
		List<Map<String, String>> childGroup0 = new ArrayList<Map<String, String>>();
		Map<String, String> childMap0 = new HashMap<String, String>();
		childGroup0.add(childMap0);
		childMap0.put(attribute, getIntent().getExtras().getString("title"));
		listOfChildGroups.add(childGroup0);
		
		//-------------------add group cipher-------------------
		if (getIntent().getExtras().getString("visible1")!=null)
		{
		Map<String, String> groupMap1 = new HashMap<String, String>();
		groupData.add(groupMap1);
		groupMap1.put(attribute, getString(R.string.char_cipher));
		
		List<Map<String, String>> childGroup1 = new ArrayList<Map<String, String>>();
		Map<String, String> childMap1 = new HashMap<String, String>();
		childGroup1.add(childMap1);
		childMap1.put(attribute, getIntent().getExtras().getString("cipher"));
		listOfChildGroups.add(childGroup1);
		}
		
		//-------------------add group publisher-------------------
		if (getIntent().getExtras().getString("visible2")!=null)
		{
		Map<String, String> groupMap2 = new HashMap<String, String>();
		groupData.add(groupMap2);
		groupMap2.put(attribute, getString(R.string.char_publisher));
		
		List<Map<String, String>> childGroup2 = new ArrayList<Map<String, String>>();
		Map<String, String> childMap2 = new HashMap<String, String>();
		childGroup2.add(childMap2);
		childMap2.put(attribute, getIntent().getExtras().getString("publish"));
		listOfChildGroups.add(childGroup2);
		}
		
		//-------------------add group count pages-------------------
		if (getIntent().getExtras().getString("visible3")!=null)
		{
		Map<String, String> groupMap3 = new HashMap<String, String>();
		groupData.add(groupMap3);
		groupMap3.put(attribute, getString(R.string.char_count_pages));
		
		List<Map<String, String>> childGroup3 = new ArrayList<Map<String, String>>();
		Map<String, String> childMap3 = new HashMap<String, String>();
		childGroup3.add(childMap3);
		childMap3.put(attribute, getIntent().getExtras().getString("page"));
		listOfChildGroups.add(childGroup3);
		}
		
		//-------------------add group authors-------------------
		if (getIntent().getExtras().getString("visible4")!=null)
		{
		Map<String, String> groupMap4 = new HashMap<String, String>();
		groupData.add(groupMap4);
		groupMap4.put(attribute, getString(R.string.char_authors));
		
		List<Map<String, String>> childGroup4 = new ArrayList<Map<String, String>>();
		Map<String, String> childMap4 = new HashMap<String, String>();
		childGroup4.add(childMap4);
		childMap4.put(attribute, getIntent().getExtras().getString("authors"));
		listOfChildGroups.add(childGroup4);
		}
		
		//-------------------add group items-------------------
		if (getIntent().getExtras().getString("visible5")!=null)
		{
		Map<String, String> groupMap5 = new HashMap<String, String>();
		groupData.add(groupMap5);
		groupMap5.put(attribute, getString(R.string.char_items));
		
		List<Map<String, String>> childGroup5 = new ArrayList<Map<String, String>>();
		Map<String, String> childMap5 = new HashMap<String, String>();
		childGroup5.add(childMap5);
		childMap5.put(attribute,	getIntent().getExtras().getString("items"));
		listOfChildGroups.add(childGroup5);
		}
		
		//-------------------add group description-------------------
		if (getIntent().getExtras().getString("visible6")!=null)
		{
		Map<String, String> groupMap6 = new HashMap<String, String>();		
		groupData.add(groupMap6);
		groupMap6.put(attribute, getString(R.string.char_description));
		
		List<Map<String, String>> childGroup6 = new ArrayList<Map<String, String>>();
		Map<String, String> childMap6 = new HashMap<String, String>();
		childGroup6.add(childMap6);
		childMap6.put(attribute, getIntent().getExtras().getString("description"));				
		listOfChildGroups.add(childGroup6);
		}		
		
		//-------------------add group copies-------------------
		Map<String, String> groupMap7 = new HashMap<String, String>();		
		groupMap7.put(attribute, getString(R.string.char_copies));
		groupData.add(groupMap7);
				
		List<Map<String, String>> childGroup7 = new ArrayList<Map<String, String>>();	
		Map<String, String> childMap7;
		int count = getIntent().getExtras().getStringArray("copies").length;
		for (int i=0;i<count; i++){
			childMap7 = new HashMap<String, String>();
			childMap7.put(attribute, getIntent().getExtras().getStringArray("copies")[i]);		
			childGroup7.add(childMap7);
		}				
		listOfChildGroups.add(childGroup7);
		 
		//-------------------initialization parameters-------------------
		String[] from = {attribute};
        int[] to = {android.R.id.text1};
        
        //-------------------create and use adapter-------------------
        NewSimpleExpandableListAdapter exListAdapter = new NewSimpleExpandableListAdapter(this, 
        		groupData,R.layout.new_simple_expandable_list_item_1,from, to,
        		listOfChildGroups,android.R.layout.simple_list_item_1, from, to
        		);
       
        lv.setAdapter(exListAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.second, menu);
		return true;
	}

}


class NewSimpleExpandableListAdapter extends SimpleExpandableListAdapter {
	List<? extends Map<String, ?>> mGroupData;
    int mExpandedGroupLayout;
    int mCollapsedGroupLayout;
    String[] mGroupFrom;
    int[] mGroupTo;
    
    List<? extends List<? extends Map<String, ?>>> mChildData;
    int mChildLayout;
    int mLastChildLayout;
    String[] mChildFrom;
    int[] mChildTo;
    
    LayoutInflater mInflater;
	
	public NewSimpleExpandableListAdapter(Context context,
			List<? extends Map<String, ?>> groupData, int groupLayout,
			String[] groupFrom, int[] groupTo,
			List<? extends List<? extends Map<String, ?>>> childData,
			int childLayout, String[] childFrom, int[] childTo) {		
		super(context, groupData, groupLayout, groupFrom, groupTo, childData,
				childLayout, childFrom, childTo);
		mGroupData = groupData;        
        mGroupFrom = groupFrom;
        mGroupTo = groupTo;       
        mChildData = childData;
        mChildLayout = childLayout;     
        mChildFrom = childFrom;
        mChildTo = childTo;
	}	
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = newChildView(isLastChild, parent);
        } else {
            v = convertView;
        }
        bindView(v, mChildData.get(groupPosition).get(childPosition), mChildFrom, mChildTo);
        return v;
    }
	
	private void bindView(View view, Map<String, ?> data, String[] from, int[] to) {
        int len = to.length;

        for (int i = 0; i < len; i++) {
            TextView v = (TextView)view.findViewById(to[i]);
            if (v != null) {
                v.setText(Html.fromHtml((String)data.get(from[i])));
            }
        }
    }
}

