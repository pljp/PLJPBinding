package jp.programminglife.binding.endpoint;

import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import java.lang.reflect.Method;

import jp.programminglife.binding.BindingUtils;
import jp.programminglife.binding.Converter.ToIntegerConverter;
import jp.programminglife.binding.EndPoint;


public class RadioGroupProp extends LinearLayoutProp {

    private static final Method SET_ON_CHECKED_CHANGE_LISTENER_METHOD = BindingUtils.getMethod(RadioGroup.class, "setOnCheckedChangeListener", OnCheckedChangeListener.class);


    public static void addOnCheckedChangeListener(RadioGroup view, OnCheckedChangeListener listener) {
        BindingUtils.addListener(view, SET_ON_CHECKED_CHANGE_LISTENER_METHOD, listener);
    }


    /**
     * RadioGroupのcheckedRadioButtonIdプロパティ。
     */
    public static final class CheckedButtonId extends EndPoint<RadioGroup, Object, Integer> {

        private OnCheckedChangeListener listener;


        public CheckedButtonId() {
            super(new ToIntegerConverter<>());
        }


        @Override
        protected void onBind() {

            listener = new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    set(checkedId);
                }
            };
            addOnCheckedChangeListener(getTarget(), listener);

        }

        @Override
        protected void onValueChanged() {

            int id = convert(-1);
            RadioGroup group = getTargetNotNull();
            if ( id != group.getCheckedRadioButtonId() ) {
                if ( id == -1 )
                    group.clearCheck();
                else
                    group.check(id);
            }

        }

    }

}
