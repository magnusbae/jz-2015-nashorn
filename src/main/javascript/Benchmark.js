var console = { log: print, dir: print };

load('lib/benchmark.js');
load('lib/lodash.js');

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
$ jjs src/main/javascript/Benchmark.js
RegExp#test x 5,,503,,565 ops/sec +/-5.94% (61 runs sampled)
String#indexOf x 23,,423,,545 ops/sec +/-0.39% (69 runs sampled)
lodash#sortBySin(n) x 46,,411 ops/sec +/-44.25% (62 runs sampled)
Fastest is String#indexOf
*/