package libr.sibsiu;

import android.app.Activity;
import android.os.Bundle;


public class SettingsActivity extends Activity {
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		getFragmentManager().beginTransaction()
        .replace(android.R.id.content, new SettingsFragment())
        .commit();
	}

}
