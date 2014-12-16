define([    
    'backbone',
    'marionette',    
    'templates',
    'bootbox'
], function(Backbone, Marionette, templates, bootbox){    
    return Marionette.ItemView.extend({    
    	template: templates.folder,
        tagName: 'li'        
    });
});