define([    
    'backbone',
    'marionette',    
    'templates',
    'bootbox',
    'growl'
], function(Backbone, Marionette, templates, bootbox, growl){    
    return Marionette.ItemView.extend({    
    	template: templates.setting,
        tagName: 'div',
        
        ui:{
        	inputFolderName: '.text-folder-name',
        	inputFolderPath: '.text-folder-path',
        	immediateUpload: '#immediate_upload',
        	printerPanel: '#printer_panel',
        	printerName: '#printer_name',
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
        
        initialize: function(){
        	_.bindAll(this, "onSave");
        },
        
        onRender: function(){       
        	_.each(this.model.get("folders"), function(folder, index){
        		$(this.inputFolderName[index]).val(folder.name);
        		$(this.inputFolderPath[index]).val(folder.path);
        		
        		if(folder.imageResize == 0)
        			$(this.sizeOriginal[index]).addClass('active');        		
        		else
        			$(this.sizeSmall[index]).addClass('active');
        		
        	}, this.ui);
        	this.ui.immediateUpload.prop('checked', this.model.get("uploadImmediately")=="1");
        	if(this.model.get("printer")){
        		this.ui.printerPanel.show();
        		this.ui.printerName.html(this.model.get("printer"));        		
        	}
        	else{
        		this.ui.printerPanel.hide();
        		this.ui.printerName.html("");        		
        	}
        		
        },
        
        sizeSelected: function(e){
        	$(e.target).parent().children().removeClass('active');
        	$(e.target).addClass('active');
        },        
        
        changeSetting: function(){
        	var folders = this.model.get("folders");
        	
        	_.each(this.ui.inputFolderName, function(item, index){
        		if(!_.isUndefined(this[index])){        		
            		this[index].name = $(item).val();	
        		}        		 
        	}, folders);
        	
        	_.each(this.ui.inputFolderPath, function(item, index){
        		if(!_.isUndefined(this[index])){    			
        			this[index].path = $(item).val();
        		}
        	}, folders);
        	
        	_.each(this.ui.sizeOriginal, function(item, index){
        		if(!_.isUndefined(this[index]))    			        			
        			this[index].imageResize = $(item).hasClass('active')? 0 : 1;        			        		
        	}, folders);        	        	
        	        	        	        
        	if(this.validateFolders(this.model.get("folders")) == 1){
        		this.model.set("folders", folders, {validate:true});
        		this.model.set("uploadImmediately", this.ui.immediateUpload.is(':checked')? 1:0);        	
        		this.model.save({},{ success: this.onSave});        		        		
        	}
        	else
        		bootbox.alert("Folder not valid!");
        },
        
        validateFolders: function(folders) {			
			var i = -1;
			_.each(folders, function(folder, index){
				if(_.isEmpty(folder.name) && _.isEmpty(folder.path)){
					i = 1;
				}
				else if(_.isEmpty(folder.name) || _.isEmpty(folder.path) )
					i = -1;
				else
					i = 1;
			}, i);
			return i;						
		},
        
        onSave: function(model, response){        	
        	if(response.id < 0){
        		bootbox.alert(response.message);
        	}
        	else{
        		$.growl("Settings saved. &nbsp;&nbsp;&nbsp;&nbsp;", {type: 'info', placement:{from: 'bottom', align:'center'}, delay: 2000});
        		this.model.trigger('change');
        		this.cancel();
        	}
        },
        
        cancel: function(){
            window.app.vent.trigger("dialog-close");
        }
        
    });
});