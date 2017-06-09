# PLJPBinding

## インストール

jarパッケージをlibsに入れる。
Jakarta Commons Collections 4も必要。

## 使い方

### 変数を定義

ViewModelクラスを定義。
ViewModelクラスにデータ用のフィールドを作成。

```Java
public final Var mydata = Var.create(5);
public final Bindable<MyClass> = Bindable.create();
```

#### フィールドのタイプ

- Var : どんな型の値でも入れることができる。(Bindable&lt;Object>と同じ)
- Bindable&lt;T> : 入れる値の型を限定できる。

### Viewにバインドする

```Java
TextView mytext = vm.mydata.bind(rootView, R.id.mytext, new TextViewProp.Text());
```

バインドするとBindableの値がViewに設定される。

### Formatter

```Java
Formatter f = new Formatter() {
    @Override public Object format(Object value) {
        return String.format("私のデータは %s です。", Converter.toString(value));
    }
}
TextView mytext = vm.mydata.bind(rootView, R.id.mytext, new TextViewProp.Text(), f);
```

### Viewのイベント

```Java
CheckBox mycheck = vm.check.bind(rootView, R.id.mycheck, new CompoundButtonProp.Checked());
CompoundButtonProp.addOnClickListener(mycheck, listener);
```

バインディングでsetOn～メソッドでリスナーを登録してしまうので、リスナーを上書きしないこと。
自分のリスナーを登録したいときは、Propクラスにリスナーを複数登録できるメソッドがあるのでこれを利用する。
このメソッドはリスナーを弱参照で登録するので、どこかに強参照を残しておかないとすぐにリスナーが消えてしまう。

### Bindableの更新を監視する

EndPointを作ってbindする。

```Java
EndPoint<Void> endPoint = new EndPoint<Void>() {
    @Override protected void onBind() {...}
    @Override protected void onValueChanged() { ... }
}
vm.mydata.bind(null, endPoint);
```

### Spinnerに利用すると便利なAdapter

```Java
public final Var selectedItem = Var.create(0);

Spinner mySpinner = selectedItem.bind(rootView, R.id.myspinner, new SpinnerProp.SelectedItem());
NamedItemAdapter<Integer> adapter = NamedItemAdapter.createForSpinner(context);
adapter.addAll(R.array.strarr, new Integer[] {-1, 0, 1});
mySpinner.setAdapter(adapter);
```
