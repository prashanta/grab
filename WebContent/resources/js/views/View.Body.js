define([    
    'backbone',
    'marionette',
    'models/Model.UserProfile',
    'templates',
    'bootbox',
    'growl'
], function(Backbone, Marionette, UserProfileModel, templates, bootbox, growl){    
    return Marionette.ItemView.extend({    
        template: templates.body,
        tagName: 'div',
        
        options: {
        	readyToUpload: false,
        	recentList: [],
    		recentListMaxCount: 3
    		
        },
        
        ui:{        	
        	filename: '#upload_filename',
        	file: '#file',
        	recentList: '.recent-upload-list',
        	preview: '#image_preview',
        	btnUpload: '#upload',
        	fileSize: '#file_size',
        	btnSetting: '#setting',
        	btnCurrentFolder: '#current_folder_parent',
        	currentFolderText: '#current_folder',
        	uploadForm: '#upload_form'
        },
        
        events: {
            'click #clear_all' : 'clearAll',
            'keydown @ui.filename': 'onKeyEnter',            
            'change #file' : 'processFile',
            'click @ui.btnUpload' : 'uploadFile',
            'click @ui.btnSetting' : function(){ window.app.vent.trigger("setting-show"); },            	
            'click @ui.btnCurrentFolder' : function(){ window.app.vent.trigger("folderselector-show"); }            	
        },
        
        modelEvents: {
            "change": "setupCurrentFolder",            
        },
        
        onRender: function(){
        	this.ui.btnUpload.hide();        	
        	this.setupCurrentFolder();
        	var _this = this;
        	        	
            if(localStorage.getItem("recentList")){        	
            	var recents = localStorage.getItem("recentList").split(",");        	        
            	_.each(recents, function(recentPartName){
            		_this.addToRecentList(recentPartName);
            	});
            	_this.options.recentList = recents;
            }
        },                
        
        setupCurrentFolder: function(){
        	var currentFolder = this.model.get('currentFolder'); 
        	if( currentFolder < 0){
        		this.ui.currentFolderText.text("Click to Select Folder.");
        	}
        	else{        
        		var folders = this.model.get("folders");        		
        		var error = 0;
        		_.each(folders, function(folder, index){
            		if(_.isEmpty(folder.path)){
            			if(index == currentFolder)
            				error = 1;
            		}            		
            	});     
            	if(error){
            		this.ui.currentFolderText.text("Click to Select Folder.");
            	}
            	else{
            		this.ui.btnCurrentFolder.show();
            		var folder = this.model.get('folders')[this.model.get('currentFolder')];
            		var imageSize = folder.imageResize == 0 ? "Original" : "Small";
            		this.ui.currentFolderText.text(folder.name + " [Size: " + imageSize + "]");
            	}        		
        	}
        },
        
        clearAll: function(){
        	this.ui.filename.val("");
        	this.ui.filename.focus();
        },
        
        onKeyEnter: function(e){
        	if(e.keyCode == 13){
        		event.preventDefault();
        		this.uploadFile();
        	}        	
        },                
        
        processFile: function(){
        	var f = this.ui.file[0].files[0];
        	if(typeof f === 'undefined'){        		
        		this.resetFileUpload();
        	}
        	else{        		
        		if(f.type.match('image.*')){
        			var reader = new FileReader();
        			var _this = this;
        			reader.onload = function(e){
        				var size = e.total;
        				size = _this.getHumanReadable(size);
        				_this.ui.fileSize.html("[" + size + "]");
        				$('span', _this.ui.preview).remove();					
        				var span = $("<span/>");
        				span.append("<img class='wFileUpload-thumb' src='" + e.target.result +"' />");
        				_this.ui.preview.append(span);
        				_this.ui.btnUpload.show();
        				_this.options.readyToUpload = true;
        				window.loading.hide();
        				if(_this.model.get("uploadImmediately") == 1)
        					_this.uploadFile();
        			};		
        			window.loading.show();
        			reader.readAsDataURL(f);				
        		}
        		else{
        			$('span', this.ui.preview).remove();
        			bootbox.alert("Only images allowed. Please use image.");
        		}
        	}
        },
        
        uploadFile: function(){
        	if(this.options.readyToUpload){
        		var _this = this;
	        	var filename = this.ui.filename.val();          		
	        	if(_.isEmpty(filename))
	        		bootbox.alert("Please enter filename");        		
	        	else if(RegExp(">|<|/|\\*|\\?|\\||\\\\").test(filename))
	        		bootbox.alert("Part number contains invalid characters.<br> Invalid characters: / \\ ? * | < >");        		
	        	else if(this.model.get("currentFolder") < 0)
	        		bootbox.alert("Please select destination folder.");        	
	        	else{
	        		this.ui.uploadForm.ajaxSubmit({ 
	        			resetForm: true,
			    		success: function(response){
			    			if(response){
				    			_this.resetFileUpload();
				    			_this.addToRecentList(filename);
				    			$.growl("Picture uploaded. &nbsp;&nbsp;&nbsp;&nbsp;", {type: 'info', placement:{from: 'bottom', align:'center'}, delay: 1000});				    			
			    			}
			    			else
			    				$.growl("Could not upload picture. Please try again. &nbsp;&nbsp;&nbsp;&nbsp;", {type: 'danger', placement:{from: 'bottom', align:'center'}, delay: 5000});			    			
			    		}   		    	 		
	    		    });				
				}        	    	    		        
        	}
        },
        
        resetFileUpload: function(){
        	this.ui.file.val('');
        	$('span', this.ui.preview).remove();
			this.ui.btnUpload.hide();
			this.ui.fileSize.html("");
			this.options.readyToUpload = false;
        },
        
        addToRecentList: function(partName){        	
        	var _this = this;
        	if(! _.contains(this.options.recentList, partName) ){
        		
    	    	if(this.options.recentList.length == _this.options.recentListMaxCount){
    	    		$(".recent-filename:first-child", this.el).remove();
    	    		this.options.recentList = _.rest(_this.options.recentList);	    		
    	    	}    		
        		var b = $("<span/>");
        		b.addClass('recent-filename btn btn-danger');    		
        		b.text(partName);    		
        		b.click(function(){    			
        			_this.ui.filename.val($(this).text());    			
    			});
        		this.ui.recentList.append(b);
        		this.options.recentList[_this.options.recentList.length] = partName;
        		localStorage.setItem("recentList",this.options.recentList);
        	}        		
        },
        
        getHumanReadable: function(size){
        	var val = size;
        	var count = 0;
        	var units = ["Bytes", "KB", "MB", "GB"];
        	while(val > 1024){
        		count++;
            	val = val/1024;            	
        	} 	        
        	val = val.toFixed(2);
        	return val + "&nbsp;" + units[count];
        }
    });
});