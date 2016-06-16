# Webperf
Web 项目的静态检查工具， 在SONAR-WEB插件的基础上增加了额外对css，image，script的检查规则

CSS

* 没有使用外部CSS、在页面中内嵌了CSS
* 没有指定css style name
* 外部属性缺少type 及rel
* css link style 不对
* 没有使用相对路径
* CSS文件没有在指定路径下
* 文件不存在
* 多次连接统一CSS

Image

* 没有使用相对路径
* 文件不存在
* 非法文件，读取不到属性
* img 的高度、宽度值不是数字
* 图像实际大小与html内值不匹配

Script

* 不是JavaScript
* 没有使用相对路径 
* 文件不存在
* 多次调用同一文件
* JavaScript 没有放到外部文件
