package jp.programminglife.binding.endpoint;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import jp.programminglife.binding.Converter.ToIntegerConverter;
import jp.programminglife.binding.EndPoint;
import jp.programminglife.binding.NamedItemAdapter;


public class SpinnerProp extends AdapterViewProp {

    /**
     * Spinnerで選択されたアイテムの位置にバインドするEndPoint。
     * {@link Spinner#setOnItemSelectedListener(OnItemSelectedListener)}
     * を使用するので、SpinnerにOnItemSelectedListenerを追加したい場合は
     * {@link jp.programminglife.binding.endpoint.AdapterViewProp#addOnItemSelectedListener(Spinner, OnItemSelectedListener)}
     * で追加する。
     */
    public static final class SelectedPosition extends EndPoint<Spinner, Object, Integer> {

        private OnItemSelectedListener listener;


        public SelectedPosition() {
            super(new ToIntegerConverter<>());
        }


        @Override
        protected void onBind() {

            listener = new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    set(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    set(Spinner.INVALID_POSITION);
                }
            };
            addOnItemSelectedListener(getTarget(), listener);

        }


        @Override
        protected void onValueChanged() {

            Spinner spinner = getTargetNotNull();
            int curVal = spinner.getSelectedItemPosition();
            int newVal = convert(Spinner.INVALID_POSITION);
            if ( curVal != newVal )
                spinner.setSelection(newVal);

        }

    }


    /**
     * SpinnerのAdapterがNamedItemAdapterのときはBindableとViewプロパティ値で双方向に同期する。
     * それ以外のときはViewからBindableへの同期のみ。
     * @param <I> データの型。
     */
    public static final class SelectedItem<I> extends EndPoint<Spinner, I, I> {

        private OnItemSelectedListener listener;

        @Override
        protected void onBind() {

            listener = new OnItemSelectedListener() {

                @Override
                @SuppressWarnings("unchecked")
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Object item = getTargetNotNull().getItemAtPosition(position);
                    set(item instanceof NamedItemAdapter.Item ?
                            ((NamedItemAdapter.Item<I>) item).value : (I) item);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    set(null);
                }
            };
            addOnItemSelectedListener(getTarget(), listener);

        }

        @Override
        protected void onValueChanged() {

            Spinner spinner = getTargetNotNull();
            Adapter adapter = spinner.getAdapter();
            if ( adapter instanceof NamedItemAdapter ) {
                int curPos = spinner.getSelectedItemPosition();
                @SuppressWarnings("unchecked")
                int newPos = ((NamedItemAdapter<I>)adapter).positionOf(get());
                if ( newPos >= 0 && curPos != newPos )
                    spinner.setSelection(newPos);
            }

        }

    }

}
