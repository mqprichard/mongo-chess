curl -H "Accept: application/json" http://chess.partnerdemo.cloudbees.net/chess/game/GAME ; echo
curl -H "Accept: application/json" -H "Content-Type: application/json" http://chess.partnerdemo.cloudbees.net/chess/moves/new -X POST -d '{"game":"GAME","white":"e2-e4","move":"1"}' ; echo
curl -H "Accept: application/json" -H "Content-Type: application/json" http://chess.partnerdemo.cloudbees.net/chess/moves/new -X POST -d '{"game":"GAME","black":"e7-e5","move":"1"}' ; echo
curl -H "Accept: application/json" -H "Content-Type: application/json" http://chess.partnerdemo.cloudbees.net/chess/moves/new -X POST -d '{"game":"GAME","white":"g1-f3","move":"2"}' ; echo
curl -H "Accept: application/json" -H "Content-Type: application/json" http://chess.partnerdemo.cloudbees.net/chess/moves/new -X POST -d '{"game":"GAME","black":"b8-c6","move":"2"}' ; echo
curl -H "Accept: application/json" -H "Content-Type: application/json" http://chess.partnerdemo.cloudbees.net/chess/moves/new -X POST -d '{"game":"GAME","white":"f1-b5","move":"3"}' ; echo
curl -H "Accept: application/json" http://chess.partnerdemo.cloudbees.net/chess/game/GAME ; echo
curl -H "Accept: application/json" http://chess.partnerdemo.cloudbees.net/chess/moves/GAME ; echo

