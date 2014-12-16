define([
        'backbone'
], function(Backbone){    
	return Backbone.Model.extend({
		idAttribute: 'userid',	
		defaults:{			
			userid: -1,
			username:null,
			currentFolder: -1,
			folders: [],
			uploadImmediately: 0
		},
		urlRoot: 'api/user'
	});
});    