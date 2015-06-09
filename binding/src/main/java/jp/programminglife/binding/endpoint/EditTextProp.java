package jp.programminglife.binding.endpoint;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import jp.programminglife.binding.Converter.ToStringConverter;
import jp.programminglife.binding.EndPoint;


public class EditTextProp extends TextViewProp {

    /**
     * textプロパティ。値はString型。
     * バインドすると{@link EditText#addTextChangedListener(TextWatcher)}メソッドでTextWatcherを追加する。
     */
    public static final class Text extends EndPoint<EditText, Object, String> {

        public Text() {
            super(new ToStringConverter<>());
        }


        @Override
        protected void onBind() {
            getTargetNotNull().addTextChangedListener(new TextWatcher() {
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void afterTextChanged(Editable s) {
                    set(s.toString());
                }
            });
        }

        @Override
        protected void onValueChanged() {

            String received = convert("");
            EditText target = getTargetNotNull();
            if ( received.contentEquals(target.getText()) )
                return;
            target.setText(received);

        }
    }

}
