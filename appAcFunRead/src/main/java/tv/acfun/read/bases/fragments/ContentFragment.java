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
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.harreke.easyapp.configs.ImageExecutorConfig;
import com.harreke.easyapp.frameworks.bases.fragment.FragmentFramework;
import com.harreke.easyapp.frameworks.webs.WebFramework;
import com.harreke.easyapp.helpers.DialogHelper;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.tools.GsonUtil;
import com.harreke.easyapp.tools.StringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import tv.acfun.read.R;
import tv.acfun.read.bases.activities.ComicActivity;
import tv.acfun.read.bases.activities.ContentActivity;
import tv.acfun.read.bases.activities.ProfileActivity;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.beans.ArticlePage;
import tv.acfun.read.beans.Content;
import tv.acfun.read.helpers.ImageConnectionHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/25
 */
public class ContentFragment extends FragmentFramework {
    private final static int CONTENT_A = 1;
    private final static int CONTENT_HEADER = 2;
    private final static int CONTENT_IMG = 0;
    private final static String TAG = "ContentFragment";

    private String mArticle;
    private WebViewClient mClient;
    private Content mContent;
    private WebFramework mContentWebHelper;
    private boolean mFirstRefresh = true;
    private Handler mHandler;
    private List<String> mImageList;
    private DialogInterface.OnClickListener mOnRedirectClickListener;
    private int mPagePosition;
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
    public void acquireArguments(Bundle bundle) {
        ArticlePage articlePage = GsonUtil.toBean(bundle.getString("articlePage"), ArticlePage.class);

        mContent = GsonUtil.toBean(bundle.getString("content"), Content.class);
        mPagePosition = bundle.getInt("pagePosition");
        mArticle = articlePage.getArticle();
        mImageList = articlePage.getImageList();
    }

    @Override
    public void attachCallbacks() {
    }

    @Override
    public void enquiryViews() {
        mContentWebHelper = new WebFramework(this);
        mContentWebHelper.setCanRefresh(false);
        mContentWebHelper.setCanLoad(false);
        mContentWebHelper.setAppCacheEnabled(false);
        mContentWebHelper.setJavaScriptEnabled(true);
        mContentWebHelper.addJavascriptInterface(new JsInterface(), "content");
        mContentWebHelper.setWebViewClient(mClient);

        mRedirectHelper = new DialogHelper(getActivity());
        mRedirectHelper.setPositiveButton(R.string.app_ok);
        mRedirectHelper.setNegativeButton(R.string.app_cancel);
        mRedirectHelper.setOnClickListener(mOnRedirectClickListener);
    }

    @Override
    public void establishCallbacks() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                int position;

                switch (msg.what) {
                    case CONTENT_IMG:
                        position = (Integer) msg.obj;
                        if (ImageConnectionHelper.shouldLoadImage() || imageCached(position)) {
                            start(ComicActivity
                                    .create(getActivity(), mContent.getContentId(), mPagePosition, mImageList, position));
                        } else {
                            jsReplaceLoading(position);
                            ImageLoaderHelper.loadBitmap(mImageList.get(position), new ReplaceCallback(position));
                        }
                        break;
                    case CONTENT_A:
                        parseHref((String) msg.obj);
                        break;
                    case CONTENT_HEADER:
                        start(ProfileActivity.create(getActivity(), mContent.getUser().getUserId()));
                        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }

                return false;
            }
        });
        mClient = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                int i;

                if (ImageConnectionHelper.shouldLoadImage()) {
                    for (i = 0; i < mImageList.size(); i++) {
                        ImageLoaderHelper.loadBitmap(mImageList.get(i), new ReplaceCallback(i));
                    }
                } else {
                    for (i = 0; i < mImageList.size(); i++) {
                        if (imageCached(i)) {
                            ImageLoaderHelper.loadBitmap(mImageList.get(i), new ReplaceCallback(i));
                        }
                    }
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        };
        mOnRedirectClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mRedirectHelper.hide();
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        start(ContentActivity.create(getActivity(), mRedirectId));
                }
            }
        };
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
                "<div class=\"header\" onClick=\"content.onHeaderClick()\">" +
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

    private boolean imageCached(int position) {
        return ImageExecutorConfig.isImageCacheAvailable(mImageList.get(position));
    }

    private void jsReplaceImageUrl(int position) {
        String imageUrl = mImageList.get(position);

        mContentWebHelper.loadUrl("javascript:(" +
                "function(){" +
                "var objs = document.getElementsByTagName(\"img\");" +
                "objs[" + position + "].setAttribute(\"src\", \"file://" +
                ImageExecutorConfig.getImageCachePathByUrl(imageUrl) +
                "\");" +
                "})" +
                "()");
    }

    private void jsReplaceLoading(int position) {
        mContentWebHelper.loadUrl("javascript:(" +
                "function(){" +
                "var objs = document.getElementsByTagName(\"img\");" +
                "objs[" + position + "].setAttribute(\"src\", \"file://" + AcFunRead.CacheDir + "/" + AcFunRead.DIR_ASSETS +
                "/web_loading\");" +
                "})" +
                "()");
    }

    @Override
    public void onDestroyView() {
        mRedirectHelper.hide();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mFirstRefresh) {
            mFirstRefresh = false;
        } else {
            refreshWeb();
        }
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
            start(intent);
        }
    }

    private void refreshWeb() {
        int i;

        for (i = 0; i < mImageList.size(); i++) {
            if (imageCached(i)) {
                jsReplaceImageUrl(i);
            }
        }
    }

    private String replaceImageUrlForDownload(String article, int position) {
        return article.replace(mImageList.get(position),
                "file://" + AcFunRead.CacheDir + "/" + AcFunRead.DIR_ASSETS + "/web_download");
    }

    private String replaceImageUrlForLoading(String article, int position) {
        return article.replace(mImageList.get(position),
                "file://" + AcFunRead.CacheDir + "/" + AcFunRead.DIR_ASSETS + "/web_loading");
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

        if (mImageList.size() > 0) {
            if (ImageConnectionHelper.shouldLoadImage()) {
                for (i = 0; i < mImageList.size(); i++) {
                    article = replaceImageUrlForLoading(article, i);
                }
            } else {
                for (i = 0; i < mImageList.size(); i++) {
                    if (imageCached(i)) {
                        article = replaceImageUrlForLoading(article, i);
                    } else {
                        article = replaceImageUrlForDownload(article, i);
                    }
                }
            }
        }
        mContentWebHelper.loadHtml(null, generateHtml(article));
    }

    private class JsInterface {
        @JavascriptInterface
        public void onAClick(String href) {
            mHandler.obtainMessage(CONTENT_A, href).sendToTarget();
        }

        @JavascriptInterface
        public void onHeaderClick() {
            mHandler.obtainMessage(CONTENT_HEADER).sendToTarget();
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
            jsReplaceImageUrl(mPosition);
        }
    }
}