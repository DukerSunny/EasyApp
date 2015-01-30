package tv.douyu.wrapper.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.helpers.ImageLoaderHelper;

import tv.douyu.R;
import tv.douyu.model.bean.Advertise;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/07
 */
public class AdvertiseHolder extends RecyclerHolder<Advertise> {
    private TextView advertise_desc;
    private View advertise_download;
    private View advertise_downloaded;
    private ImageView advertise_thumbnail;
    private TextView advertise_title;

    public AdvertiseHolder(View itemView) {
        super(itemView);

        advertise_thumbnail = (ImageView) findViewById(R.id.advertise_thumbnail);
        advertise_title = (TextView) findViewById(R.id.advertise_title);
        advertise_desc = (TextView) findViewById(R.id.advertise_desc);
        advertise_download = findViewById(R.id.advertise_download);
        advertise_downloaded = findViewById(R.id.advertise_downloaded);
    }

    @Override
    public void setItem(Advertise advertise) {
        ImageLoaderHelper.loadImage(advertise_thumbnail, advertise.getIcon(), R.drawable.loading_1x1, R.drawable.retry_1x1);
        advertise_title.setText(advertise.getTitle());
        advertise_desc.setText(advertise.getDepict());
        advertise_download.setTag(getPosition());
        if (advertise.isDownloaded()) {
            advertise_download.setVisibility(View.GONE);
            advertise_downloaded.setVisibility(View.VISIBLE);
        } else {
            advertise_download.setVisibility(View.VISIBLE);
            advertise_download.setVisibility(View.GONE);
        }
    }

    public void setOnDownloadListener(View.OnClickListener onDownloadListener) {
        advertise_download.setOnClickListener(onDownloadListener);
    }
}