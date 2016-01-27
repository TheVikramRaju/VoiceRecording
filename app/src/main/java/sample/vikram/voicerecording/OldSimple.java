package sample.vikram.voicerecording;

import android.app.Activity;
import android.app.Dialog;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by bosco on 9/5/2015.
 */
public class OldSimple extends Activity  implements View.OnClickListener {
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    ImageView imgrecord;
    TextView message;
    ProgressBar background;
    RelativeLayout recording_layout;
    boolean is_recording_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        message = (TextView) findViewById(R.id.txt_msg);
        imgrecord = (ImageView) findViewById(R.id.record);
        background = (ProgressBar) findViewById(R.id.rotate);
        recording_layout = (RelativeLayout) findViewById(R.id.record_lyt);
        is_recording_pressed = false;
        imgrecord.setOnClickListener(this);

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

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
           case R.id.record:
               if (!is_recording_pressed) {
                   message.setText("Tab the icon again to Stop Recording..!");
                   background.setVisibility(View.VISIBLE);
                   is_recording_pressed = true;
                   try {
                       myAudioRecorder.prepare();
                       myAudioRecorder.start();
                   } catch (IllegalStateException e) {
                       // TODO Auto-generated catch block
                       e.printStackTrace();
                   } catch (IOException e) {
                       // TODO Auto-generated catch block
                       e.printStackTrace();
                   }
//                    Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
               } else {
                   message.setText("Tap the icon to Start Recording..");
                   background.setVisibility(View.GONE);
                   is_recording_pressed = false;
                   myAudioRecorder.stop();
                   myAudioRecorder.release();
                   myAudioRecorder = null;
                   final Dialog dialog = new Dialog(OldSimple.this);

                   //setting custom layout to dialog
                   dialog.setContentView(R.layout.dialog);
                   dialog.setTitle("Done Recording");

                   //adding text dynamically
                   TextView txt = (TextView) dialog.findViewById(R.id.textView);
                   txt.setText("Media stroed in " + outputFile);
                   //adding button click event
                   Button dismissButton = (Button) dialog.findViewById(R.id.button);
                   dismissButton.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           dialog.dismiss();
                       }
                   });
                   dialog.show();
               }
               break;
       }
   }

}
