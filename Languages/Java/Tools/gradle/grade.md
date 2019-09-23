## gradle

### 概述

类似于 Maven 的 `pom.xml` 文件，每个 Gradle 项目都需要一个对应的 `build.gradle` 文件，该文件定义一些任务（task）来完成构建工作，每个任务是可配置的，任务之间也可以依赖。

#### 约定优于配置

`gradle` 提供了 maven 的约定优于配置方式，通过 `gradle` 的 java plugin 实现，`gradle` 推荐这种方式

```
src/main/java
src/main/resources
src/test/java
src/test/resources
```

区别在于，使用 groovy 自定义项目布局更加方便

```groovy
sourceSets {
	main {
		java {
			srcDir 'src/java'
		}
		resources {
			srcDir 'src/resources'
		}
	}
}
```

#### 使用命令行

* `gradle -q tasks`

  列出项目中所有可用的 task，`gradle` 提供了任务组的概念，每个构建脚本都会默认暴露 help tasks 任务组，如果某个 task 不属于一个任务组，那么它就会显示在 other tasks 中。要获得关于 task 的更多信息，使用 `--all` 选项`gradle -q tasks --all`，`--all` 选项是决定 task 执行顺序的好办法。

* 任务执行

  `gradle <taskname>`

* 排除一个任务

  `gradle groupTherapy -x yaygradle0`

  `gradle` 排除 `yaygradle0` 任务和它依赖的任务 `startSession`

##### 命令行选项

* `-?-h,--help`

* `-b,--build-file:Gradle`

  默认执行 `build.gradle` 脚本，如果执行其他脚本可以使用这个命令

* `--offline`

  通常，构建中声明的依赖必须在离线仓库中存在才可用。如果这些依赖在本地缓存中没有，那么运行在一个没有网络连接环境中的构建失败。使用这个选项可以以离线模式运行构建，仅仅在本地缓存中检查依赖是否存在

##### 参数选项

* `-D,--system-prop`

  `gradle` 是以一个 JVM 进程运行的。和所有的 Java 进程一样，可以提供一个系统参数 `key=value`

* `-P,--project-prop`

  项目参数是构建脚本中可用的变量。可以使用该选项直接向构建脚本中传入参数 `key=value`

##### 日志选项

* `-i,--info`

  默认不会输出很多信息，可以使用该选项改变日志级别为 info

* `-s,--stacktrace`

  如果构建在运行中出现错误，该选项在有异常抛出时会打印出简短的堆栈跟踪信息

* `-q,--quiet`

  减少构建出错时打印出来的错误日志信息

##### 守护进程

在命令行中启动 `gradle` 守护进程：在运行 `gradle` 命令时加上 `--daemon` 选项。后续触发的 `gradle` 命令都会重用守护进程。守护进程只会被创建一次，会在 3 小时空闲时间之后自动过期。执行构建时不使用守护进程：`--no-daemon` 。手动停止守护进程 `gradle --stop` 。