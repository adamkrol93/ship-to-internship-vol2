#!/bin/bash -ueE

function catch_errors() {
 	echo "####  Program failed with errors. (Exit code is not 0)  Full console output of your program:";
	cat $FOUT
    exit 1;
}

function show_link() {
    echo -e "\n#############################################\n"
    echo "Congratulations, you successfully pass phase 4"
    echo -e "\n#############################################\n"
}

trap catch_errors ERR;
[[ ${DEBUG:-} ]] && set -x

MODE=${MODE:-check}

if [[ $OSTYPE == darwin* ]]; then
    echo "Ensure you have coreutils installed"
    alias readlink=greadlink
fi

MYDIR="$(readlink -f $(dirname $0))"

echo "Building..."
mvn package -DmainClass=App.java

counter=0

for suite in "$MYDIR/test-vectors/phase4/"**; do
    cat "$suite/title.txt"
    mvn -q exec:java -Dexec.mainClass="com.ship.SpringStart" & sleep 15
    for FIN in "$suite"/*_in; do
        BASE=$(basename "$FIN")
        echo -e "####  Starting test $BASE ####"

        FTEST=$(dirname "$FIN")/${BASE/%_in/_expected}

        PROTOCOL=$(head -1 $FIN)
        ADDRESS=$(head -2 $FIN | tail -n 1)
        LINE_NUMBER=$(awk 'END { print NR }' $FIN)
        REQUEST=""
        while read args; do
              if [[ -z "$args" ]]; then continue; fi
                  REQUEST=$REQUEST$args
        done < <(sed -n '3,'$LINE_NUMBER'p' $FIN)

        eval "curl -s --connect-timeout 3 --max-time 30 -X $PROTOCOL -H 'Content-Type: application/json' --data '$REQUEST' http://localhost:8080$ADDRESS"
        eval "curl -s --connect-timeout 3 --max-time 30 -X GET http://localhost:8080/idemiomat | jq '.'" > response

        diff -yB response "$FTEST"
        counter=$((counter+1))

    done
    ps -ef | grep com.ship.SpringStart | grep -v grep | tr -s ' ' | cut -d ' ' -f2 | xargs kill -9
    #pkill -f com.ship.SpringStart
done

if [[ $counter == 39 ]]; then show_link
fi

#pkill -f idemia.internship.tictactoe.web.Application
wait
