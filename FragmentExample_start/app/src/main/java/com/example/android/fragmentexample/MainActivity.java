/*
 * Copyright (C) 2017 Google Inc.
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

package com.example.android.fragmentexample;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements SimpleFragment.onFragmentInteractionListener{
    private Button mButton;
    private boolean isFragmentDisplayed=false;
    private int mRadioButtonChoice=2;
    private float mRate=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton=findViewById(R.id.open_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFragmentDisplayed){
                    displayFragment();
                }else{
                    closeFragment();
                }
            }
        });

    }
    private void  displayFragment(){
        SimpleFragment simpleFragment=SimpleFragment.newInstance(mRadioButtonChoice,mRate);
        android.support.v4.app.FragmentTransaction fragmentTransaction
                = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,simpleFragment)
                .addToBackStack(null).commit();
        isFragmentDisplayed=true;
        mButton.setText("Close");
    }

    private  void  closeFragment(){
        FragmentManager fragmentManager=getSupportFragmentManager();
        SimpleFragment simpleFragment=(SimpleFragment)
                fragmentManager.findFragmentById(R.id.fragment_container);
        if(simpleFragment!=null){
            FragmentTransaction fragmentTransaction
                    = fragmentManager.beginTransaction();
            fragmentTransaction.remove(simpleFragment).commit();
        }
        mButton.setText("Open");
        isFragmentDisplayed=false;
    }

    @Override
    public void onRadioButtonChoice(int choice) {
        mRadioButtonChoice=choice;
    }

    @Override
    public void onRate(float rate) {
        mRate=rate;
    }
}
