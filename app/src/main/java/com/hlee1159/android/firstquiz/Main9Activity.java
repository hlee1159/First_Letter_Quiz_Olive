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

public class Main9Activity extends GroundActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_page_advanced);
        setStage();
        Toast.makeText(this, "초천재 등급으로 승급하셨습니다!", Toast.LENGTH_LONG).show();
        init();
        askForReview();
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
        questions_list = new String[]{"ㄱㅇㅂㅇㅂ", "ㅇㅅㅈㄱ", "ㅊㄱㅍ", "ㅂㅅㅂㅇ", "ㅅㅂㄹ", "ㅇㄹㅋ", "ㄴㄷㅇㄱ", "ㅇㅈㄹ", "ㅈㅈㄱ", "ㅂㅈㄱ"};
        answers_list = new String[]{"가위바위보", "이실직고", "책갈피", "반신반의", "심부름", "오랑캐", "늑대인간", "오지랖", "족집게", "보조개"};
        hint1_list = new String[]{"승부", "사실대로", "사이사이", "의심", "일", "야만","보름달", "쓸데없이", "과외", "볼"};
        hint2_list = new String[]{"", "", "", "", "","", "", "", "", ""};
        hint3_list = new String[]{"가위바위보", "이실직고", "책갈피", "반신반의", "심부름", "오랑캐", "늑대인간", "오지랖", "족집게", "보조개"};
        hint4_list = new String[]{"", "", "", "", "", "", "", "", "", ""};
        questions = new ArrayList<String>();
        answers = new ArrayList<String>();
        hint1 = new ArrayList<String>();
        hint2 = new ArrayList<String>();
        hint3 = new ArrayList<String>();
        hint4 = new ArrayList<String>();
        answerList = new HashSet<String>();
        hintplusList = new ArrayList<String>();
        message1 = new String ("축하합니다!"+"\n초천재 단계를 통과하셨습니다.");
        message2 = new String ("초인 단계에 도전!");
        stage=new String ("level9");

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
        view.setOnTouchListener(new GroundActivity.OnSwipeTouchListener(com.hlee1159.android.firstquiz.Main9Activity.this) {

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
        level.setTextColor(getResources().getColor(R.color.color9));
        level.setText("초천재 단계");

        answersCorrectLayout= (RelativeLayout) findViewById(R.id.answersCorrectLayout);
        answersCorrectLayout.setBackgroundResource(R.drawable.check9);

        answerText = (EditText) findViewById(R.id.AnswerText);
        answerText.setBackgroundResource(R.drawable.edittext9);

        questionView = (TextView) findViewById(R.id.QuestionTextView);

        wordbox = (RelativeLayout) findViewById(R.id.wordbox);
        wordbox.setBackgroundResource(R.drawable.hintbox9);

        hint1View = (TextView) findViewById(R.id.textView);
        textBar1 = (TextView) findViewById(R.id.textbar1);
        textBar1.setBackgroundResource(R.drawable.border9);

        hint2View = (TextView) findViewById(R.id.textView2);
        textBar2 = (TextView) findViewById(R.id.textbar2);
        textBar2.setBackgroundResource(R.drawable.border9);

        box = (RelativeLayout) findViewById(R.id.checkbox);
        box.setBackgroundResource(R.drawable.check9);

        hint3view = (TextView) findViewById(R.id.textView3);
        hint3view.setBackgroundResource(R.drawable.hintbox9);
        hint4view = (TextView) findViewById(R.id.textView4);
        textBar3 = (TextView) findViewById(R.id.textbar3);
        textBar3.setBackgroundResource(R.drawable.border1);

        hintplusview = (RelativeLayout) findViewById(R.id.hintplusview);
        hintplusview.setBackgroundResource(R.drawable.check9);

        answerButton = (Button) findViewById(R.id.AnswerButton);
        answerButton.setBackgroundResource(R.drawable.check9);

        forwardLayout=(RelativeLayout) findViewById(R.id.forwardLayout);
        forwardLayout.setBackgroundResource(R.drawable.check9);

        backLayout=(RelativeLayout) findViewById((R.id.backLayout));
        backLayout.setBackgroundResource(R.drawable.check9);

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
        Intent intent2 = new Intent(this, Main10Activity.class);
        startActivity(intent2);
    }

    //This method starts the previous level
    @Override
    public void startPreviousLevel() {
        Intent intent1 = new Intent(com.hlee1159.android.firstquiz.Main9Activity.this, Main8Activity.class);
        startActivity(intent1);
    }

    //This method enables skip button
    public void enableSkip(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String level9=preferences.getString("level9", DEFAULT);
        if (level9!=DEFAULT) {
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
