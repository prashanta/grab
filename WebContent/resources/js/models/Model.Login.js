define([
    'backbone'
], function(Backbone){    
    return Backbone.Model.extend({
	   defaults:{
		   uname : "", 
		   pass : ""
	   },
	   url: 'Login',
	   
	   validate: function(attrs, options) {
		   if(_.isEmpty(options.data[0].value))
			   return "Username is empty";
		   else if(_.isEmpty(options.data[1].value))
			   return "Password is empty";
  	   }
    });
});    