package com.example.alarmclock;

import android.app.Activity;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmDetailsActivity extends Activity {
	private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
	private AlarmModel alarmDetails;
	
	private TimePicker timePicker;
	private EditText edtName;
	private CustomToggleButton chkWeekly;
	private CustomToggleButton chkSunday;
	private CustomToggleButton chkMonday;
	private CustomToggleButton chkTuesday;
	private CustomToggleButton chkWednesday;
	private CustomToggleButton chkThursday;
	private CustomToggleButton chkFriday;
	private CustomToggleButton chkSaturday;
	private TextView txtToneSelection;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		getActionBar().setTitle("Create New Alarm");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		timePicker = (TimePicker) findViewById(R.id.alarm_details_time_picker);
		edtName = (EditText) findViewById(R.id.alarm_details_name);
		chkWeekly = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_weekly);
		chkSunday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_sunday);
		chkMonday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_monday);
		chkTuesday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_tuesday);
		chkWednesday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_wednesday);
		chkThursday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_thursday);
		chkFriday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_friday);
		chkSaturday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_saturday);
		txtToneSelection = (TextView) findViewById(R.id.alarm_label_tone_selection);


		setContentView(R.layout.activity_alarm_details);
		alarmDetails = new AlarmModel();
		
		final LinearLayout ringToneContainer = (LinearLayout) findViewById(R.id.alarm_ringtone_container);
		ringToneContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
				startActivityForResult(intent , 1);
			}
		});
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
	        switch (requestCode) {
		        case 1: {
		        	alarmDetails.alarmTone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

		        	TextView txtToneSelection = (TextView) findViewById(R.id.alarm_label_tone_selection);
		        	txtToneSelection.setText(RingtoneManager.getRingtone(this, alarmDetails.alarmTone).getTitle(this));
		            break;
		        }
		        default: {
		            break;
		        }
	        }
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
			case android.R.id.home: {
				finish();
				break;
			}
			case R.id.action_save_alarm_details: {
				updateModelFromLayout();
				
				AlarmDBHelper dbHelper = new AlarmDBHelper(this);
				if(alarmDetails.id <0 ) {
					dbHelper.createAlarm(alarmDetails);
					
				} else {
					dbHelper.updateAlarm(alarmDetails);
				}
				finish();
			}
		}
		
		return super.onOptionsItemSelected(item);
	}

	void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
		
	private void updateModelFromLayout() {

		String tPmsg;
		
		showToast("getting time");
		
		tPmsg = timePicker.getCurrentMinute().toString();
		

		showToast(tPmsg);
		alarmDetails.timeMinute = timePicker.getCurrentMinute().intValue();
		
//		alarmDetails.timeMinute = 2;
//		alarmDetails.timeHour = timePicker.getCurrentHour().intValue();
//		alarmDetails.timeHour = 4;
//		alarmDetails.name = edtName.getText().toString();
		alarmDetails.repeatWeekly = chkWeekly.isChecked();	
		if(alarmDetails.repeatWeekly){
			showToast("checked weekly");
		}
		alarmDetails.setRepeatingDay(AlarmModel.SUNDAY, chkSunday.isChecked());	
		alarmDetails.setRepeatingDay(AlarmModel.MONDAY, chkMonday.isChecked());	
		alarmDetails.setRepeatingDay(AlarmModel.TUESDAY, chkTuesday.isChecked());
		alarmDetails.setRepeatingDay(AlarmModel.WEDNESDAY, chkWednesday.isChecked());	
		alarmDetails.setRepeatingDay(AlarmModel.THURSDAY, chkThursday.isChecked());
		alarmDetails.setRepeatingDay(AlarmModel.FRIDAY, chkFriday.isChecked());
		alarmDetails.setRepeatingDay(AlarmModel.SATURDAY, chkSaturday.isChecked());
		alarmDetails.isEnabled = true;
	}
}
