package com.naver.naverspeech.client;

import com.naver.speech.clientapi.SpeechConfig;
import com.naver.speech.clientapi.SpeechRecognitionException;
import com.naver.speech.clientapi.SpeechRecognitionListener;
import com.naver.speech.clientapi.SpeechRecognizer;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

class NaverRecognizer implements SpeechRecognitionListener {

	private final static String TAG = NaverRecognizer.class.getSimpleName();

	private Handler mHandler;
	private SpeechRecognizer mRecognizer;

	public NaverRecognizer(Activity activity, Handler handler, String clientId, SpeechConfig config) {
		this.mHandler = handler;

		try {
			mRecognizer = new SpeechRecognizer(activity, clientId, config);
		} catch (SpeechRecognitionException e) {
			// 예외가 발생하는 경우는 아래와 같습니다.
			//   1. activity 파라미터가 올바른 MainActivity의 인스턴스가 아닙니다.
			//   2. AndroidManifest.xml에서 package를 올바르게 등록하지 않았습니다.
			//   3. package를 올바르게 등록했지만 과도하게 긴 경우, 256바이트 이하면 좋습니다.
			//   4. clientId가 null인 경우
			//
			// 개발하면서 예외가 발생하지 않았다면 실서비스에서도 예외는 발생하지 않습니다.
			// 개발 초기에만 주의하시면 됩니다.
			e.printStackTrace();
		}

		mRecognizer.setSpeechRecognitionListener(this);

		// If you want to recognize audio file, please add codes as follows.
		// mRecognizer.setAudioFileInput(true);
		// mRecognizer.setAudioFilePath(Environment.getExternalStorageDirectory().getAbsolutePath()
		// + "/NaverSpeechTest/TestInput.pcm");
	}

	public SpeechRecognizer getSpeechRecognizer() {
		return mRecognizer;
	}

	public void recognize() {
		try {
			mRecognizer.recognize();
		} catch (SpeechRecognitionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onInactive() {
		Log.d(TAG, "Event occurred : Inactive");
		Message msg = Message.obtain(mHandler, R.id.clientInactive);
		msg.sendToTarget();
	}

	@Override
	public void onReady() {
		Log.d(TAG, "Event occurred : Ready");
		Message msg = Message.obtain(mHandler, R.id.clientReady);
		msg.sendToTarget();
	}

	@Override
	public void onRecord(short[] speech) {
		Log.d(TAG, "Event occurred : Record");
		Message msg = Message.obtain(mHandler, R.id.audioRecording, speech);
		msg.sendToTarget();
	}

	@Override
	public void onPartitialResult(String result) {
		Log.d(TAG, "Partial Result!! (" + result + ")");
		Message msg = Message.obtain(mHandler, R.id.partialResult, result);
		msg.sendToTarget();
	}

	@Override
	public void onEndPointDetected() {
		Log.d(TAG, "Event occurred : EndPointDetected");
	}

	@Override
	public void onResult(Object[] result) {
		Log.d(TAG, "Final Result!! (" + result[0] + ")");
		Message msg = Message.obtain(mHandler, R.id.finalResult, result);
		msg.sendToTarget();
	}

	@Override
	public void onError(int errorCode) {
		Message msg = Message.obtain(mHandler, R.id.recognitionError, errorCode);
		msg.sendToTarget();
	}
}