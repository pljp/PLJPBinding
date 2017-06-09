package jp.programminglife.binding;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.io.Serializable;

public class BindableNonNull<Value> implements Bindable<Value> {

    private final BindableObject<Value> bindable;


    @NonNull
    public static <V> BindableNonNull<V> create(@NonNull V value) {
        return new BindableNonNull<>(value);
    }


    private BindableNonNull(Value value) {
        bindable = BindableObject.create(value);
    }


    @NonNull
    @Override
    public <Target extends View, TargetValue> Target bind(
            @NonNull View rootView, int id,
            @NonNull EndPoint<? super Target, Value, TargetValue> endPoint) {
        return bindable.bind(rootView, id, endPoint);
    }


    @NonNull
    @Override
    public <Target extends View, TargetValue> Target bind(
            @NonNull View rootView, int id,
            @NonNull EndPoint<? super Target, Value, TargetValue> endPoint,
            boolean notify, Converter<Value, TargetValue> converter) {
        return bindable.bind(rootView, id, endPoint, notify, converter);
    }


    @NonNull
    @Override
    public <Target extends View, TargetValue> Target bind(
            @NonNull Activity activity, int id,
            @NonNull EndPoint<? super Target, Value, TargetValue> endPoint) {
        return bindable.bind(activity, id, endPoint);
    }


    @NonNull
    @Override
    public <Target extends View, TargetValue> Target bind(
            @NonNull Activity activity, int id,
            @NonNull EndPoint<? super Target, Value, TargetValue> endPoint,
            boolean notify, Converter<Value, TargetValue> converter) {
        return bindable.bind(activity, id, endPoint, notify, converter);
    }


    @Override
    public <Target, TargetValue> void bind(
            Target target,
            @NonNull EndPoint<? super Target, Value, TargetValue> endPoint) {bindable.bind(target, endPoint);}


    @Override
    public <Target, TargetValue> void bind(
            Target target,
            @NonNull EndPoint<? super Target, Value, TargetValue> endPoint,
            boolean notify, Converter<Value, TargetValue> converter) {
        bindable.bind(target, endPoint, notify, converter);
    }


    @Override
    public void bind(Action<Value> observer) {
        bindable.bind(observer);
    }


    @Override
    public void bind(Action<Value> observer, boolean notify) {
        bindable.bind(observer, notify);
    }


    @Override
    public <BindableValue> void observe(@NonNull Bindable<BindableValue> b) {bindable.observe(b);}


    @Override
    public <T> void attach(
            @NonNull Bindable<T> target,
            @NonNull Converter<Value, T> converter) {bindable.attach(target, converter);}


    @Override
    public void set(@NonNull Value value) {bindable.set(value);}


    @Override
    public void set(@NonNull Value value, EndPoint<?, Value, ?> source) {bindable.set(value, source);}


    @Override
    public void notifyValueChanged(EndPoint<?, Value, ?> source) {bindable.notifyValueChanged(source);}


    @NonNull
    @Override
    public Value get() {return bindable.get();}


    @NonNull
    @Override
    public Value get(@NonNull Value defaultValue) {return bindable.get(defaultValue);}


    @Override
    public boolean isNull() {return bindable.isNull();}


    @Nullable
    @Override
    public Serializable getSerializableValue() {return bindable.getSerializableValue();}


    @Override
    public void restore(Serializable ser) {bindable.restore(ser);}


    @Override
    public void notifyValueRestored() {bindable.notifyValueRestored();}


    @NonNull
    public BindableNonNull<Value> serializer(
            @Nullable Serializer<Value> serializer) {bindable.serializer(serializer); return this;}


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
}
