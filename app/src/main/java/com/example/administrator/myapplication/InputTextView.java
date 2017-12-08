package com.example.administrator.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/12/8 0008.
 */

public class InputTextView extends ImageView {
    private  InputMethodManager inputMethodManager;
    private String inputString;
    private String nowString;
    private Paint paint;
    private float startX;
    private float startY;

    public InputTextView(Context context) {
        this(context,null);
    }

    public InputTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public InputTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setTextSize(36);
        paint.setColor(Color.GREEN);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        // outAttrs就是我们需要设置的输入法的各种类型最重要的就是:
        outAttrs.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI;
        outAttrs.inputType = InputType.TYPE_NULL;
        return new MyInputConnection(this,true);
    }

    class MyInputConnection extends BaseInputConnection {

        public MyInputConnection(View targetView, boolean fullEditor) {
            super(targetView, fullEditor);
        }

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            Log.d("tag", "commitText:" + text + "\t" + newCursorPosition);
            if (TextUtils.isEmpty(nowString)){
                nowString = text.toString();
            }else{
                inputString = text.toString();
            }
            postInvalidate();
            return true;
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            /** 当手指离开的按键的时候 */
            Log.d("tag", "sendKeyEvent:KeyCode=" + event.getKeyCode());
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    //删除
                    if(nowString.length()>0){
                        nowString = nowString.substring(0,nowString.length()-1);
                    }
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    //回车
                    nowString = nowString+"\n"+inputString;
                }
            }
            postInvalidate();
            return true;
        }

        //当然删除的时候也会触发
        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            Log.d("tag", "deleteSurroundingText " + "beforeLength=" + beforeLength + " afterLength=" + afterLength);
            return true;
        }

        @Override
        public boolean finishComposingText() {
            //结束组合文本输入的时候
            Log.d("tag", "finishComposingText");

            return true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!TextUtils.isEmpty(nowString)) {
            canvas.drawText(nowString, startX, startY, paint);
        }

        canvas.drawLine(startX,startY+18,startX,startY-18,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            startX = event.getX();
            startY = event.getY();
            InputMethodManager m = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            Log.e("tag","**************************");
        }
        return super.onTouchEvent(event);
    }
}
