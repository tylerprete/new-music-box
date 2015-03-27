mvn clean compile assembly:assembly
java -cp "target/processing-demo-1.0-SNAPSHOT-jar-with-dependencies.jar:libs/*" org.processing.Board
