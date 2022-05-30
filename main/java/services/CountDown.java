package com.example.shahidhussain.assignemnt2.services;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class CountDown {

    private int minutes=0;
    private int seconds=0;
    private long inminute=0;//minute
    private long inseconds=0;//second

    public CountDown(long inminute, long inseconds,TextView view,int alertTime) {
        this.inminute = inminute;
        this.inseconds = inseconds;
        countDownTimer(getStartTimeinMills(),view,alertTime);
    }

    private long convertinMillis(long minute){
        long t = (inminute * 60) + inseconds;
        return TimeUnit.SECONDS.toMillis(t);
    }
    private long getStartTimeinMills(){
        return convertinMillis(inminute);
    }
    private CountDownTimer countDownTimer;
    private long mTimeLeftinMills=getStartTimeinMills();
    private void countDownTimer(long timeLeft, final TextView view, final int alertTime){
        new CountDownTimer(timeLeft, 1000) {

            public void onTick(long millisUntilFinished) {
                mTimeLeftinMills=millisUntilFinished;
                UpdateTime(view,alertTime);
            }

            public void onFinish() {

            }
        }.start();
    }
    private void UpdateTime(TextView countdown, int alertTime){
        minutes=(int) (mTimeLeftinMills/1000)/60;
        seconds=(int) (mTimeLeftinMills/1000)%60;
        String timeleft=String.format("%02d:%02d",minutes,seconds);
        if(alertTime!=0){
            if(minutes>alertTime) {
                countdown.setText(timeleft);
                countdown.setTextColor(Color.parseColor("#20c9bb"));
            }else{
                countdown.setText(timeleft);
                countdown.setTextColor(Color.parseColor("#e2142c"));
            }
        }else{
            countdown.setText(timeleft);
        }
    }
}
