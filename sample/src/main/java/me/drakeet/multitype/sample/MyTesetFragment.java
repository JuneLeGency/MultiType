package me.drakeet.multitype.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import june.legency.aamultitype.BaseItemViewBinder;
import june.legency.aamultitype.DataProvider;
import june.legency.aamultitype.DataRequest;
import june.legency.aamultitype.IViewBinder;
import june.legency.aamultitype.RefreshMultiTypeAdapter;

/**
 * Created by lichen:) on 2017/7/4.
 */

public class MyTesetFragment extends Fragment implements DataProvider {

    RecyclerView recycler_view;

    SwipeRefreshLayout swipe_refresh_layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_view, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recycler_view = (RecyclerView)view.findViewById(R.id.recycler_view);
        swipe_refresh_layout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        RefreshMultiTypeAdapter adapter = new RefreshMultiTypeAdapter();
        adapter.register(new BaseItemViewBinder<>(Bean.class, TestView.class));
        adapter.setDataProvider(this);
    }

    @Override
    public void request(int page, DataRequest response) {

    }
}

class Bean {

}

class TestView extends View implements IViewBinder<Bean> {

    public TestView(Context context) {
        super(context);
    }

    @Override
    public void bind(Bean data) {

    }

    @Override
    public void onClick(Bean data) {

    }
}