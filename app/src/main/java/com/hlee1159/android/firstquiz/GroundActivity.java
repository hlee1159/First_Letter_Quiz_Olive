package com.hlee1159.android.firstquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import java.util.ArrayList;
import java.util.HashSet;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.jirbo.adcolony.AdColony;
import com.jirbo.adcolony.AdColonyAdapter;
import com.jirbo.adcolony.AdColonyBundleBuilder;

import org.w3c.dom.Text;

/**
 * Created by Hyoung Jun on 2016-02-26.
 */
public class GroundActivity extends Activity {
    public int currentQuestion;
    public String message1;
    public String message2;
    public String stage;
    public ArrayList<String> questions;
    public ArrayList<String> answers;
    public ArrayList<String> hint1;
    public ArrayList<String> hint2;
    public ArrayList<String> hint3;
    public HashSet <String> answerList;
    public ArrayList<String> hintplusList;
    public TextView hint1View;
    public TextView hint2View;
    public TextView textBar1;
    public TextView textBar2;
    public TextView answersCorrect;
    public TextView level;
    public TextView level_bar1;
    public TextView level_bar2;
    public Button answerButton;
    public TextView questionView;
    public EditText answerText;
    public AdView mAdView;
    public RelativeLayout box;
    public Button back;
    public Button forward;
    public RelativeLayout view;
    public Button hintplus;
    public TextView hint3view;
    public RelativeLayout hintplusview;
    public RelativeLayout answersCorrectLayout;
    public ImageView answersCorrectImage;
    public Button answersCorrectButton;
    public String [] questions_list;
    public String [] answers_list;
    public String [] hint1_list;
    public String[] hint2_list;
    public String[] hint3_list;
    public InterstitialAd mInterstitial;
    public RelativeLayout wordbox;
    public ImageView symbol;
    public RelativeLayout hintWord;
    public RelativeLayout forwardLayout;
    public RelativeLayout backLayout;
    public TextView boxName;

    private static Typeface mTypeface = null;
    public static final String DEFAULT="N/A";

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(this.getAssets(), "NanumBarunpenB.ttf"); // 외부폰트 사용
            // mTypeface = Typeface.MONOSPACE; // 내장 폰트 사용
        }
        setGlobalFont(getWindow().getDecorView());
        // 또는
        // View view = findViewById(android.R.id.content);
        // setGlobalFont(view);
    }

    private void setGlobalFont(View view) {
        if (view != null) {
            if(view instanceof ViewGroup){
                ViewGroup vg = (ViewGroup)view;
                int vgCnt = vg.getChildCount();
                for(int i=0; i < vgCnt; i++){
                    View v = vg.getChildAt(i);
                    if(v instanceof TextView){
                        ((TextView) v).setTypeface(mTypeface);
                    }
                    setGlobalFont(v);
                }
            }
        }
    }

    public void loadBanner() {
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("53A5F3593D943AF2D44924D08C75E278").build();
        // Check the LogCat to get your test device ID
        mAdView.loadAd(adRequest);
    }
    public void loadInterstitial() {
        mInterstitial = new InterstitialAd(this);
        mInterstitial.setAdUnitId("ca-app-pub-7941816792723862/9247062233");
        AdRequest adrequest = new AdRequest.Builder().addTestDevice("53A5F3593D943AF2D44924D08C75E278").build();
        // Check the LogCat to get your test device ID
        mInterstitial.loadAd(adrequest);

    }
    public void showInterstitial() {
        if (mInterstitial.isLoaded()) {
            mInterstitial.show();
        }
    }

    //This method asks the user whether he or she will view the additional hint by watching a video ad
    public void additionalHint() {
        AlertDialog.Builder endLevel = new AlertDialog.Builder(this);
        endLevel.setCancelable(true);
        endLevel.setMessage("추가힌트를 사용하시겠습니까?");

        endLevel.setPositiveButton("사용하겠습니다", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hintplusview.setVisibility(View.GONE);
                hint3view.setVisibility(View.VISIBLE);
                hintplusList.add(0, questions.get(currentQuestion));
             //   boxName.setVisibility(View.INVISIBLE);
              //  reward.setVisibility(View.VISIBLE);

            }
        });
        endLevel.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = endLevel.create();
        alert.show();

    }


    //This method hides the keyboard
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    //This method manipulates the light bulb box so that it makes the right box visible at the right moment
    public void manipulateBox() {
        // if the user got the answer right, make the check box visible and everything else invisible
        if (answerList.contains(questions.get(currentQuestion))) {
            box.setVisibility(View.VISIBLE);
            hintplusview.setVisibility(View.GONE);
            hint3view.setVisibility(View.INVISIBLE);
           // reward.setVisibility(View.INVISIBLE);
           // boxName.setVisibility(View.VISIBLE);
        } else {
            // if the user used the additional hint, make the hint visible
            if (hintplusList.contains(questions.get(currentQuestion))) {
                box.setVisibility(View.INVISIBLE);
                hintplusview.setVisibility(View.GONE);
                hint3view.setVisibility(View.VISIBLE);
             //   reward.setVisibility(View.VISIBLE);
             //   boxName.setVisibility(View.INVISIBLE);
            }

            // if the user did not get the question right and did not view the addtional hint, just show the light bulb
            if (!hintplusList.contains(questions.get(currentQuestion))) {
                box.setVisibility(View.INVISIBLE);
                hintplusview.setVisibility(View.VISIBLE);
                hint3view.setVisibility(View.INVISIBLE);
              //  reward.setVisibility(View.INVISIBLE);
              //  boxName.setVisibility(View.VISIBLE);
            }
        }
    }

    //This method shows the question the user has to solve
    public void showQuestion() {
        questionView.setText(questions.get(currentQuestion));
        hint1View.setText(hint1.get(currentQuestion));
        hint2View.setText(hint2.get(currentQuestion));
        hint3view.setText(hint3.get(currentQuestion));
        answerText.setText("");
        manipulateBox();

    }

    //This method shows the next question
    public void nextQuestion() {
        currentQuestion++;

        if (currentQuestion == questions.size()) {
            Toast.makeText(this, "이 단계의 모든 문제를 풀어야 다음 단계로 넘어갈 수 있습니다.", Toast.LENGTH_SHORT).show();
            currentQuestion = currentQuestion - 1;
            return;
        }
        manipulateBox();
        showQuestion();
    }

    //This method shows the previous question
    public void pastQuestion() {
        if (currentQuestion == 0) {
            Toast.makeText(this, "이전 단계로 돌아가시려면 '뒤로' 버튼을 눌러주십시오.", Toast.LENGTH_SHORT).show();
            return;
        }
        currentQuestion = currentQuestion - 1;
        manipulateBox();
        showQuestion();
    }


    /**
     * Detects left and right swipes across a view.
     */
    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new GestureListener());
        }

        public void onSwipeLeft() {
        }

        public void onSwipeRight() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0)
                        onSwipeRight();
                    else
                        onSwipeLeft();
                    return true;
                }
                return false;
            }
        }
    }

    //This method returns true if the answer equals to correct answer
    public boolean isCorrect(String answer) {
        return (answer.equalsIgnoreCase(answers.get(currentQuestion)));
    }


    //This method stores the value "passed" to the shared preferences
    public void save(String string) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(string, "passed");
        editor.apply();
    }


    //This method checks the answer and updates the answer list
    public void checkAnswer() {
        String answer = answerText.getText().toString();
        if (isCorrect(answer)) {
            Toast.makeText(this, "정답입니다", Toast.LENGTH_SHORT).show();

            // if the answer is correct update the answer list
            answerList.add(questions.get(currentQuestion));

            //If the answer is correct, move the question to the very front and display the questions yet to be solved.

            questions.add(0, questions.get(currentQuestion));
            questions.remove(currentQuestion + 1);
            answers.add(0, answers.get(currentQuestion));
            answers.remove(currentQuestion + 1);
            hint1.add(0, hint1.get(currentQuestion));
            hint1.remove(currentQuestion + 1);
            hint2.add(0, hint2.get(currentQuestion));
            hint2.remove(currentQuestion + 1);
            hint3.add(0, hint3.get(currentQuestion));
            hint3.remove(currentQuestion + 1);

            //if the answer is correct update the number of questions correct
            if (answerList.size() < 10) {
                answersCorrect.setText(Integer.toString(answerList.size()));
            }

            //if all the answers are correct, end level
            if (answerList.size() >= 10) {
                answersCorrect.setText("");
                answersCorrectImage.setVisibility(View.VISIBLE);
                endOfTheLevel(message1, message2);
                return;
            }
            //if the user solves the last question first, show the previous question
            if (currentQuestion == 9)
                showQuestion();
            //if none of the above, show next question
            if (currentQuestion != 9)
                nextQuestion();
        } else
            Toast.makeText(this, "틀렸습니다.", Toast.LENGTH_SHORT).show();
    }

    //This method asks the user what to do when the level is ended
    public void endOfTheLevel(String string1, String string2) {
        AlertDialog.Builder endLevel = new AlertDialog.Builder(this);
        endLevel.setCancelable(false);
        endLevel.setMessage(string1);

        endLevel.setPositiveButton(string2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showInterstitial();
                answerText.setText("");
                save(stage);
                mInterstitial.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        startNextLevel();
                    }
                });
                if (!mInterstitial.isLoaded()){
                    startNextLevel();
                }


            }
        });
        endLevel.setNegativeButton("이 페이지에 머무르기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                showQuestion();
                dialog.cancel();
            }
        });
        AlertDialog alert = endLevel.create();
        alert.show();
    }
    //This method starts the next level
    public void startNextLevel() {
        Intent intent2 = new Intent(this, Main3Activity.class);
        startActivity(intent2);
    }


    //This method tells the user what to do when back button is pressed
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("게임을 종료하시겠습니까?");
        builder.setPositiveButton("이전 단계로 돌아가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                startPreviousLevel();

            }
        });
        builder.setNeutralButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    //This method starts previous level
    public void startPreviousLevel() {
        Intent intent1 = new Intent(this, MainActivity.class);
        startActivity(intent1);
    }





}
