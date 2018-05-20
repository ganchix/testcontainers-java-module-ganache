# TestContainers Ganache testing module [![Build Status](https://travis-ci.org/ganchix/testcontainers-java-module-ganache.svg?branch=master)](https://travis-ci.org/ganchix/testcontainers-java-module-ganache) [![codecov](https://codecov.io/gh/ganchix/testcontainers-java-module-ganache/branch/master/graph/badge.svg)](https://codecov.io/gh/ganchix/testcontainers-java-module-ganache) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.ganchix/testcontainers-java-module-ganache/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/io.github.ganchix/testcontainers-java-module-ganache) [![GitHub stars](https://img.shields.io/github/stars/badges/shields.svg?style=social&label=Star)](https://github.com/ganchix/testcontainers-java-module-ganache)

Testcontainers module for [Ganache](http://truffleframework.com/ganache/).

# Table of Contents
 
- [Overview](#overview)
- [Getting started](#getting-started)
- [License](#license)


### Overview

A simple way to test [Ethereum](https://www.ethereum.org/) in your Java Code

See [testcontainers.org](https://www.testcontainers.org) for more information about Testcontainers.

### Getting started

#### Add dependency

```
<dependency>
    <groupId>io.github.ganchix</groupId>
    <artifactId>testcontainers-java-module-ganache</artifactId>
    <version>0.0.1</version>
</dependency>
```

### Gradle

```
compile group: 'io.github.ganchix', name: 'testcontainers-java-module-ganache', version: '0.0.1'
```

#### Code example

Running Ganache during a test:

```java
public class SomeTest {


	@Rule
	public GanacheContainer GanacheContainer = new GanacheContainer();

    
	@Test
	public void simpleTestWithClientCreation() {
        Web3j web3j = ganacheContainer.getWeb3j();
        assertEquals( web3j.ethBlockNumber().send().getBlockNumber(), BigInteger.ZERO);
        assertNotNull(ganacheContainer);
	}
}
```


### License

Testcontainers module for Ganache is licensed under the MIT License. See [LICENSE](LICENSE.md) for details.

Copyright (c) 2018 Rafael Ríos Moya


