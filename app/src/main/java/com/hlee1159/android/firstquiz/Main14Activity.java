package com.hlee1159.android.firstquiz;

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

public class Main14Activity extends GroundActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_page_advanced);
        message3 = "인공지능 단계";
        colorid=getResources().getColor(R.color.color14);
        checkid=getResources().getIdentifier("check14", "drawable", getPackageName());
        edittextid=getResources().getIdentifier("edittext14", "drawable", getPackageName());
        hintboxid=getResources().getIdentifier("hintbox14", "drawable", getPackageName());
        borderid=getResources().getIdentifier("border14", "drawable", getPackageName());
        setStage();
        Toast.makeText(this, "인공지능 등급으로 승급하셨습니다!", Toast.LENGTH_LONG).show();
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
        questions_list = new String[]{"ㅇㅂ", "ㄷㄷ", "ㅈㄱㅌ", "ㄷㅅㄴㅂ", "ㄴㅇㄱ", "ㅁㅇㅈㅁ", "ㅈㅈㅋ", "ㅂㅂㅂㅈ", "ㄱㅈ", "ㅅㅅㅁㅅ"};
        answers_list = new String[]{"완벽", "덕담", "저금통", "동서남북", "나잇값", "무용지물", "잠자코", "백발백중", "굴지", "세상만사"};
        hint1_list = new String[]{"흠이 없는", "새해", "돼지", "모든 방향", "어른됨", "쓸데없는","가만히", "명사수", "손꼽힘", "온갖 일"};
        hint2_list = new String[]{"", "", "", "", "","", "", "", "", ""};
        hint3_list = new String[]{"완벽", "덕담", "저금통", "동서남북", "나잇값", "무용지물", "잠자코", "백발백중", "굴지", "세상만사"};
        hint4_list = new String[]{"", "", "", "", "", "", "", "", "", ""};
        questions = new ArrayList<String>();
        answers = new ArrayList<String>();
        hint1 = new ArrayList<String>();
        hint2 = new ArrayList<String>();
        hint3 = new ArrayList<String>();
        hint4 = new ArrayList<String>();
        answerList = new HashSet<String>();
        hintplusList = new ArrayList<String>();
        message1 = "축하합니다!"+"\n인공지능 단계를 통과하셨습니다.";
        message2 = "초인공지능 단계에 도전!";
        stage= "level14";
        answerAdId = "ca-app-pub-7941816792723862/5602880633";
        bannerAdId = "ca-app-pub-7941816792723862/1451882639";
        levelAdId = "ca-app-pub-7941816792723862/7197365036";

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
        view.setOnTouchListener(new GroundActivity.OnSwipeTouchListener(com.hlee1159.android.firstquiz.Main14Activity.this) {

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
        Intent intent2 = new Intent(this, Main15Activity.class);
        startActivity(intent2);
    }

    //This method starts the previous level
    @Override
    public void startPreviousLevel() {
        Intent intent1 = new Intent(com.hlee1159.android.firstquiz.Main14Activity.this, Main13Activity.class);
        startActivity(intent1);
    }

    //This method enables skip button
    public void enableSkip(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String level14=preferences.getString("level14", DEFAULT);
        if (level14!=DEFAULT) {
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
