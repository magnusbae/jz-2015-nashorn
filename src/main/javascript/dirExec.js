var lines = $EXEC("ls -lsa").split("\n");
for each (var line in lines) {
  print("|> " + line);
}

#run with jjs -scripting dir.js