require.config({
    
    paths:{        
        jquery: '../libs/jquery-1.11.1.min',
        bootstrap: '../libs/bootstrap-3.1.1/js/bootstrap',
        bootbox: '../libs/bootbox-4.2.0.min',        
        underscore: '../libs/underscore-1.6.0.min',
        handlebars: '../libs/handlebars-v1.3.0',
        backbone: '../libs/backbone-1.1.2.min',
        marionette: '../libs/backbone.marionette-2.1.0.min',                
        text: '../libs/text'        
    },
    shim:{
        backbone: {
			exports: 'Backbone',
			deps: ['jquery', 'underscore', 'handlebars']
		},
        marionette: {
			exports: 'Marionette',
			deps: ['backbone']
		},		
        handlebars: {
			exports: 'Handlebars'			
		},
        bootstrap: {			
            deps: ['jquery']
		},
        bootbox: {
            deps: ['bootstrap']
		}
    },
    deps: ['jquery', 'underscore', 'handlebars', 'bootstrap','bootbox', 'backbone', 'marionette'],
    
    callback: function(marionette){
    	Marionette.TemplateCache.prototype.compileTemplate = function (rawTemplate) {
            return Handlebars.compile(rawTemplate);        
        };
    }    
});


require([    
	'backbone',
    'marionette',
    'models/Model.App',        
    'views/View.Header',
    'views/View.Register',
    'templates'
    ], function(Backbone, Marionette, AppModel, Header, Register,  templates){

		var $loading = $('#loadingDiv').hide();
		$(document).ajaxStart(function () { $loading.show(); }).ajaxStop(function () { $loading.hide(); });
		
		var app = new Marionette.Application();
		app.addRegions({
			header : '#header',
			register : '#register'			
		});
		
		var header = new Header({model: new AppModel()});
		var register = new Register();
		
		app.header.show(header);
		app.register.show(register);
		
		app.start();
 });