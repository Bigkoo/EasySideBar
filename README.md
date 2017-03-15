# EasySideBar
一款按字母排序的库，已封装好城市数据，可定制化强，也可以下载源代码用Module的形式引入自己改一改来使用。欢迎Star、提建议、提Issue。

![EasySideBar.gif](https://github.com/Bigkoo/EasySideBar/blob/master/preview/GIF.gif)

## **使用步骤：**

### 1.添加Jcenter仓库 Gradle依赖：

```java
compile 'com.contrarywind:EasySideBar:1.1.0'
```

## 2.在Activity中添加如下代码：

```java

//热门城市数据 ，不添加数据的时候会隐藏该布局
ArrayList<String> hotCityList = new ArrayList<>();
                hotCityList.add("北京");
                hotCityList.add("上海");
                hotCityList.add("广州");
                hotCityList.add("深圳");
                hotCityList.add("杭州");
                hotCityList.add("成都");
                hotCityList.add("厦门");
                hotCityList.add("天津");
                hotCityList.add("武汉");
                hotCityList.add("长沙");
                
//初始化以及配置
new EasySideBarBuilder(MainActivity.this)
                        .setTitle("城市选择")
                        /*.setIndexColor(Color.BLUE)*/
                        .setIndexColor(0xFF0095EE)
                        /*.isLazyRespond(true) //懒加载模式*/
                        .setHotCityList(hotCityList)//热门城市列表
                        .setIndexItems(mIndexItems)//索引字母
                        .setLocationCity("广州")//定位城市
                        .setMaxOffset(60)//索引的最大偏移量
                        .start();

```
## 3.在Activity中重写onActivityResult方法，接收回调数据：

```java

    //resultCode 是使用封装好的EasySideBarBuilder.CODE_SIDEREQUEST
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EasySideBarBuilder.CODE_SIDEREQUEST:
             if (data!=null){
                 String city = data.getStringExtra("selected");
                 Toast.makeText(this,"选择的城市："+city,Toast.LENGTH_SHORT).show();
             }
                break;

            default:
                break;
        }

        }

```


## Thanks

- [WaveSideBar](https://github.com/gjiazhe/WaveSideBar)


#License

```
Copyright 2014 Bigkoo
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
