package com.xmx.homenurse.Prescription;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.xmx.homenurse.R;
import com.xmx.homenurse.Tools.ActivityBase.BaseTempActivity;
import com.xmx.homenurse.Tools.Data.Callback.SelectCallback;
import com.xmx.homenurse.Tools.Timer;

import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class PrescriptionActivity extends BaseTempActivity {
    BluetoothSPP bt;
    boolean connectFlag = false;
    TextView stateText;
    Prescription pre;
    int sendingRate = 0;
    String sendingData;
    Timer sending;
    boolean sendingFlag = false;

    static long SENDING_INTERVAL = 800;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_prescritpion);

        stateText = getViewById(R.id.bluetooth_state);
        bt = new BluetoothSPP(getBaseContext());
        if (bt.isBluetoothAvailable()) {
            if (bt.isBluetoothEnabled()) {
                stateText.setText(R.string.bluetooth_opened);
            } else {
                stateText.setText(R.string.bluetooth_unopened);
            }
        } else {
            stateText.setText(R.string.bluetooth_unavailable);
        }

        bt.setBluetoothStateListener(new BluetoothSPP.BluetoothStateListener() {
            public void onServiceStateChanged(int state) {
                if (state == BluetoothState.STATE_CONNECTED) {
                    showToast(R.string.connect_bluetooth_success);
                    stateText.setText(R.string.connect_bluetooth_success);
                    connectFlag = true;
                } else if (state == BluetoothState.STATE_CONNECTING) {
                    showToast(R.string.connecting_bluetooth);
                    stateText.setText(R.string.connecting_bluetooth);
                } else if (state == BluetoothState.STATE_LISTEN) {
                    stateText.setText(R.string.waiting_bluetooth);
                } else if (state == BluetoothState.STATE_NONE) {
                    stateText.setText(R.string.no_bluetooth);
                }
            }
        });

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                showToast("接受到数据：" + data);
                showToast(message);
            }
        });
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        PrescriptionManager.getInstance().syncFromCloud(null, new SelectCallback<Prescription>() {
            @Override
            public void success(List<Prescription> prescriptions) {
                if (prescriptions.size() > 0) {
                    pre = prescriptions.get(0);
                    ((TextView) getViewById(R.id.tv_time)).setText(String.format("%02d:%02d",
                            pre.mHour, pre.mMinute));

                    ((TextView) getViewById(R.id.tv_name1)).setText(pre.mName[0]);
                    ((TextView) getViewById(R.id.tv_count1)).setText("*" + pre.mCount[0]);
                    ((TextView) getViewById(R.id.tv_name2)).setText(pre.mName[1]);
                    ((TextView) getViewById(R.id.tv_count2)).setText("*" + pre.mCount[1]);
                    ((TextView) getViewById(R.id.tv_name3)).setText(pre.mName[2]);
                    ((TextView) getViewById(R.id.tv_count3)).setText("*" + pre.mCount[2]);
                    ((TextView) getViewById(R.id.tv_name4)).setText(pre.mName[3]);
                    ((TextView) getViewById(R.id.tv_count4)).setText("*" + pre.mCount[3]);
                    ((TextView) getViewById(R.id.tv_name5)).setText(pre.mName[4]);
                    ((TextView) getViewById(R.id.tv_count5)).setText("*" + pre.mCount[4]);

                    getViewById(R.id.open_bluetooth).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (bt.isBluetoothEnabled()) {
                                showToast(R.string.bluetooth_opened);
                            } else {
                                bt.enable();
                                showToast(R.string.opening_bluetooth);
                            }
                            stateText.setText(R.string.bluetooth_opened);
                        }
                    });

                    getViewById(R.id.search_device).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (bt.isBluetoothEnabled()) {
                                Intent intent = new Intent(PrescriptionActivity.this, DeviceList.class);
                                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                            } else {
                                showToast(R.string.open_bluetooth_hint);
                            }
                        }
                    });

                    getViewById(R.id.send_data).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (connectFlag && pre != null && !sendingFlag) {
                                sendingData = "";
                                for (int i=0; i<5; ++i) {
                                    int count = pre.mCount[i];
                                    for (int j=0; j<count; ++j) {
                                        sendingData += "2";
                                    }
                                    sendingData += "1";
                                }
                                if (!sendingData.equals("")) {
                                    showToast(R.string.sending);
                                    sendingFlag = true;
                                    sendingRate = 0;
                                    sending = new Timer() {
                                        @Override
                                        public void timer() {
                                            if (sendingRate < sendingData.length()) {
                                                char c = sendingData.charAt(sendingRate);
                                                String data = String.valueOf(c);
                                                bt.send(data, true);
                                                sendingRate++;
                                            } else {
                                                sending.stop();
                                                sendingFlag = false;
                                            }
                                        }
                                    };
                                    sending.start(SENDING_INTERVAL);
                                    showToast(R.string.send_bluetooth_success);
                                } else {
                                    showToast(R.string.empty_hint);
                                }
                            } else {
                                showToast(R.string.connect_bluetooth_hint);
                            }
                        }
                    });
                }
            }

            @Override
            public void notInit() {
                showToast(R.string.failure);
            }

            @Override
            public void syncError(AVException e) {
                showToast(R.string.sync_failure);
            }

            @Override
            public void notLoggedIn() {
                showToast(R.string.not_loggedin);
            }

            @Override
            public void errorNetwork() {
                showToast(R.string.network_error);
            }

            @Override
            public void errorUsername() {
                showToast(R.string.username_error);
            }

            @Override
            public void errorChecksum() {
                showToast(R.string.not_loggedin);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                bt.connect(data);
            }
        }
    }
}
