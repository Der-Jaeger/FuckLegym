# 通知（2022.3.12）
在[release](https://github.com/Foreverddb/FuckLegym/releases/)中下载最新版的app

IOS 版已着手开始开发，详情请关注仓库 [iLegym](https://github.com/Bill-Haku/iLegym)

# 乐健一键跑步、签到app
仅支持Android， 安装后一键签到、刷跑步、课程签到

支持自选跑步方式，可以自选跑步位置（默认本地存有电子科技大学两校区跑步路径，app内可云端导入其他学校地图，可自己新增路径）

支持自选活动一键报名并签到

支持课程一键签到

在[release](https://github.com/Foreverddb/FuckLegym/releases/)中下载需要的app版本即可

**注意：**若账号密码输入错误的话其他任何功能都无法正常使用，因此若出现活动或课程一直在加载中，则请先检查一下账号密码。

**本人为大一学生，课业繁忙，issues可能无法及时回复，请见谅。**

没有做过各系统版本兼容，大部分安卓手机应该都能用。

# 开源声明
本仓库原fork自[RisingEntropy](https://github.com/RisingEntropy)老哥的FuckLegymApp仓库，后来老哥取消了public，加上我又有些更多需求，就加了一下功能，优化了一下排版来发布

本着开源精神，将源码开放，**本仓库遵守GPL3.0开源协议, 任何采用了本仓库代码的必须同样开源且遵守并附带GPL3.0协议**

# 其他
欢迎各位大佬pr，因为大部分时候自己用用就好，所以就没有怎么优化内部逻辑，请见谅。

[原作者README](https://github.com/Foreverddb/FuckLegym/blob/master/release/README.md)

# 如何新增跑步地图

## **懒人法（可能我没时间会等挺久）**

首先在高德地图上找到你想要跑步的地方，在[高德地图api](https://lbs.amap.com/tools/picker)上选取你要跑步的路线坐标，按照路线顺序写成如下方式：

```
"xx大学（xx校区）": {
    "latitude": [30.708182, 30.708122, 30.707937, 30.707808, 30.707753, 30.707587, ...],
    "longitude": [103.863881, 103.863988, 103.864122, 103.864139, 103.864106, 103.86401, ...]
  }
```

每个逗号间隔一个坐标，latitude对应的是纬度，longitude对应的是经度，每个经纬度坐标要一一对应。示例因篇幅原因给的坐标较少，正常情况下如绕操场跑一圈至少应有50个左右坐标。

在 **issues** 里附带你的地图名称（或者学校名称），将所有坐标数据按此格式发给我，待我有时间加上去。

## 自主fork法（推荐此方法）

自己fork一份到自己仓库，然后到 **docs/maps.json** 文件中严格按照 **json** 格式在末尾添加：

```java
"xx大学（xx校区）": {
    "latitude": [纬度1, 纬度2, ..., 纬度N],
    "longitude": [经度1, 经度2, ..., 经度N]
  }
```

添加后向本仓库发起 Pull Request， 待审核后即可。