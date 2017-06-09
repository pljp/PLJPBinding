package jp.programminglife.binding.endpoint;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

import jp.programminglife.binding.Converter.ToIntegerConverter;
import jp.programminglife.binding.EndPoint;
import jp.programminglife.binding.NamedItemAdapter;
import jp.programminglife.libpljp.android.Logger;

public class ListViewProp extends AdapterViewProp {
/*
    public static final class DataSource<VM> extends EndPoint<ListView, List<VM>, List<VM>> {
        // TODO


        @Override
        protected void onBind() {

            ListView listView = getTargetNotNull();
            listView.setAdapter();
            listView.setOnScrollListener();


        }
    }
*/

    public static final class SelectedPosition extends EndPoint<ListView, Object, Integer> {

        private AdapterView.OnItemClickListener listener;


        public SelectedPosition() {
            super(new ToIntegerConverter<>());
        }


        @Override
        protected void onBind() {

            ListView listView = getTargetNotNull();
            listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            listener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    set(position);
                }
            };
            addOnItemClickListener(listView, listener);

        }

        @Override
        protected void onValueChanged() {

            ListView listView = getTargetNotNull();
            int curVal = listView.getCheckedItemPosition();
            int newVal = convert(AdapterView.INVALID_POSITION);
            if ( curVal != newVal )
                listView.setItemChecked(newVal, true);

        }

    }


    /**
     * AdapterViewのAdapterがNamedItemAdapterのときはBindableとViewプロパティ値で双方向に同期する。
     * それ以外のときはViewからBindableへの同期のみ。
     */
    public static final class SelectedItem<V> extends EndPoint<ListView, V, V> {

        private AdapterView.OnItemClickListener listener;

        @Override
        protected void onBind() {

            ListView listView = getTargetNotNull();
            listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            listener = new AdapterView.OnItemClickListener() {
                @Override
                @SuppressWarnings("unchecked")
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object item = getTargetNotNull().getItemAtPosition(position);
                    set(item instanceof NamedItemAdapter.Item ? ((NamedItemAdapter.Item<V>)item).value : (V)item);
                }
            };
            addOnItemClickListener(listView, listener);

        }

        @Override
        protected void onValueChanged() {

            ListView listView = getTargetNotNull();
            int curPos = listView.getCheckedItemPosition();
            Adapter adapter = listView.getAdapter();
            if ( adapter instanceof NamedItemAdapter ) {
                @SuppressWarnings("unchecked")
                int newPos = ((NamedItemAdapter<V>)adapter).positionOf(convert());
                if ( curPos != newPos )
                    listView.setItemChecked(newPos, true);
            }

        }

    }


    /**
     * HashMapにリストの選択されたアイテムをバインドするためのEndPoint。
     * サポートするBindableの値はMap&lt;Long, Object&gt;のみ。それ以外の型の値が入っているときは上書きされる。
     * このEndPointは{@link ListView#setMultiChoiceModeListener(AbsListView.MultiChoiceModeListener)}を使用する。
     *
     */
    public static final class MultipleModalSelectedItems<V> extends EndPoint<ListView, Map<Long, V>, Map<Long, V>> {

        final MultipleModalSelectedItems<V> self = this;
        final Logger log = Logger.create(getClass());
        final AbsListView.MultiChoiceModeListener multiChoiceModeListener;
        private final AbsListView.MultiChoiceModeListener multiChoiceModeListenerWrapper =
                new AbsListView.MultiChoiceModeListener() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                //log.v();
                return multiChoiceModeListener.onCreateActionMode(mode, menu);
            }


            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                //log.v();
                return multiChoiceModeListener.onPrepareActionMode(mode, menu);
            }


            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                //log.v();
                return multiChoiceModeListener.onActionItemClicked(mode, item);
            }


            @Override
            public void onDestroyActionMode(ActionMode mode) {
                //log.v();
                multiChoiceModeListener.onDestroyActionMode(mode);
                Map<Long, V> selectedValues = get();
                if ( selectedValues != null && selectedValues.size() > 0 ) {
                    selectedValues.clear();
                    getBindable().notifyValueChanged(self);
                }

            }


            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                //log.v("pos=%d, id=%d, checked=%b", position, id, checked);
                setItemChecked(id, checked);
                multiChoiceModeListener.onItemCheckedStateChanged(mode, position, id, checked);
            }
        };


        public MultipleModalSelectedItems(AbsListView.MultiChoiceModeListener listener) {
            this.multiChoiceModeListener = listener;
        }


        @Override
        protected void onBind() {

            ListView list = getTargetNotNull();

            list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            list.setMultiChoiceModeListener(multiChoiceModeListenerWrapper);

        }


        /**
         * Bindableの値を更新する。
         * @param id 選択状態を変更するアイテムのID。
         * @param checked 選択状態。
         */
        void setItemChecked(long id, boolean checked) {

//            log.v("id=%d, checked=%b", id, checked);
            // idでリストアイテムを探す。

            Adapter adapter = getTargetNotNull().getAdapter();
            if (!(adapter instanceof NamedItemAdapter)) return;

            @SuppressWarnings("unchecked")
            NamedItemAdapter<V> namedItemAdapter = (NamedItemAdapter<V>) adapter;
            NamedItemAdapter.Item<V> listItem = namedItemAdapter.findItemById(id);
            if ( listItem == null ) {
                checked = false;
//                log.v("namedItemAdapter.findItemById(id) == null");
            }

            // Bindableにアイテムの値を入れる、または削除する。

//            log.v("itemValue=%s", itemValue.toString());
            Map<Long, V> selectedValues = convert();
            boolean created = false;
            if (selectedValues == null) {
//                log.v("Bindableの中身(HashSet)を作成");
                selectedValues = new HashMap<>();
                created = true;
            }
            boolean contains = selectedValues.containsKey(id);
//            log.v("selectedItem.contains(): %b", contains);
            if (checked) {
                if (!contains) {
//                    log.v("selectedItemsにitemValueを追加");
                    selectedValues.put(id, listItem.value);
                }
            }
            else {
                if (contains) {
//                    log.v("selectedItemsからitemValueを削除");
                    selectedValues.remove(id);
                }
            }
            boolean changed = checked != contains;

            // 変更通知

            log.v("id=%d, checked=%b, created=%b, changed=%b", id, checked, created, changed);
            log.v("%s", selectedValues);
            if (created)
                set(selectedValues);
            else if (changed)
                getBindable().notifyValueChanged(this);

        }


        @Override
        protected void onValueChanged() {

            ListView list = getTargetNotNull();
            list.clearChoices();
            Map<Long, V> selectedItems = convert();
            if (selectedItems == null) return;

            Adapter adapter = list.getAdapter();
            if (!(adapter instanceof NamedItemAdapter)) return;

            @SuppressWarnings("unchecked")
            NamedItemAdapter<V> namedItemAdapter = (NamedItemAdapter<V>) adapter;
            for (long id : selectedItems.keySet()) {
                int pos = namedItemAdapter.positionOf(selectedItems.get(id));
                if ( pos >= 0 )
                    list.setItemChecked(pos, true);
            }

        }

    }


    private ListViewProp() {}

}
