#!/bin/sh
set -eo pipefail

cd $(dirname $0)
cd ../..
mkdir logs || true
DIR='sc-6-tracing'

echo "Starting Config Server..."
java -jar $DIR/configserver/build/libs/configserver-1.0.0.jar &>logs/configserver_output$$.log &
CONFIGSERVER_PID=$!
sleep 5
echo "Starting Registry Server..."
java -jar $DIR/registry/build/libs/registry-1.0.0.jar &>logs/registry_output$$.log &
REGISTRY_PID=$!
sleep 5
echo "Starting Book Service..."
java -jar $DIR/book/build/libs/book-1.0.0.jar &>logs/book_output$$.log &
BOOK_PID=$!
sleep 5
echo "Starting Reader Service..."
java -jar $DIR/reader/build/libs/reader-1.0.0.jar &>logs/reader_output$$.log &
READER_PID=$!
sleep 5
echo "Starting Library..."
java -jar $DIR/application/build/libs/application-1.0.0.jar &>logs/application_output$$.log &
APPLICATION_PID=$!
sleep 5
echo
echo "The application and services are running in the background."
echo "You can access the Library at http://localhost:8080"
echo "You can access the config server at port 8888. For example:  http://localhost:8888/library-book-service/default"
echo "You can access the registry server at http://localhost:8761"
echo
read -p "Press enter to finish all services"
echo
echo "Killing all processes"
kill $CONFIGSERVER_PID
kill $REGISTRY_PID
kill $BOOK_PID
kill $READER_PID
kill $APPLICATION_PID
rm -rf logs
echo "Done!"

