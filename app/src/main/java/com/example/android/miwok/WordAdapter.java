package com.example.android.miwok;

import android.app.Activity;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Yasuaki on 2016/08/25.
 */
public class WordAdapter extends ArrayAdapter<Word> {
    private int mColorResourceId;

    /**
     * コンストラクタ
     * @param context
     * @param words
     * @param colorResourceId Activity から渡された 色情報を、フィールドに格納
     */
    public WordAdapter(Activity context, ArrayList<Word> words, int colorResourceId) {
        super(context, 0, words);
        mColorResourceId = colorResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        //get the Word Obj located at this position in the list
        Word currentWord = getItem(position);
        ImageView iconImage = (ImageView) listItemView.findViewById(R.id.image);

        /**
         * @condition imageView が 画像を持っているかどうか
         * ⇒imgeView の領域をレイアウトに表示するかどうか
         * ⇒リソースをView にセット
         * ※hasImage の定義は、Adapter ではなく、Word クラスにて定義
         */
        if(currentWord.hasImage()){
            iconImage.setVisibility(View.VISIBLE);
            iconImage.setImageResource(currentWord.getImageResourceId());
        } else {
            iconImage.setVisibility(View.GONE);
        }

        /**
         *  View に背景色を設定する
         */
        // 2つの TextView をコンテナとする リニアレイアウト を参照し、View を OBJ化
        View textContainer = listItemView.findViewById(R.id.text_container);
        //リソース ID がマップしている色を探して格納
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        textContainer.setBackgroundColor(color);

        TextView defaultTv = (TextView) listItemView.findViewById(R.id.english_text_view);
        defaultTv.setText(currentWord.getDefaultTranslation());

        TextView miworkTv = (TextView) listItemView.findViewById(R.id.miwok_text_view);
        miworkTv.setText(currentWord.getMiwokTranslation());

        return listItemView;
    }
}