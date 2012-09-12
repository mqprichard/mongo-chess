#!/bin/bash

jsonElement(){
        #echo $1
        out=$(echo $2 | awk -F"[,:]" '{for(i=1;i<=NF;i++){if($i~/'$1'\042/){print $(i+1)} } }')
        if [[ $out == \"*\" ]]; #test if the string starts and ends in a quote
        then
            echo "${out:1:${#out}-2}" #removes first and last quotes
        elif [[  $out == \"*\"} ]];
        then
            echo "${out:1:${#out}-3}" #removes first and last quotes plus the curly brace
        else
            echo $out
        fi
}

newgame=`curl -s -H "Accept: application/json" -H "Content-Type: application/json" -X POST -d '{"white":"Spassky","black":"Fischer","description":"Reykjavik Game 13"}' http://chess.partnerdemo.cloudbees.net/chess/game/new`
gameid=$(jsonElement "id" $newgame)

sed s/GAME/$gameid/g templateSpasskyFischer.sh > tmpSpasskyFischer.sh
sed s/GAME/$gameid/g templateSpasskyFischer.out > tmpSpasskyFischer.out

chmod u+x tmpSpasskyFischer.sh

diff <(./tmpSpasskyFischer.sh) tmpSpasskyFischer.out

 
