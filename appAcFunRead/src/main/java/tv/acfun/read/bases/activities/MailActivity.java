package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;

import java.util.List;

import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.beans.Mail;
import tv.acfun.read.helpers.LoginHelper;
import tv.acfun.read.holders.MailHolder;
import tv.acfun.read.parsers.MailListParser;

//import com.harreke.easyapp.widgets.InfoView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/27
 */
public class MailActivity extends ActivityFramework {
    private View.OnClickListener mClickListener;
    private LoginHelper mLoginHelper;
    private MailRecyclerHelper mMailRecyclerHelper;
    private View mail_back;

    public static Intent create(Context context) {
        return new Intent(context, MailActivity.class);
    }

    @Override
    public void acquireArguments(Intent intent) {
    }

    @Override
    public void attachCallbacks() {
        mail_back.setOnClickListener(mClickListener);
    }

    @Override
    public void enquiryViews() {
        mail_back = findViewById(R.id.mail_back);

        mLoginHelper = new LoginHelper(this);
        mMailRecyclerHelper = new MailRecyclerHelper(this);
        mMailRecyclerHelper.attachAdapter();
        //        mMailListHelper.setInfoView((InfoView) findViewById(R.id.mail_info));
    }

    @Override
    public void establishCallbacks() {
        mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.mail_back:
                        onBackPressed();
                }
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mail;
    }

    @Override
    public void onBackPressed() {
        exit(Transition.Exit_Left);
    }

    @Override
    public void startAction() {
        AcFunRead acFunRead = AcFunRead.getInstance();

        if (acFunRead.isExpired()) {
            acFunRead.clearLogin();
            mLoginHelper.show(LoginHelper.Reason.Expired);
        } else {
            mMailRecyclerHelper.from(API.getMail(acFunRead.readToken(), 20, mMailRecyclerHelper.getCurrentPage()));
        }
    }

    private class MailRecyclerHelper extends RecyclerFramework<Mail> {
        public MailRecyclerHelper(IFramework framework) {
            super(framework);
        }

        @Override
        protected RecyclerHolder<Mail> createHolder(View itemView, int viewType) {
            return new MailHolder(itemView);
        }

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return inflater.inflate(R.layout.item_mail, parent, false);
        }

        @Override
        public void onItemClick(int position, Mail mail) {
            //            start(ChatActivity.create(getActivity(), mail));
        }

        @Override
        public List<Mail> onParse(String json) {
            MailListParser parser = MailListParser.parse(json);

            if (parser != null) {
                return parser.getItemList();
            } else {
                return null;
            }
        }
    }
}