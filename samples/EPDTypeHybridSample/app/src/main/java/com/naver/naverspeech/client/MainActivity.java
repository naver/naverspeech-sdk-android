package com.naver.naverspeech.client;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.naverspeech.client.utils.AudioWriterPCM;
import com.naver.speech.clientapi.SpeechConfig.EndPointDetectType;
import com.naver.speech.clientapi.SpeechRecognitionResult;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final String CLIENT_ID = "YOUR CLIENT ID";
    // 1. "내 애플리케이션"에서 Client ID를 확인해서 이곳에 적어주세요.
    // 2. build.gradle (Module:app)에서 패키지명을 실제 개발자센터 애플리케이션 설정의 '안드로이드 앱 패키지 이름'으로 바꿔 주세요

    private RecognitionHandler handler;
    private NaverRecognizer naverRecognizer;

    private Button btnHybridRecognize;
    private TextView txtResult;
    private String strResult;

    private AudioWriterPCM writer;

    private EndPointDetectType currentEpdType;
    private boolean isEpdTypeSelected;

    // Handle speech recognition Messages.
    private void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
                // Now an user can speak.
                txtResult.setText("Connected");
                writer = new AudioWriterPCM(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                break;

            case R.id.audioRecording:
                writer.write((short[]) msg.obj);
                break;

            case R.id.partialResult:
                // Extract obj property typed with String.
                strResult = (String) (msg.obj);
                txtResult.setText(strResult);
                break;

            case R.id.finalResult:
                // Extract obj property typed with String array.
                // The first element is recognition result for speech.
            	SpeechRecognitionResult speechRecognitionResult = (SpeechRecognitionResult) msg.obj;
            	List<String> results = speechRecognitionResult.getResults();
            	StringBuilder strBuf = new StringBuilder();
            	for(String result : results) {
            		strBuf.append(result);
            		strBuf.append("\n");
            	}
                strResult = strBuf.toString();
                txtResult.setText(strResult);
                break;

            case R.id.recognitionError:
                if (writer != null) {
                    writer.close();
                }

                strResult = "Error code : " + msg.obj.toString();
                txtResult.setText(strResult);
                btnHybridRecognize.setText(R.string.str_start);
                btnHybridRecognize.setEnabled(true);
                break;

            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }

                btnHybridRecognize.setText(R.string.str_start);
                btnHybridRecognize.setEnabled(true);
                break;

            case R.id.endPointDetectTypeSelected:
                isEpdTypeSelected = true;
                currentEpdType = (EndPointDetectType) msg.obj;
                if(currentEpdType == EndPointDetectType.AUTO) {
                    Toast.makeText(this, "AUTO epd type is selected.", Toast.LENGTH_SHORT).show();
                } else if(currentEpdType == EndPointDetectType.MANUAL) {
                    Toast.makeText(this, "MANUAL epd type is selected.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResult = (TextView) findViewById(R.id.txt_result);
        btnHybridRecognize = (Button) findViewById(R.id.btn_start);

        handler = new RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer(this, handler, CLIENT_ID);

        btnHybridRecognize.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(!naverRecognizer.getSpeechRecognizer().isRunning()) {
                        // Run SpeechRecongizer by calling recognize().
                        strResult = "";
                        txtResult.setText("Connecting...");
                        btnHybridRecognize.setText(R.string.str_stop);

                        currentEpdType = EndPointDetectType.HYBRID;
                        isEpdTypeSelected = false;
                        naverRecognizer.recognize();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if(!isEpdTypeSelected) {
                        if(naverRecognizer.getSpeechRecognizer().isRunning()) {
                            naverRecognizer.getSpeechRecognizer().selectEPDTypeInHybrid(EndPointDetectType.AUTO);
                        }
                    } else {
                        if(!naverRecognizer.getSpeechRecognizer().isRunning()) {
                            Log.e(TAG, "Recognition is already finished.");
                        } else {
                            btnHybridRecognize.setEnabled(false);
                            naverRecognizer.getSpeechRecognizer().stop();
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
    	super.onStart();
    	// NOTE : initialize() must be called on start time.
    	naverRecognizer.getSpeechRecognizer().initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();

        strResult = "";
        txtResult.setText("");
        btnHybridRecognize.setText(R.string.str_start);
        btnHybridRecognize.setEnabled(true);
    }

    @Override
    protected void onStop() {
    	super.onStop();
    	// NOTE : release() must be called on stop time.
    	naverRecognizer.getSpeechRecognizer().release();
    }

    // Declare handler for handling SpeechRecognizer thread's Messages.
    static class RecognitionHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        RecognitionHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }
}
