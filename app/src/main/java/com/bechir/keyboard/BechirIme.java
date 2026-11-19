package com.bechir.keyboard;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.content.res.ColorStateList;
import android.inputmethodservice.InputMethodService;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Locale;
public class BechirIme extends InputMethodService {
 private boolean arabic=true, shifted=false, symbols=false;
 private LinearLayout root;
 private int dp(float x){return (int)(getResources().getDisplayMetrics().density*x+.5f);}
 private GradientDrawable bg(int top,int bottom,int radius,int border){GradientDrawable d=new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,new int[]{top,bottom});d.setCornerRadius(dp(radius));d.setStroke(dp(1.4f),border);return d;}
 @Override public View onCreateInputView(){root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(dp(4),dp(5),dp(4),dp(9));root.setBackground(bg(0xff064452,0xff052D3D,17,0xff24899A));draw();return root;}
 private void draw(){
  root.removeAllViews();
  LinearLayout bar=new LinearLayout(this);bar.setGravity(Gravity.CENTER_VERTICAL);bar.setPadding(dp(8),dp(1),dp(8),dp(3));
  LinearLayout heading=new LinearLayout(this);heading.setOrientation(LinearLayout.VERTICAL);heading.setGravity(Gravity.CENTER_VERTICAL);TextView name=new TextView(this);name.setText("⌨  كيبورد بشير");name.setTextColor(Color.WHITE);name.setTextSize(22);name.setTypeface(Typeface.DEFAULT,Typeface.BOLD);heading.addView(name);TextView sub=new TextView(this);sub.setText("Bechir Keyboard");sub.setTextSize(10);sub.setTextColor(0xffA6EBF2);heading.addView(sub);bar.addView(heading,new LinearLayout.LayoutParams(0,dp(48),1));
  TextView lang=new TextView(this);lang.setText(arabic?"AR":"FR");lang.setTextColor(0xffB9FAFF);lang.setTextSize(16);lang.setGravity(Gravity.CENTER);bar.addView(lang,new LinearLayout.LayoutParams(dp(40),dp(39)));root.addView(bar);
  if(symbols){addRow(new String[]{"1","2","3","4","5","6","7","8","9","0"});addRow(new String[]{"@","#","$","%","&","-","+","(",")","/"});addRow(new String[]{"*","\"",":",";","!","؟","?","،",".","⌫"});}
  else if(arabic){addRow(new String[]{"ض","ص","ث","ق","ف","غ","ع","ه","خ","ح","ج"});addRow(new String[]{"ش","س","ي","ب","ل","ا","ت","ن","م","ك","ط"});addRow(new String[]{"ذ","ء","ؤ","ر","ى","ة","و","ز","ظ","د","⌫"});addRow(new String[]{"أ","إ","آ","ئ","لا","،"});}
  else{addRow(new String[]{"a","z","e","r","t","y","u","i","o","p"});addRow(new String[]{"q","s","d","f","g","h","j","k","l","m"});addRow(new String[]{"⇧","w","x","c","v","b","n","é","è","⌫"});addRow(new String[]{"à","ù","ê","ç","â","î","ô","û","!"});}
  LinearLayout bottom=new LinearLayout(this);bottom.setGravity(Gravity.CENTER);
  addKey(bottom,"?123",1.15f,0xff0A6D80);addKey(bottom,"🌐",.9f,0xff0A6D80);addKey(bottom,arabic?"،":",",.65f,0xff116C7A);addKey(bottom,arabic?"العربية":"espace",3.9f,0xff168A9C);addKey(bottom,arabic?"؟":"?",.7f,0xff116C7A);addKey(bottom,"↵",1.1f,0xff17B1C5);root.addView(bottom);
 }
 private void addRow(String[] keys){LinearLayout row=new LinearLayout(this);row.setGravity(Gravity.CENTER);for(String k:keys)addKey(row,k,1f,k.equals("⌫")?0xff08778B:0xff117F91);root.addView(row);}
 private void addKey(LinearLayout row,String value,float weight,int base){
  TextView t=new TextView(this);t.setText(value);t.setTextColor(Color.WHITE);t.setTextSize(value.length()>4?13:value.length()>2?17:21);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);t.setGravity(Gravity.CENTER);
  t.setBackground(new RippleDrawable(ColorStateList.valueOf(0xAA9BFFFF),bg(base+0x000F0F0F,base,11,0xff45B5C5),null));
  t.setOnTouchListener((v,event)->{if(event.getAction()==android.view.MotionEvent.ACTION_DOWN){v.animate().scaleX(0.93f).scaleY(0.93f).setDuration(65).start();}else if(event.getAction()==android.view.MotionEvent.ACTION_UP||event.getAction()==android.view.MotionEvent.ACTION_CANCEL){v.animate().scaleX(1f).scaleY(1f).setDuration(95).start();}return false;});
  LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,dp(49),weight);p.setMargins(dp(2),dp(2),dp(2),dp(3));row.addView(t,p);t.setOnClickListener(v->press(value));
  if(value.equals("⌫"))t.setOnLongClickListener(v->{InputConnection ic=getCurrentInputConnection();if(ic!=null)ic.deleteSurroundingText(12,0);return true;});
 }
 private void press(String s){
  InputConnection ic=getCurrentInputConnection();if(ic==null)return;
  switch(s){
   case "⌫":ic.deleteSurroundingText(1,0);break;
   case "🌐":arabic=!arabic;symbols=false;shifted=false;draw();break;
   case "?123":symbols=!symbols;draw();break;
   case "⇧":shifted=!shifted;draw();break;
   case "↵":ic.sendKeyEvent(new android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN,android.view.KeyEvent.KEYCODE_ENTER));ic.sendKeyEvent(new android.view.KeyEvent(android.view.KeyEvent.ACTION_UP,android.view.KeyEvent.KEYCODE_ENTER));break;
   case "العربية":case "espace":ic.commitText(" ",1);break;
   default:ic.commitText(shifted&&!arabic?s.toUpperCase(Locale.FRENCH):s,1);if(shifted&&!arabic){shifted=false;draw();}
  }
 }
 @Override public void onStartInput(EditorInfo attribute,boolean restarting){super.onStartInput(attribute,restarting);if(root!=null)draw();}
}
