package tv.acfun.read.bases.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.bases.fragment.FragmentFramework;

import tv.acfun.read.R;
import tv.acfun.read.holders.EmotHolder;
import tv.acfun.read.listeners.OnEmotClickListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/22
 */
public class EmotFragment extends FragmentFramework {
    private RecyclerView emot_recycler;
    private EmotAdapter mEmotAdapter;
    private int mEmotCount;
    private String mEmotId;
    private String mEmotName;
    private OnEmotClickListener mOnEmotClickListener = null;
    private View.OnClickListener mOnItemClickListener;

    public static EmotFragment create(String emotName, String emotId, int emotCount) {
        EmotFragment fragment = new EmotFragment();
        Bundle bundle = new Bundle();

        bundle.putString("emotName", emotName);
        bundle.putString("emotId", emotId);
        bundle.putInt("emotCount", emotCount);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected void acquireArguments(Bundle bundle) {
        mEmotName = bundle.getString("emotName");
        mEmotId = bundle.getString("emotId");
        mEmotCount = bundle.getInt("emotCount");
    }

    @Override
    public void attachCallbacks() {
    }

    @Override
    public void enquiryViews() {
        emot_recycler = (RecyclerView) findViewById(R.id.emot_recycler);
        emot_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 5));

        mEmotAdapter = new EmotAdapter();
    }

    @Override
    public void establishCallbacks() {
        mOnItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnEmotClickListener != null) {
                    mOnEmotClickListener.onEmotClick(mEmotName, mEmotId, emot_recycler.getChildPosition(v));
                }
            }
        };
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOnEmotClickListener = (OnEmotClickListener) activity;
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.fragment_emot);
    }

    @Override
    public void startAction() {
        emot_recycler.setAdapter(mEmotAdapter);
        mEmotAdapter.notifyDataSetChanged();
    }

    private class EmotAdapter extends RecyclerView.Adapter<EmotHolder> {
        @Override
        public int getItemCount() {
            return mEmotCount;
        }

        @Override
        public void onBindViewHolder(EmotHolder holder, int position) {
            holder.setItem(mEmotId);
        }

        @Override
        public EmotHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_emot, parent, false);
            EmotHolder emotHolder = new EmotHolder(itemView);

            emotHolder.setOnClickListener(mOnItemClickListener);

            return emotHolder;
        }
    }
}