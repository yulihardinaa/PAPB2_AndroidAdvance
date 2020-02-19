package com.example.android.fragmentexample;


import android.content.Context;
import android.graphics.RadialGradient;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.security.PublicKey;


/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleFragment extends Fragment {
    private	static	final	int	YES	=	0;
    private	static	final	int	NO	=	1;
    private static  final String CHOICE="choice";
    private static final String RATE="rate";
    private int mRadioButtonChoice=2;
    private float mRate=0;
    private onFragmentInteractionListener mListener;

    public interface onFragmentInteractionListener{
        void onRadioButtonChoice(int choice);
        void onRate(float rate);
    }


    public void  onAttach(Context context){
        super.onAttach(context);
        mListener=(onFragmentInteractionListener)context;
    }
    public void onDetach(){
        super.onDetach();
        mListener=null;
    }
    public static  SimpleFragment newInstance(int choice, float rate){
        SimpleFragment fragment=new SimpleFragment();
        Bundle args=new Bundle();
        args.putInt(CHOICE,choice);
        args.putFloat(RATE,rate);
        fragment.setArguments(args);
     return fragment;

    }

    public SimpleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_simple,container,false);
        final RadioGroup radioGroup=rootView.findViewById(R.id.radio_group);
        final TextView textView=rootView.findViewById(R.id.fragment_header);
        final RatingBar ratingBar=rootView.findViewById(R.id.ratingBar);
        if(getArguments().containsKey(RATE)){
            mRate=getArguments().getFloat(RATE);
            ratingBar.setRating(mRate);

        }
        if(getArguments().containsKey(CHOICE)){
            mRadioButtonChoice=getArguments().getInt(CHOICE);
            if(mRadioButtonChoice!=2){
                radioGroup.check(radioGroup.getChildAt(mRadioButtonChoice).getId());
            }

        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                View radioButton=radioGroup.findViewById(i);
                int index=radioGroup.indexOfChild(radioButton);
                switch (index){
                    case YES:
                        textView.setText(R.string.yes_message);
                        mRadioButtonChoice=0;
                        mListener.onRadioButtonChoice(mRadioButtonChoice);
                        break;
                    case NO:
                        textView.setText(R.string.no_message);
                        mRadioButtonChoice=1;
                        mListener.onRadioButtonChoice(mRadioButtonChoice);
                        break;
                        default:
                            mRadioButtonChoice=2;
                            mListener.onRadioButtonChoice(mRadioButtonChoice);
                            break;
                }
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                String myRating =(getString(R.string.my_rating)
                        + String.valueOf(ratingBar.getRating()));
                Toast.makeText(getContext(),myRating, Toast.LENGTH_SHORT).show();
                mRate=v;
                mListener.onRate(mRate);
            }
        });
        return rootView;
    }



}
