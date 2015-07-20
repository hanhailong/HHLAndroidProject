package com.hhl.hhlandroidproject.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.hhl.hhlandroidproject.R;
import com.hhl.hhlandroidproject.ui.base.BaseSwipeBackActivity;
import com.hhl.hhlandroidproject.ui.view.FlipCardView;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;

public class FlipCardActivity extends BaseSwipeBackActivity {

    @Bind(R.id.flip_cardview)
    FlipCardView mFlipCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_card);


        init();
    }

    private void init() {
        CircleImageView aboveIv = new CircleImageView(this);
        aboveIv.setImageResource(R.drawable.test);
        mFlipCardView.setAboveView(aboveIv);

        CircleImageView behindIv = new CircleImageView(this);
        behindIv.setBackgroundResource(R.drawable.circle_shape);
        behindIv.setImageResource(R.mipmap.ic_launcher);
        mFlipCardView.setBehindView(behindIv);
    }

    public void flipView(View v){
        mFlipCardView.startFlipCard();
    }
}
