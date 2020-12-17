# TREDIS Implementation - FCT-UNL Master Thesis

In this repository we present the implementation of out TREDIS solution.

Here, we show in:

*/src* - the implementation of the Proxy component

*/configurationsInCloud* - the configurations used for running redis in our cloud environment

### Setup

How to run:
- A) need Redis installed
- B) need SGX hardware support
- C) need a KeyCloak authentication server running (configurations made in */src/main/resources/application.properties*)
- D) need Docker

In */configurationsInCloud/redis*, we present the configurations to run Redis instances in both SGX and non-SGX environments.
All the redis.conf files there present can and must be modified to comply with personal usage of the solution.

To run a docker container with a redis instance **outside SGX**, run a normal docker run command, indicating the image.

> sudo docker run -d -p _**port**_:6379 _**image**_

To run a docker container with a redis instance **inside SGX** (i.e.: Standalone Redis) use:

> sudo docker run -d --device=/dev/isgx -e "SCONE_MODE=HW" -e "SCONE_VERSION=1" -p _**port**_:6379 _**image**_

The same applies to running the proxy component.

**outside SGX**

> sudo docker run -d -p _**port**_:8444 _**image**_

**inside SGX**

> sudo docker run -d --device=/dev/isgx -e "SCONE_MODE=HW" -e "SCONE_VERSION=1" -p _**port**_:8444 _**image**_

Notes:

--device: path of the sgx files in the machine

-e "SCONE_MODE=HW": runs the container on top of SGX

-e "SCONE_VERSION=1": shows SCONE info in the container

The image _**image**_ needs to comply to a SCONE container image. To see how, check the Dockerfiles (i.e) in *\configurationsInCloud\redis\sgx*

For redis configurations with more than one replica, we provide docker-compose files (i.e: ..\sgx\scone_cluster\redis-cluster.yml) that can be used to ease the launch of the redis system.


### Attestation

To enable enclave attestation, we stored some application secrets in an external Configuration and Attestation Service, provided by SCONE [CAS](https://sconedocs.github.io/CASOverview/). There you can follow the tutorial in order to setup the environment (both CAS and LAS components)
An example of a application secret used is shown in *\configurationsInCloud\attestation\sessionJcreis.yml* (check [here](https://sconedocs.github.io/Running_Java_Applications_in_Scone_with_remote_attestation/) to understand how to write a secret)
This means that every application needs to contact CAS in order to start. To do so, they also need to grab a quote from LAS.
