package com.gane.shoper.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.gane.shoper.R;
import com.landicorp.android.scan.scanDecoder.CaptureActivity;
import com.landicorp.android.scan.scanDecoder.ScanDecoder;
import com.landicorp.android.scan.scanDecoder.ScanDecoder.ResultCallback;

import java.util.HashMap;
import java.util.Map;

public class ScanDecoderActivity extends AppCompatActivity implements ResultCallback {

	private static final String DEBUG_TAG = "MainActivity";
	private EditText m_editRecvData;
	private Handler mMainMessageHandler;
	private ScanDecoder mScanDecoder = null;
	private boolean cycle_test_enable = false;
	private boolean cycle_test_everytime_create_destroy = false;
	private boolean cancel = false;
	private int counts = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(DEBUG_TAG, "onCreate()");
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.scan_decoder);
		setResult(Activity.RESULT_CANCELED);

		((Button) findViewById(R.id.button_openFrontCamera))
				.setOnClickListener(btnClick);
		((Button) findViewById(R.id.button_openBackCamera))
				.setOnClickListener(btnClick);
		((Button) findViewById(R.id.button_startScan))
				.setOnClickListener(btnClick);
		((Button) findViewById(R.id.button_closeCamera))
				.setOnClickListener(btnClick);
		m_editRecvData = (EditText) findViewById(R.id.info_text);
		mMainMessageHandler = new MessageHandler(Looper.myLooper());
		mScanDecoder = new ScanDecoder(this);
		showLogMessage("ScanDecoder version=" + mScanDecoder.getVesion());
		counts = 0;
		cancel = true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.e(DEBUG_TAG, "onStart()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.e(DEBUG_TAG, "onStop()");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.e(DEBUG_TAG, "onRestart()");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e(DEBUG_TAG, "onResume()");
		// m_editRecvData.setText("");

		// cycle test
		if (cycle_test_enable) {
			cycle_test();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.e(DEBUG_TAG, "onPause()");

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e(DEBUG_TAG, "onDestroy...");
		mScanDecoder.Destroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e(DEBUG_TAG, "requestCode=" + requestCode + ",onActivityResult="
				+ resultCode);
		switch (requestCode) {
		case CaptureActivity.REQUEST_CODE_SCAN_DECODE:
			Log.e(DEBUG_TAG, "return from CaptureActivity");
			if (resultCode == Activity.RESULT_OK) {
				Log.e(DEBUG_TAG, "return from CaptureActivity,result OK");
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.e(DEBUG_TAG, "return from CaptureActivity,result CANCELED");
			}
			break;
		}
	}

	private View.OnClickListener btnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_openFrontCamera: {
				int ret = mScanDecoder.Create(ScanDecoder.CAMERA_ID_FRONT,
						ScanDecoderActivity.this);
				if (ret != 0) {
					String msg = null;
					switch (ret) {
					case 1:
						msg = "init decode lib failed";
						break;
					case 2:
						msg = "you has Created once";
						break;
					case 3:
						msg = "open camera failed";
						break;
					case 4:
						msg = "license activate failed";
						break;
					}
					Log.e(DEBUG_TAG, "Create failed,ret=" + ret + ", " + msg);
					showLogMessage("Create failed,ret=" + ret + ", " + msg);
				} else {
					Log.e(DEBUG_TAG, "Create successful");
					showLogMessage("Create successful");
				}
				break;
			}
			case R.id.button_openBackCamera: {
				int ret = mScanDecoder.Create(ScanDecoder.CAMERA_ID_BACK,
						ScanDecoderActivity.this);
				if (ret != 0) {
					String msg = null;
					switch (ret) {
					case 1:
						msg = "init decode lib failed";
						break;
					case 2:
						msg = "you has Created once";
						break;
					case 3:
						msg = "open camera failed";
						break;
					case 4:
						msg = "license activate failed";
						break;
					}
					Log.e(DEBUG_TAG, "Create failed,ret=" + ret + ", " + msg);
					showLogMessage("Create failed,ret=" + ret + ", " + msg);
				} else {
					Log.e(DEBUG_TAG, "Create successful");
					showLogMessage("Create successful");
				}
				break;
			}
			case R.id.button_startScan: {
				Map<String, String> par = new HashMap<String, String>();
				// Timeout setting,the unit milliseconds,value=0 or not set this
				// parameter means no timeout.
				// par.put(ScanDecoder.PAR_SCAN_TIMEOUT, "10000");
				int ret = mScanDecoder.startScanDecode(
						ScanDecoderActivity.this, par);
				if (ret != 0) {
					String msg = null;
					switch (ret) {
					case 1:
						msg = "ScanDecoder no create";
						break;
					}
					Log.e(DEBUG_TAG, "startScanDecode failed,ret=" + ret + ", "
							+ msg);
					showLogMessage("startScanDecode failed,ret=" + ret + ", "
							+ msg);
				}
				break;
			}
			case R.id.button_closeCamera: {
				mScanDecoder.Destroy();
				Log.e(DEBUG_TAG, "Destroy successful");
				showLogMessage("Destroy successful");
				break;
			}
			default:
				break;
			}
		}
	};

	public void showLogMessage(String msg) {
		Message updateMessage = mMainMessageHandler.obtainMessage();
		updateMessage.obj = msg;
		updateMessage.what = R.id.info_text;
		updateMessage.sendToTarget();
	}

	class MessageHandler extends Handler {
		private long mLogCount = 0;

		public MessageHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case R.id.info_text:
				if (mLogCount > 100) {
					mLogCount = 0;
					m_editRecvData.setText("");
				}
				String messageString = (String) (msg.obj);
				int cursor = m_editRecvData.getSelectionStart();
				m_editRecvData.getText().insert(cursor, messageString + "\n");
				++mLogCount;
				break;
			}

		}
	}

	// cycle test
	private void cycle_test() {
		if (cancel == true) {
			Log.e(DEBUG_TAG, "cancel...");
			cancel = false;
			Log.e(DEBUG_TAG, "test counts=" + counts);
			showLogMessage("test counts=" + counts);
			return;
		}

		int ret;
		if (cycle_test_everytime_create_destroy) {
			mScanDecoder.Destroy();
			Log.e(DEBUG_TAG, "Destroy successful");
			showLogMessage("Destroy successful");

			ret = mScanDecoder.Create(
					ScanDecoder.CAMERA_ID_BACK/* CAMERA_ID_FRONT */,
					ScanDecoderActivity.this);
			if (ret != 0) {
				String msg = null;
				switch (ret) {
				case 1:
					msg = "init decode lib failed";
					break;
				case 2:
					msg = "you has Created once";
					break;
				case 3:
					msg = "open camera failed";
					break;
				case 4:
					msg = "license activate failed";
					break;
				}
				Log.e(DEBUG_TAG, "Create failed,ret=" + ret + ", " + msg);
				showLogMessage("Create failed,ret=" + ret + ", " + msg);
				return;
			} else {
				Log.e(DEBUG_TAG, "Create successful");
				showLogMessage("Create successful");
			}
		}

		ret = mScanDecoder.startScanDecode(ScanDecoderActivity.this, null);
		if (ret != 0) {
			String msg = null;
			switch (ret) {
			case 1:
				msg = "ScanDecoder no create";
				break;
			}
			Log.e(DEBUG_TAG, "startScanDecode failed,ret=" + ret + ", " + msg);
			showLogMessage("startScanDecode failed,ret=" + ret + ", " + msg);
			return;
		}
		counts++;
		Log.e(DEBUG_TAG, "test counts=" + counts);
		showLogMessage("test counts=" + counts);
	}

	@Override
	public void onResult(String result) {
		Log.e(DEBUG_TAG, "result=" + result);
		showLogMessage("result=" + result);
	}

	@Override
	public void onCancel() {
		Log.e(DEBUG_TAG, "user cancel");
		showLogMessage("user cancel");
		cancel = true;
	}

	@Override
	public void onTimeout() {
		Log.e(DEBUG_TAG, "Timeout");
		showLogMessage("Timeout");
	}
}
