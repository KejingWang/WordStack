/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.wordstack;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final int WORD_LENGTH = 5;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    private ArrayList<String> words = new ArrayList<>();
    private HashSet<String> wordSet = new HashSet<>();
    private Random random = new Random();
    private StackedLayout stackedLayout;
    private String word1, word2;
    private Stack<LetterTile> placedTiles = new Stack<>();

    private Stack<Character> stackWord1 = new Stack<>(); //keep track of the chars placed on row1
    private Stack<Character> stackWord2 = new Stack<>(); // ... row2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        try {

            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = in.readLine()) != null) {
                String word = line.trim();
                //my code
                if (word.length() == WORD_LENGTH){
                    words.add(word);
                    wordSet.add(word);
                }//
            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
        stackedLayout = new StackedLayout(this);
        verticalLayout.addView(stackedLayout, 3);

        View word1LinearLayout = findViewById(R.id.word1);
        //word1LinearLayout.setOnTouchListener(new TouchListener());
        word1LinearLayout.setOnDragListener(new DragListener());
        View word2LinearLayout = findViewById(R.id.word2);
        //word2LinearLayout.setOnTouchListener(new TouchListener());
        word2LinearLayout.setOnDragListener(new DragListener());
    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
                LetterTile tile = (LetterTile) stackedLayout.peek();
                tile.moveToViewGroup((ViewGroup) v);
                if (stackedLayout.empty()) {
                    TextView messageBox = (TextView) findViewById(R.id.message_box);
                    messageBox.setText(word1 + " " + word2);
                }
                //my code
                placedTiles.push(tile);
                //extension1
                if(v.equals(findViewById(R.id.word1))){
                    stackWord1.push(tile.getLetter());
                } else if(v.equals(findViewById((R.id.word2)))) {
                    stackWord2.push(tile.getLetter());
                }
                //
                return true;
            }
            return false;
        }
    }

    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.WHITE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign Tile to the target Layout
                    LetterTile tile = (LetterTile) event.getLocalState();
                    tile.moveToViewGroup((ViewGroup) v);

                    //my code
                    placedTiles.push(tile);

                    //extension1
                    if(v.equals(findViewById(R.id.word1))){
                        stackWord1.push(tile.getLetter());
                    } else if(v.equals(findViewById((R.id.word2)))) {
                        stackWord2.push(tile.getLetter());
                    }

                    if (stackedLayout.empty()) {
                        TextView messageBox = (TextView) findViewById(R.id.message_box);
                        messageBox.setText(word1 + " " + word2);
                        //my code
                        String string1 = stackWord1.toArray().toString();
                        System.out.println(string1);
                        String string2 = stackWord2.toArray().toString();
                        System.out.println(string2);
                        if(wordSet.contains(string1) && wordSet.contains(string2)){
                            String resp = "Your answer is also right: " + string1 + " " + string2;
                            ((TextView) findViewById(R.id.message_box2)).setText(resp);
                            System.out.println(resp);
                        }
                    }

                    return true;
            }
            return false;
        }
    }

    public boolean onStartGame(View view) {
        TextView messageBox = (TextView) findViewById(R.id.message_box);
        messageBox.setText("Game started");
        //my code
        //clean up residues of last round
        stackedLayout.clear();
        LinearLayout word1LinearLayout = findViewById(R.id.word1); //linearLayOut is a subclass of ViewGroup
        word1LinearLayout.removeAllViews();
        LinearLayout word2LinearLayout = findViewById(R.id.word2);
        word2LinearLayout.removeAllViews();
        //scrambling algo
        int random1 = random.nextInt(words.size());
        int random2 = random.nextInt(words.size());
        //word1 = words.get(random1);
        //word2 = words.get(random2);
        word1 = "dates";
        word2 = "loved";
        int i = 0; //counter for word1
        int j = 0; //counter for word2
        String scramble = "";
        while(i<WORD_LENGTH && j<WORD_LENGTH){
            int choice = random.nextInt(2);
            if(choice == 0){
                scramble = scramble + word1.charAt(i);
                i++;
            } else {
                scramble = scramble + word2.charAt(j);
                j++;
            }
        }
        while(i<WORD_LENGTH){
            scramble = scramble + word1.charAt(i);
            i++;
        }
        while(j<WORD_LENGTH){
            scramble = scramble + word2.charAt(j);
            j++;
        }
        messageBox.setText(scramble);
        for(int k=scramble.length()-1; k>= 0; k--){
            LetterTile tile = new LetterTile(this, scramble.charAt(k));
            stackedLayout.push(tile);
        }
        return true;
    }

    public boolean onUndo(View view) {
        //my code
        if(placedTiles.size()!= 0){
            LetterTile mostRecentTile = placedTiles.pop();
            if(view.getParent().equals(findViewById(R.id.word1))){
                stackWord1.pop();
            } else if(view.getParent().equals(findViewById(R.id.word2))) {
                stackWord2.pop();
            }
            mostRecentTile.moveToViewGroup(stackedLayout);
        }
        //
        return true;
    }
}
