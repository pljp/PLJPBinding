package jp.programminglife.binding;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.io.Serializable;


public final class Var implements Bindable<Object> {

    @NonNull
    private final BindableObject<Object> bindable;


    public static Var create() {
        return new Var(BindableObject.create());
    }


    public static Var create(Object value) {
        return new Var(BindableObject.create(value));
    }


    private Var(@NonNull BindableObject<Object> bindable) {
        this.bindable = bindable;
    }


    @NonNull
    @Override
    public <Target extends View, TargetValue> Target bind(@NonNull View rootView, int id,
            @NonNull EndPoint<? super Target, Object, TargetValue> endPoint) {
        return bindable.bind(rootView, id, endPoint);
    }


    @NonNull
    @Override
    public <Target extends View, TargetValue> Target bind(@NonNull View rootView, int id,
            @NonNull EndPoint<? super Target, Object, TargetValue> endPoint,
            boolean notify, Converter<Object, TargetValue> converter) {
        return bindable.bind(rootView, id, endPoint, notify, converter);
    }


    @NonNull
    @Override
    public <Target extends View, TargetValue> Target bind(@NonNull Activity activity, int id,
            @NonNull EndPoint<? super Target, Object, TargetValue> endPoint) {
        return bindable.bind(activity, id, endPoint);
    }


    @NonNull
    @Override
    public <Target extends View, TargetValue> Target bind(@NonNull Activity activity, int id,
            @NonNull EndPoint<? super Target, Object, TargetValue> endPoint,
            boolean notify, Converter<Object, TargetValue> converter) {
        return bindable.bind(activity, id, endPoint, notify, converter);
    }


    @Override
    public <Target, TargetValue> void bind(Target target,
            @NonNull EndPoint<? super Target, Object, TargetValue> endPoint) {bindable.bind(target, endPoint);}


    @Override
    public <Target, TargetValue> void bind(Target target,
            @NonNull EndPoint<? super Target, Object, TargetValue> endPoint,
            boolean notify, Converter<Object, TargetValue> converter) {
        bindable.bind(target, endPoint, notify, converter);
    }


    @Override
    public void bind(Action<Object> observer) {
        bindable.bind(observer);
    }


    @Override
    public void bind(Action<Object> observer, boolean notify) {
        bindable.bind(observer);
    }


    @Override
    public <BindableValue> void observe(@NonNull Bindable<BindableValue> b) {bindable.observe(b);}


    @Override
    public <T> void attach(@NonNull Bindable<T> target, @NonNull Converter<Object, T> converter) {
        bindable.attach(target, converter);
    }


    @Override
    public void set(Object value) {bindable.set(value);}


    @Override
    public void set(Object o, EndPoint<?, Object, ?> source) {bindable.set(o, source);}


    @Override
    public void notifyValueChanged(EndPoint<?, Object, ?> source) {bindable.notifyValueChanged(source);}


    @Override
    public Object get() {return bindable.get();}


    @NonNull
    @Override
    public Object get(@NonNull Object defaultValue) {
        return bindable.get(defaultValue);
    }


    @Override
    public boolean isNull() {return bindable.isNull();}


    @Override
    public Serializable getSerializableValue() {return bindable.getSerializableValue();}


    @Override
    public void restore(Serializable ser) {bindable.restore(ser);}


    @Override
    public void notifyValueRestored() {bindable.notifyValueRestored();}


    @Override
    public boolean toBoolean(boolean defaultValue) {return bindable.toBoolean(defaultValue);}


    @Override
    public int toInt(int defaultValue) {return bindable.toInt(defaultValue);}


    @Override
    public long toLong(long defaultValue) {return bindable.toLong(defaultValue);}


    @Override
    public float toFloat(float defaultValue) {return bindable.toFloat(defaultValue);}


    @Override
    public double toDouble(double defaultValue) {return bindable.toDouble(defaultValue);}


    @NonNull
    @Override
    public Number toNumber(@NonNull Number defaultValue) {return bindable.toNumber(defaultValue);}


    @Nullable
    @Override
    public Number toNumber() {return bindable.toNumber();}


    @NonNull
    @Override
    public String toString(@NonNull String defaultValue) {return bindable.toString(defaultValue);}


    @Nullable
    @Override
    public String toStringOrNull() {return bindable.toStringOrNull();}


    @Override
    public boolean equals(Object o) {

        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;

        Var var = (Var) o;

        return bindable.equals(var.bindable);
    }


    @Override
    public int hashCode() {

        return bindable.hashCode();
    }
}
