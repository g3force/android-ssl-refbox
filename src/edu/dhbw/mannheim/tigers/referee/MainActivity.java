package edu.dhbw.mannheim.tigers.referee;

import org.dhbw.mannheim.tigers.referee.R;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import edu.dhbw.mannheim.tigers.referee.data.Constants;
import edu.dhbw.mannheim.tigers.referee.data.ERefereeCommand;
import edu.dhbw.mannheim.tigers.referee.data.RefereeMsgTransmitter;

public class MainActivity extends Activity {

	private static final RefereeMsgTransmitter refereeMsgTransmitter = new RefereeMsgTransmitter();

//	private static final SparseArray<ERefereeCommand> buttonMap = new SparseArray<ERefereeCommand>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		populateButtonMap();
		refereeMsgTransmitter.start();
//		assignButtonListeners();

		ListView listView = (ListView) findViewById(R.id.listView1);
		Resources res = getResources();
		final TypedArray cmds = res.obtainTypedArray(R.array.refereeCommands);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				Toast.makeText(getApplicationContext(),
//						"Click ListItem " + cmds.getString(position),
//						Toast.LENGTH_LONG).show();
				refereeMsgTransmitter.sendSimpleRefereeMsg(ERefereeCommand.valueOf(cmds.getString(position)));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		MenuItem item = menu.add("Switch to " + Constants.SECONDARY_PORT);
		item.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if(refereeMsgTransmitter.getTargetPort() == Constants.DEFAULT_PORT) {
					refereeMsgTransmitter.setTargetPort(Constants.SECONDARY_PORT);
					item.setTitle("Switch to " + Constants.DEFAULT_PORT);
				} else {
					refereeMsgTransmitter.setTargetPort(Constants.DEFAULT_PORT);
					item.setTitle("Switch to " + Constants.SECONDARY_PORT);
				}
				return false;
			}
		});
		return true;
	}

//	private void populateButtonMap() {
//		for (ERefereeCommand cmd : ERefereeCommand.values()) {
//			Field idField;
//			try {
//				idField = R.id.class.getField(cmd.name());
//				int id = idField.getInt(idField);
//				buttonMap.put(id, cmd);
//			} catch (NoSuchFieldException e) {
//				// ignore
//			} catch (IllegalArgumentException e) {
//				// ignore
//			} catch (IllegalAccessException e) {
//				// ignore
//			}
//		}
//	}

//	private void assignButtonListeners() {
//		for (int i = 0; i < buttonMap.size(); i++) {
//			int key = buttonMap.keyAt(i);
//			final ERefereeCommand cmd = buttonMap.valueAt(i);
//			final Button button = (Button) findViewById(key);
//
//			// listener here
//			button.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					refereeMsgTransmitter.sendSimpleRefereeMsg(cmd);
//				}
//			});
//		}
//	}
}
