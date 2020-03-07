[![demo](https://img.shields.io/badge/demo-try%20online-FF874B.svg)](https://shio.viglet.net) [![downloads](https://img.shields.io/github/downloads/ShioCMS/shio/total.svg)](https://github.com/ShioCMS/shio/releases/download/v0.3.5/viglet-shiohara.jar) [![Build Status](https://travis-ci.com/ShioCMS/shio.svg?branch=master)](https://travis-ci.com/ShioCMS/shio) [![codecov](https://codecov.io/gh/ShioCMS/shio/branch/master/graph/badge.svg)](https://codecov.io/gh/ShioCMS/shio) [![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=ShioCMS_shio&metric=alert_status)](https://sonarcloud.io/dashboard/index/ShioCMS_shio)
[![Twitter](https://img.shields.io/twitter/follow/shiocms.svg?style=social&label=Follow)](https://twitter.com/intent/follow?screen_name=shiocms)

![shio_banner.png](https://shiocms.github.io/shio/img/shio_banner.png) 
------

**Viglet Shio CMS** - Model Content and Create Site using Javascript with Native Cache and Search.

Shio (pronounced [strong/ʃiː/ weak/ʃɪ/ o])

**If you'd like to contribute to Viglet Shio, be sure to review the [contribution
guidelines](CONTRIBUTING.md).**

**We use [GitHub issues](https://github.com/ShioCMS/shio/issues) for tracking requests and bugs.**

# Installation

## Download

```shell
$ git clone --recurse-submodules https://github.com/ShioCMS/shio.git
$ cd shio
```

## Deploy 

### 1. Runtime

Use Gradle to execute Shio CMS, without generate jar file.

```shell
$ ./gradlew bootrun
```


### 2. Or Generate JAR File

Use Gradle to generate Shiohara CMS executable JAR file.

```shell
$ ./gradlew build
```

#### 2.1 Run

To run Shio CMS executable JAR file, just execute the following line:

```shell
$ java -jar build/libs/viglet-shio.jar
```

## Viglet Shiohara
* Administration Console: [http://localhost:2710](http://localhost:2710).

> login/password: admin/admin

* Sample Site: [http://localhost:2710/sites/viglet/default/en-us](http://localhost:2710/sites/viglet/default/en-us).
