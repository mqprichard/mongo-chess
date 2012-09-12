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

newgame=`curl -s -H "Accept: application/json" -H "Content-Type: application/json" -X POST -d '{"white":"Player1","black":"Player2","description":"Championship"}' http://chess.partnerdemo.cloudbees.net/chess/game/new`
gameid=$(jsonElement "id" $newgame)

sed s/GAME/$gameid/g templateGame1.sh > tmpGame1.sh
sed s/GAME/$gameid/g templateGame1.out > tmpGame1.out
chmod u+x tmpGame1.sh

diff <(./tmpGame1.sh) tmpGame1.out
#rm -f tmpGame1.sh tmpGame1.out

newgame=`curl -s -H "Accept: application/json" -H "Content-Type: application/json" -X POST -d '{"white":"Player1","black":"Player2","description":"Championship"}' http://chess.partnerdemo.cloudbees.net/chess/game/new`
gameid=$(jsonElement "id" $newgame)

sed s/GAME/$gameid/g templateGame2.sh > tmpGame2.sh
sed s/GAME/$gameid/g templateGame2.out > tmpGame2.out
chmod u+x tmpGame2.sh
 
diff <(./tmpGame2.sh) tmpGame2.out
#rm -f tmpGame2.sh tmpGame2.out

