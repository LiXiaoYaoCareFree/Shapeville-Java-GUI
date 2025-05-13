
---

## ✅ IntelliJ IDEA 中运行

### 步骤：

1. 打开 `MainWindow.java`
2. 右键点击代码中的 `main` 方法 → `Run 'MainWindow.main()'`
3. 或点击右上角绿色三角运行按钮

> 如果你第一次运行，可能需要配置运行配置（Run Configurations）并指定类路径。

---

![](images/img.png)

![](images/img_1.png)

![](images/img_2.png)

![](images/img_3.png)

![](images/img_4.png)

![](images/img_5.png)

![](images/img_6.png)

![](images/img_7.png)

可以使用 Java 自带的 javadoc 工具生成 HTML 格式的 Java 文档。具体步骤如下：

1. 打开命令行工具，进入项目的源代码目录。
2. 运行以下命令生成 Java 文档：
```shell
javadoc -d docs -sourcepath src/main/java -subpackages com.Shapeville
```

