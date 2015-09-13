var Benchmark = require('benchmark');
var _ = require('lodash');

var suite = new Benchmark.Suite;

// add tests
suite.add('RegExp#test', function() {
  /o/.test('Hello World!');
})
.add('String#indexOf', function() {
  'Hello World!'.indexOf('o') > -1;
})
.add('lodash#sortBySin(n)', function(){
  _.sortBy([0,1,2,3,4,5], function(n){
    return Math.sin(n);
  });
})
// add listeners
.on('cycle', function(event) {
  console.log(String(event.target));
})
.on('complete', function() {
  console.log('Fastest is ' + this.filter('fastest').pluck('name'));
})
// run async
.run({ 'async': true });


/*
$ node NodeBenchmark.js
RegExp#test x 9,288,051 ops/sec ±1.87% (94 runs sampled)
String#indexOf x 15,563,240 ops/sec ±1.53% (90 runs sampled)
lodash#sortBySin(n) x 481,666 ops/sec ±1.28% (93 runs sampled)
Fastest is String#indexOf
*/