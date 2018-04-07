package com.android.tool.nfc;

import com.example.nfcdemo.R;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

@SuppressLint("NewApi")
public class NFCActivity extends BaseActivity {

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		// 检查nfc是否开启
		checkNfc();
		onNewIntent(getIntent());

		initListener();

	}

	private void initListener() {
		readButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkBlock()) {
					MifareClassicCard mifareClassicCard = new MifareClassicCard(
							mifareClassic);
					int block = Integer.parseInt(blockIdEditText.getText()
							.toString().trim());
					String content = mifareClassicCard.readCarCode(block,
							keyEdittext.getText().toString().trim());
					if ("秘钥错误".equals(content) || "读取失败".equals(content))
						setHintToContentEd(content);
					else
						contentEditText.setText(content);
				}

			}
		});
		wriButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkBlock()) {
					MifareClassicCard mifareClassicCard = new MifareClassicCard(
							mifareClassic);
					int block = Integer.parseInt(blockIdEditText.getText()
							.toString().trim());
					String content = contentEditText.getText().toString()
							.trim();
					String result = mifareClassicCard.wirteCarCode(content,
							block, keyEdittext.getText().toString().trim());
					if ("".equals(result))
						setHintToContentEd("写入成功");
					else
						setHintToContentEd(result);

				}

			}
		});

		modifyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkBlock()) {
					MifareClassicCard mifareClassicCard = new MifareClassicCard(
							mifareClassic);
					int block = Integer.parseInt(blockIdEditText.getText()
							.toString().trim());
					String content = contentEditText.getText().toString()
							.trim();
					String key = keyEdittext.getText().toString().trim();
					String result = mifareClassicCard.modifyPassword(block,
							content, key);
					if ("".equals(result))
						setHintToContentEd("修改成功");
					else
						setHintToContentEd(result);
				}

			}
		});
	}

	@SuppressLint("NewApi")
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (nfcAdapter != null)
			nfcAdapter.enableForegroundDispatch(this, pendingIntent, FILTERS,
					TECHLISTS);

	}

	@SuppressLint("NewApi")
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (nfcAdapter != null)
			nfcAdapter.disableForegroundDispatch(this);
	}

	@SuppressLint("NewApi")
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {

		} else {
			if (tag != null) {

				// 获取卡类型，根据卡类型可推出卡协议
				String[] techList = tag.getTechList();
				StringBuffer techString = new StringBuffer();
				for (int i = 0; i < techList.length; i++) {
					techString.append(techList[i]);
					techString.append(";\n");
				}
				typeEditText.setText(techString.toString());
				// 获取卡id
				byte[] id = tag.getId();
				carCodeEditText.setText(ByteArrayToHexString(id));

				mifareClassic = MifareClassic.get(tag);
				if (mifareClassic != null) {
					showView(1);
				} else {
					showView(0);
				}
			}

		}
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        PackageManager pk = getPackageManager();
        PackageInfo pi;
        try {
            pi = pk.getPackageInfo(getPackageName(), 0);
            Toast.makeText(this, "V" +pi.versionName , Toast.LENGTH_SHORT).show();
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }
}
