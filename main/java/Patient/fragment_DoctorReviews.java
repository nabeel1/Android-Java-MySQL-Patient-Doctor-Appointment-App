package com.example.shahidhussain.assignemnt2.Patient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.shahidhussain.assignemnt2.R;

public class fragment_DoctorReviews extends Fragment {

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_fragment__doctor_reviews, container, false);
        ListView commentlist=view.findViewById(R.id.commentsListView);
        commentlist.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        commentCustomAdaptor commentadapter=new commentCustomAdaptor();
        commentlist.setAdapter(commentadapter);
        return view;
    }

    public class commentCustomAdaptor extends BaseAdapter {

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view1, ViewGroup viewGroup) {
            view1=View.inflate(getContext(),R.layout.customercommentslist,null);
            TextView name= view1.findViewById(R.id.commentusername);
            TextView usercomment=view1.findViewById(R.id.usercomment);
            RatingBar rate= view1.findViewById(R.id.userratingBar);
            return view1;
        }
    }
}
