package com.logistics.activity;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import android.widget.Toast;
import android.content.Intent;
import com.logistics.R;

public class WelcomeActivity extends Activity implements OnGestureListener {
    private ViewFlipper viewFlipper;
    private GestureDetector detector; //手势检测

    int curIndex = 0;
    int maxIndex = 3;
    private SharedPreferences sharedPreferences;

    Animation leftInAnimation;
    Animation leftOutAnimation;
    Animation rightInAnimation;
    Animation rightOutAnimation;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);

        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        detector = new GestureDetector(this);
        sharedPreferences = this.getSharedPreferences("user_info",MODE_WORLD_READABLE);
        Boolean beIn = sharedPreferences.getBoolean("state", false);
        if(beIn){
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        }

        //往viewFlipper添加View
        viewFlipper.addView(getImageView(R.drawable.intro1));
        viewFlipper.addView(getImageView(R.drawable.intro2));
        viewFlipper.addView(getImageView(R.drawable.intro3));
        viewFlipper.addView(getImageView(R.drawable.intro4));


        //动画效果
        leftInAnimation = AnimationUtils.loadAnimation(this, R.anim.left_in);
        leftOutAnimation = AnimationUtils.loadAnimation(this, R.anim.left_out);
        rightInAnimation = AnimationUtils.loadAnimation(this, R.anim.right_in);
        rightOutAnimation = AnimationUtils.loadAnimation(this, R.anim.right_out);
    }

    private ImageView getImageView(int id){
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(id);
        return imageView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return this.detector.onTouchEvent(event); //touch事件交给手势处理。
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
       // Log.i(TAG, "e1="+e1.getX()+" e2="+e2.getX()+" e1-e2="+(e1.getX()-e2.getX()));

        if(e1.getX()-e2.getX()>150){
            if(curIndex>=0 && curIndex < maxIndex){
                viewFlipper.setInAnimation(leftInAnimation);
                viewFlipper.setOutAnimation(leftOutAnimation);
                viewFlipper.showNext();//向右滑动
                curIndex++;
            return true;
            }

            if(curIndex == maxIndex){
               // Toast.makeText(WelcomeActivity.this, "end", Toast.LENGTH_LONG).show();
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                WelcomeActivity.this.finish();
            }
        }else if(e1.getX()-e2.getY()<-150){
            if(curIndex>0 && curIndex < maxIndex){
            viewFlipper.setInAnimation(rightInAnimation);
            viewFlipper.setOutAnimation(rightOutAnimation);
            curIndex--;
            viewFlipper.showPrevious();//向左滑动
            return true;}

        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = this.getSharedPreferences("user_info",MODE_WORLD_READABLE);
        Boolean beIn = sharedPreferences.getBoolean("state", false);
        if(beIn){
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPreferences = this.getSharedPreferences("user_info",MODE_WORLD_READABLE);
        Boolean beIn = sharedPreferences.getBoolean("state", false);
        if(beIn){
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        }
    }
}
