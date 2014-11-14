package tranduythanh.com;

import tranduythanh.com.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsMessage;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	BroadcastReceiver receiver=null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//tạo bộ lọc để lắng nghe tin nhắn gửi tới
		IntentFilter filter=new IntentFilter
					("android.provider.Telephony.SMS_RECEIVED");
		//tạo bộ lắng nghe
		receiver=new BroadcastReceiver() {
			//hàm này sẽ tự động được kích hoạt khi có tin nhắn gửi tới
			public void onReceive(Context arg0, Intent arg1) {
				//Tui tách ra hàm riêng để xử lý
				//chú ý là dữ liệu trong tin nhắn được lưu trữ trong arg1
				processReceive(arg0, arg1);
			}
		};
		//đăng ký bộ lắng nghe vào hệ thống
		registerReceiver(receiver, filter);
	}
	protected void onDestroy() {
		super.onDestroy();
		//hủy bỏ đăng ký khi tắt ứng dụng
		unregisterReceiver(receiver);
	}
	public void processReceive(Context context, Intent intent)
	{
		Toast.makeText(context, "Hello .. có tin nhắn tới đó", Toast.LENGTH_LONG).show();
		TextView lbl=(TextView) findViewById(R.id.textView1);
		//pdus để lấy gói tin nhắn
		String sms_extra="pdus";
		Bundle bundle=intent.getExtras();
		//bundle trả về tập các tin nhắn gửi về cùng lúc
		Object []objArr= (Object[]) bundle.get(sms_extra);
		String sms="";
		//duyệt vòng lặp để đọc từng tin nhắn
		for(int i=0;i<objArr.length;i++)
		{
			//lệnh chuyển đổi về tin nhắn createFromPdu
			SmsMessage smsMsg=SmsMessage.
					createFromPdu((byte[]) objArr[i]);
			//lấy nội dung tin nhắn
			String body=smsMsg.getMessageBody();
			//lấy số điện thoại tin nhắn
			String address=smsMsg.getDisplayOriginatingAddress();
			sms+=address+":\n"+body+"\n";
		}
		//hiển thị lên giao diện
		lbl.setText(sms);
	}
}
