package tv.acfun.read.bases.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.bases.fragment.FragmentFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.listeners.OnTagClickListener;

import java.util.List;

import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.bases.activities.ComicActivity;
import tv.acfun.read.bases.activities.ContentActivity;
import tv.acfun.read.bases.activities.ProfileActivity;
import tv.acfun.read.bases.activities.QuoteActivity;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.beans.Conversion;
import tv.acfun.read.beans.FullConversion;
import tv.acfun.read.beans.Setting;
import tv.acfun.read.holders.FullConversionHolder;
import tv.acfun.read.listeners.OnCommentListener;
import tv.acfun.read.parsers.CommentListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class CommentFragment extends FragmentFramework {
    private CommentListHelper mCommentListHelper;
    private int mContentId;
    private int mMaxQuoteCount;
    private View.OnClickListener mOnAvatarClickListener;
    private OnCommentListener mOnCommentListener;
    private View.OnClickListener mOnQuoteClickListener;
    private View.OnClickListener mOnQuoteExpandClickListener;
    private OnTagClickListener mOnTagClickListener;
    private int mPageNo;
    private int mTextSize;

    public static CommentFragment create(int contentId, int pageNo) {
        CommentFragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();

        bundle.putInt("contentId", contentId);
        bundle.putInt("pageNo", pageNo);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void acquireArguments(Bundle bundle) {
        Setting setting = AcFunRead.getInstance().readSetting();

        mContentId = bundle.getInt("contentId");
        mPageNo = bundle.getInt("pageNo");

        mMaxQuoteCount = setting.getMaxQuoteCount();
        mTextSize = setting.getDefaultTextSize();
    }

    @Override
    public void attachCallbacks() {
    }

    @Override
    public void enquiryViews() {
        mCommentListHelper = new CommentListHelper(this);
        mCommentListHelper.setCanLoad(false);
        mCommentListHelper.setHasFixedSize(false);
        mCommentListHelper.attachAdapter();
    }

    @Override
    public void establishCallbacks() {
        mOnQuoteExpandClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullConversion fullConversion = mCommentListHelper.getItem((Integer) v.getTag());

                start(QuoteActivity.create(getActivity(), mContentId, mCommentListHelper.getCurrentPage(),
                        fullConversion.getConversion().getCid()));
            }
        };
        mOnTagClickListener = new OnTagClickListener() {
            @Override
            public void onTagClick(String tag, String link) {
                switch (tag) {
                    case "img":
                        start(ComicActivity.create(getActivity(), link));
                        break;
                    case "ac":
                        start(ContentActivity.create(getActivity(), Integer.valueOf(link)));
                        break;
                    case "at":
                        start(ProfileActivity.create(getActivity(), link));
                }
            }
        };
        mOnAvatarClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();

                start(ProfileActivity.create(activity, (Integer) v.getTag()), ActivityFramework.Transition.Enter_Right);
            }
        };
        mOnQuoteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int floorPosition;
                int quotePosition;
                Conversion conversion;

                if (mOnCommentListener != null) {
                    floorPosition = (Integer) v.getTag(R.id.comment_floor_position);
                    quotePosition = (Integer) v.getTag(R.id.comment_quote_position);
                    conversion = mCommentListHelper.getItem(floorPosition).getQuoteList().get(quotePosition);
                    mOnCommentListener.onCommentClick(conversion);
                }
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_comment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOnCommentListener = (OnCommentListener) getActivity();
    }

    @Override
    public void onDestroyView() {
        mCommentListHelper.destroy();
        super.onDestroyView();
    }

    @Override
    public void startAction() {
        mCommentListHelper.from(API.getContentComment(mContentId, 50, mPageNo));
    }

    private class CommentListHelper extends RecyclerFramework<FullConversion> {
        public CommentListHelper(IFramework framework) {
            super(framework);
        }

        @Override
        protected RecyclerHolder<FullConversion> createHolder(View convertView, int viewType) {
            return new FullConversionHolder(convertView, mTextSize, mMaxQuoteCount, mOnAvatarClickListener,
                    mOnQuoteExpandClickListener, mOnQuoteClickListener);
        }

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return inflater.inflate(R.layout.item_fullconversion, parent, false);
        }

        @Override
        protected void onHandleMessage(Message message) {
            if (mOnCommentListener != null) {
                mOnCommentListener.onTotalPageChanged(message.what);
            }
        }

        @Override
        public void onItemClick(int position, FullConversion fullConversion) {
            Conversion conversion;

            if (mOnCommentListener != null) {
                conversion = fullConversion.getConversion();
                mOnCommentListener.onCommentClick(conversion);
            }
        }

        @Override
        public List<FullConversion> onParse(String json) {
            CommentListParser parser = CommentListParser.parse(json, mMaxQuoteCount, 0, mOnTagClickListener);

            if (parser != null) {
                postMessage(parser.getTotalPage());

                return parser.getItemList();
            } else {
                return null;
            }
        }
    }
}