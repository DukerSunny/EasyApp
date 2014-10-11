package tv.acfun.read.bases.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.harreke.easyapp.configs.ImageExecutorConfig;
import com.harreke.easyapp.frameworks.bases.fragment.FragmentFramework;
import com.harreke.easyapp.helpers.DialogHelper;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.tools.GsonUtil;
import com.harreke.easyapp.tools.StringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;

import tv.acfun.read.R;
import tv.acfun.read.bases.activities.ComicActivity;
import tv.acfun.read.bases.activities.ContentActivity;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.beans.ArticlePage;
import tv.acfun.read.beans.Content;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/25
 */
public class ContentFragment extends FragmentFramework {
    private final static int CONTENT_A = 1;
    private final static int CONTENT_IMG = 0;
    private final static String TAG = "ContentFragment";
    private WebView content_web;
    private String mArticle;
    private WebViewClient mClient;
    private Content mContent;
    private Handler mHandler;
    private ArrayList<String> mImageList;
    private int mPagePosition;
    private DialogInterface.OnClickListener mRedirectClickListener;
    private DialogHelper mRedirectHelper;
    private int mRedirectId;

    public static ContentFragment create(Content content, int pagePosition, ArticlePage articlePage) {
        ContentFragment fragment = new ContentFragment();
        Bundle bundle = new Bundle();

        bundle.putString("content", GsonUtil.toString(content));
        bundle.putInt("pagePosition", pagePosition);
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
                "body > .content img[src*=\"editor/dialogs/emotion/images\"]{max-width:80px !important}" +
                "body > .content img," +
                "body > .content embed," +
                "body > .content iframe," +
                "body > .content object," +
                "body > .content div{max-width:100% !important;height:auto}" +
                "body > .content .img-emot-ac{max-width:80px}" +
                "</style>" +
                "</header>" +
                "<body>" +
                "<div class=\"header\">" +
                "<p class=\"title\">" + mContent.getTitle() +
                "</p>" +
                "<p class=\"username\">" + mContent.getUser().getUsername() +
                "</p>" +
                "<p class=\"info\">" +
                new SimpleDateFormat(getString(R.string.content_date)).format(new Date(mContent.getReleaseDate())) + " " +
                getString(R.string.content_info, mContent.getComments(), mContent.getViews()) +
                "</p>" +
                "</div>" +
                "<div class=\"content\">" +
                article +
                "</div>" +
                "</body>";
    }

    @Override
    public void initData(Bundle bundle) {
        ArticlePage articlePage = GsonUtil.toBean(bundle.getString("articlePage"), ArticlePage.class);

        mContent = GsonUtil.toBean(bundle.getString("content"), Content.class);
        mPagePosition = bundle.getInt("pagePosition");
        mArticle = articlePage.getArticle();
        mImageList = articlePage.getImageList();
    }

    @Override
    public void newEvents() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case CONTENT_IMG:
                        start(ComicActivity
                                .create(getActivity(), mContent.getContentId(), mPagePosition, mImageList, (Integer) msg.obj));
                        break;
                    case CONTENT_A:
                        parseHref((String) msg.obj);
                }

                return false;
            }
        });
        mClient = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                int i;

                for (i = 0; i < mImageList.size(); i++) {
                    ImageLoaderHelper.loadBitmap(mImageList.get(i), new ReplaceCallback(i));
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(TAG, "url loading=" + url);

                return false;
            }
        };
        mRedirectClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        start(ContentActivity.create(getActivity(), mRedirectId));
                }
            }
        };
    }

    @Override
    public void onDestroyView() {
        mRedirectHelper.hide();
        super.onDestroyView();
    }

    private void parseHref(String href) {
        Matcher matcher;
        Intent intent;

        matcher = StringUtil.getMatcher("/a/ac([0-9]+)", href);
        if (matcher.find()) {
            mRedirectId = Integer.valueOf(matcher.group(1));
            mRedirectHelper.setTitle(getString(R.string.content_redirect_ac, mRedirectId));
            mRedirectHelper.show();
        }

        matcher = StringUtil.getMatcher("/a/aa([0-9]+)", href);
        if (matcher.find()) {
            showToast(getString(R.string.content_redirect_aa, matcher.group(1)));
        }

        matcher = StringUtil.getMatcher("http://[\\S\\s]+?", href);
        if (matcher.find()) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(href));
            start(intent, false);
        }
    }

    @Override
    public void queryLayout() {
        content_web = (WebView) findViewById(R.id.content_web);
        mRedirectHelper = new DialogHelper(getActivity());
        mRedirectHelper.setPositiveButton(R.string.app_ok, mRedirectClickListener);
        mRedirectHelper.setNegativeButton(R.string.app_cancel, mRedirectClickListener);
    }

    private void replaceImageUrl(int position) {
        String imageUrl = mImageList.get(position);

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
            article = article.replace(mImageList.get(i),
                    "file://" + AcFunRead.CacheDir + "/" + AcFunRead.DIR_ASSETS + "/web_loading");
        }
        content_web.loadDataWithBaseURL(null, generateHtml(article), "text/html", "UTF-8", null);
    }

    private class JsInterface {
        @JavascriptInterface
        public void onAClick(String href) {
            mHandler.obtainMessage(CONTENT_A, href).sendToTarget();
        }

        @JavascriptInterface
        public void onImgClick(int position) {
            mHandler.obtainMessage(CONTENT_IMG, position).sendToTarget();
        }
    }

    private class ReplaceCallback implements IRequestCallback<Bitmap> {
        private int mPosition;

        public ReplaceCallback(int position) {
            mPosition = position;
        }

        @Override
        public void onFailure(String requestUrl) {
        }

        @Override
        public void onSuccess(String requestUrl, Bitmap bitmap) {
            if (bitmap != null) {
                saveBitmap(requestUrl, bitmap);
            }
            replaceImageUrl(mPosition);
        }
    }
}