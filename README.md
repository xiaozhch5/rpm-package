# rpm-package
该项目用于将任意文件按照描述文件的组织方式打包成rpm包。

[描述文件样例](doc/example.json)

## 如何使用
您可以从源码编译该项目或者从右侧release包中下载！

### 源码编译安装
编译依赖：
* jdk8
* maven3

```bash
git clone https://github.com/xiaozhch5/rpm-package.git
cd rpm-package
mvn clean package
```
编译完该代码可以在target中得到压缩文件`rpm-package-1.0-bin.tar.gz`，将该文件复制到centos7服务器中，并在centos服务器安装打包依赖：
```bash
yum install rpm-build -y
```

解压文件：
```bash
tar zxvf rpm-package-1.0-bin.tar.gz
```
得到 `rpm-package-1.0-bin`，

#### 运行测试用例：

1. 修改描述文件

```bash
vi rpm-package-1.0-bin/examples/example-1.json
```
得到：
```json
{
  "rpmName": "test",
  "rpmVersion": "1.0",
  "rpmNeedarch": "x86_64",
  "originalPath": "/root/rpm-package-1.0-bin/examples",
  "targetRpmPath": "/root",
  "dirMappings":
  [
    {
      "from": "/var",
      "to":"/usr/examples/var"
    },
    {
      "from": "/usr",
      "to":"/usr/examples/var"
    },
    {
      "from": "/log",
      "to":"/usr/examples/log"
    }
  ],
  "prepareScriptlet":
  {
    "script": "echo \"Hello World\""
  },
  "postinstallScriptlet":
  {
    "fileEncoding": "UTF-8",
    "scriptFile": "/root/rpm-package-1.0-bin/examples/postinstall.sh"
  }

}
```
其中
* rpmName表示该rpm包的名称
* rpmVersion表示rpm包的版本
* rpmNeedarch为CPU架构。 
* originalPath表示将该路径下的文件或者文件夹尽心打包（需要与dirMappings的from结合），注意dirMappings的第一层from - to，
表示该将/root/rpm-package-1.0-bin/examples/var进行打包，对应于rpm包中的/usr/examples/var路径。
/root/rpm-package-1.0-bin/examples需要修改为本机真实路径。 
* targetRpmPath表示rpm最终的打包路径（存放xxx.rpm文件）
* prepareScriptlet表示在该rpm包被安装之前会执行的脚本。 
* postinstallScriptlet表示在该rpm包被安装之后会执行的脚本。

2. 修改完上述描述文件之后，即可执行测试用例：

```bash
cd rpm-package-1.0-bin && sh build-rpm.sh --mappingsFilePath /root/rpm-package-1.0-bin/examples/example-1.json
```

3. 执行完测试用例，即可看到对应rpm包
```bash
[root@hadoop2 rpm-package-1.0-bin]# ll  /root/test-1.0/target/rpm/test/RPMS/x86_64
total 4
-rw-r--r-- 1 root root 2656 Aug 23 15:53 test-1.0-1.x86_64.rpm
```

### 直接下载二进制文件使用
直接下载二进制文件可省略编译阶段，其他流程全部一样。

### 详细执行过程
```bash
[root@hadoop2 ~]# pwd
/root
[root@hadoop2 ~]# wget rpm-package-1.0-bin.tar.gz
[root@hadoop2 ~]# tar zxvf rpm-package-1.0-bin.tar.gz 
rpm-package-1.0-bin/lib/rpm-package-1.0.jar
rpm-package-1.0-bin/
rpm-package-1.0-bin/build-rpm.sh
rpm-package-1.0-bin/lib/
rpm-package-1.0-bin/lib/annotations-2.0.1.jar
rpm-package-1.0-bin/lib/commons-codec-1.2.jar
rpm-package-1.0-bin/lib/commons-httpclient-3.0.jar
rpm-package-1.0-bin/lib/commons-io-2.6.jar
rpm-package-1.0-bin/lib/commons-lang3-3.11.jar
rpm-package-1.0-bin/lib/commons-logging-1.0.3.jar
rpm-package-1.0-bin/lib/fastjson-1.2.71.jar
rpm-package-1.0-bin/lib/ganymed-ssh2-build210.jar
rpm-package-1.0-bin/lib/guava-15.0.jar
rpm-package-1.0-bin/lib/hamcrest-core-1.3.jar
rpm-package-1.0-bin/lib/javassist-3.19.0-GA.jar
rpm-package-1.0-bin/lib/jsch-0.1.54.jar
rpm-package-1.0-bin/lib/junit-4.11.jar
rpm-package-1.0-bin/lib/log4j-1.2.17.jar
rpm-package-1.0-bin/lib/lombok-1.18.12.jar
rpm-package-1.0-bin/lib/maven-model-3.6.3.jar
rpm-package-1.0-bin/lib/plexus-utils-3.2.1.jar
rpm-package-1.0-bin/lib/reflections-0.9.10.jar
rpm-package-1.0-bin/lib/slf4j-api-1.7.25.jar
rpm-package-1.0-bin/lib/slf4j-log4j12-1.7.25.jar
rpm-package-1.0-bin/examples/
rpm-package-1.0-bin/examples/log/
rpm-package-1.0-bin/examples/usr/
rpm-package-1.0-bin/examples/var/
rpm-package-1.0-bin/examples/example-1.json
rpm-package-1.0-bin/examples/log/test.log
rpm-package-1.0-bin/examples/postinstall.sh
rpm-package-1.0-bin/examples/usr/usr.txt
rpm-package-1.0-bin/examples/var/var.txt
[root@hadoop2 ~]# cd rpm-package-1.0-bin && sh build-rpm.sh --mappingsFilePath /root/rpm-package-1.0-bin/examples/example-1.json
[INFO] Scanning for projects...
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building rpm-package 1.0-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- maven-clean-plugin:2.4.1:clean (default-clean) @ rpm-package ---
[INFO] 
[INFO] --- rpm-maven-plugin:2.1.4:rpm (default-cli) @ rpm-package ---
[INFO] Creating directory /root/test-1.0/target/rpm/test
[INFO] Creating directory /root/test-1.0/target/rpm/test/BUILD
[INFO] Creating directory /root/test-1.0/target/rpm/test/RPMS
[INFO] Creating directory /root/test-1.0/target/rpm/test/SOURCES
[INFO] Creating directory /root/test-1.0/target/rpm/test/SPECS
[INFO] Creating directory /root/test-1.0/target/rpm/test/SRPMS
[INFO] Creating directory /root/test-1.0/target/rpm/test/tmp-buildroot
[INFO] Creating directory /root/test-1.0/target/rpm/test/buildroot
[INFO] Copying files to /root/test-1.0/target/rpm/test/tmp-buildroot/usr/examples/var
[INFO] Copying files to /root/test-1.0/target/rpm/test/tmp-buildroot/usr/examples/usr
[INFO] Copying files to /root/test-1.0/target/rpm/test/tmp-buildroot/usr/examples/log
[INFO] Creating spec file /root/test-1.0/target/rpm/test/SPECS/test.spec
[INFO] Building target platforms: x86_64-redhat-linux
[INFO] Building for target x86_64-redhat-linux
[INFO] Executing(%prep): /bin/sh -e /var/tmp/rpm-tmp.tqIKGN
[INFO] + umask 022
[INFO] + cd /root/test-1.0/target/rpm/test/BUILD
[INFO] + echo 'Hello World'
[INFO] Hello World
[INFO] + exit 0
[INFO] Executing(%install): /bin/sh -e /var/tmp/rpm-tmp.fxEzby
[INFO] + umask 022
[INFO] + cd /root/test-1.0/target/rpm/test/BUILD
[INFO] + '[' /root/test-1.0/target/rpm/test/buildroot '!=' / ']'
[INFO] + rm -rf /root/test-1.0/target/rpm/test/buildroot
[INFO] ++ dirname /root/test-1.0/target/rpm/test/buildroot
[INFO] + mkdir -p /root/test-1.0/target/rpm/test
[INFO] + mkdir /root/test-1.0/target/rpm/test/buildroot
[INFO] + '[' -d /root/test-1.0/target/rpm/test/buildroot ']'
[INFO] + mv /root/test-1.0/target/rpm/test/tmp-buildroot/usr /root/test-1.0/target/rpm/test/buildroot
[INFO] + chmod -R +w /root/test-1.0/target/rpm/test/buildroot
[INFO] + /usr/lib/rpm/check-buildroot
[INFO] + /usr/lib/rpm/redhat/brp-compress
[INFO] + /usr/lib/rpm/redhat/brp-strip /usr/bin/strip
[INFO] + /usr/lib/rpm/redhat/brp-strip-comment-note /usr/bin/strip /usr/bin/objdump
[INFO] + /usr/lib/rpm/redhat/brp-strip-static-archive /usr/bin/strip
[INFO] + /usr/lib/rpm/brp-python-bytecompile /usr/bin/python 1
[INFO] + /usr/lib/rpm/redhat/brp-python-hardlink
[INFO] Processing files: test-1.0-1.x86_64
[INFO] Provides: test = 1.0-1 test(x86-64) = 1.0-1
[INFO] Requires(interp): /bin/sh
[INFO] Requires(rpmlib): rpmlib(FileDigests) <= 4.6.0-1 rpmlib(PayloadFilesHavePrefix) <= 4.0-1 rpmlib(CompressedFileNames) <= 3.0.4-1
[INFO] Requires(post): /bin/sh
[INFO] Checking for unpackaged file(s): /usr/lib/rpm/check-files /root/test-1.0/target/rpm/test/buildroot
[INFO] Wrote: /root/test-1.0/target/rpm/test/RPMS/x86_64/test-1.0-1.x86_64.rpm
[INFO] Executing(%clean): /bin/sh -e /var/tmp/rpm-tmp.heEeO3
[INFO] + umask 022
[INFO] + cd /root/test-1.0/target/rpm/test/BUILD
[INFO] + /usr/bin/rm -rf /root/test-1.0/target/rpm/test/buildroot
[INFO] + exit 0
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 0.732s
[INFO] Finished at: Mon Aug 23 15:53:07 CST 2021
[INFO] Final Memory: 8M/150M
[INFO] ------------------------------------------------------------------------
[root@hadoop2 rpm-package-1.0-bin]# yum install /root/test-1.0/target/rpm/test/RPMS/x86_64/test-1.0-1.x86_64.rpm
Loaded plugins: fastestmirror
Examining /root/test-1.0/target/rpm/test/RPMS/x86_64/test-1.0-1.x86_64.rpm: test-1.0-1.x86_64
Marking /root/test-1.0/target/rpm/test/RPMS/x86_64/test-1.0-1.x86_64.rpm to be installed
Resolving Dependencies
--> Running transaction check
---> Package test.x86_64 0:1.0-1 will be installed
--> Finished Dependency Resolution

Dependencies Resolved

================================================================================================================================================================================================================================ Package                                           Arch                                                Version                                            Repository                                                       Size
================================================================================================================================================================================================================================Installing:
 test                                              x86_64                                              1.0-1                                              /test-1.0-1.x86_64                                               23  

Transaction Summary
================================================================================================================================================================================================================================Install  1 Package

Total size: 23  
Installed size: 23  
Is this ok [y/d/N]: y
Downloading packages:
Running transaction check
Running transaction test
Transaction test succeeded
Running transaction
  Installing : test-1.0-1.x86_64                                                                                                                                                                                            1/1 
postinstall
  Verifying  : test-1.0-1.x86_64                                                                                                                                                                                            1/1 

Installed:
  test.x86_64 0:1.0-1                                                                                                                                                                                                           

Complete!
[root@hadoop2 rpm-package-1.0-bin]# ll /usr/examples/
total 12
drwxr-xr-x 2 root root 4096 Aug 23 15:53 log
drwxr-xr-x 2 root root 4096 Aug 23 15:53 usr
drwxr-xr-x 2 root root 4096 Aug 23 15:53 var
```

