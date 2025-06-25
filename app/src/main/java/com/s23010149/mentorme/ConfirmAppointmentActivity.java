package com.s23010149.mentorme;

import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ConfirmAppointmentActivity extends AppCompatActivity {
    private DatePicker datePicker;
    private TimePicker timePicker;
    private EditText etBriefIntro;
    private Button btnSendAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentee_appointment_confirm);

        datePicker = findViewById(R.id.date_picker);
        timePicker = findViewById(R.id.time_picker);
        etBriefIntro = findViewById(R.id.et_brief_intro);
        btnSendAppointment = findViewById(R.id.btn_send_appointment);

        // Set TimePicker to 12-hour mode (remove this for 24-hour)
        timePicker.setIs24HourView(false);

        btnSendAppointment.setOnClickListener(v -> {
            // Get date
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();

            // Get time (API >= 23 has getHour/getMinute, older use getCurrentHour/getCurrentMinute)
            int hour, minute;
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                hour = timePicker.getHour();
                minute = timePicker.getMinute();
            } else {
                hour = timePicker.getCurrentHour();
                minute = timePicker.getCurrentMinute();
            }

            // Get brief intro
            String briefIntro = etBriefIntro.getText().toString().trim();

            // Format date and time
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day, hour, minute);
            String dateTime = android.text.format.DateFormat.format("yyyy-MM-dd hh:mm aa", calendar).toString();

            // You can now use these values as needed (e.g., send to server, display, etc.)
            Toast.makeText(
                    ConfirmAppointmentActivity.this,
                    "Appointment set for " + dateTime + "\nIntro: " + briefIntro,
                    Toast.LENGTH_LONG
            ).show();

            //TODO: Send the appointment data to your backend/server or next screen
        });
    }
}