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
import android.widget.PopupWindow;
import android.graphics.drawable.ColorDrawable;
import java.util.Locale;
public class BechirIme extends InputMethodService {
 private boolean arabic=true, shifted=false, symbols=false;
 private String activeVariants=null;
 private LinearLayout root;
 private static final int[] RAINBOW={0xffD65B91,0xffAF67D9,0xff7478E9,0xff438CE7,0xff26A8D2,0xff20B7A9,0xff57B86E,0xffB7BB4C,0xffE6A34B,0xffE37C5C};
 private int dp(float x){return (int)(getResources().getDisplayMetrics().density*x+.5f);}
 private GradientDrawable bg(int top,int bottom,int radius,int border){GradientDrawable d=new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,new int[]{top,bottom});d.setCornerRadius(dp(radius));d.setStroke(dp(1.4f),border);return d;}
 @Override public View onCreateInputView(){root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(dp(4),dp(5),dp(4),dp(26));root.setBackground(bg(0xff064452,0xff052D3D,17,0xff24899A));draw();return root;}
 private void draw(){
  root.removeAllViews();
  LinearLayout bar=new LinearLayout(this);bar.setGravity(Gravity.CENTER_VERTICAL);bar.setPadding(dp(8),dp(1),dp(8),dp(3));
  LinearLayout heading=new LinearLayout(this);heading.setOrientation(LinearLayout.VERTICAL);heading.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);TextView name=new TextView(this);name.setText("⌨  كيبورد بشير");name.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);name.setTextColor(Color.WHITE);name.setTextSize(22);name.setTypeface(Typeface.DEFAULT,Typeface.BOLD);heading.addView(name);TextView sub=new TextView(this);sub.setText("Bechir Keyboard");sub.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);sub.setTextSize(10);sub.setTextColor(0xffA6EBF2);heading.addView(sub);bar.addView(heading,new LinearLayout.LayoutParams(0,dp(48),1));
  TextView lang=new TextView(this);lang.setText(arabic?"AR":"FR");lang.setTextColor(0xffB9FAFF);lang.setTextSize(16);lang.setGravity(Gravity.CENTER);bar.addView(lang,new LinearLayout.LayoutParams(dp(40),dp(39)));root.addView(bar);
  if(true){
   LinearLayout options=new LinearLayout(this);options.setGravity(Gravity.CENTER);options.setPadding(dp(6),dp(2),dp(6),dp(2));
   for(String option:(activeVariants==null?"":activeVariants).split(",")){
    TextView key=new TextView(this);key.setText(option);key.setTextColor(Color.WHITE);key.setTextSize(19);key.setGravity(Gravity.CENTER);key.setIncludeFontPadding(true);key.setPadding(0,dp(2),0,dp(2));
    key.setBackground(bg(0xff319BB3,0xff127083,12,0xffD0F8FF));
    LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(dp(58),dp(38));lp.setMargins(dp(3),0,dp(3),dp(3));options.addView(key,lp);
    key.setOnClickListener(v->{InputConnection ic=getCurrentInputConnection();if(ic!=null)ic.commitText(option,1);activeVariants=null;refreshKeys();});
   }
   TextView close=new TextView(this);close.setText("×");close.setTextColor(Color.WHITE);close.setTextSize(24);close.setGravity(Gravity.CENTER);
   options.addView(close,new LinearLayout.LayoutParams(dp(36),dp(38)));close.setOnClickListener(v->{activeVariants=null;refreshKeys();});
   options.setVisibility(activeVariants==null?View.INVISIBLE:View.VISIBLE);
   root.addView(options);
  }
  if(symbols){addRow(new String[]{"1","2","3","4","5","6","7","8","9","0"},dp(64));addRow(new String[]{"@","#","$","%","&","-","+","(",")","/"});addRow(new String[]{"*","\"",":",";","!","؟","?","،",".","⌫"});}
  else if(arabic){addRow(new String[]{"ض","ص","ث","ق","ف","غ","ع","ه","خ","ح","ج"});addRow(new String[]{"ش","س","ي","ب","ل","ا","ت","ن","م","ك","ط"});addRow(new String[]{"ذ","ء","ر","ى","لا","و","ز","ظ","د","⌫"});}
  else{addRow(new String[]{"a","z","e","r","t","y","u","i","o","p"});addRow(new String[]{"q","s","d","f","g","h","j","k","l","m"});addRow(new String[]{"⇧","w","x","c","v","b","n","⌫"});}
  LinearLayout bottom=new LinearLayout(this);bottom.setGravity(Gravity.CENTER);bottom.setPadding(0,dp(4),0,dp(11));
  addKey(bottom,symbols?(arabic?"أبج":"ABC"):"123",1.8f,0xff0A6D80);addKey(bottom,"🌐",.9f,0xff0A6D80);addKey(bottom,".",.65f,0xff116C7A);addKey(bottom,arabic?"العربية":"espace",3.9f,0xff168A9C);addKey(bottom,arabic?"؟":"?",.7f,0xff116C7A);addKey(bottom,"↵",1.1f,0xff17B1C5);root.addView(bottom);
 }
 private void addRow(String[] keys){addRow(keys,dp(49));}
 private void addRow(String[] keys,int height){LinearLayout row=new LinearLayout(this);row.setGravity(Gravity.CENTER);for(int i=0;i<keys.length;i++){String k=keys[i];addKey(row,k,1f,k.equals("⌫")?0xff08778B:RAINBOW[(i+root.getChildCount()*3)%RAINBOW.length],height,true);}root.addView(row);}
 private int lighten(int color){int r=Math.min(255,Color.red(color)+22),g=Math.min(255,Color.green(color)+22),b=Math.min(255,Color.blue(color)+22);return Color.rgb(r,g,b);}
 private void addKey(LinearLayout row,String value,float weight,int base){addKey(row,value,weight,base,dp(49),false);}
 private void addKey(LinearLayout row,String value,float weight,int base,int height,boolean letterRow){
  TextView t=new TextView(this);t.setText(shifted&&!arabic&&!symbols&&value.length()==1&&Character.isLetter(value.charAt(0))?value.toUpperCase(Locale.FRENCH):value);t.setTextColor(Color.WHITE);t.setTextSize(value.length()>4?13:value.length()>2?17:21);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);t.setGravity(Gravity.CENTER);
  if(value.equals("⇧")&&shifted)t.setTextColor(0xffFFEEA5);
  t.setBackground(new RippleDrawable(ColorStateList.valueOf(0xAA9BFFFF),bg(lighten(base),base,13,0xffC0F5FA),null));
  t.setOnTouchListener((v,event)->{if(event.getAction()==android.view.MotionEvent.ACTION_DOWN){v.animate().scaleX(0.93f).scaleY(0.93f).setDuration(65).start();}else if(event.getAction()==android.view.MotionEvent.ACTION_UP||event.getAction()==android.view.MotionEvent.ACTION_CANCEL){v.animate().scaleX(1f).scaleY(1f).setDuration(95).start();}return false;});
  LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,height,weight);p.setMargins(dp(2),dp(2),dp(2),dp(3));row.addView(t,p);t.setOnClickListener(v->press(value));
  String variants=variantsFor(value);
  if(variants!=null)t.setOnLongClickListener(v->{activeVariants=variants;refreshKeys();return true;});
  if(value.equals("⌫"))t.setOnLongClickListener(v->{InputConnection ic=getCurrentInputConnection();if(ic!=null)ic.deleteSurroundingText(12,0);return true;});
 }
 private String variantsFor(String value){
  switch(value){
   case "ا":return "أ,إ,آ,ٱ";
   case "ي":return "ى,ئ";
   case "ى":return "ئ,ي";
   case "ئ":return "ى,ي";
   case "و":return "ؤ";
   case "ه":return "ة";
   case "ء":return "أ,إ,ؤ,ئ";
   case "a":return "à,â,ä,æ";
   case "e":return "é,è,ê,ë";
   case "c":return "ç";
   case "i":return "î,ï";
   case "o":return "ô,ö,œ";
   case "u":return "ù,û,ü";
   case "y":return "ÿ";
   default:return null;
  }
 }
 private void refreshKeys(){if(root!=null)root.post(()->{if(root!=null)draw();});}
 private void press(String s){
  InputConnection ic=getCurrentInputConnection();
  switch(s){
   case "⌫":if(ic!=null)ic.deleteSurroundingText(1,0);break;
   case "🌐":activeVariants=null;arabic=!arabic;symbols=false;shifted=false;refreshKeys();break;
   case "123":case "?123":case "ABC":case "أبج":activeVariants=null;symbols=!symbols;refreshKeys();break;
   case "⇧":shifted=!shifted;refreshKeys();break;
   case "↵":if(ic==null)break;ic.sendKeyEvent(new android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN,android.view.KeyEvent.KEYCODE_ENTER));ic.sendKeyEvent(new android.view.KeyEvent(android.view.KeyEvent.ACTION_UP,android.view.KeyEvent.KEYCODE_ENTER));break;
   case "العربية":case "espace":if(ic!=null)ic.commitText(" ",1);break;
   default:if(ic==null)break;ic.commitText(shifted&&!arabic?s.toUpperCase(Locale.FRENCH):s,1);if(shifted&&!arabic){shifted=false;refreshKeys();}
  }
 }
 @Override public void onStartInput(EditorInfo attribute,boolean restarting){super.onStartInput(attribute,restarting);if(root!=null)draw();}
}
