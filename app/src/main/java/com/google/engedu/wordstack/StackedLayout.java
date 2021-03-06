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
import android.view.View;
import android.widget.LinearLayout;

import java.util.Stack;

public class StackedLayout extends LinearLayout {

    private Stack<View> tiles = new Stack();

    public StackedLayout(Context context) {
        super(context);
    } //create a linearlayout below 2 linearlayout????

    public void push(View tile) {
        //my code
        if(tiles.size() != 0) {
            removeView(tiles.peek()); //use default mothod??? (since it's a linearLayOut)
        }
        tiles.push(tile);
        addView(tile);
    }

    public View pop() {
        View popped = null;
        //my code
        if(tiles.size()!=0){
            popped = tiles.pop();
            removeView(popped);
            if(tiles.size()!=0) addView(tiles.peek());
        }
        return popped;
    }

    public View peek() {
        return tiles.peek();
    }

    public boolean empty() {
        return tiles.empty();
    }

    public void clear() {
        while(tiles.size()!=0){
            removeView(tiles.pop());
        }
    }
}
