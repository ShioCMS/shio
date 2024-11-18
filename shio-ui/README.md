[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ShioCMS_shio-ui-primer&metric=alert_status)](https://sonarcloud.io/dashboard?id=ShioCMS_shio-ui-primer) [![Twitter](https://img.shields.io/twitter/follow/shiocms.svg?style=social&label=Follow)](https://twitter.com/intent/follow?screen_name=shiocms)

# Shio CMS user interface using Primer CSS

![screen01.png](https://shiocms.github.io/shio-ui-primer/img/screen01.png) 

![screen02.png](https://shiocms.github.io/shio-ui-primer/img/screen02.png) 

![screen03.png](https://shiocms.github.io/shio-ui-primer/img/screen03.png)

![screen04.png](https://shiocms.github.io/shio-ui-primer/img/screen04.png)

![screen05.png](https://shiocms.github.io/shio-ui-primer/img/screen05.png)

![screen06.png](https://shiocms.github.io/shio-ui-primer/img/screen06.png)

**If you'd like to contribute to Shio CMS UI Primer, be sure to review the [contribution
guidelines](CONTRIBUTING.md).**

**We use [GitHub issues](https://github.com/ShioCMS/shio-ui-primer/issues) for tracking requests and bugs.**

# Installation

## Download

```shell
$ git clone https://github.com/ShioCMS/shio-ui-primer.git
$ cd shio-ui-primer
```

## Deploy 

### 1. Install NPM Modules

Use NPM to install the modules.

```shell
$ npm install
```

### 2. Start Shio CMS

Start Shio CMS following the procedure described in https://github.com/ShioCMS/shio/blob/master/README.md and use the ui-dev profile to start the Shio CMS.

#### Linux

```shell
export SPRING_PROFILES_ACTIVE=ui-dev
$ ./mvnw spring-boot:run
```
#### Windows

```shell
set SPRING_PROFILES_ACTIVE=ui-dev
mvnw spring-boot:run
```


### 3. Runtime

Use ng to execute Shio CMS UI Primer.

```shell
$ npx ng serve --open
```

### 4. Build

Use ng to build Shio CMS UI Primer.

```shell
$ npx ng build --prod
```

## URL

Home: http://localhost:4200
