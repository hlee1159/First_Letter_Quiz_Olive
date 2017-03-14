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

import java.util.ArrayList;
import java.util.HashSet;

public class Main7Activity extends GroundActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_page_advanced);
        setStage();
        Toast.makeText(this, "달인 등급으로 승급하셨습니다!", Toast.LENGTH_LONG).show();
        init();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        loadBanner();
        loadInterstitial();
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
        questions_list = new String[]{"ㄷㅈ", "ㅇㅁㅇ", "ㅎㄱㅅ", "ㅅㄱㅁㅈ", "ㅊㅇㄱ", "ㅇㅎㅇㅊ", "ㅁㄱㅅ", "ㅍㅂㅇ", "ㅅㅂㅅ", "ㄷㅇㄹ"};
        answers_list = new String[]{"뒷짐", "알맹이", "흑기사", "시간문제", "초읽기", "육하원칙", "물귀신", "판박이", "승부수", "덩어리"};
        hint1_list = new String[]{"구경", "핵심", "대신", "결정됨", "급박함", "6개","공멸", "닮다", "결정적", "뭉치"};
        hint2_list = new String[]{"", "", "", "", "","", "", "", "", ""};
        hint3_list = new String[]{"~지다", "껍질", "술자리", "사실상", "바둑", "기자", "~작전", "부자", "~ 두다", "심술~"};
        questions = new ArrayList<String>();
        answers = new ArrayList<String>();
        hint1 = new ArrayList<String>();
        hint2 = new ArrayList<String>();
        hint3 = new ArrayList<String>();
        answerList = new HashSet<String>();
        hintplusList = new ArrayList<String>();
        message1 = new String ("축하합니다!"+"\n달인 단계를 통과하셨습니다.");
        message2 = new String ("초인 단계에 도전!");
        stage=new String ("level7");

        //make array lists of all the answer list, hint plust list, questions and all the hints
        for (int index = 0; index < 10; index++) {
            questions.add(questions_list[index]);
            answers.add(answers_list[index]);
            hint1.add(hint1_list[index]);
            hint2.add(hint2_list[index]);
            hint3.add(hint3_list[index]);
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

    public void setStage() {

        level=(TextView) findViewById(R.id.level);
        level.setTextColor(getResources().getColor(R.color.color7));
        level.setText("달인 단계");

        answersCorrectLayout= (RelativeLayout) findViewById(R.id.answersCorrectLayout);
        answersCorrectLayout.setBackgroundResource(R.drawable.check7);

        answerText = (EditText) findViewById(R.id.AnswerText);
        answerText.setBackgroundResource(R.drawable.edittext7);

        questionView = (TextView) findViewById(R.id.QuestionTextView);

        wordbox = (RelativeLayout) findViewById(R.id.wordbox);
        wordbox.setBackgroundResource(R.drawable.hintbox7);

        hint1View = (TextView) findViewById(R.id.textView);
        textBar1 = (TextView) findViewById(R.id.textbar1);
        textBar1.setBackgroundResource(R.drawable.border7);

        hint2View = (TextView) findViewById(R.id.textView2);
        textBar2 = (TextView) findViewById(R.id.textbar2);
        textBar2.setBackgroundResource(R.drawable.border4);

        box = (RelativeLayout) findViewById(R.id.checkbox);
        box.setBackgroundResource(R.drawable.check7);

        hint3view = (TextView) findViewById(R.id.textView3);
        hint3view.setBackgroundResource(R.drawable.hintbox7);

        hintplusview = (RelativeLayout) findViewById(R.id.hintplusview);
        hintplusview.setBackgroundResource(R.drawable.check7);

        answerButton = (Button) findViewById(R.id.AnswerButton);
        answerButton.setBackgroundResource(R.drawable.check7);

        forwardLayout=(RelativeLayout) findViewById(R.id.forwardLayout);
        forwardLayout.setBackgroundResource(R.drawable.check7);

        backLayout=(RelativeLayout) findViewById((R.id.backLayout));
        backLayout.setBackgroundResource(R.drawable.check7);

        boxName=(TextView) findViewById(R.id.boxName);

        answersCorrect = (TextView) findViewById(R.id.answersCorrect);
        answersCorrectImage = (ImageView) findViewById(R.id.answersCorrectImage);
        answersCorrectButton = (Button) findViewById(R.id.answersCorrectButton);
        hintWord= (RelativeLayout) findViewById(R.id.hintWord);
        back = (Button) findViewById(R.id.back);
        view = (RelativeLayout) findViewById(R.id.view);
        forward = (Button) findViewById(R.id.forward);
        hintplus = (Button) findViewById(R.id.hintplus);
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

}
