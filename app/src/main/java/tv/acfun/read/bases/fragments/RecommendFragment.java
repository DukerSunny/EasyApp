package tv.acfun.read.bases.fragments;

import android.os.Bundle;
import android.view.View;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.fragment.FragmentFramework;
import com.harreke.easyapp.frameworks.list.abslistview.AbsListFramework;

import java.util.ArrayList;

import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.bases.activities.ContentActivity;
import tv.acfun.read.beans.Content;
import tv.acfun.read.holders.RecommendHolder;
import tv.acfun.read.parsers.ChannelListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/23
 */
public class RecommendFragment extends FragmentFramework {
    private Helper mHelper;

    @Override
    public void assignEvents() {
    }

    @Override
    public void initData(Bundle bundle) {
    }

    @Override
    public void newEvents() {
    }

    @Override
    public void queryLayout() {
        mHelper = new Helper(this, R.id.selection_list);
        mHelper.bindAdapter();
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.fragment_recommend);
    }

    @Override
    public void startAction() {
        mHelper.from(API.getChannelRecommend("110,73,74,75", 10));
    }

    private class Helper extends AbsListFramework<Content, RecommendHolder> {
        public Helper(IFramework framework, int listId) {
            super(framework, listId);
        }

        @Override
        public RecommendHolder createHolder(View convertView) {
            return new RecommendHolder(convertView);
        }

        @Override
        public View createView() {
            return View.inflate(getActivity(), R.layout.item_recommend, null);
        }

        @Override
        public void onAction() {
            startAction();
        }

        @Override
        public void onItemClick(int position, Content content) {
            start(ContentActivity.create(getActivity(), content.getContentId()));
        }

        @Override
        public ArrayList<Content> onParse(String json) {
            ChannelListParser parser = ChannelListParser.parse(json);

            if (parser != null) {
                return parser.getItemList();
            } else {
                return null;
            }
        }

        //        @Override
        //        public void onPostAction() {
        //            super.onPostAction();
        //            long millisecond = System.currentTimeMillis();
        //            int hour;
        //            String time;
        //
        //            mDate.setTime(millisecond);
        //            selection_date.setText(mDateFormat.format(mDate));
        //            selection_date_stroke.setText(mDateFormat.format(mDate));
        //            hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        //            Log.e(null, "hour=" + hour);
        //            if (hour >= 3 && hour < 6) {
        //                time = header_selection_time_early;
        //            } else if (hour >= 6 && hour < 12) {
        //                time = header_selection_time_morning;
        //            } else if (hour >= 12 && hour < 15) {
        //                time = header_selection_time_noon;
        //            } else if (hour >= 15 && hour < 18) {
        //                time = header_selection_time_afternoon;
        //            } else if (hour >= 18 && hour < 21) {
        //                time = header_selection_time_nightfall;
        //            } else if (hour >= 21 && hour < 24) {
        //                time = header_selection_time_evening;
        //            } else {
        //                time = header_selection_time_night;
        //            }
        //            selection_time.setText(mTimeFormat.format(mDate) + " " + time);
        //            selection_time_stroke.setText(mTimeFormat.format(mDate) + " " + time);
        //        }

        @Override
        public int parseItemId(Content content) {
            return content.getContentId();
        }
    }
}