package jp.programminglife.binding;


import android.app.Activity;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class BindableLongObjectMap<I> implements Bindable<Map<Long, I>> {

    private final BindableObject<Map<Long, I>> bindable;


    public static <II> BindableLongObjectMap<II> create() {return new BindableLongObjectMap<>();}


    public BindableLongObjectMap() {
        bindable = new BindableObject<>();
        bindable.set(new HashMap<Long, I>());
    }


    public void set(long id, I item) {
        bindable.get().put(id, item);
    }


    //
    // delegateメソッド
    //

    @NotNull
    @Override
    public <Target extends View, TargetValue> Target bind(
            @NotNull View rootView, int id,
            @NotNull EndPoint<? super Target, Map<Long, I>, TargetValue> endPoint) {
        return bindable.bind(rootView, id, endPoint);
    }


    @NotNull
    @Override
    public <Target extends View, TargetValue> Target bind(
            @NotNull View rootView, int id,
            @NotNull EndPoint<? super Target, Map<Long, I>, TargetValue> endPoint,
            boolean notify,
            Converter<Map<Long, I>, TargetValue> converter) {
        return bindable.bind(rootView, id, endPoint, notify, converter);
    }


    @NotNull
    @Override
    public <Target extends View, TargetValue> Target bind(
            @NotNull Activity activity, int id,
            @NotNull EndPoint<? super Target, Map<Long, I>, TargetValue> endPoint) {
        return bindable.bind(activity, id, endPoint);
    }


    @NotNull
    @Override
    public <Target extends View, TargetValue> Target bind(
            @NotNull Activity activity, int id,
            @NotNull EndPoint<? super Target, Map<Long, I>, TargetValue> endPoint,
            boolean notify,
            Converter<Map<Long, I>, TargetValue> converter) {
        return bindable.bind(activity, id, endPoint, notify, converter);
    }


    @Override
    public <Target, TargetValue> void bind(
            Target target,
            @NotNull EndPoint<? super Target, Map<Long, I>, TargetValue> endPoint) {bindable.bind(target, endPoint);}


    @Override
    public <Target, TargetValue> void bind(
            Target target,
            @NotNull EndPoint<? super Target, Map<Long, I>, TargetValue> endPoint,
            boolean notify,
            Converter<Map<Long, I>, TargetValue> converter) {bindable.bind(target, endPoint, notify, converter);}


    @Override
    public <T> void attach(
            @NotNull Bindable<T> target,
            @NotNull Converter<Map<Long, I>, T> converter) {bindable.attach(target, converter);}


    @Override
    public void set(@NotNull Map<Long, I> items) {bindable.set(items);}


    @Override
    public void set(
            @NotNull Map<Long, I> items,
            EndPoint<?, Map<Long, I>, ?> source) {bindable.set(items, source);}


    @Override
    public void notifyValueChanged(
            EndPoint<?, Map<Long, I>, ?> source) {bindable.notifyValueChanged(source);}


    @Override
    public Map<Long, I> get() {return bindable.get();}


    @NotNull
    @Override
    public Map<Long, I> get(@NotNull Map<Long, I> defaultValue) {return bindable.get(defaultValue);}


    @Override
    public boolean isNull() {return bindable.isNull();}


    @Nullable
    @Override
    public Serializable getSerializableValue() {return bindable.getSerializableValue();}


    @Override
    public void restore(Serializable ser) {bindable.restore(ser);}


    @Override
    public void notifyValueRestored() {bindable.notifyValueRestored();}


    @NotNull
    public BindableLongObjectMap<I> serializer(
            @Nullable Serializer<Map<Long, I>> serializer) {bindable.serializer(serializer); return this;}


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


    @NotNull
    @Override
    public Number toNumber(@NotNull Number defaultValue) {return bindable.toNumber(defaultValue);}


    @Nullable
    @Override
    public Number toNumber() {return bindable.toNumber();}


    @NotNull
    @Override
    public String toString(@NotNull String defaultValue) {return bindable.toString(defaultValue);}


    @Nullable
    @Override
    public String toStringOrNull() {return bindable.toStringOrNull();}
}
