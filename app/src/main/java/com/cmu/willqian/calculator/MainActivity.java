package com.cmu.willqian.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    /**
     * IDs of all the numeric buttons
     */
    private int[] numBtn = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
    /**
     * IDs of all the operator buttons
     */
    private int[] opBtn = {R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide, R.id.btnMod, R.id.btnExponentation};
    /**
     * TextView used to display the input and results on the screen
     */
    private TextView txtOnScreen;
    /**
     * True if the number already has a dot
     */
    private boolean hasDot = false;
    /**
     * True if the input state has an error
     */
    private boolean stateError = false;
    /**
     * True if the last input is a number
     */
    private boolean lastNumeric = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtOnScreen = (TextView) findViewById(R.id.txt);
        setNumBtnClickListener();
        setOpBtnClickListener();
    }

    /**
     * Set the click listeners to each numeric button
     */
    private void setNumBtnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the button view
                Button button = (Button) v;
                //If last input results an error, we will just clear it and set new input text
                if(stateError) {
                    txtOnScreen.setText(button.getText());
                    stateError = false;//Now the input has no error
                } else {
                    //Otherwise, we will just append the new digit to the screen text
                    txtOnScreen.append(button.getText());
                }
                lastNumeric = true;// The last input is a number
            }
        };
        for(int id : numBtn) {
            //Set the click listener for each button
            findViewById(id).setOnClickListener(listener);
        }
    }

    private  void setOpBtnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                //If the last input is number and there is no state error, we will just append the operator to the statement
                if(lastNumeric && !stateError) {
                    txtOnScreen.append(button.getText());
                    lastNumeric = false;// Last input is operator
                    hasDot = false;//The next number does have the dot yet, so it is false
                }
            }
        };
        for(int id : opBtn) {
            //Set the click listener for each button
            findViewById(id).setOnClickListener(listener);
        }

        findViewById(R.id.btnC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Reset all variables and clear the screen
                hasDot = false;
                stateError = false;
                lastNumeric = false;
                txtOnScreen.setText("");
            }
        });

        findViewById(R.id.btnDot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                //If the last input is number and there is no state error, and there is no dot yet in the number
                // we will just append the dot to the statement
                if(lastNumeric && !stateError && !hasDot) {
                    txtOnScreen.append(button.getText());
                    lastNumeric = false;//Last input is dot
                    hasDot = true;//Now it has dot and it can not accept another dot for this number
                }
            }
        });

        findViewById(R.id.btnEqual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                //If the last input is number and there is no state error, we will calculate the input
                if(lastNumeric && !stateError) {
                    //Get the text from the screen and use the Expression from library exp4j to do the calculation
                    String txt = txtOnScreen.getText().toString();
                    Expression exp = new ExpressionBuilder(txt).build();
                    try {
                        //Get the result
                        double result = exp.evaluate();
                        //Set the output format to two digits after decimal point
                        DecimalFormat df = new DecimalFormat("###.00");
                        lastNumeric = true;//The output is a number, so the lastNumeric is true
                        stateError = false;//We get the result, so there is no error
                        txtOnScreen.setText(df.format(result));//Output the result to the screen
                        hasDot = true;//Set the output always to have decimal point, so the hasDot is true
                    } catch (ArithmeticException e) {//If the input can cause an arithmetic exception, it will be caught here
                        txtOnScreen.setText("Error");//Set the error text
                        lastNumeric = false;//We can start to input number now
                        stateError = true;//Set to true since there is an error
                    }
                }
            }
        });

    }
}
