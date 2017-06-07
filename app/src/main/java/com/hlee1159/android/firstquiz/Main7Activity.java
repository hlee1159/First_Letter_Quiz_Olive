package com.hlee1159.android.firstquiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.tnkfactory.ad.TnkAdListener;
import com.tnkfactory.ad.TnkSession;

import java.util.ArrayList;
import java.util.HashSet;

public class Main7Activity extends GroundActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_page_intermediate);
        message3 = "달인 단계";
        colorid=getResources().getColor(R.color.color7);
        checkid=getResources().getIdentifier("check7", "drawable", getPackageName());
        edittextid=getResources().getIdentifier("edittext7", "drawable", getPackageName());
        hintboxid=getResources().getIdentifier("hintbox7", "drawable", getPackageName());
        borderid=getResources().getIdentifier("border7", "drawable", getPackageName());
        setStage();
        Toast.makeText(this, "달인 등급으로 승급하셨습니다!", Toast.LENGTH_LONG).show();
        init();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        loadBanner();
        loadInterstitial();
        loadCheatInterstitial();
        enableSkip();
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    public void init() {
        questions_list = new String[]{"ㅇㅁㄱㅂ", "ㅇㅁㅇ", "ㅎㄱㅅ", "ㅅㄱㅁㅈ", "ㅊㅇㄱ", "ㅇㅎㅇㅊ", "ㅁㅈㅇㄱㅈㅇ", "ㅍㅂㅇ", "ㅅㅂㅅ", "ㄷㅇㄹ"};
        answers_list = new String[]{"이목구비", "알맹이", "흑기사", "시간문제", "초읽기", "육하원칙", "미주알고주알", "판박이", "승부수", "덩어리"};
        hint1_list = new String[]{"인물", "핵심", "대신", "결정됨", "급박함", "6개","속속들이", "닮다", "결정적", "뭉치"};
        hint2_list = new String[]{"얼굴", "껍질", "술자리", "사실상", "바둑", "기자", "시시콜콜", "부자", "~ 두다", "심술~"};
        hint3_list = new String[]{"이목구비", "알맹이", "흑기사", "시간문제", "초읽기", "육하원칙", "미주알고주알", "판박이", "승부수", "덩어리"};
        hint4_list = new String[]{"", "", "", "", "", "", "", "", "", ""};
        questions = new ArrayList<String>();
        answers = new ArrayList<String>();
        hint1 = new ArrayList<String>();
        hint2 = new ArrayList<String>();
        hint3 = new ArrayList<String>();
        hint4 = new ArrayList<String>();
        answerList = new HashSet<String>();
        hintplusList = new ArrayList<String>();
        message1 = "축하합니다!"+"\n달인 단계를 통과하셨습니다.";
        message2 = "천재 단계에 도전!";
        stage="level7";
        answerAdId = "ca-app-pub-7941816792723862/4265748237";
        bannerAdId = "ca-app-pub-7941816792723862/8718522237";
        levelAdId = "ca-app-pub-7941816792723862/4383499435";

        //make array lists of all the answer list, hint plust list, questions and all the hints
        for (int index = 0; index < 10; index++) {
            questions.add(questions_list[index]);
            answers.add(answers_list[index]);
            hint1.add(hint1_list[index]);
            hint2.add(hint2_list[index]);
            hint3.add(hint3_list[index]);
            hint4.add(hint4_list[index]);
        }

        //set current question to be 0
        currentQuestion = 0;


        showQuestion();


        //When user presses enter in the keyboard, the app will perform checkAnswer
        answerText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(view);
                    checkAnswer();
                    handled = true;
                }
                return handled;
            }
        });


        //When user presses the answer button, check if the answer is right.
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }


        });

        //When user presses forward, move to next question
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }


        });

        //When user presses back, move to previous question
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pastQuestion();
            }
        });

        //When user presses hint button, show the additional hint
        hintplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additionalHint();
            }

        });


        //When the user swipes the screen left to right, move to next question or previous question.
        view.setOnTouchListener(new GroundActivity.OnSwipeTouchListener(Main7Activity.this) {

            @Override
            public void onSwipeLeft() {
                nextQuestion();
                hideKeyboard(view);
            }

            public void onSwipeRight() {
                pastQuestion();
                hideKeyboard(view);
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(view);
                return super.onTouch(v, event);
            }
        });

        //If user touches not-keyboard part of the screen, hide the keyboard
        answerText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    //This method starts the next level
    @Override
    public void startNextLevel() {
        Intent intent2 = new Intent(this, Main8Activity.class);
        startActivity(intent2);
    }

    //This method starts the previous level
    @Override
    public void startPreviousLevel() {
        Intent intent1 = new Intent(Main7Activity.this, Main6Activity.class);
        startActivity(intent1);
    }

    //This method enables skip button
    public void enableSkip(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String level7=preferences.getString("level7", DEFAULT);
        if (level7!=DEFAULT) {
            answersCorrect.setVisibility(View.GONE);
            answersCorrectImage.setVisibility(View.VISIBLE);
            //When user presses the fire button, move to next level.
            answersCorrectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    endOfTheLevel(message1, message2);
                }


            });
        }


    }

    //This method asks the user whether he or she will view the additional hint by watching a video ad
    @Override
    public void additionalHint() {


        showCheatInterstitial();
        cInterstitial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                hintplusview.setVisibility(View.GONE);
                hint3view.setVisibility(View.VISIBLE);
                hintplusList.add(0, questions.get(currentQuestion));
                answerText.setText(hint3.get(currentQuestion));
                loadCheatInterstitial();

            }
        });
        if (!cInterstitial.isLoaded()){
            hintplusview.setVisibility(View.GONE);
            hint3view.setVisibility(View.VISIBLE);
            hintplusList.add(0, questions.get(currentQuestion));
            answerText.setText(hint3.get(currentQuestion));
            loadCheatInterstitial();
        }
    }





}
