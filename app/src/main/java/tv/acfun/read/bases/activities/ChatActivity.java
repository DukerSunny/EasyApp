package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.list.abslistview.AbsListFramework;
import com.harreke.easyapp.widgets.InfoView;

import java.util.ArrayList;

import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.beans.Mail;
import tv.acfun.read.helpers.LoginHelper;
import tv.acfun.read.holders.MailHolder;
import tv.acfun.read.parsers.MailListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/27
 */
public class ChatActivity extends ActivityFramework {
    private View.OnClickListener mClickListener;
    private LoginHelper mLoginHelper;
    private MailListHelper mMailListHelper;
    private View mail_back;

    public static Intent create(Context context) {
        return new Intent(context, ChatActivity.class);
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

        mLoginHelper = new LoginHelper(getActivity(), null);
        mMailListHelper = new MailListHelper(this, R.id.mail_list);
        mMailListHelper.setRootView(findViewById(R.id.mail_list));
        mMailListHelper.setInfoView((InfoView) findViewById(R.id.mail_info));
        mMailListHelper.bindAdapter();
    }

    @Override
    public void establishCallbacks() {
        mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.mail_back:
                        onBackPressed();
                        break;
                }
            }
        };
    }

    @Override
    public void onActionBarItemClick(int id, View item) {

    }

    @Override
    public void onBackPressed() {
        exit(false);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_mail);
    }

    @Override
    public void startAction() {
        AcFunRead acFunRead = AcFunRead.getInstance();

        if (acFunRead.isExpired()) {
            acFunRead.clearLogin();
            mLoginHelper.show(LoginHelper.Reason.Expired);
        } else {
            mMailListHelper.from(API.getMail(acFunRead.readToken(), 20, mMailListHelper.getCurrentPage()));
        }
    }

    private class MailListHelper extends AbsListFramework<Mail, MailHolder> {
        public MailListHelper(IFramework framework, int listId) {
            super(framework, listId);
        }

        @Override
        public MailHolder createHolder(View convertView) {
            return new MailHolder(convertView);
        }

        @Override
        public View createView() {
            return View.inflate(getActivity(), R.layout.item_mail, null);
        }

        @Override
        public void onAction() {
            startAction();
        }

        @Override
        public void onItemClick(int position, Mail mail) {
            //            start(ChatActivity.create(getActivity(), mail));
        }

        @Override
        public ArrayList<Mail> onParse(String json) {
            MailListParser parser = MailListParser.parse(json);

            if (parser != null) {
                setTotalPage(parser.getTotalPage());

                return parser.getItemList();
            } else {
                return null;
            }
        }

        @Override
        public int parseItemId(Mail mail) {
            return mail.getMailGroupId();
        }
    }
}