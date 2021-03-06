#!/bin/bash -ueE

function catch_errors() {
 	echo "####  Program failed with errors. (Exit code is not 0)  Full console output of your program:";
	cat $FOUT
    exit 1;
}

function show_link() {
    echo -e "\n#############################################\n"
    echo "Congratulations, you successfully pass phase 1"
    echo -e "\n#############################################\n"
}

trap catch_errors ERR;
[[ ${DEBUG:-} ]] && set -x

MODE=${MODE:-check}

PHASE_PATH=${PHASE_PATH:-"test-vectors/phase1/"}

if [[ $OSTYPE == darwin* ]]; then
    echo "Ensure you have coreutils installed"
    alias readlink=greadlink
fi

MYDIR="$(readlink -f $(dirname $0))"

echo "Building..."
mvn package -DmainClass=App.java

counter=0

for suite in "$MYDIR/$PHASE_PATH"**; do
    cat "$suite/title.txt"
    ARGS=$(cat "$suite/args.txt")
    for FIN in "$suite"/*_in; do
        BASE=$(basename "$FIN")
        echo -e "####  Starting test $BASE ####"

        FTEST=$(dirname "$FIN")/${BASE/%_in/_expected}

        FOUT=$(dirname "$FIN")/${BASE/%_in/_actual}

        if [[ $MODE == generate ]]; then
            FOUT="$FTEST"
        fi

        if [[ $MODE == check ]]; then
            echo -e "\nINPUT ($(wc -l $FIN | cut -d' ' -f1) lines)"
            cat $FIN
        fi

        echo -e "\n\nRunning Main...\n"
        java -jar target/ship-to-internship-2021-1.0-SNAPSHOT.jar $ARGS < "$FIN" > "$FOUT"
        if [[ $MODE == check ]]; then
            echo -e "\nLEFT: expected, RIGHT: actual"
            diff -yB "$FTEST" "$FOUT"
            echo -e "####  Test $BASE passed  ####"
            counter=$((counter+1))
        fi
    done
done

if [[ $counter == 7 ]]; then show_link
fi
