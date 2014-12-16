define([
        'backbone'
], function(Backbone){    
	return Backbone.Model.extend({
		idAttribute: 'folderId',	
		defaults:{			
			folderId: -1,
			name: null,
			path: -1,
			imageResize: 0
		},
		urlRoot: 'api/folder'
	});
});    