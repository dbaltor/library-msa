#!/bin/bash
set -eo pipefail

cd $(dirname $0)
cd ../..
mkdir logs || true
DIR='sc-6-tracing'

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
echo
read -p "Press enter to finish all services"
echo
echo "Killing all processes"
kill $BOOK_PID
kill $READER_PID
kill $APPLICATION_PID
rm -rf logs
echo "Done!"

