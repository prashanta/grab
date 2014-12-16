define([    
    'backbone',
    'marionette',
    'templates'
], function(Backbone, Marionette, templates){    
    return Marionette.ItemView.extend({
	    template: templates.header,
        tagName: 'div',
        
    	options:{
    		title: 'HeaderView',
    	}
    });
});