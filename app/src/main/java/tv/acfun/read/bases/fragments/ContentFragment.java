package tv.acfun.read.bases.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.fragment.FragmentFramework;
import com.harreke.easyapp.frameworks.list.abslistview.AbsListFramework;
import com.harreke.easyapp.tools.GsonUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tv.acfun.read.R;
import tv.acfun.read.beans.ArticleContent;
import tv.acfun.read.beans.Content;
import tv.acfun.read.holders.ArticleContentHolder;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/25
 */
public class ContentFragment extends FragmentFramework {
    private Content mContent;
    private Helper mHelper;
    private ArrayList<ArticleContent> mPage;

    public static ContentFragment create(Content content, ArrayList<ArticleContent> page) {
        ContentFragment fragment = new ContentFragment();
        Bundle bundle = new Bundle();

        bundle.putString("content", GsonUtil.toString(content));
        bundle.putString("page", GsonUtil.toString(page));
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void assignEvents() {
    }

    @Override
    public void initData(Bundle bundle) {
        mContent = GsonUtil.toBean(bundle.getString("content"), Content.class);
        mPage = GsonUtil.toBean(bundle.getString("page"), new TypeToken<ArrayList<ArticleContent>>() {
        }.getType());
    }

    @Override
    public void newEvents() {
    }

    @Override
    public void queryLayout() {
        View header_content = View.inflate(getActivity(), R.layout.header_content, null);
        TextView content_title = (TextView) header_content.findViewById(R.id.content_title);
        TextView content_user_name = (TextView) header_content.findViewById(R.id.content_user_name);
        TextView content_date = (TextView) header_content.findViewById(R.id.content_date);
        TextView content_info = (TextView) header_content.findViewById(R.id.content_info);

        content_title.setText(mContent.getTitle());
        content_user_name.setText(mContent.getUser().getUsername());
        content_date.setText(new SimpleDateFormat(getString(R.string.content_date)).format(new Date(mContent.getReleaseDate())));
        content_info.setText(getString(R.string.content_info, mContent.getComments(), mContent.getViews()));

        mHelper = new Helper(this, R.id.content_list);
        mHelper.addHeaderView(header_content);
        mHelper.bindAdapter();
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.fragment_content);
    }

    @Override
    public void startAction() {
        mHelper.from(mPage);
    }

    private class Helper extends AbsListFramework<ArticleContent, ArticleContentHolder> {
        public Helper(IFramework framework, int listId) {
            super(framework, listId);
        }

        @Override
        public ArticleContentHolder createHolder(View convertView) {
            return new ArticleContentHolder(convertView);
        }

        @Override
        public View createView(ArticleContent articleContent) {
            return View.inflate(getActivity(), R.layout.item_content, null);
        }

        @Override
        public void onAction() {
        }

        @Override
        public void onItemClick(int position, ArticleContent articleContent) {
        }

        @Override
        public ArrayList<ArticleContent> onParse(String json) {
            return null;
        }

        @Override
        public int parseItemId(ArticleContent articleContent) {
            return getItemCount();
        }
    }
}
