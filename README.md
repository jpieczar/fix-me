# fix-me

A java maven project with 3 components that communicate over a network

## Run

- ./build.sh (mvn clean package)

a folder called 'jars' will contain the runnable jars for each component (router, market, broker)

- first start the Router - ./run.sh router

- then the Market - ./run.sh market

- lastly the Broker - ./run.sh broker

The market and broker will connect to the router, will be assigned unique ID's
and will be able to communicate with each other.
