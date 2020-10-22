if [ "$1" == router ];
then
    echo "Running router jar..."
    java -jar ./jars/router.jar

elif [ "$1" == market ];
then
    echo "Running market jar..."
    java -jar ./jars/market.jar 2 2

elif [ "$1" == broker ]
then
    echo "Running broker jar..."
    java -jar ./jars/broker.jar

else
    echo "Module not found"
fi