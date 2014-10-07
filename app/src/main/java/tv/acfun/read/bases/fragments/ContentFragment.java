package tv.acfun.read.bases.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.harreke.easyapp.configs.ImageExecutorConfig;
import com.harreke.easyapp.frameworks.bases.fragment.FragmentFramework;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.tools.GsonUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.WeakHashMap;

import tv.acfun.read.R;
import tv.acfun.read.bases.activities.ComicActivity;
import tv.acfun.read.beans.ArticlePage;
import tv.acfun.read.beans.Content;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/25
 */
public class ContentFragment extends FragmentFramework {
    private final static String TAG = "ContentFragment";
    private WebView content_web;
    private String mArticle;
    private IRequestCallback<Bitmap> mCallback;
    private WebViewClient mClient;
    private Content mContent;
    private Handler mHandler;
    private ArrayList<String> mImageList;
    private WeakHashMap<String, Bitmap> mMap = new WeakHashMap<String, Bitmap>();

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

        settings.setAppCacheEnabled(false);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkLoads(true);

        content_web.addJavascriptInterface(new JsInterface(), "content");
        content_web.setWebViewClient(mClient);
    }

    private String generateHtml(String article) {
        return "<header>" +
                "<style>" +
                "html," +
                "body{width:100%;height:100%}" +
                "body{margin:0;background-color:#ebebeb;}" +
                "body > .header{border-bottom:1px solid #ccc;width:100%;}" +
                "body > .header .title{font-size:24px;color:#303030;margin:0;}" +
                "body > .header .username{color:#b22222;font-size:16px;margin:4px;}" +
                "body > .header .info{color:#848484;font-size:12px;margin:4px;}" +
                "body > .content{width:100%;color:#303030;padding:0;overflow:hidden;line-height:1.6;font-size:16px;}" +
                "body > .content p," +
                "body > .content span," +
                "body > .content div{white-space:normal !important;word-break:break-all !important}" +
                "body > .content img[src*='editor/dialogs/emotion/images']{max-width:80px !important}" +
                "body > .content img," +
                "body > .content embed," +
                "body > .content iframe," +
                "body > .content object," +
                "body > .content div{max-width:100% !important;height:auto}" +
                "body > .content .img-emot-ac{max-width:80px}" +
                "</style>" +
                "</header>" +
                "<body>" +
                "<div class='header'>" +
                "<p class='title'>" + mContent.getTitle() +
                "</p>" +
                "<p class='username'>" + mContent.getUser().getUsername() +
                "</p>" +
                "<p class='info'>" +
                new SimpleDateFormat(getString(R.string.content_date)).format(new Date(mContent.getReleaseDate())) + " " +
                getString(R.string.content_info, mContent.getComments(), mContent.getViews()) +
                "</p>" +
                "</div>" +
                "<div class='content'>" +
                article +
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
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                start(ComicActivity.create(getActivity(), mImageList, msg.what));

                return false;
            }
        });
        mCallback = new IRequestCallback<Bitmap>() {
            @Override
            public void onFailure(String requestUrl) {
            }

            @Override
            public void onSuccess(String requestUrl, Bitmap bitmap) {
                saveBitmap(requestUrl, bitmap);
                replaceImageUrl(requestUrl);
            }
        };
        mClient = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                int i;

                for (i = 0; i < mImageList.size(); i++) {
                    ImageLoaderHelper.loadBitmap(mImageList.get(i), mCallback);
                }
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
        content_date
                .setText(new SimpleDateFormat(getString(R.string.content_date)).format(new Date(mContent.getReleaseDate())));
        content_info.setText(getString(R.string.content_info, mContent.getComments(), mContent.getViews()));

        content_web = (WebView) findContentView(R.id.content_web);
    }

    private void replaceImageUrl(String imageUrl) {
        int position = mImageList.indexOf(imageUrl);

        content_web.loadUrl("javascript:(" +
                "function(){" +
                "var objs = document.getElementsByTagName(\"img\");" +
                "objs[" + position + "].setAttribute(\"src\", \"file://" +
                ImageExecutorConfig.getImageCachePathByUrl(imageUrl) +
                "\");" +
                "})" +
                "()");
    }

    private void saveBitmap(String imageUrl, Bitmap bitmap) {
        File file = new File(ImageExecutorConfig.getImageCachePathByUrl(imageUrl));

        try {
            if (bitmap != null && !file.exists() && file.createNewFile()) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, new FileOutputStream(file));
            }
        } catch (IOException e) {
            Log.e(TAG, "Save bitmap " + imageUrl + " io error!");
        }
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.fragment_content);
    }

    @Override
    public void startAction() {
        String article = mArticle;
        int i;

        for (i = 0; i < mImageList.size(); i++) {
            article = article.replace("src='" + mImageList.get(i), "src='file:///android_asset/web_loading.gif");
        }
        content_web.loadDataWithBaseURL(null, generateHtml(article), "text/html", "UTF-8", null);
    }

    private class JsInterface {
        @JavascriptInterface
        public void onSingleClicked(String url) {
            if (mImageList != null && mImageList.contains(url)) {
                mHandler.sendEmptyMessage(mImageList.indexOf(url));
            }
        }
    }
}