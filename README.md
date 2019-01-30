# AdapterSuperior
### 简介以及背景简介
>这是一个**RecyclerView.Adapter**组件库, 可以快速自动化构造一个干净有效可用**RecyclerView.Adapter**。通常绘制的**RecyclerView**列表视图过程中，编写的**RecyclerView.Adapter**大部分代码基本可以视为‘冗余’重复的代码，且又不能复用导致浪费一半的开发时间。通过使用这个组件则可以很好节省这部分时间。它是通过依赖注入方式来使用，**RecyclerView.Adapter**++由编译器自动生成，不再不需要开发人员去编写++，这就意味着直接抹去编写**RecyclerView.Adapter**需要的时间，以及产生的**BUG**数。开发人员只需要根据需求实现**RecyclerView.ViewHolder**这部分代码。并且**RecyclerView.ViewHolder**是通过++组合方式++构建自动化生成的**RecyclerView.Adapter**. 从而使得可复用，解耦脱离**RecyclerView.Adapter**的业务依赖编码.






## 演示（详细请参考源码演示）







### 创建Demo1Activity.class(下面将会看到)，并定于布局 activity_demo0.xml


```xml

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    >


    <android.support.v7.widget.RecyclerView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/list"
        />


</LinearLayout>



```

### 创建item布局 adapter_demo0.xml


```xml

<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:gravity="center"
    android:orientation="horizontal"
    android:padding="10dp">


    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="位置：" />

    <TextView
        android:id="@+id/tv_position"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="，文本数据：" />

    <TextView
        android:id="@+id/tv_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />


</LinearLayout>


```

### 接着我们需要编写RecyclerView.ViewHolder代码，创建 DemoViewHolder0.class，并创建数据绑定的数据模型DemoModel0（可以使用接口方式，演示方便直接用类）


```java

public class DemoModel0 {
    public String text;

    public DemoModel0(String text) {
        this.text = text;
    }
}


```
注：使用@AdapterHolder注解来提供item视图，其中layoutResName参数代表item的视图布局名字（adapter_demo0.xml）, model代表要绑定的数据模型

每个 **RecyclerView.ViewHolder**子类需要实现IAdapterHolder方法，每个item绘制的时候需要实现已定义方法update.
其中参数为position（当前项的位置）， 所绑定的DemoModel0， list（数据源数组）

```java
@AdapterHolder(layoutResName = "adapter_demo0", model = DemoModel0.class)
public class DemoViewHolder0 extends RecyclerView.ViewHolder implements IAdapterHolder<DemoModel0> {

    TextView tvPosition;

    TextView tvText;

    public DemoViewHolder0(View itemView) {
        super(itemView);
        tvPosition = itemView.findViewById(R.id.tv_position);
        tvText = itemView.findViewById(R.id.tv_text);
    }

    @Override
    public void update(int position, DemoModel0 demoModel0, List list, Object o) {
        if(null == demoModel0)
            return;
        tvPosition.setText(""+i);
        tvText.setText(""+ demoModel0.text);
    }


}

```

### 在Demo1Activity.class使用创建好的DemoViewHolder0.class

注：使用@InjectAdapter来引入DemoViewHolder0.class. 其中IAdapter是一个接口，是编译器实现RecyclerView.Adapter创建的引用， (源码的里面有演示如何提供自己模板的RecyclerView.Adapter).  每个容器里面需要使用   AdapterSuperior.inject(this);来为所有承接的提供RecyclerView.Adapter提供引用

```java


public class Demo1Activity extends Activity {


    RecyclerView recyclerView;

    @InjectAdapter(DemoViewHolder0.class)
    IAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo0);
         AdapterSuperior.inject(this);
        initView();
        initData();
    }

    private void initView() {

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter((RecyclerView.Adapter) adapter);
    }

    private void initData() {

        //添加测试数据
        List list = new ArrayList();
        for (int i = 0; i < 5; i++) {
            list.add(new DemoModel0("哈哈哒"+i));
        }

        adapter.setDataSet(list);
        adapter.notifyDataSetChanged();
        for (int i = 0; i < 1000; i++) {
            list.add(new DemoModel0("呵呵哒哒"+i));
        }
        adapter.notifyDataSetChanged();
    }


}



```
### 编译后,你会发现，Demo1Activit.class包下多了个class文件, Demo1Activity$$Adapter(按shift健+Demo1Activity)


```java

public class Demo1Activity$$Adapter extends SupportDefaultAdapter {
  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    final java.util.List list =  getDataSet();
    final boolean enableObtainData = null !=  list && !list.isEmpty() &&position>=0 && position<list.size();
    if (holder instanceof com.to8to.app.adaptersuperior.demo1.DemoViewHolder0) {
      final com.to8to.app.adaptersuperior.demo1.DemoViewHolder0 tHolder = (com.to8to.app.adaptersuperior.demo1.DemoViewHolder0)holder ;
      if(enableObtainData) {
        tHolder.update(position, (com.to8to.app.adaptersuperior.demo1.DemoModel0) list.get(position) , list, this);
      }
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
    final  android.content.Context context = parent.getContext() ;
    if(0 == viewType) {
      return  new com.to8to.app.adaptersuperior.demo1.DemoViewHolder0(android.view.LayoutInflater.from(context).inflate(com.to8to.app.adaptersuperior.lib.AdapterSuperiorAppUtil.getLayoutResId("adapter_demo0"), parent, false));
    }
    return  new com.to8to.app.adaptersuperior.lib.EmptyAdapterHolder(new android.view.View(parent.getContext()));
  }

  @Override
  public int getItemViewType(final int position) {
    final Object data = this.getItemData(position);
    if( data instanceof com.to8to.app.adaptersuperior.demo1.DemoModel0) return 0;
    return 0xFF666666;
  }
}


```

## 多种类型布局（详细看demo2）

通过使用@AdapterModel来绑定视图类型,viewType代表视图id。为了避免冲突。你可以使用viewTypeIdResName属性来做区分，参数值传安卓中R文件的id值。如果model没有使用到AdapterModel注解。那么这个model在getItemViewType()的值返回为0.


```

@AdapterModel(viewType = 1)
public class DemoModel1 {
    public String text;
    public DemoModel1(String text) {
        this.text = text;
    }
}

@AdapterHolder(layoutResName = "adapter_demo1", model = DemoModel1.class)
public class DemoViewHolder1 extends RecyclerView.ViewHolder implements IAdapterHolder<DemoModel1> {


    TextView tvText;

    public DemoViewHolder1(View itemView) {
        super(itemView);
        tvText = itemView.findViewById(R.id.tv_text);
    }

    @Override
    public void update(int i, DemoModel1 demoModel1, List list, Object o) {
        tvText.setText(""+ demoModel1.text);
    }


}


@AdapterModel(viewType = 2)
public class DemoModel2 {
    public String text;

    public DemoModel2(String text) {
        this.text = text;
    }
}

@AdapterHolder(layoutResName = "adapter_demo2", model = DemoModel2.class)
public class DemoViewHolder2 extends RecyclerView.ViewHolder implements IAdapterHolder<DemoModel2> {


    TextView tvText;

    public DemoViewHolder2(View itemView) {
        super(itemView);
        tvText = itemView.findViewById(R.id.tv_text);
    }

    @Override
    public void update(int i, DemoModel2 demoModel2, List list, Object o) {
        tvText.setText(""+ demoModel2.text);
    }


}

@AdapterModel(viewType = 3)
public interface DemoModel3 {
    String getText();
}


@AdapterHolder(layoutResName = "adapter_demo3", model = DemoModel3.class)
public class DemoViewHolder3 extends RecyclerView.ViewHolder implements IAdapterHolder<DemoModel3> {


    TextView tvText;

    public DemoViewHolder3(View itemView) {
        super(itemView);
        tvText = itemView.findViewById(R.id.tv_text);
    }

    @Override
    public void update(int i, DemoModel3 demoModel3, List list, Object o) {
        tvText.setText(""+ demoModel3.getText());
    }


}


@InjectAdapter(DemoViewHolder0.class)
IAdapter list0Adapter;

@InjectAdapter({DemoViewHolder1.class,DemoViewHolder0.class,DemoViewHolder2.class , DemoViewHolder3.class })
IAdapter list1Adapter;


@InjectAdapter({DemoViewHolder1.class,DemoViewHolder0.class,DemoViewHolder2.class })
IAdapter list2Adapter;





```

## 自定义RecyclerView.Adapter模板
当平常生成的RecyclerView.Adapter不在满足你的使用时候， 你可以自定义RecyclerView.Adapter的生成模板结构。不过需要实现IAdapter这个接口提供数据获取。



```java

public abstract class MyAdapter extends RecyclerView.Adapter implements IAdapter {

    public List itemListData;

    public MyAdapter() {
    }

    public int getItemCount() {
        return null != this.itemListData ? this.itemListData.size() : 0;
    }

    public Object getItemData(int position) {
        return this.itemListData.get(position);
    }

    public List getDataSet() {
        return this.itemListData;
    }

    public void setDataSet(List itemListData) {
        this.itemListData = itemListData;
    }
}



public class CustomAdapterActivity extends Activity {

    //..............


    @InjectAdapter(value = {TestViewHolder.class, Test2ViewHolder.class}, parent = MyAdapter.class)
    MyAdapter adapter;

   //..............




}


```


### 使用@AdapterConfig
在默认情况下， 生成的适配器会覆盖模板的3个方法:onCreateViewHolder, onBindViewHolder, getItemViewType.
如果不想生成的适配器某个方法覆盖模板的方法，如getItemViewType,那么给overrideGetItemViewType复制false即可。
如果不赋值，默认值是true，也就是默认是生成覆盖模板的方法。

```java

@AdapterConfig(overrideGetItemViewType = false, overrideOnBindViewHolder = true, overrideOnCreateViewHolder = true)
public abstract class MyAdapter extends RecyclerView.Adapter implements IAdapter {

    // ..............
}

```















## 注意事项：

### 1. 在同一IAdapter注入多个布局类型RecyclerView.ViewHolder绑定的Model可以有相同的父类，但是不能相互之间有派生关系。如运行model1 继承 b,  model2 继承 b. 但是不允许出现有关系model1继承model2去使用。
### 2. 如果运行后发现没有Adapter编译出来，请Rebuild下工程（这种情况下比较少见）
### 3. item的布局名字在整项目之中必须唯一的.如在A模块创建了adapter_a.xml。就不能在b模块创建同一样文件的adapter_a.xml










