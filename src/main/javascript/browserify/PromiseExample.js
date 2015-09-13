var console = require('./Console');
var Promise = require('promise');

var validateMyForm = function(form){
    return new Promise(function (fulfill, reject){
        if(form.name != null && form.name.length > 0){
            fulfill();
        }else{
            reject('Name is required');
        }
    });
};

validateMyForm({name: "text"})
    .then(function(){
        console.log("form validated");
    }, function(err){
        console.log(err);
    });