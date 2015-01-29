define([
    'backbone'
], function(Backbone){    
    return Backbone.Model.extend({
	   defaults:{
		   title:"GRAB", 
		   version:"1.0.2"
	   }	
    });
});    