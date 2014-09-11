DL : Apk动态加载框架
================
![mahua](http://www.renyugang.cn/resources/image/DL.png)
##前言
为了更好地阅读本文，你需要先阅读[Android apk动态加载机制的研究](http://blog.csdn.net/singwhatiwanna/article/details/22597587)这篇文章，在此文中，博主分析了Android中apk的动态加载机制，并在文章的最后指出需要解决的两个复杂问题：资源的访问和activity生命周期的管理，而本文将会分析这两个复杂问题的解决方法。需要说明的一点是，我们不可能调起任何一个未安装的apk，这在技术上是无法实现的，我们调起的apk必须受某种规范的约束，只有在这种约束下开发的apk，我们才能将其调起。另外，本文给出的解决方案也不是完美的，但是逻辑已经可以正常地跑通了，剩下的极个别细节问题是可以优化的。
##资源管理
我们知道，宿主程序调起未安装的apk，一个很大的问题就是资源如何访问，具体来说就是，凡是以R开头的资源都不能访问了，因为宿主程序中并没有apk中的资源，所以通过R来加载资源是行不通的，程序会报错：无法找到某某id所对应的资源。针对这个问题，有人提出了将apk中的资源在宿主程序中也copy一份，这虽然能解决问题，可以一听起来就很奇怪，首先这样会持有两份资源，会增加宿主程序包的大小，其次，没发布一个插件都需要将资源copy到宿主程序中，这样就意味着每发布一个插件都要更新一下宿主程序，这和插件化的思想是相悖的，插件化的目的就是要减小宿主程序apk包的大小同时降低宿主程序的更新频率并做到自由装载模块。所以这种方法并不可行。还有人提供了一种方式：将apk中的资源解压出来，然后通过文件流去读取资源，这样做理论上是可行的，但是实际操作起来还是有很大难度的，首先不同资源有不同的文件流格式，比如图片、xml等，还有就是针对不同设备加载的资源可能是不一样的，如果选择合适的资源也是一个需要解决的问题，基于这两点，这种方法不建议使用，因为它实现起来有难度。下面说说本文所采用的方法。

我们知道，activity的工作主要是由ContextImpl来完成的， 它在activity中是一个叫做mBase的成员变量。注意到Context中有如下两个抽象方法，看起来是和资源有关的，实际上context就是通过它们来获取资源的，这两个抽象方法的真正实现在ContextImpl中。也即是说，只要我们自己实现这两个方法，就可以解决资源问题了。
```java
/** Return an AssetManager instance for your application's package. */
public abstract AssetManager getAssets();
/** Return a Resources instance for your application's package. */
public abstract Resources getResources();
```
下面看一下如何实现这两个方法
首先要加载apk中的资源：
```java
protected void loadResources() {  
    try {  
        AssetManager assetManager = AssetManager.class.newInstance();  
        Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);  
        addAssetPath.invoke(assetManager, mDexPath);  
        mAssetManager = assetManager;  
    } catch (Exception e) {  
        e.printStackTrace();  
    }  
    Resources superRes = super.getResources();  
    mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(),  
            superRes.getConfiguration());  
    mTheme = mResources.newTheme();  
    mTheme.setTo(super.getTheme());  
}
```
说明：加载的方法是通过反射，通过调用AssetManager中的addAssetPath方法，我们可以将一个apk中的资源加载到Resources中，由于addAssetPath是隐藏api我们无法直接调用，所以只能通过反射，下面是它的声明，通过注释我们可以看出，传递的路径可以是zip文件也可以是一个资源目录，而apk就是一个zip，所以直接将apk的路径传给它，资源就加载到AssetManager中了，然后再通过AssetManager来创建一个新的Resources对象，这个对象就是我们可以使用的apk中的资源了，这样我们的问题就解决了。
```java
/** 
 * Add an additional set of assets to the asset manager.  This can be 
 * either a directory or ZIP file.  Not for use by applications.  Returns 
 * the cookie of the added asset, or 0 on failure. 
 * {@hide} 
 */  
public final int addAssetPath(String path) {  
    int res = addAssetPathNative(path);  
    return res;  
}
```
其次是要实现那两个抽象方法
```java
@Override  
public AssetManager getAssets() {  
    return mAssetManager == null ? super.getAssets() : mAssetManager;  
}  
  
@Override  
public Resources getResources() {  
    return mResources == null ? super.getResources() : mResources;  
}
```
okay，问题搞定。这样一来，在apk中就可以通过R来访问资源了。
##activity生命周期的管理
这是本文开头提到的另一个需要解决的难题。为什么会有这个问题，其实很好理解，apk被宿主程序调起以后，apk中的activity其实就是一个普通的对象，不具有activity的性质，因为系统启动activity是要做很多初始化工作的，而我们在应用层通过反射去启动activity是很难完成系统所做的初始化工作的，所以activity的大部分特性都无法使用包括activity的生命周期管理，这就需要我们自己去管理。谈到activity生命周期，其实就是那几个常见的方法：onCreate、onStart、onResume、onPause等，由于apk中的activity不是真正意义上的activity（没有在宿主程序中注册且没有完全初始化），所以这几个生命周期的方法系统就不会去自动调用了。针对此类问题，采用Fragment是一个不错的方法，Fragment从3.0引入，通过support-v4包，可以兼容3.0以下的android版本。Fragment既有类似于Activity的生命周期，又有类似于View的界面，将Fragment加入到Activity中，activity会自动管理Fragment的生命周期，通过第一篇文章我们知道，apk中的activity是通过宿主程序中的代理activity启动的，将Fragment加入到代理activity内部，其生命周期将完全由代理activity来管理，但是采用这种方法，就要求apk尽量采用Fragment来实现，还有就是在做页面跳转的时候有点麻烦，当然关于Fragment相关的内容我将在后面再做研究，本文不采用Fragment而是通过反射去手动管理activity的生命周期。

我们要在代理activity中去反射apk中activity的所有生命周期的方法，然后将activity的生命周期和代理activity的生命周期进行同步。首先，反射activity生命周期的所有方法，还反射了onActivityResult这个方法，尽管它不是典型的生命周期方法，但是它很有用。
```java
protected void instantiateLifecircleMethods(Class<?> localClass) {  
    String[] methodNames = new String[] {  
            "onRestart",  
            "onStart",  
            "onResume",  
            "onPause",  
            "onStop",  
            "onDestory"  
    };  
    for (String methodName : methodNames) {  
        Method method = null;  
        try {  
            method = localClass.getDeclaredMethod(methodName, new Class[] { });  
            method.setAccessible(true);  
        } catch (NoSuchMethodException e) {  
            e.printStackTrace();  
        }  
        mActivityLifecircleMethods.put(methodName, method);  
    }  
  
    Method onCreate = null;  
    try {  
        onCreate = localClass.getDeclaredMethod("onCreate", new Class[] { Bundle.class });  
        onCreate.setAccessible(true);  
    } catch (NoSuchMethodException e) {  
        e.printStackTrace();  
    }  
    mActivityLifecircleMethods.put("onCreate", onCreate);  
  
    Method onActivityResult = null;  
    try {  
        onActivityResult = localClass.getDeclaredMethod("onActivityResult",  
                new Class[] { int.class, int.class, Intent.class });  
        onActivityResult.setAccessible(true);  
    } catch (NoSuchMethodException e) {  
        e.printStackTrace();  
    }  
    mActivityLifecircleMethods.put("onActivityResult", onActivityResult);  
}
```
其次，同步生命周期，主要看一下onResume和onPause，其他方法是类似的。看如下代码，很好理解，就是当系统调用代理activity生命周期方法的时候，就通过反射去显式调用apk中activity的对应方法。
```java
@Override  
protected void onResume() {  
    super.onResume();  
    Method onResume = mActivityLifecircleMethods.get("onResume");  
    if (onResume != null) {  
        try {  
            onResume.invoke(mRemoteActivity, new Object[] { });  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}  
  
@Override  
protected void onPause() {  
    Method onPause = mActivityLifecircleMethods.get("onPause");  
    if (onPause != null) {  
        try {  
            onPause.invoke(mRemoteActivity, new Object[] { });  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    super.onPause();  
}
```
##插件apk的开发规范
文章开头提到，要想成为一个插件apk，是要满足一定条件的，如下是采用本文机制开发插件apk所需要遵循的规范：
1. 不能用this：因为this指向的是当前对象，即apk中的activity，但是由于activity已经不是常规意义上的activity，所以this是没有意义的。
2. 使用that：既然this不能用，那就用that，that是apk中activity的基类BaseActivity中的一个成员，它在apk安装运行的时候指向this，而在未安装的时候指向宿主程序中的代理activity，anyway，that is better than this。
3. 不能直接调用activity的成员方法：而必须通过that去调用，由于that的动态分配特性，通过that去调用activity的成员方法，在apk安装以后仍然可以正常运行。
4. 启动新activity的约束：启动外部activity不受限制，启动apk内部的activity有限制，首先由于apk中的activity没注册，所以不支持隐式调用，其次必须通过BaseActivity中定义的新方法startActivityByProxy和startActivityForResultByProxy，还有就是不支持LaunchMode。
5. 目前暂不支持Service、BroadcastReceiver等需要注册才能使用的组件。
##后续工作
1. DLIntent的定义，通过自定义的intent，来完成activity的无约束调起
2. 采用Fragment的生命周期管理
3. Service、BroadcastReceiver等组件的调起
4. 性能优化

##效果
![mahua](http://img.blog.csdn.net/20140411000445437?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc2luZ3doYXRpd2FubmE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

首先宿主程序运行后，会把位于/mnt/sdcard/DynamicLoadHost目录下的所有apk都加载进来，然后点击列表就可以调起对应的apk，本文中的demo和第一篇文章中的demo看起来差不多，实际是有区别的，区别有两点：activity具有生命周期、加载资源可以用R，具体的代码实现请大家参见源码。
###特别感谢[nealgao](http://nealgao06.lofter.com)为本项目设计的logo。
## License

    Copyright 2014 singwhatiwanna & tianxiao

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
