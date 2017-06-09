package jp.programminglife.binding;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.LruCache;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jp.programminglife.libpljp.android.Logger;

/**
 * 必要な時にデータをロードするAdapter。
 */
public final class LazyLoadRecyclerAdapter<I, VH extends ViewHolder> extends RecyclerView.Adapter<VH> {

    private static final int NO_REQUEST = -1;

    private final Logger log = Logger.create(LazyLoadRecyclerAdapter.class);
    @NonNull private final DataSource<I, VH> dataSource;
    private int blockSize;
    private boolean loading;
    /** 次にロードするブロックの番号 */
    private int loadRequest;
    private LruCache<Integer, List<I>> cache;


    /**
     * @param dataSource データの供給を行うデータソース。
     */
    public LazyLoadRecyclerAdapter(@NonNull DataSource<I, VH> dataSource) {

        this.dataSource = dataSource;
        blockSize = 100;
        loadRequest = NO_REQUEST;
        cache = new LruCache<>(2);

    }


    /**
     * アイテムをセットする。
     * @param blockNumber ブロック番号。
     * @param items セットするアイテム。配列はコピーされる。
     * @throws IllegalArgumentException itemsのサイズがブロックサイズと違うとき。
     */
    public void setItems(int blockNumber, @NonNull List<I> items) throws IllegalArgumentException {

        if ( items.size() != blockSize ) throw new IllegalArgumentException();
        List<I> newList = new ArrayList<>(items);
        cache.put(blockNumber, newList);
        notifyDataSetChanged();

        loading = false;
        if ( loadRequest != NO_REQUEST ) {
            int req = loadRequest;
            loadRequest = NO_REQUEST;
            startLoading(req);
        }

    }


    private void startLoading(int blockNumber) {

        if ( loading ) {
            loadRequest = blockNumber;
        }
        else {
            loading = true;
            dataSource.startLoading(blockNumber);
        }
    }


    public void clear() {

        dataSource.cancelLoading();
        loading = false;
        loadRequest = NO_REQUEST;
        cache.evictAll();
    }


    @Override
    public int getItemCount() {
        return dataSource.getCount();
    }


    private I getItem(int position) {
        List<I> items = cache.get(position / blockSize);
        return items != null ? items.get(position % blockSize) : null;
    }


    @Override
    public long getItemId(int position) {
        I item = getItem(position);
        return dataSource.getItemId(item, position);
    }


    @Override
    public int getItemViewType(int position) {
        I item = getItem(position);
        return item == null ? 0 : dataSource.getItemViewType(item, position);
    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return dataSource.createViewHolder(viewType, parent);
    }


    @Override
    public void onBindViewHolder(VH holder, int position) {

        I item = getItem(position);
        if ( item == null )
            startLoading(position / blockSize);
        dataSource.bind(holder, item, position);
    }


    public interface DataSource<I, VH extends ViewHolder> {

        /**
         * ロードを開始する。ロード完了したら {@link LazyLoadRecyclerAdapter#setItems(int, List)} を呼び出してデータをセットする。
         * ここでロードをスタートしたあと、setItems()を呼び出すまで次のロードは開始されない。
         * @param blockNumber ロードを開始するブロック番号。ブロックサイズと乗算するとポジションになる。
         */
        void startLoading(int blockNumber);

        void cancelLoading();

        int getCount();

        long getItemId(@Nullable I item, int position);

        /**
         * アイテムのタイプを返す。ItemAdapter内部でタイプ0はロードされていないアイテムとして使用するので、1以上を返すこと。
         */
        int getItemViewType(@NonNull I item, int position);

        /**
         * Viewを保持するViewHolderを作る。Viewもこのタイミングで作る。
         * @param viewType getItemViewType()で返すビューのタイプ。0はロードされていないアイテムを表す。
         */
        @NonNull
        VH createViewHolder(int viewType, @NonNull ViewGroup parent);

        void bind(@NonNull VH holder, @Nullable I item, int position);
    }

}
