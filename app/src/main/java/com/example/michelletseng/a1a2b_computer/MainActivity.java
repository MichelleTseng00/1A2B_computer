package com.example.michelletseng.a1a2b_computer;

import android.content.Context;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.solver.widgets.ConstraintTableLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int[][] allNumMatrix = new int[5040][4];
    private int[] returnRandomNumber = new int[4];
    private int allNumber = 5040 , inputA = 0, inputB =0, runTime=1;
    private EditText editTextA, editTextB;
    private Button buttonOK, buttonPlayAgain;
    private ImageButton buttonInformation;
    private TextView textViewNum;
    private ConstraintLayout CLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find
        textViewNum = (TextView) findViewById(R.id.textView_num);
        editTextA = (EditText) findViewById(R.id.editTextInputA);
        editTextB = (EditText) findViewById(R.id.editTextInputB);
        buttonOK = (Button)findViewById(R.id.button);
        buttonPlayAgain = (Button)findViewById(R.id.buttonAgain);
        buttonInformation =(ImageButton)findViewById(R.id.imageButtonInformation);
        CLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        textViewNum.setMovementMethod(ScrollingMovementMethod.getInstance());

        editTextA.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        editTextB.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

        buttonPlayAgain.setOnClickListener(ButtonRightListner);
        buttonInformation.setOnClickListener(ButtonInformationListner);

        textViewNum.setText("Hello!");

        CLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the input method manager
                InputMethodManager inputMethodManager = (InputMethodManager)
                        view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                // Hide the soft keyboard
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
            }
        });

         //產生所有可能
        allNumMatrix = AllNumberCreate(allNumMatrix);
        //顯示初始四位數
        textViewNum.append("開始嘍!\n");
        returnRandomNumber = ShowRandomNumber(allNumMatrix, allNumber);

        buttonOK.setOnClickListener(ButtonOKListner);





    }

    // 產生亂數
    public int[][] AllNumberCreate (int[][] num_all){
        int num_1=0,num_10=0,num_100=0,num_1000=0,tamp=0;
        for(int i=123;i<9877;i++){
            num_1 = (i/1)%10;
            num_10 = (i/10)%10;
            num_100 = (i/100)%10;
            num_1000 = (i/1000)%10;
            if((num_1 != num_10) && (num_1 != num_100) && (num_1 != num_1000) && (num_10 != num_100) && (num_10 != num_1000) && (num_100 != num_1000)){

                num_all[tamp][0] = num_1;
                num_all[tamp][1] = num_10;
                num_all[tamp][2] = num_100;
                num_all[tamp][3] = num_1000;

                tamp++;
            }
        }

        return num_all;
    }

    //顯示初始四位數
    public int[] ShowRandomNumber(int[][] randNum ,int allNumber){
        Random ran = new Random();
        int  randomNum = 0;
        int[] returnNum = new int[4];
        String finalNum = "";
        randomNum = (int) ran.nextInt(allNumber);
        textViewNum.append("第"+runTime+"次:");
        for(int i =0;i<4;i++){
            returnNum[i] = randNum[randomNum][i];
            finalNum = Integer.toString(randNum[randomNum][i]);
            textViewNum.append(finalNum);
        }
        textViewNum.append("\t這個嗎?\n");
        //textViewNum.append(">.<\n");
        runTime++;

        if(allNumber == 1){
            textViewNum.append("只能是它了!!!");
            runTime = 1;
        }

        return returnNum;
    }

    public void CheckNumber() {
        int aa = 0, bb = 0;

        //比對所有數值的a b
        for (int i = 0; i < allNumber; i++) {
            aa = bb = 0;

            //藉由當前數值比對所有的數值
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    if (returnRandomNumber[j] == allNumMatrix[i][k]) {
                        if (j == k) {
                            aa++;
                        } else {
                            bb++;
                        }
                    }
                }
            }

            //檢查AB是否吻合，不吻合則進行覆蓋
            if (!((inputA == aa ) && (inputB == bb))) {
                for (int l = i; l<allNumber-1; l++) {
                    for (int h = 0; h < 4; h++) {
                        allNumMatrix[l][h] = allNumMatrix[l + 1][h];
                    }
                }
                allNumber--; //往上覆蓋
                i--; //因為當前的數值往上，所以下一個數要減一
            }

        }
    }

    private Button.OnClickListener ButtonRightListner = new Button.OnClickListener(){
        @Override
        public void onClick(View v){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("提示").setMessage("找到了!!!")
                    .setPositiveButton("重新開始", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            runTime = 1;
                            textViewNum.setText("");
                            allNumber = 5040;
                            allNumMatrix = AllNumberCreate(allNumMatrix);
                            textViewNum.append("開始嘍!\n");
                            returnRandomNumber = ShowRandomNumber(allNumMatrix, allNumber);
                            buttonOK.setOnClickListener(ButtonOKListner);

                        }
                    }).show();
            return;
        }
    };

    private Button.OnClickListener ButtonInformationListner = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("提示").setMessage("請在心裏想一組四位數，其數字為0~9不重複，接著電腦會開始推測你心中的數字，請輸入A和B提示電腦\nA表示有您心中的數字且在對的位子上\nB表示有您心中的數字但不在它的位子上")
                    .setPositiveButton("了解!", null).show();
            return;
        }
    };

    private Button.OnClickListener ButtonOKListner = new
            Button.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if(editTextA.getText().toString().trim().equals("") || editTextB.getText().toString().trim().equals("")){
                        // 未輸入任何東西 或是 其中一個沒有輸入到
                        textViewNum.append("輸入未完成"+"\n");
                    }else{

                        int editTextViewInputA = Integer.parseInt(editTextA.getText().toString());
                        int editTextViewInputB = Integer.parseInt(editTextB.getText().toString());

                        //有輸入東西確認格式
                        if(editTextViewInputA+editTextViewInputB > 4){
                            //相加大於4
                            textViewNum.append("錯誤輸入: 不會大於四\n");
                            editTextA.setText("");
                            editTextB.setText("");
                        }else if(editTextViewInputA > 4 || editTextViewInputB > 4 ){
                            textViewNum.append("錯誤輸入: 其中一個大於四了\n");
                            editTextA.setText("");
                            editTextB.setText("");
                        }else if(editTextViewInputA==3 && editTextViewInputB==1){
                            textViewNum.append("嗯? 輸入哪裡怪怪的@@\n");
                            editTextA.setText("");
                            editTextB.setText("");
                        }else{

                            inputA = editTextViewInputA;
                            inputB = editTextViewInputB;

                            editTextA.setText("");
                            editTextB.setText("");

                            CheckNumber();
                            //textViewNum.append("\n");
                            returnRandomNumber = ShowRandomNumber(allNumMatrix, allNumber);
                            //textViewNum.append("0.0\n");
                            //runTime++;
                        }

                    }

                }

            };

}
