##用Android Studio导入并运行DL注意事项

###1.请用Android studio 1.0以上版本
###2.根目录下的脚本是无法运行的，请运行对应子项目
###3.也可以用命令行运行：
    
     cd DynamicLoadApk
     gradlew :main-plugin-host:build
###4.为了方便上传插件APK到手机，提供了uploadDebug task:
     cd DynamicLoadApk
     gradlew :main-plugin-a:uploadDebug
     
####上传路径在根目录下build.gradle修改
    def dlPath = '/sdcard/DynamicLoadHost'
