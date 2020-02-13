[![demo](https://img.shields.io/badge/demo-try%20online-FF874B.svg)](https://demo.shiohara.org) [![downloads](https://img.shields.io/github/downloads/ShioharaCMS/shiohara/total.svg)](https://github.com/ShioharaCMS/shiohara/releases/download/v0.3.5/viglet-shiohara.jar) [![Build Status](https://travis-ci.com/ShioharaCMS/shiohara.svg?branch=master)](https://travis-ci.com/ShioharaCMS/shiohara) [![codecov](https://codecov.io/gh/ShioharaCMS/shiohara/branch/master/graph/badge.svg)](https://codecov.io/gh/ShioharaCMS/shiohara) [![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=openviglet_shiohara&metric=alert_status)](https://sonarcloud.io/dashboard/index/openviglet_shiohara)
[![Twitter](https://img.shields.io/twitter/follow/shioharacms.svg?style=social&label=Follow)](https://twitter.com/intent/follow?screen_name=shioharacms)

![shiohara_banner.png](https://shioharacms.github.io/shiohara/img/shiohara_banner.png) 
------

**Viglet Shiohara CMS** - Model Content and Create Site using Javascript with Native Cache and Search.

Shiohara (pronounced [strong/ʃiː/ weak/ʃɪ/ o həˈrəˈ]) [Audio](https://shioharacms.github.io/shiohara/mp3/shiohara-voice.mp3)

**If you'd like to contribute to Viglet Shiohara, be sure to review the [contribution
guidelines](CONTRIBUTING.md).**

**We use [GitHub issues](https://github.com/ShioharaCMS/shiohara/issues) for tracking requests and bugs.**

# Installation

## Download

```shell
$ git clone --recurse-submodules https://github.com/ShioharaCMS/shiohara.git
$ cd shiohara
```

## Deploy 

### 1. Runtime

Use Gradle to execute Shiohara CMS, without generate jar file.

```shell
$ ./gradlew bootrun
```


### 2. Or Generate JAR File

Use Gradle to generate Shiohara CMS executable JAR file.

```shell
$ ./gradlew build
```

#### 2.1 Run

To run Shiohara CMS executable JAR file, just execute the following line:

```shell
$ java -jar build/libs/viglet-shiohara.jar
```

## Viglet Shiohara
* Administration Console: [http://localhost:2710](http://localhost:2710).

> login/password: admin/admin

* Sample Site: [http://localhost:2710/sites/viglet/default/en-us](http://localhost:2710/sites/viglet/default/en-us).
