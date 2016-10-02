package com.example.android.miwok;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;

/**
 * Created by Yasuaki on 2016/08/25.
 */
public class Word {
    private String mDefaultTranslation = "";

    private String mMiwokTranslation = "";

    /**
     * 画像がない という状態を示すコンスタント値
     */
    private int mImageResourceId = NO_IMAGE_PROVIDED;
    /**
     * invalid であること確実な値 -1 を割当る
     */
    private static final int NO_IMAGE_PROVIDED = -1;

    private int mMiwokPronounciation;

    public Word(String defaultTlanslation, String miwokTranslation, int miwokPronounciation) {
        mDefaultTranslation = defaultTlanslation;
        mMiwokTranslation = miwokTranslation;
        mMiwokPronounciation = miwokPronounciation;
    }

    public Word(String defaultTlanslation, String miwokTranslation, int miwokPronounciation, int imageResourceId) {
        mDefaultTranslation = defaultTlanslation;
        mMiwokTranslation = miwokTranslation;
        mMiwokPronounciation = miwokPronounciation;
        mImageResourceId = imageResourceId;
    }

    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public int getMiwokPronounciation() {
        return mMiwokPronounciation;
    }

    @Override
    //Alt + Insert で toString() を生成 ⇒同一クラス内の全ての変数を出力できる。
    public String toString() {
        return "Word{" +
                "mDefaultTranslation='" + mDefaultTranslation + '\'' +
                ", mMiwokTranslation='" + mMiwokTranslation + '\'' +
                ", mImageResourceId=" + mImageResourceId +
                ", mMiwokPronounciation=" + mMiwokPronounciation +
                '}';
    }

    /**
     * 画像の有無といった詳細情報は、Adapter ではなく 情報元 OBJ で定義
     *
     * @return Whether there is an image for this word.
     */
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }
}


