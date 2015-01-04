package tv.acfun.read.parsers;

import com.harreke.easyapp.utils.GsonUtil;
import com.harreke.easyapp.utils.NetUtil;

import java.util.ArrayList;
import java.util.HashMap;

import tv.acfun.read.beans.Mail;
import tv.acfun.read.beans.MailListPage;
import tv.acfun.read.tools.ubb.UBBEncoder;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/27
 */
public class MailListParser {
    private HashMap<String, MailListPage> data;
    private ArrayList<Mail> mItemList;
    private int mTotalPage;
    private String msg;
    private int status;

    public static MailListParser parse(String json) {
        MailListParser parser = GsonUtil.toBean(json, MailListParser.class);
        MailListPage page;

        if (parser != null) {
            if (NetUtil.isStatusOk(parser.status) && parser.data != null) {
                page = parser.data.get("page");
                if (page != null) {
                    parser.decode(page);

                    return parser;
                }
            }
        }

        return null;
    }

    private void decode(MailListPage page) {
        ArrayList<String> mailList = page.getMailList();
        UBBEncoder encoder = new UBBEncoder();
        Mail mail;
        int i;

        if (mailList != null) {
            mItemList = new ArrayList<Mail>();
            for (i = 0; i < mailList.size(); i++) {
                mail = GsonUtil.toBean(mailList.get(i), Mail.class);
                if (mail != null) {
                    mail.setSpanned(encoder.encode(mail.getLastMessage(), null));
                    mItemList.add(mail);
                }
            }
            mTotalPage = page.getTotalPage();
        }
    }

    public ArrayList<Mail> getItemList() {
        return mItemList;
    }

    public int getTotalPage() {
        return mTotalPage;
    }
}