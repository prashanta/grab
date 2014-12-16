define([    
    'backbone',
    'marionette', 
    'views/View.Folder',
    'templates',
    'bootbox'
], function(Backbone, Marionette, FolderView, templates, bootbox){    
    return Marionette.CompositeView.extend({  
    	template: templates.settings,
    	childView: FolderView,
    	childViewContainer: '#folder-list',
    	
    	ui:{
        	inputFolderName: 'input[placeholder=Name]',
        	inputFolderPath: 'input[placeholder=Path]',
        	immediateUpload: '#immediate_upload',
        	sizeOriginal: 'button[data=original]',        	
        	sizeSmall: 'button[data=small]',
        	btnChange: '#go_setting',
        	btnCancel: '#cancel'
        },
        
        events:{
        	'click @ui.btnChange' : 'changeSetting',
        	'click @ui.btnCancel' : 'cancel',
        	'click @ui.sizeOriginal' : 'sizeSelected',        	
        	'click @ui.sizeSmall' : 'sizeSelected'
        },
    	
        onRender: function(){       
        	_.each(this.collection.models, function(folder, index){
        		console.log($('button[data=original]'));
        		console.log(index);
        		if(folder.get('imageResize') == 0)
        			$(this.sizeOriginal[index]).addClass('active');        		
        		else
        			$(this.sizeSmall[index]).addClass('active');        		
        	}, this.ui);
        	this.ui.immediateUpload.prop('checked', this.model.get("uploadImmediately")=="1");                	
        },
    });
});