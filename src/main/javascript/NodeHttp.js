// load module 'http' (this is blocking) to handle http requests
var http = require('http');

// when there is a request we return 'Hello, World\n'
function handleRequest(req, res) {
  res.writeHead(200, {'Content-Type': 'text/plain'});
  res.end('Hello, World\n');
}

// we listen on localhost, port 1337
// and give handleRequest as call back
// you see the non-blocking / asynchronous nature here
http.createServer(handleRequest).listen(1337, '127.0.0.1');

// logs to the console to reassure that we are on our way
console.log('Get your hello at http://127.0.0.1:1337/');


/*

To run (from project root):

java -jar lib/avatar-js.jar src\main\javascript\NodeHttp.js

System specific library must be in path/path-ish
(https://avatar-js.java.net/binaries.html#/Avatarjs_Binaries)

*/