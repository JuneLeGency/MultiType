package june.legency.aamultitype;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView.ViewHolder;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * @author legency
 */

interface RecycleItemListener {
    void reachLast();
}

interface DataListener {
    void refreshed();

    void loadFailed();
}

public class RefreshMultiTypeAdapter extends MultiTypeAdapter {

    public static Handler handler = new Handler(Looper.getMainLooper());
    private DataProvider dataProvider;

    public void register(BaseItemViewBinder binder) {
        register(binder.getModelClass(), binder);
    }

    DataRequest dataRequest;

    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (position == getItemCount() - 1) {
            reachLast();
        }
    }

    private List<RecycleItemListener> recycleItemListeners;

    public void addToLast(List items) {
        int startPosition = this.items.size();
        int count = items.size();
        if (this.items.isEmpty()) {
            this.items = items;
        } else {
            this.items.addAll(0, items);
        }
        notifyItemRangeInserted(startPosition, count);
    }

    public void addToFirst(List items) {
        int startPosition = 0;
        int count = items.size();
        if (this.items.isEmpty()) {
            this.items = items;
        } else {
            this.items.addAll(0, items);
        }
        notifyItemRangeInserted(startPosition, count);
    }

    public void addRecycleItemListener(RecycleItemListener recycleItemListener) {
        if (recycleItemListeners == null) {
            recycleItemListeners = new ArrayList<>();
        }
        recycleItemListeners.add(recycleItemListener);
    }

    private void reachLast() {
        if (recycleItemListeners != null) {
            for (RecycleItemListener recycleItemListener : recycleItemListeners) {
                recycleItemListener.reachLast();
            }
        }
    }

    protected int page = 0;

    protected boolean loading;

    private List<DataListener> dataListenListeners;

    public RefreshMultiTypeAdapter() {
        addRecycleItemListener(new RecycleItemListener() {
            @Override
            public void reachLast() {
                loadData(false);
            }
        });
    }

    public void addDataListenListener(DataListener dataListenListener) {
        if (dataListenListeners == null) {
            dataListenListeners = new ArrayList<>();
        }
        dataListenListeners.add(dataListenListener);
    }

    public void loadData(final boolean refresh) {
        loadDataInner(refresh);
    }

    private void loadDataInner(final boolean refresh) {
        if (loading) {
            if (refresh) {
                notifyRefreshed();
            }
            return;
        }
        loading = true;
        if (dataProvider != null) {
            dataProvider.request(page, new DataRequest() {
                @Override
                public void getData(final List data) {
                    try {
                        if (data == null || data.isEmpty()) {
                            notifyGetDataFailed();
                            loading = false;
                            return;
                        }
                        runUI(new Runnable() {
                            @Override
                            public void run() {
                                if (refresh) {
                                    addToFirst(data);
                                } else {
                                    addToLast(data);
                                }
                                page++;
                                dataLoaded(refresh);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        notifyGetDataFailed();
                    }
                    loading = false;
                }
            });
        }
    }

    void dataLoaded(final boolean refresh) {
        runUI(new Runnable() {
                  @Override
                  public void run() {
                      dataLoadedInner(refresh);
                  }
              }
        );
    }

    void dataLoadedInner(boolean refresh) {
        if (refresh) {
            notifyRefreshed();
        }
    }

    private void notifyGetDataFailed() {
        runUI(new Runnable() {
            @Override
            public void run() {
                if (dataListenListeners != null) {
                    for (DataListener dataListenListener : dataListenListeners) {
                        dataListenListener.loadFailed();
                    }
                }
            }
        });
    }

    private void notifyRefreshed() {
        runUI(new Runnable() {
            @Override
            public void run() {
                if (dataListenListeners != null) {
                    for (DataListener dataListenListener : dataListenListeners) {
                        dataListenListener.refreshed();
                    }
                }
            }
        });
    }

    public static void runUI(Runnable runnable) {
        handler.post(runnable);
    }

    public void setUpSwipeRefresh(final SwipeRefreshLayout swipe_refresh_layout) {
        if (swipe_refresh_layout == null) { return; }
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(true);
            }
        });
        addDataListenListener(new DataListener() {
            @Override
            public void refreshed() {
                swipe_refresh_layout.setRefreshing(false);
            }

            @Override
            public void loadFailed() {
                swipe_refresh_layout.setRefreshing(false);
            }
        });
        loadData(true);
        swipe_refresh_layout.setRefreshing(true);
    }

}
