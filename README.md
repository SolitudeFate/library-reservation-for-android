# 2023年4月30日更新

已经被馆长发现了，不可以用程序预约了哦

# 声明

**建议：建议11点半左右使用此APP预约，预约太早可能会被管理员发现，后果自负**

学校图书馆公众号只有在中午12点以后才可以预约第二天的座位，使用此APP可以在当天中午12点之前预约第二天的座位

设计APP初衷是因为有时电脑不在身边，不方便运行Python脚本，正好刚学完Android，就动手做了该APP，**方便自己使用**

APP适用于经常约同一个位置，因为你的座位和馆室自己是清楚的，所以做了记住上一次输入的功能

# 使用方法

下载：[library reservation for android APP](https://github.com/SolitudeFate/library-reservation-for-android/releases/download/v1.0/library-reservation-v1.0.apk)

将apk通过QQ发送到手机下载。**不能通过微信发送！（微信会给 .apk 添加 .p 后缀）**

该apk在项目的保存位置：`app/release`

+ 第一次打开APP，无论输入是否正确，都会闪退

+ 图书馆馆室、座位号必须是正确的，不然会闪退

+ JSESSIONID如果输入错误或者失效，会返回页面的HTML代码，只需要重新获取当前的JSESSIONID即可解决

+ 点击多次**预约按钮**会很卡，因为在发送请求，别折磨它

+ 只要输入内容正确，就不会闪退，点击预约按钮后，只需要等待即可

+ 不要来测我的bug，全是bug！

# 技术栈

## Material Design 2

对TextInputLayout使用findViewById，然后获取文本内容，应将id设置在内部的TextInputEditText上，不然会闪退。

## Chaquopy

Python SDK for Android

**在Android应用程序中使用Python的最简单方法**

Chaquopy提供了在Android应用程序中包含Python组件所需的一切，包括：

- 与Android Studio的标准Gradle构建系统完全集成。

- 用于从 Java/Kotlin 调用 Python 代码的简单 API，反之亦然。

- 广泛的第三方Python包，包括SciPy，OpenCV，TensorFlow等等。

### 配置及使用Chaquopy参考资料

- [Chaquopy官方文档](https://chaquo.com/chaquopy/doc/current/android.html)

- [Yuko Araki的博客-CSDN博客](https://blog.csdn.net/yukoaraki/category_10361755.html)

### 基本设置

在项目目录下的`build.gradle`添加：

```
plugins {
 id 'com.chaquo.python' version '14.0.2' apply false
}
```

在模块级目录下`app/build.gradle`添加：

```
plugins {
    id 'com.android.application'
    id 'com.chaquo.python'
}
```

#### ABI 选择

目前可用的 ABI 是：

- `armeabi-v7a`，几乎所有安卓设备都支持。

- `arm64-v8a`，受最新的安卓设备支持。

- `x86`，适用于安卓模拟器。

- `x86_64`，适用于安卓模拟器。

在模块级目录下`app/build.gradle`添加：

```
android{
    defaultConfig {
        ndk {
           abiFilters "armeabi-v7a", "x86"
        }
    }
}
```

如需在模拟器调试，添加"x86"，否则指定"armeabi-v7a"即可

#### Python版本

默认是3.8

在模块级目录下`app/build.gradle`添加：

```
android{
    defaultConfig {
        python {
            //根据自己的Python版本
            version "3.10"
        }
    }
}
```

#### 构建Python

在模块级目录下`app/build.gradle`添加：

```
android{
    defaultConfig {
        python {
            //根据自己的Python路径
            buildPython "D:\\python\\python3.10.7\\python.exe"
        }
    }
}
```

上述所有步骤配置完成后，点右上角的”Sync Now“，同步一下，确定在build框中显示“synced successfully”再进行后续操作。

此时在`src/main`的目录下可以看到命名为`python`的文件夹。

#### Python Community Edition安装

在Android Studio中，点击**FIle -> Settings -> Plugins -> 搜索Python Community Edition -> 安装 -> 重启Android Studio**

右击`src/main`目录下的`python`文件夹，在`new`中可以看到`Python File`，表示配置成功

#### pip安装第三方库

在模块级目录下`app/build.gradle`添加：

```
android{
    defaultConfig {
        python {
            pip{
                install "requests"
            }    
        }
    }
}
```

这里遇到个bug，不能install "time"，我猜应该是不能install内置的库（这个bug找了一下午才解决）

#### 注意

+ 在编写Python代码的过程中可能会提示没有Python解释器或者import第三方库是显示红色下划线，只要前文配置过程没有出错，就不用在意，因为程序是可以正常运行的。

+ 在这里编写代码，没有代码补全功能

### 使用Chaquopy

#### 导入chaquopy相关类

```
import com.chaquo.python.Kwarg; 
import com.chaquo.python.PyObject;
import com.chaquo.python.android.AndroidPlatform;
import com.chaquo.python.Python;
```

+ Kwarg与函数传参有关的类；

+ PyObject与变量有关的类；

+ AndroidPlatform初始化Python环境需要的类

+ Python调用Python代码必须要的接口类。

#### 调用Python前首先需要初始化Python的环境

```
void initPython(){
    if (! Python.isStarted()) {
        Python.start(new AndroidPlatform(this));
    }
}
```

#### 初始化python环境，再创建一个连接Python的接口

```
initPython();
Python py = Python.getInstance();
```

#### 调用Python代码

```
PyObject obj1 = py.getModule("filename")
        .callAttr("defname",
            new Kwarg("para1", para1),
            new Kwarg("para2", para2));
```

+ "filename"为需要调用的Python文件名

+ "defname"为需要调用的函数名

+ new Kwarg(“para1”, para1) 表示函数的形参“para1”传入para1的值

+ obj1表示调用函数之后的返回值

因为Python中的数据类型和Java中有些不一样，所以一般不能直接使用，需要进行转换，比如：

```
1.如果Python函数return(以上述obj1为例)为int类型，需要使用一下语句进行转换：  
Integer result = obj1.toJava(Integer.class);  

2.如果Python函数return为一维int的list类型，使用以下语句进行转换：  
int[] result = obj1.toJava(int[].class);

3.如果Python函数return为二维int的list类型，使用以下语句进行转换：  
int[][] result = obj1.toJava(int[][].class);
```

以上的int可以按照实际情况替换成float，double等数据类型，转换语句的主要结构是：

```
Java数据类型 Java数据变量名 = obj1.toJava(Java数据类型.class)
```

这样就转换后的Java数据变量就是调用Python代码后的返回结果。

## OkHttp

OkHttp是一个来自[Square](https://link.juejin.cn/?target=https%3A%2F%2Fsquareup.com%2Fus%2Fen "https://squareup.com/us/en")的HTTP客户端，用于Java和Android应用程序。它的设计是为了更快地加载资源并节省带宽。

使用方法可以参考：[OkHttp的完整指南 - 掘金 (juejin.cn)](https://juejin.cn/post/7068162792154464264#heading-12)

# 收获

该APP实际上只码了两三天，但是无法发送请求这个bug找了三天！！

在晚上准备洗澡的时候，突然意识到，Android Studio虚拟机没有联网！！（不联网咋发请求？！）然后研究了如何给虚拟机联网，最终也没连上，甚至还下载了夜神模拟器，也没找到解决方案。

只能在真机调试。幸好真机通过抓包，发现请求已经发出。

**学会了Chaquopy和OkHttp的使用是该项目最大的收获**

# 疑惑

该app的实现，仅仅是Java通过OkHttp成功发送请求，但是Java调用Python的Requests并未成功，目前仍未找到原因，有人知道的话望告知

**解答：**

解决问题了，Java可以调用Python的Requests，之前一直失败，是因为python文件夹里的python文件后缀写成了txt了。这样的话，Chaquopy真的好强大！！！
