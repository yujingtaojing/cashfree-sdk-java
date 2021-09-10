Cashfree SDK's are released in Beta. This is work in progress and we are continuously working on improving various aspects of it. It is released as learning aid and example kit for the API integrators. This is not recommended by Cashfree for direct use in production code. Please report any bugs to Cashfree at techsupport@cashfree.com.
# cashfree-sdk-java

The official Cashfree SDK for Java,

Get started quickly using Cashfree with the Cashfree SDK for java. The SDK helps take the complexity out of coding by providing java objects for Cashfree services including Payouts, Payment Gateway, Marketplace and Autocollect. The single, downloadable package includes the Cashfree Java Library and documentation.

Please refer to the [Cashfree Docs](https://docs.cashfree.com/docs/)  for the complete API reference.

## Installing

The preferred way to install the Cashfree SDK for Java is to use the [maven-central](https://mvnrepository.com/repos/central) dependency manager. Simply add the following in your pom.xml or build.gradle:
```sh

```

## Getting Started
### Pre-requisites
  - A [Cashfree Merchant Account](https://merchant.cashfree.com/merchant/sign-up)
  - API keys for different products. You can generate them from your Dashboard
### IP Whitelisting and dynamic IPs
Your IP has to be whitelisted to hit Cashfree's server. Or if you have a dynamic IP please pass in the public key parameter during the init method as shown below. For more information please go [here](https://dev.cashfree.com/development/quickstart#ip-whitelisting).
## Usage
### Payouts
The package needs to be configured with your account's secret key which is available in your Cashfree Dashboard.
Init the package with your credentials and add the below code in your config.py of your package.
##### In case of static IP (Your IP is whitelisted)
```java

import com.cashfree.lib.payout.clients.Payouts;

// Get instance for Cashfree Payout
client_id = ""; //your client id here
client_secret = ""; //your client secret here
public_key_path = ""; //if you have public key, then your path shoul be provided here
Payouts payouts = Payouts.getInstance(Environment.PRODUCTION, client_id, client_secret ,public_key_path );
```
##### In case of dynamic IP you will need a public key to generate a signature(which will be done by sdk itself)

```python
Needs to be implmented.
```


| Option              | Default                       | Description                                                                           |
| ------------------- | ----------------------------- | ------------------------------------------------------------------------------------- |
| `env`        | `TEST`                        | Environment to be initialized. Can be set to `TEST` or `PROD` |
| `client_id` | ``                             | `client_id` which can be generated on cashfree dashboard.                  |
| `client_secret`         | ``                        | `client_secret` which can be found alongside generated `client_id`. |
| `public_key_path`         | ``                        | `public_key_path` specify the path to your .pem public key file `. |
| `public_key`         | ``                        | `public_key` Pass your Public Key to this parameter as an alternative to `public_key_path` . |                     

#### [Pg Library Docs](cashfree_sdk/payouts/README.md)
#### [Payout Library Docs](cashfree_sdk/payouts/README.md)

- For more information about the APIs go to [Payouts](Payouts).
- Please also refer to experiments/PayoutExperiments.java and PgExperiments.java for sample usages for each api call. 
- Complete list of [APIs](https://docs.cashfree.com/docs/payout/guide/#fetch-beneficiary-id).
### TODO
- #### Market Place
- #### Autocollect