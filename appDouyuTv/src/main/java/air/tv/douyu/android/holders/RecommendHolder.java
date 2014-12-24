package air.tv.douyu.android.holders;

import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.holders.recycerview.RecyclerHolder;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;

import air.tv.douyu.android.R;
import air.tv.douyu.android.beans.Recommend;
import air.tv.douyu.android.beans.Room;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/19
 */
public class RecommendHolder extends RecyclerHolder<Recommend> {
    private RoomHolder[] mRoomHolders;
    private View recommend_title;
    private TextView recommend_title_text;

    public RecommendHolder(View itemView, View.OnClickListener onRoomClickListener) {
        super(itemView);

        recommend_title = itemView.findViewById(R.id.recommend_title);
        recommend_title_text = (TextView) itemView.findViewById(R.id.recommend_title_text);
        mRoomHolders = new RoomHolder[4];
        mRoomHolders[0] = new RoomHolder(itemView.findViewById(R.id.recommend_room_1));
        mRoomHolders[1] = new RoomHolder(itemView.findViewById(R.id.recommend_room_2));
        mRoomHolders[2] = new RoomHolder(itemView.findViewById(R.id.recommend_room_3));
        mRoomHolders[3] = new RoomHolder(itemView.findViewById(R.id.recommend_room_4));

        mRoomHolders[0].setOnClickListener(onRoomClickListener);
        mRoomHolders[1].setOnClickListener(onRoomClickListener);
        mRoomHolders[2].setOnClickListener(onRoomClickListener);
        mRoomHolders[3].setOnClickListener(onRoomClickListener);

        RippleDrawable.attach(itemView.findViewById(R.id.recommend_title));
    }

    @Override
    public void setItem(Recommend recommend) {
        Room room;
        int i;

        recommend_title.setTag(getPosition());
        recommend_title_text.setText(recommend.getTitle());
        for (i = 0; i < 4; i++) {
            room = recommend.getRoomlist().get(i);
            mRoomHolders[i].setItem(room);
            mRoomHolders[i].itemView.setTag(room.getRoom_id());
        }
    }

    @Override
    public void setOnClickListener(View.OnClickListener onClickListener) {
        recommend_title.setOnClickListener(onClickListener);
    }
}
