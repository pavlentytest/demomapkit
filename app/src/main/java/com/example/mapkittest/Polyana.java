package com.example.mapkittest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class Polyana extends Activity {

    private final static int[] blocks = {
            R.id.blockone,
            R.id.blocktwo,
            R.id.blockthree,
            R.id.blockfour,
            R.id.blockone2
    };

    private final static int[] buttons = {
            R.id.b1,
            R.id.b2,
            R.id.b3,
            R.id.b4
    };

    private int currentButton = 1;

    private final static int BUTTONS_AMOUNT = 4,
            BLOCKS_AMOUNT = 4;

    Button sarrow, farrow, sarrow2, farrow2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polyana);
        proceedButtons();
    }

    private void proceedButtons() {
        sarrow = findViewById(R.id.sarrow);
        sarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVisibility(1,4);
                changeButtonsVisibility(1, 4);
                currentButton = 1;
            }
        });
        sarrow2 = findViewById(R.id.sarrow2);
        sarrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentButton > 2)
                    changeButtonsVisibility(currentButton-2, currentButton-3+ BUTTONS_AMOUNT);
                else if (currentButton == 2)
                    changeButtonsVisibility(1,4);
                if (currentButton>=2) {
                    changeVisibility((currentButton-2)*BLOCKS_AMOUNT+1, (currentButton-1)*BLOCKS_AMOUNT);
                    currentButton-=1;
                }
            }
        });
        farrow = findViewById(R.id.farrow);
        farrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVisibility(blocks.length - BLOCKS_AMOUNT + 1, blocks.length);
                changeButtonsVisibility(buttons.length - BUTTONS_AMOUNT + 1, buttons.length);
                currentButton = buttons.length;
            }
        });
        farrow2 = findViewById(R.id.farrow2);
        farrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentButton < buttons.length)
                    changeButtonsVisibility(currentButton-2, currentButton-3+ BUTTONS_AMOUNT);
                else if (currentButton == buttons.length)
                    changeButtonsVisibility(buttons.length - BUTTONS_AMOUNT + 1,buttons.length);
                if (currentButton>=2) {
                    changeVisibility(currentButton*BLOCKS_AMOUNT + 1, (currentButton+1)*BLOCKS_AMOUNT);
                    currentButton+=1;
                }
            }
        });
        for (int i = 0; i < buttons.length; i++) {
            int currIndex = i;
            Button btn = findViewById(buttons[i]);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeVisibility(currIndex+1, currIndex+BLOCKS_AMOUNT);
                    changeButtonsVisibility(currIndex+1, currIndex+ BUTTONS_AMOUNT);
                }
            });
        }
    }

    private void changeVisibility(int s, int f) {
        for (int i = 0; i < s-1; i++) {
            View v = findViewById(blocks[i]);
            v.setVisibility(View.GONE);
        }
        for (int i = s-1; i < f; i++) {
            View v = findViewById(blocks[i]);
            v.setVisibility(View.VISIBLE);
        }
        for (int i = f; i < blocks.length; i++) {
            View v = findViewById(blocks[i]);
            v.setVisibility(View.GONE);
        }
    }

    private void changeButtonsVisibility(int s, int f) {
        for (int i = 0; i < s-1; i++) {
            Button b = findViewById(buttons[i]);
            b.setVisibility(Button.GONE);
        }
        for (int i = s-1; i < f; i++) {
            Button b = findViewById(buttons[i]);
            b.setVisibility(View.VISIBLE);
        }
        for (int i = f; i < blocks.length; i++) {
            Button b = findViewById(buttons[i]);
            b.setVisibility(Button.GONE);
        }
    }
}
