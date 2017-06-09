package jp.programminglife.binding;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.io.Serializable;

public interface Bindable<Value> extends ConvertUtils.Convertible {

    /**
     * このオブジェクトにtargetをバインドする。
     * @param rootView idで検索するビューのルート。
     * @param id バインドするターゲットのビューID。
     * @param endPoint targetがオブジェクトにアクセスするための終端インターフェイス。
     * @param <Target> targetの型。
     * @param <TargetValue> targetが期待する型。Bindableの保持する値をConverterで変換した後の型。
     */
    @NonNull
    <Target extends View, TargetValue> Target bind(
            @NonNull View rootView, int id,
            @NonNull EndPoint<? super Target, Value, TargetValue> endPoint);

    /**
     * このオブジェクトにtargetをバインドする。
     * @param rootView idで検索するビューのルート。
     * @param id バインドするターゲットのビューID。
     * @param endPoint targetがオブジェクトにアクセスするための終端インターフェイス。
     * @param notify バインドしたときに、EndPointのnotifyValueChanged()を呼び出す(つまりViewをアップデートする)ならtrue。
     * @param converter EndPoint向けに値を変換するConverter。nullの場合はデフォルトのコンバーターがセットされる。
     * @param <Target> targetの型。
     * @param <TargetValue> targetが期待する型。Bindableの保持する値をConverterで変換した後の型。
     */
    @NonNull
    <Target extends View, TargetValue> Target bind(
            @NonNull View rootView, int id,
            @NonNull EndPoint<? super Target, Value, TargetValue> endPoint,
            boolean notify, Converter<Value, TargetValue> converter);

    /**
     * このオブジェクトにtargetをバインドする。
     * @param activity idで検索するビューのルート。
     * @param id バインドするターゲットのビューID。
     * @param endPoint targetがオブジェクトにアクセスするための終端インターフェイス。
     * @param <Target> targetの型。
     * @param <TargetValue> targetが期待する型。Bindableの保持する値をConverterで変換した後の型。
     */
    @NonNull
    <Target extends View, TargetValue> Target bind(
            @NonNull Activity activity, int id,
            @NonNull EndPoint<? super Target, Value, TargetValue> endPoint);

    /**
     * このオブジェクトにtargetをバインドする。
     * @param activity idで検索するビューのルート。
     * @param id バインドするターゲットのビューID。
     * @param endPoint targetがオブジェクトにアクセスするための終端インターフェイス。
     * @param notify バインドしたときに、EndPointのnotifyValueChanged()を呼び出す(つまりViewをアップデートする)ならtrue。
     * @param converter EndPoint向けに値を変換するConverter。nullの場合はデフォルトのコンバーターがセットされる。
     * @param <Target> targetの型。
     * @param <TargetValue> targetが期待する型。Bindableの保持する値をConverterで変換した後の型。
     */
    @NonNull
    <Target extends View, TargetValue> Target bind(
            @NonNull Activity activity, int id,
            @NonNull EndPoint<? super Target, Value, TargetValue> endPoint, boolean notify,
            Converter<Value, TargetValue> converter);

    /**
     * このオブジェクトにtargetをバインドする。
     * @param target バインドするオブジェクト。
     * @param endPoint targetがオブジェクトにアクセスするための終端インターフェイス。
     * @param <Target> targetの型。
     * @param <TargetValue> targetが期待する型。Bindableの保持する値をConverterで変換した後の型。
     */
    <Target, TargetValue> void bind(Target target, @NonNull EndPoint<? super Target, Value, TargetValue> endPoint);

    /**
     * このオブジェクトにtargetをバインドする。
     * @param target バインドするオブジェクト。
     * @param endPoint targetがオブジェクトにアクセスするための終端インターフェイス。
     * @param notify バインドしたときに、EndPointのnotifyValueChanged()を呼び出す(つまりtargetをアップデートする)ならtrue。
     * @param converter EndPoint向けに値を変換するConverter。nullの場合はデフォルトのコンバーターがセットされる。
     * @param <Target> targetの型。
     * @param <TargetValue> targetが期待する型。Bindableの保持する値をConverterで変換した後の型。
     */
    <Target, TargetValue> void bind(
            Target target, @NonNull EndPoint<? super Target, Value, TargetValue> endPoint,
            boolean notify, Converter<Value, TargetValue> converter);

    /**
     * このオブジェクトにオブザーバーをバインドする。
     * @param observer 値が変化したときに呼び出されるアクション。EndPointのonValueChanged()の通知だけに応答する。
     */
    void bind(Action<Value> observer);

    /**
     * このオブジェクトにオブザーバーをバインドする。
     * @param observer 値が変化したときに呼び出されるアクション。EndPointのonValueChanged()の通知だけに応答する。
     * @param notify バインドしたときに、observerを呼び出すならtrue。
     */
    void bind(Action<Value> observer, boolean notify);

    /*
     * 監視対象のBindableの変更通知に連動してこのBindableから変更通知を出すようにする。
     * 変更通知が連動するだけで値は変更されない。
     * @param b 監視対象のBindable
     */
    //@Deprecated
    //<BindableValue> void observe(@NonNull Bindable<BindableValue> b);

    <BindableValue> void observe(@NonNull Bindable<BindableValue> b);

    /**
     * このBindableで変更があったらconverterを通してtargetに値をセットするように設定する。
     */
    <T> void attach(@NonNull Bindable<T> target, @NonNull Converter<Value, T> converter);

    /**
     * 値をセットする。
     * @param value セットする値。
     */
    void set(Value value);

    /**
     * EndPointから値をセットする。sourceで指定されているEndPointの中からのみ呼び出すこと。
     * @param value セットする値。
     * @param source 値の出どころ。
     */
    void set(Value value, @Nullable EndPoint<?, Value, ?> source);

    Value get();

    @NonNull Value get(@NonNull Value defaultValue);

    boolean isNull();

    /**
     * このBindableの値をserializerでSerializableに変換して返す。
     * @return 変換された値。serializerが登録されていなければ値をそのまま(値がSerializableの場合)、またはnullを返す。
     * @throws IllegalStateException serializerがnullで、値がSerializableではないとき。
     */
    @Nullable Serializable getSerializableValue() throws IllegalStateException;

    /**
     * シリアライズされたデータから値を復元する。
     * このメソッドでは通知を出さずに値を変更する。
     * @param ser シリアライズされた値。
     */
    void restore(@Nullable Serializable ser);

    void notifyValueChanged(@Nullable EndPoint<?, Value, ?> source);

    void notifyValueRestored();
}
