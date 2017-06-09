package jp.programminglife.binding;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import jp.programminglife.libpljp.android.Logger;


/**
 * アイテム毎に表示名、値、レイアウト、ドロップダウンレイアウト、タイプ(int)を持つアダプター。
 * このクラスではaddなどの変更の際に自動的に{@link #notifyDataSetChanged()}を呼び出さないので注意すること。
 * @param <V> 保持する値の型。
 */
public final class NamedItemAdapter<V> extends BaseAdapter {

    private final Logger log = Logger.create(getClass());
    private final LayoutInflater inf;
    private final Context context;
    final ArrayList<Item<V>> items;
    final int viewTypeCount;
    final ItemParams<V> defaultItemParams;
    final LongSparseArray<Item<V>> idToItem;
    long nextId;


    public static <V> NamedItemAdapter<V> createForListView(@NonNull Context context) {
        return new NamedItemAdapter<>(context, 1, ItemParams.<V>createDefault());
    }


    public static <V> NamedItemAdapter<V> createForSelectableListView(@NonNull Context context) {
        return new NamedItemAdapter<>(context, 1,
                ItemParams.<V>createDefault().layout(android.R.layout.simple_list_item_activated_1));
    }


    public static <V> NamedItemAdapter<V> createForSpinner(@NonNull Context context) {
        return new NamedItemAdapter<>(context, 1,
                ItemParams.<V>createDefault()
                        .layout(android.R.layout.simple_spinner_item)
                        .dropDownLayout(android.R.layout.simple_spinner_dropdown_item));
    }


    public interface ViewUpdater<V> {
        void update(@NonNull View view, @NonNull Item<V> item);
    }


    /**
     * アイテムのレイアウトやViewUpdaterの設定。
     */
    public static final class ItemParams<V> {

        public final int viewType;
        public final int layout;
        public final int dropDownLayout;
        public final ViewUpdater<V> viewUpdater;
        public final ViewUpdater<V> dropDownViewUpdater;
        public final int idMSB;


        public static <V> ItemParams<V> createDefault() {

            ViewUpdater<V> vu = new ViewUpdater<V>() {
                @Override public void update(@NonNull View view, @NonNull Item<V> item) {
                    ((TextView)view).setText(item.label);
                }
            };

            return new ItemParams<>(
                    0,
                    android.R.layout.simple_list_item_1, android.R.layout.simple_list_item_1,
                    vu, vu, 0);
        }


        public ItemParams(
                int viewType, int layout, int dropDownLayout, ViewUpdater<V> viewUpdater,
                ViewUpdater<V> dropDownViewUpdater, int idMSB) {

            this.viewType = viewType;
            this.layout = layout;
            this.dropDownLayout = dropDownLayout;
            this.viewUpdater = viewUpdater;
            this.dropDownViewUpdater = dropDownViewUpdater;
            this.idMSB = idMSB;
        }


        public ItemParams<V> viewType(int viewType) {
            return new ItemParams<>(viewType, layout, dropDownLayout, viewUpdater, dropDownViewUpdater, idMSB);
        }

        public ItemParams<V> layout(int layout) {
            return new ItemParams<>(viewType, layout, dropDownLayout, viewUpdater, dropDownViewUpdater, idMSB);
        }

        public ItemParams<V> dropDownLayout(int dropDownLayout) {
            return new ItemParams<>(viewType, layout, dropDownLayout, viewUpdater, dropDownViewUpdater, idMSB);
        }

        public ItemParams<V> viewUpdater(@NonNull ViewUpdater<V> viewUpdater) {
            return new ItemParams<>(viewType, layout, dropDownLayout, viewUpdater, dropDownViewUpdater, idMSB);
        }

        public ItemParams<V> dropDownViewUpdater(@NonNull ViewUpdater<V> dropDownViewUpdater) {
            return new ItemParams<>(viewType, layout, dropDownLayout, viewUpdater, dropDownViewUpdater, idMSB);
        }

        public ItemParams<V> idMSB(int idMSB) {
            return new ItemParams<>(viewType, layout, dropDownLayout, viewUpdater, dropDownViewUpdater, idMSB);
        }

    }


    /**
     * リストアイテム。
     * idの最上位バイトにはItemParams.idMSBが自動的に付加される。
     */
    public static final class Item<V> {

        public final String label;
        public final V value;
        /**
         * 最上位バイトを{@link NamedItemAdapter.ItemParams#idMSB}に置き換えたID。
         * Adapterの中でIDといったらこの値のこと指す。
         */
        public final long id;
        /**
         * アイテムに設定したデータの本来のID値。
         * AdapterのアイテムIDはこの値の最上位バイトを{@link NamedItemAdapter.ItemParams#idMSB}
         * に置き換えた値が使われる。
         */
        public final long rawId;
        public final boolean enable;
        public final ItemParams<V> params;

        Item(String label, V value, long id, boolean enabled, @NonNull ItemParams<V> params) {
            this.label = label;
            this.value = value;
            this.id = ((long)params.idMSB << 56) | (id & 0x00ffffffffffffffL);
            this.rawId = id;
            this.enable = enabled;
            this.params = params;
        }

    }


    /**
     *
     * @param context リソースやレイアウトにアクセスするためのコンテキスト。
     * @param viewTypeCount ビュータイプの数。
     * @param defaultItemParams デフォルトのアイテムパラメーター。ここで渡したインスタンスはコピーせずに利用される。
     */
    public NamedItemAdapter(@NonNull Context context, int viewTypeCount, @NonNull ItemParams<V> defaultItemParams) {

        this.context = context;
        this.inf = LayoutInflater.from(context);
        this.items = new ArrayList<>();
        this.viewTypeCount = viewTypeCount;
        this.defaultItemParams = defaultItemParams;
        this.nextId = 1L;
        this.idToItem = new LongSparseArray<>();
    }


    public void clear() {
        items.clear();
        idToItem.clear();
    }


    public void add(@NonNull String label, @Nullable V value) {
        add(label, value, getNextId(defaultItemParams.idMSB), true, defaultItemParams);
    }


    public void add(@NonNull String label, @Nullable V value, boolean enabled) {
        add(label, value, getNextId(defaultItemParams.idMSB), enabled, defaultItemParams);
    }


    /**
     *
     * @param itemParams アイテムのパラメーター。ここで渡したインスタンスはコピーせずに利用される。
     */
    public void add(@NonNull String label, @Nullable V value, long id, boolean enabled,
            @NonNull ItemParams<V> itemParams) {
        add(new Item<>(label, value, id, enabled, itemParams));
    }


    private void add(@NonNull Item<V> item) {
        items.add(item);
        idToItem.put(item.id, item);
    }


    @SafeVarargs
    public final void addAll(int arrayResourceId, V... values) {

        String[] labels = context.getResources().getStringArray(arrayResourceId);
        addAll(labels, values);

    }


    public void addAll(@NonNull String[] labels, @NonNull V[] values) {

        if ( labels.length != values.length ) throw new IllegalArgumentException("labels.length != values.length");
        for (int i=0; i<labels.length; i++)
            add(labels[i], values[i]);

    }


    /**
     * デフォルトのアイテムパラメーターを返す。
     * @return デフォルトのアイテムパラメーター。このクラスが内部で保持するインスタンスをそのまま返す。
     */
    @NonNull
    public ItemParams<V> getDefaultItemParams() {
        return defaultItemParams;
    }


    public long getNextId(int msb) {
        return (long)msb << 56 | nextId++;
    }


    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    @NonNull
    public Item<V> getItem(int position) {
        return items.get(position);
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public long getItemId(int position) {
        Item item = items.get(position);
        return item.id;
    }


    /**
     * valueを持つItemの位置を返す。
     * @param value 検索する値。
     * @return valueと同じ値を持つItemが最初に見つかった位置。見つからなかったら-1を返す。
     */
    public int positionOf(V value) {

        for (int i=0, n=items.size(); i<n; i++) {

            V itemValue = items.get(i).value;
            if ( itemValue == value ) return i;
            if ( itemValue != null && itemValue.equals(value) )
                return i;

        }
        return -1;

    }


    /**
     * idからアイテムを探す。
     * @param id アイテムのID。
     * @return 見つかったアイテム。見つからなかったらnull。
     */
    public Item<V> findItemById(long id) {
        return idToItem.get(id);
    }


    @Override
    public int getViewTypeCount() {
        return viewTypeCount;
    }


    @Override
    public int getItemViewType(int position) {
        Item<V> item = items.get(position);
        return item.params.viewType;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Item<V> item = items.get(position);
        if ( convertView == null )
            convertView = inf.inflate(item.params.layout, parent, false);
        item.params.viewUpdater.update(convertView, item);
        return convertView;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        Item<V> item = items.get(position);
        if ( item.params.dropDownViewUpdater == null )
            return super.getDropDownView(position, convertView, parent);
        if ( convertView == null )
            convertView = inf.inflate(item.params.dropDownLayout, parent, false);
        item.params.dropDownViewUpdater.update(convertView, item);
        return convertView;
    }


    @Override
    public boolean areAllItemsEnabled() {

        log.v();
        for (Item item : items)
            if ( !item.enable ) return false;
        return true;

    }


    @Override
    public boolean isEnabled(int position) {
        return items.get(position).enable;
    }

}
