package jp.programminglife.binding.endpoint;

import android.app.ActionBar;
import android.widget.SpinnerAdapter;

import jp.programminglife.binding.Converter.ToIntegerConverter;
import jp.programminglife.binding.EndPoint;
import jp.programminglife.binding.NamedItemAdapter;


public class ActionBarProp {

    /**
     * Bindableに選択されたアイテムの位置をバインドする。
     * このEndPointはActionVarをリストナビゲーションモードに設定する。
     */
    public static final class SelectedItemPosition extends EndPoint<ActionBar, Object, Integer> {

        private SpinnerAdapter adapter;


        public SelectedItemPosition(SpinnerAdapter adapter) {
            super(new ToIntegerConverter<>());
            this.adapter = adapter;
        }


        @Override
        protected void onBind() {
            ActionBar actionBar = getTargetNotNull();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            actionBar.setListNavigationCallbacks(adapter, new ActionBar.OnNavigationListener() {
                @Override
                public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                    set(itemPosition);
                    return true;
                }
            });

        }


        @Override
        protected void onValueChanged() {

            ActionBar actionBar = getTargetNotNull();
            int curVal = actionBar.getSelectedNavigationIndex();
            int newVal = convert(0);
            if ( curVal != newVal )
                actionBar.setSelectedNavigationItem(newVal);

        }
    }


    public static final class SelectedItem<I> extends EndPoint<ActionBar, I, I> {

        private NamedItemAdapter<I> adapter;


        public SelectedItem(NamedItemAdapter<I> adapter) {
            this.adapter = adapter;
        }


        @Override
        protected void onBind() {

            ActionBar actionBar = getTargetNotNull();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            actionBar.setListNavigationCallbacks(adapter, new ActionBar.OnNavigationListener() {
                @Override
                public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                    set(adapter.getItem(itemPosition).value);
                    return true;
                }
            });

        }


        @Override
        protected void onValueChanged() {

            ActionBar actionBar = getTargetNotNull();
            int curPos = actionBar.getSelectedNavigationIndex();
            int newPos = adapter.positionOf(convert());
            if ( curPos != newPos)
                actionBar.setSelectedNavigationItem(newPos);

        }
    }


}
