package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumbersFragment extends Fragment {
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

    /**
     * on completion Listener コールバックをインスタンス化。これで、再利用できる。
     */
    private MediaPlayer.OnCompletionListener mCompletionListener =
            new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    releaseMediaPlayer();
                }
            };

    //Listener インターフェイス をインスタンス化 ※ クラスに implement もできるけれども。
    private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    mMediaPlayer.start();
                    break;

                case AudioManager.AUDIOFOCUS_LOSS:
                    if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
                    mMediaPlayer.seekTo(0);//短いファイルなので、最初から再生
                    break;
            }
        }
    };//Listener インターフェイス をインスタンス化 終了

    public NumbersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("one", "lutti", R.raw.number_one, R.drawable.number_one));
        words.add(new Word("two", "otiiko", R.raw.number_two, R.drawable.number_two));
        words.add(new Word("three", "tolookosu", R.raw.number_three, R.drawable.number_three));
        words.add(new Word("four", "oyyisa", R.raw.number_four, R.drawable.number_four));
        words.add(new Word("five", "massokka", R.raw.number_five, R.drawable.number_five));
        words.add(new Word("six", "temmokka", R.raw.number_six, R.drawable.number_six));
        words.add(new Word("seven", "kenekaku", R.raw.number_seven, R.drawable.number_seven));
        words.add(new Word("eight", "kawinta", R.raw.number_eight, R.drawable.number_eight));
        words.add(new Word("nine", "wo’e", R.raw.number_nine, R.drawable.number_nine));
        words.add(new Word("ten", "na’aacha", R.raw.number_ten, R.drawable.number_ten));

        //カスタムAdapterを生成
        //変更3.Fragment は Adapter に渡すコンテキストになれないので、
        //this ではなく Fragment を内包する Activity をコンテキストとして渡す
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_numbers);

        View rootView = inflater.inflate(R.layout.word_list, container, false);

        //コンテナとして、ListView を使用
        //変更1.Fragment には findViewById メソッドがないため、rootView OBJ に対して行うような形をとる
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);


        //AudioManager の初期化
        //変更2. Fragment は System サービスへのサクセスがないため、
//        まず Fragment を 内包するActivity の OBJ を取得し、それに対してgetSystemService する。
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = words.get(position);

//              他のAudioとの再生重複回避のため、 MediaPlayer が クリエイトされる前に、一度リリースする。
                releaseMediaPlayer();


                //Audio Focus をリクエスト
                int audioGetResult = mAudioManager.requestAudioFocus(mAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC,
                        //短い音声ファイルのため、一時的な duration
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                //Audio Focus が取得できていたら、再生
                if (audioGetResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    //メディアプレイヤー 生成//取得できなきゃ生成もいらん
                    //変更4. 内包するActivityを、コンテキストとして渡す用に変える
                    mMediaPlayer = MediaPlayer.create(getActivity(),
                            word.getMiwokPronounciation());

                    //メディアプレイヤー 再生
                    mMediaPlayer.start();

                    //Listener の new は インスタンス変数にて定義済み
                    //再生完了時にリソースがリリースされるよう定義
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }

                Log.v("NumbersFragment", "Current word: " + word);//こんな感じで挟み込む
            }
        });
        //▼変更5.追加
        return rootView;
    }


    @Override
    public void onStop() {//Activity の方では、onStop は protected だった。
        super.onStop();
        releaseMediaPlayer();
    }

    /**
     * リソースを解放し、MediaPlayer をクリーンアップ
     * null なのに release() を試みたりしないように配慮したロジック。
     */
    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;

            //Audio Focus を破棄（Listener を unregister している）
            mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
        }
    }
}
