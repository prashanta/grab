define([
        'backbone',
        'models/Model.Folder'
], function(Backbone, FolderModel){    
	return Backbone.Collection.extend({
		model: FolderModel,
		url: 'api/folder/3',
		
		parse: function(response) {
			  return response.folderBean;
		}
	});
});    