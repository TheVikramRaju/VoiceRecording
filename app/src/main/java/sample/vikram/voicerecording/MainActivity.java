package sample.vikram.voicerecording;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements View.OnClickListener {
    Button btntimer;
    private String outputFile = null;
    boolean is_recording_pressed;
    Timer t;
    Typeface face;
    int minute = 0, seconds = 0, hour = 0;

    private static final String AUDIO_RECORDER_FILE_EXT_MP3 = ".mp3";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private MediaRecorder recorder = null;
    private String file_exts = AUDIO_RECORDER_FILE_EXT_MP3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btntimer = (Button) findViewById(R.id.btntimer);
        t = new Timer("hello", true);
        is_recording_pressed = false;
        btntimer.setOnClickListener(this);
        face= Typeface.createFromAsset(getAssets(), "SeasideResortNF.ttf");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btntimer:
                ShowDialogCreateData();
                break;
        }
    }

    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }
        return (file.getAbsolutePath() + "/ file_" + System.currentTimeMillis() + file_exts);
    }


    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setAudioEncodingBitRate(16);
        recorder.setAudioSamplingRate(44100);
        outputFile = getFilename();
        recorder.setOutputFile(outputFile);
        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (null != recorder) {
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
        }
    }

    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            Toast.makeText(MainActivity.this, "Error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            Toast.makeText(MainActivity.this, "Warning: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
        }
    };

    private void ShowDialogCreateData() {
        // custom dialog

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.audio_record);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView timer = (TextView) dialog.findViewById(R.id.timer);
        final TextView message = (TextView) dialog.findViewById(R.id.txt_msg);
        final ImageView imgrecord = (ImageView) dialog.findViewById(R.id.record);
        final ProgressBar background = (ProgressBar) dialog.findViewById(R.id.rotate);
        timer.setTypeface(face);
        imgrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.record:
                        if (!is_recording_pressed) {
                            timer.setTypeface(face);
                            message.setTextAppearance(MainActivity.this, android.R.style.TextAppearance_Small);
                            message.setText("Tab the icon again to Stop Recording..!");
                            background.setVisibility(View.VISIBLE);
                            imgrecord.setImageDrawable(getResources().getDrawable(R.drawable.ic_recording));
                            is_recording_pressed = true;
                            if(is_recording_pressed){
                                dialog.setCancelable(false);
                            }else {
                                dialog.setCancelable(true);
                            }
                            startRecording();
                            minute = 0;
                            seconds = 0;
                            hour = 0;

                            t = new Timer("hello", true);
                            t.schedule(new TimerTask() {

                                @Override
                                public void run() {
                                    timer.post(new Runnable() {

                                        public void run() {
                                            seconds++;
                                            if (seconds == 60) {
                                                seconds = 0;
                                                minute++;
                                            }
                                            if (minute == 60) {
                                                minute = 0;
                                                hour++;
                                            }
                                            timer.setText(""
                                                    + (hour > 9 ? hour : ("0" + hour)) + " : "
                                                    + (minute > 9 ? minute : ("0" + minute))
                                                    + " : "
                                                    + (seconds > 9 ? seconds : "0" + seconds));

                                        }
                                    });

                                }
                            }, 1000, 1000);
                        } else {
                            t.cancel();
                            dialog.setCancelable(true);
                            dialog.dismiss();
                            timer.setTypeface(face);
                            message.setTextAppearance(MainActivity.this, android.R.style.TextAppearance_Small);
                            message.setText("Tap the icon to Start Recording..");
                            imgrecord.setImageDrawable(getResources().getDrawable(R.drawable.ic_record));
                            background.setVisibility(View.GONE);
                            is_recording_pressed = false;
                            stopRecording();

                            final Dialog info_dialog = new Dialog(MainActivity.this);

                            info_dialog.setContentView(R.layout.dialog);
                            info_dialog.setTitle("Done Recording");
                            TextView txt = (TextView) info_dialog.findViewById(R.id.textView);
                            txt.setText("Media stroed in " + outputFile);
                            Button dismissButton = (Button) info_dialog.findViewById(R.id.button);
                            dismissButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    info_dialog.dismiss();
                                    timer.setText("00 : 00 : 00");
                                }
                            });
                            info_dialog.show();
                        }
                        break;
                }
            }
        });
        dialog.show();
    }
}
