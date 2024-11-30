out_path="../out/production/P2P-PCDProject"

port=$(($1 + 6666))
host="127.0.0.1"

#javac -d $out_path Node.java
java -cp $out_path Node $host $port