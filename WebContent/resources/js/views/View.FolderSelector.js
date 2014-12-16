define([    
    'backbone',
    'marionette',    
    'templates'
], function(Backbone, Marionette, templates){    
    return Marionette.ItemView.extend({    
    	template: templates.folderselector,
        tagName: 'div',
        
        ui:{
        	list: '.list-group',
        	listItem: '.list-group-item',
        	btnCancel: '#cancel'
        },
        
        events:{
        	'click @ui.listItem' : 'itemClicked',
        	'click @ui.btnCancel' : function(){ window.app.vent.trigger("dialog-close"); }
        },                
        
        onRender: function(){
        	var folderIndex = this.model.get("currentFolder");        	
        	var folders = this.model.get("folders");
        	var count = 0;
        	var _this = this;        	
        	_.each(folders, function(folder, index){
        		// Highlight the folder that is currently selected
        		if(folderIndex == index)
        			$(_this.ui.listItem[index]).addClass("active");        		
        		if(_.isEmpty(folder.path))
        			count++;
        	});
        	// check if any folder exists
        	if(count > 2)
        		this.ui.list.text("Folders not added. Please add folders from Setting.");        	
        },
        
        itemClicked: function(e){
        	if($('a', this.el).index($(e.currentTarget)) != this.model.get('currentFolder')){
        		this.model.set('currentFolder', $('a', this.el).index($(e.currentTarget)));        	
        		this.model.save({},{ success: function(){ window.app.vent.trigger("dialog-close"); }});
        	}
        }
    });
});