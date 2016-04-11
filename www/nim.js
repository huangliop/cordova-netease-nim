cordova.define("cordova-netease-nim.NIM", function(require, exports, module) {
var exec = require('cordova/exec');
module.exports = {
	login: function(user, token,success,error) {
        exec(success,error, "NIM", "login", [user,token]);
	},
    logout:function(){
        exec(function(){},function(){},"NIM","logout",[]);
    },
    setNotification:function(b){
        exec(function(){},function(){},"NIM","setNotification",[b]);
    },
    moveToBack:function(){
        exec(function(){},function(){},"NIM","moveToBack",[]);
    }
};
});
