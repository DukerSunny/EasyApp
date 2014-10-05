package tv.acfun.read.bases.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.harreke.easyapp.frameworks.bases.fragment.FragmentFramework;
import com.harreke.easyapp.tools.GsonUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tv.acfun.read.R;
import tv.acfun.read.bases.activities.ComicActivity;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.beans.ArticlePage;
import tv.acfun.read.beans.Content;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/25
 */
public class ContentFragment extends FragmentFramework {
    private final static String TAG = "ContentFragment";
    private WebView content_web;
    private String mArticle;
    private WebViewClient mClient;
    private Content mContent;
    private ArrayList<String> mImageList;

    public static ContentFragment create(Content content, ArticlePage articlePage) {
        ContentFragment fragment = new ContentFragment();
        Bundle bundle = new Bundle();

        bundle.putString("content", GsonUtil.toString(content));
        bundle.putString("articlePage", GsonUtil.toString(articlePage));
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void assignEvents() {
        WebSettings settings = content_web.getSettings();

        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(AcFunRead.CacheDir + "/" + AcFunRead.DIR_CACHES);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        content_web.setWebViewClient(mClient);
    }

    private String generateHtml() {
        return "<header>" +
                "<style>" +
                "html,body{width:100%;height:100%}" +
                "body{margin:0;background-color:#ebebeb;}" +
                "body > .header{border-bottom:1px solid #ccc;width:100%;}" +
                "body > .header .title{font-size:24px;color:#303030;margin:0;}" +
                "body > .header .username{color:#b22222;font-size:16px;margin:4px;}" +
                "body > .header .info{color:#848484;font-size:12px;margin:4px;}" +
                "body > .content{width:100%;color:#303030;padding:0;overflow:hidden;line-height:1.6;font-size:16px;}body > .content p,body > .content span,body > .content div{white-space:normal !important;word-break:break-all !important}body > .content img[src*='editor/dialogs/emotion/images']{max-width:80px !important}body > .content img,body > .content embed,body > .content iframe,body > .content object,body > .content div{max-width:100% !important;height:auto}body > .content .img-emot-ac{max-width:80px}" +
                "</style>" +
                "</header>" +
                "<body>" +
                "<div class='header'>" +
                "<p class='title'>" + mContent.getTitle() +
                "</p>" +
                "<p class='username'>" + mContent.getUser().getUsername() +
                "</p>" +
                "<p class='info'>" + new SimpleDateFormat(getString(R.string.content_date)).format(new Date(mContent.getReleaseDate())) + " " +
                getString(R.string.content_info, mContent.getComments(), mContent.getViews()) +
                "</p>" +
                "</div>" +
                "<div class='content'>" +
                mArticle +
                "</div>" +
                "</body>";
    }

    @Override
    public void initData(Bundle bundle) {
        ArticlePage articlePage = GsonUtil.toBean(bundle.getString("articlePage"), ArticlePage.class);

        mContent = GsonUtil.toBean(bundle.getString("content"), Content.class);
        mArticle = articlePage.getArticle();
        mImageList = articlePage.getImageList();
    }

    @Override
    public void newEvents() {
        mClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(TAG, "url=" + url);
                if (mImageList != null && mImageList.contains(url)) {
                    start(ComicActivity.create(getActivity(), mImageList, mImageList.indexOf(url)));
                }

                return true;
            }
        };
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

        content_web = (WebView) findContentView(R.id.content_web);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.fragment_content);
    }

    @Override
    public void startAction() {
        content_web.loadDataWithBaseURL(null, generateHtml(), "text/html", "UTF-8", null);
    }
}