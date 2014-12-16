require.config({
    
    paths:{        
        jquery: '../libs/jquery-1.11.1.min',                
        bootstrap: '../libs/bootstrap-3.1.1/js/bootstrap',
        bootbox: '../libs/bootbox-4.2.0.min',        
        growl: '../libs/bootstrap-growl.min',        
        jquery_form: '../libs/jquery.form',        
        jquery_cookie: '../libs/jquery.cookie',        
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
		},
		growl: {
			deps: ['bootstrap']
		},
		jquery_form: {
            deps: ['jquery']
		},
		jquery_cookie: {
            deps: ['jquery']
		}
    },
    deps: ['jquery', 'underscore', 'handlebars', 'bootstrap','bootbox','growl','jquery_form','jquery_cookie']    
});

require(['app'], function(App){    
	App.start(); 
});