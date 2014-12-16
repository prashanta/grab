define([
    'backbone', 
    'marionette',
    'models/Model.App',
    'models/Model.UserProfile',
    'collections/Collection.Folders',
    'views/View.Header',    
    'views/View.Body',    
    'views/View.Setting',    
    'views/View.Settings',    
    'views/View.FolderSelector',    
    'regions/Region.Setting'    
], function(Backbone, Marionette, AppModel, UserProfileModel, FoldersCollection, Header, Body, SettingView, SettingsView, FolderSelectorView, SettingRegion){
	
    // Replace underscore.js with handlebars.js template complier 
    Marionette.TemplateCache.prototype.compileTemplate = function (rawTemplate) {
        return Handlebars.compile(rawTemplate);        
    };
    
    // Loading animation stuff
    var $loading = $('#loadingDiv').hide();
    window.loading = $loading;
	$(document).ajaxStart(function () { $loading.show(); }).ajaxStop(function () { $loading.hide(); });
    
    // Create application
    var app = new Marionette.Application();
    app.addRegions({
        header: '#header',        
        body: '#body',
        modalRegion: {
            regionClass: SettingRegion,
            selector: '#modal_setting'
        }   	
    });        
    
    var profile = new UserProfileModel({userid: $.cookie("uid")});
    //var folders = new FoldersCollection();
    //folders.fetch();
    var header = new Header({model: new AppModel()});
    app.header.show(header);
        
    app.vent.on('setting-show', function(){        		
		app.modalRegion.show(new SettingView({model: profile}));
		app.modalRegion.showModal();
    	//app.body.show(new SettingsView({collection: folders, model: profile}));
	});
    
    app.vent.on('folderselector-show', function(){        		
    	app.modalRegion.show(new FolderSelectorView({model: profile}));
    	app.modalRegion.showModal();
    });        
    
    app.vent.on('dialog-close', function(){        
    	app.modalRegion.closeDialog();		
    });
        
    profile.fetch({
    	success: function(){
    	    var body = new Body({model: profile});
    		app.body.show(body);    		    		    	
    	}
    }); 
    
    
    window.app = app;
    return app; 
});