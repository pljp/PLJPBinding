package jp.programminglife.binding.endpoint;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import java.lang.reflect.Method;

import jp.programminglife.binding.BindingUtils;
import jp.programminglife.binding.Converter.ToIntegerConverter;
import jp.programminglife.binding.EndPoint;
import jp.programminglife.binding.NamedItemAdapter;


public class AdapterViewProp extends ViewGroupProp {

    private static final Method SET_ON_ITEM_CLICK_LISTENER_METHOD = BindingUtils
            .getMethod(AdapterView.class, "setOnItemClickListener", OnItemClickListener.class);
    private static final Method SET_ON_ITEM_SELECTED_LISTENER_METHOD = BindingUtils
            .getMethod(AdapterView.class, "setOnItemSelectedListener", OnItemSelectedListener.class);


    public static void addOnItemClickListener(AdapterView<? extends Adapter> view, OnItemClickListener listener) {
        BindingUtils.addListener(view, SET_ON_ITEM_CLICK_LISTENER_METHOD, listener);
    }


    public static void addOnItemSelectedListener(Spinner view, OnItemSelectedListener listener) {
        BindingUtils.addListener(view, SET_ON_ITEM_SELECTED_LISTENER_METHOD, listener);
    }


    public static final class SelectedPosition extends EndPoint<AdapterView<? extends Adapter>, Object, Integer> {

        private OnItemClickListener listener;


        public SelectedPosition() {
            super(new ToIntegerConverter<>());
        }


        @Override
        protected void onBind() {

            listener = new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    set(position);
                }
            };
            addOnItemClickListener(getTarget(), listener);

        }

        @Override
        protected void onValueChanged() {

            AdapterView<? extends Adapter> adapterView = getTargetNotNull();
            int curVal = adapterView.getSelectedItemPosition();
            int newVal = convert(AdapterView.INVALID_POSITION);
            if ( curVal != newVal )
                adapterView.setSelection(newVal);

        }

    }


    /**
     * AdapterViewのAdapterがNamedItemAdapterのときはBindableとViewプロパティ値で双方向に同期する。
     * それ以外のときはViewからBindableへの同期のみ。
     */
    public static final class SelectedItem<I> extends EndPoint<AdapterView<? extends Adapter>, I, I> {

        private OnItemClickListener listener;

        @Override
        protected void onBind() {

            listener = new OnItemClickListener() {
                @Override
                @SuppressWarnings("unchecked")
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object item = getTargetNotNull().getItemAtPosition(position);
                    set(item instanceof NamedItemAdapter.Item ?
                            (I)((NamedItemAdapter.Item)item).value : (I)item);
                }
            };
            addOnItemClickListener(getTarget(), listener);

        }

        @Override
        protected void onValueChanged() {

            AdapterView<? extends Adapter> adapterView = getTargetNotNull();
            int curPos = adapterView.getSelectedItemPosition();
            Adapter adapter = adapterView.getAdapter();
            if ( adapter instanceof NamedItemAdapter ) {

                @SuppressWarnings("unchecked")
                int newPos = ((NamedItemAdapter<I>)adapter).positionOf(convert());
                if ( curPos != newPos )
                    adapterView.setSelection(newPos);

            }
        }

    }

}
