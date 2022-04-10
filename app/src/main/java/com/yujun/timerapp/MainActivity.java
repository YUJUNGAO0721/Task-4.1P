package com.yujun.timerapp;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yujun.timerapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    SharedPrefManager pref;
    long timeWhenStopped = 0;

    /**
     *  We need to save "Activity's State" before changing orientation.
     * onSaveInstanceState helps us to save all the values we want to get back after orientation changes.
     * Then restore them in onCreate() method.
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        timeWhenStopped = binding.chronometerCounter.getBase() - SystemClock.elapsedRealtime();
        binding.chronometerCounter.stop();
        Log.d("timeBeforeStateChange", String.valueOf(timeWhenStopped));
        outState.putString("timeOnStateChange", String.valueOf(timeWhenStopped));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pref = new SharedPrefManager(MainActivity.this);

        if (savedInstanceState == null) {
            // activity is being created a first time
            binding.previousTimer.setText(String.format("You spent %s on %s last time",
                    pref.getPREVIOUS_STUDY_HOUR(), pref.getPREVIOUS_STUDY_UNIT()));
        } else {
            // activity is being recreated and onSaveInstanceState() has already been called.
            binding.previousTimer.setText(String.format("You spent %s on %s last time",
                    pref.getPREVIOUS_STUDY_HOUR(), pref.getPREVIOUS_STUDY_UNIT()));
            long timeOnStateChange = Long.parseLong(savedInstanceState.getString("timeOnStateChange"));
            binding.chronometerCounter.setBase(SystemClock.elapsedRealtime() + timeOnStateChange);
            binding.chronometerCounter.start();
        }

        binding.startTimer.setOnClickListener(view -> startTimer());
        binding.stopTimer.setOnClickListener(view -> stopTimer());
        binding.pauseTimer.setOnClickListener(view -> pauseTimer());
        binding.resumeTimer.setOnClickListener(view -> resumeTimer());
    }

    private void startTimer() {
        // must enter task type before startTimer
        if (TextUtils.isEmpty(binding.taskType.getText().toString().trim())) {
            binding.taskType.setError("Kindly enter your task!");
        } else {
            binding.chronometerCounter.start();
            binding.chronometerCounter.setBase(SystemClock.elapsedRealtime());
        }
    }

    private void stopTimer() {
        if (TextUtils.isEmpty(binding.taskType.getText().toString().trim())) {
            binding.taskType.setError("Kindly enter your task!");
        } else {
            binding.chronometerCounter.stop();
            pref.setPREVIOUS_STUDY_HOUR(binding.chronometerCounter.getText().toString().trim());
            pref.setPREVIOUS_STUDY_UNIT(binding.taskType.getText().toString().trim());
            Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
            binding.previousTimer.setText(String.format("You spent %s on %s last time",
                    pref.getPREVIOUS_STUDY_HOUR(), pref.getPREVIOUS_STUDY_UNIT()));
        }
    }

    private void pauseTimer() {
        if (TextUtils.isEmpty(binding.taskType.getText().toString().trim())) {
            binding.taskType.setError("Kindly enter your task!");
        } else {
            timeWhenStopped = binding.chronometerCounter.getBase() - SystemClock.elapsedRealtime();
            binding.chronometerCounter.stop();
            Log.d("timeWhenStopped", String.valueOf(timeWhenStopped));
            Toast.makeText(this, "Paused", Toast.LENGTH_LONG).show();
            // hide ICs
            binding.startTimer.setVisibility(View.GONE);
            binding.pauseTimer.setVisibility(View.GONE);
            binding.stopTimer.setVisibility(View.GONE);
            binding.resumeTimer.setVisibility(View.VISIBLE);
        }
    }

    private void resumeTimer() {
        Log.d("timeWhenStopped", SystemClock.elapsedRealtime() + ":" + timeWhenStopped);
        binding.chronometerCounter.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        binding.chronometerCounter.start();
        //reset your chronometer
        timeWhenStopped = 0;
        Toast.makeText(this, "Resumed", Toast.LENGTH_SHORT).show();

        // un-hide UI
        binding.startTimer.setVisibility(View.VISIBLE);
        binding.pauseTimer.setVisibility(View.VISIBLE);
        binding.stopTimer.setVisibility(View.VISIBLE);
        binding.resumeTimer.setVisibility(View.GONE);
    }
}