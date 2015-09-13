
var returnArray = function(){
  return _.sortBy([2,1,3], function(n){
    return n;
  });
};

console.log(returnArray());