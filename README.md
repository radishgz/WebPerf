# WebPerf

Web 项目的静态检查工具， 在SONAR-WEB插件的基础上增加了额外对css，image，script的检查规则

CSS 1、没有使用外部CSS、在页面中内嵌了CSS 2、没有指定css style name 3、外部属性缺少type 及rel 4、css link style 不对 5、没有使用相对路径 6、CSS文件没有在指定路径下 7、文件不存在 8、多次连接统一CSS

Image 1、没有使用相对路径 2、文件不存在 3、非法文件，读取不到属性 4、img 的高度、宽度值不是数字 5、图像实际大小与html内值不匹配

Script 1、不是JavaScript 2、没有使用相对路径 3、文件不存在 4、多次调用同一文件 5、JavaScript 没有放到外部文件

--------
