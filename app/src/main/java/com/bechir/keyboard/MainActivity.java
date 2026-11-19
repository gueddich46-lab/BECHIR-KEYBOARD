package com.bechir.keyboard;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
public class MainActivity extends Activity {
 @Override public void onCreate(Bundle b) {
  super.onCreate(b);
  LinearLayout root=new LinearLayout(this); root.setOrientation(1); root.setGravity(Gravity.CENTER); root.setPadding(28,40,28,40); root.setBackgroundColor(0xff054957);
  TextView title=new TextView(this); title.setText("كيبورد بشير"); title.setTextColor(Color.WHITE); title.setTextSize(34); title.setTypeface(null,Typeface.BOLD); title.setGravity(Gravity.CENTER); root.addView(title);
  TextView subtitle=new TextView(this); subtitle.setText("BECHIR KEYBOARD\nلوحة مفاتيح عربية وفرنسية"); subtitle.setTextColor(0xffBCECF3); subtitle.setTextSize(18); subtitle.setGravity(Gravity.CENTER); subtitle.setPadding(0,12,0,32); root.addView(subtitle);
  Button enable=new Button(this); enable.setText("١. تفعيل كيبورد بشير"); enable.setOnClickListener(v->startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))); root.addView(enable);
  Button select=new Button(this); select.setText("٢. اختيار كيبورد بشير"); select.setOnClickListener(v->{InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);imm.showInputMethodPicker();});root.addView(select);
  TextView info=new TextView(this);info.setText("بعد التفعيل والاختيار، افتح أي حقل كتابة لتجربة الكيبورد.");info.setTextColor(Color.WHITE);info.setGravity(Gravity.CENTER);info.setTextSize(15);info.setPadding(0,32,0,0);root.addView(info);
  setContentView(root);
 }
}
