![banner_shiohara.jpg](https://openviglet.github.io/shiohara/img/banner_shiohara.jpg)
------
**Viglet Shiohara** is a simple and intuitive Web Content Management.

**If you'd like to contribute to Viglet Shiohara, be sure to review the [contribution
guidelines](CONTRIBUTING.md).**

**We use [GitHub issues](https://github.com/openviglet/shiohara/issues) for tracking requests and bugs.**

# Installation

## Deploy 

### 1. Runtime

Use Gradle to execute a Viglet Shiohara, without generate jar file.

```shell
$ ./gradlew bootrun
```


### 2. Or Generate JAR File

Use Gradle to generate a Viglet Shiohara executable JAR file.

```shell
$ ./gradlew build
```

#### 2.1 Run

To run Viglet Shiohara executable JAR file, just execute the following line:

```shell
$ java -jar build/libs/viglet-shiohara.jar
```

## Viglet Shiohara
* Administration Console: [http://localhost:2710](http://localhost:2710).

> login/password: admin/admin

* Sample Site: [http://localhost:2710/sites/sample/default/en-us](http://localhost:2710/sites/sample/default/en-us).
