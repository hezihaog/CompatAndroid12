package com.zh.android.compatandroid12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zh.android.compatandroid12.item.ImageCenterViewBinder;
import com.zh.android.compatandroid12.item.ImageLeftViewBinder;
import com.zh.android.compatandroid12.item.ImageRightViewBinder;
import com.zh.android.compatandroid12.item.NumberViewBinder;
import com.zh.android.compatandroid12.item.StringViewBinder;
import com.zh.android.compatandroid12.model.ImageModel;
import com.zh.android.compatandroid12.model.NumberModel;
import com.zh.android.compatandroid12.model.StringModel;

import me.drakeet.multitype.ClassLinker;
import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.ItemViewHolder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class MainActivity extends AppCompatActivity {
    private ListView vListView;

    private final Items mListItems = new Items();
    private final MultiTypeAdapter mListAdapter = new MultiTypeAdapter(mListItems);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        bindView();
        setData();
    }

    private void findView() {
        vListView = findViewById(R.id.list_view);
    }

    private void bindView() {
        //多类型
        mListAdapter.register(StringModel.class, new StringViewBinder());
        mListAdapter.register(NumberModel.class, new NumberViewBinder());
        //一对多
        mListAdapter.register(ImageModel.class).to(
                new ImageCenterViewBinder(),
                new ImageLeftViewBinder(),
                new ImageRightViewBinder()
        ).withClassLinker(new ClassLinker<ImageModel>() {
            @NonNull
            @Override
            public Class<? extends ItemViewBinder<ImageModel, ?>> index(int position, @NonNull ImageModel model) {
                if (model.getType() == ImageModel.TYPE_CENTER) {
                    return ImageCenterViewBinder.class;
                } else if (model.getType() == ImageModel.TYPE_LEFT) {
                    return ImageLeftViewBinder.class;
                } else if (model.getType() == ImageModel.TYPE_RIGHT) {
                    return ImageRightViewBinder.class;
                }
                return ImageCenterViewBinder.class;
            }
        });
        vListView.setAdapter(mListAdapter);
        vListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Object itemModel = mListItems.get(position);
                Toast.makeText(getApplicationContext(), itemModel.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {
        //一对多
        mListItems.add(new ImageModel(ImageModel.TYPE_CENTER, R.mipmap.ic_launcher));
        mListItems.add(new ImageModel(ImageModel.TYPE_LEFT, R.mipmap.ic_launcher));
        mListItems.add(new ImageModel(ImageModel.TYPE_RIGHT, R.mipmap.ic_launcher));
        //多类型
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                mListItems.add(new StringModel("String：" + i));
            } else {
                mListItems.add(new NumberModel(i));
            }
        }
        mListAdapter.notifyDataSetChanged();
    }
}