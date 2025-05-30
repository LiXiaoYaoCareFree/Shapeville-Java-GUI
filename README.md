
---

## ✅ IntelliJ IDEA 中运行

### 步骤：

1. 打开 `MainWindow.java`
2. 右键点击代码中的 `main` 方法 → `Run 'MainWindow.main()'`
3. 或点击右上角绿色三角运行按钮

> 如果你第一次运行，可能需要配置运行配置（Run Configurations）并指定类路径。

或者直接进入项目的根路径下，使用命令行直接运行:
```shell
mkdir -p bin
```

```bash
javac -d bin -sourcepath src/main/java src/main/java/com/Shapeville/MainWindow.java
```

```bash
cp -r src/main/resources/images bin/
```

```bash
java -cp bin com.Shapeville.MainWindow
```


![](src/main/resources/images/README/images/img.png)

![](src/main/resources/images/README/images/img_1.png)

![](src/main/resources/images/README/images/img_2.png)

![](src/main/resources/images/README/images/img_3.png)

![](src/main/resources/images/README/images/img_4.png)

![](src/main/resources/images/README/images/img_5.png)

![](src/main/resources/images/README/images/img_6.png)

![](src/main/resources/images/README/images/img_7.png)


可以使用 Java 自带的 javadoc 工具生成 HTML 格式的 Java 文档。具体步骤如下：

1. 打开命令行工具，进入项目的源代码目录。
2. 运行以下命令生成 Java 文档：
```shell
javadoc -d docs -sourcepath src/main/java -subpackages com.Shapeville
```

