package com.kale.activityoptions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class ActivityCompatHelper {

    private static int enterResId = 0;
    private static int exitResId = 0;

    public static void startActivity(Activity activity, Intent intent, int requestCode, Bundle bundle) {
        if (bundle == null) {
            throw new RuntimeException("Bundle must be not null");
        }

        int animType = bundle.getInt(ActivityOptionsCompatHelper.KEY_ANIM_TYPE, 0);
        if (animType == ActivityOptionsCompatHelper.ANIM_CUSTOM) {
            enterResId = bundle.getInt(ActivityOptionsCompatHelper.KEY_ANIM_ENTER_RES_ID);
            exitResId = bundle.getInt(ActivityOptionsCompatHelper.KEY_ANIM_EXIT_RES_ID);
        } else {
            enterResId = 0;
            exitResId = 0;
        }

        intent.putExtras(bundle);
        //        activity.startActivityForResult(intent, ActivityOptionsCompatHelper.RESULT_CODE);
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(enterResId, exitResId);
    }

    public static void startActivity(Fragment fragment, Intent intent, int requestCode, Bundle bundle) {
        if (bundle == null) {
            throw new RuntimeException("Bundle must be not null");
        }

        int animType = bundle.getInt(ActivityOptionsCompatHelper.KEY_ANIM_TYPE, 0);
        if (animType == ActivityOptionsCompatHelper.ANIM_CUSTOM) {
            enterResId = bundle.getInt(ActivityOptionsCompatHelper.KEY_ANIM_ENTER_RES_ID);
            exitResId = bundle.getInt(ActivityOptionsCompatHelper.KEY_ANIM_EXIT_RES_ID);
        } else {
            enterResId = 0;
            exitResId = 0;
        }

        intent.putExtras(bundle);
        //        activity.startActivityForResult(intent, ActivityOptionsCompatHelper.RESULT_CODE);
        fragment.startActivityForResult(intent, requestCode);
        fragment.getActivity().overridePendingTransition(enterResId, exitResId);
    }
}
