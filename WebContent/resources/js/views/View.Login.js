define([    
    'backbone',
    'marionette',
    'models/Model.Login',
    'templates',
    'bootbox'
], function(Backbone, Marionette, LoginModel, templates, bootbox){    
    return Marionette.ItemView.extend({    
        template: templates.login,        
        tagName: 'div',
        model: new LoginModel(),
        
        ui:{
        	form: 'form',
        },
        
        events: {
            'click #submit': 'submitLogin',
        },
        
        modelEvents:{
        	'invalid' : 'dataInvalid'
        },
        
        submitLogin: function(){        	
            Backbone.emulateJSON = true;            
            this.model.save({}, {
            	data: this.ui.form.serializeArray(),
            	success: function(model,response){
            		if(response){
            			window.location = 'start.jsp';
            		}
            		else
            			bootbox.alert("Username or password is wrong. Please try again.");
            	}
            });
        },
        
        dataInvalid: function(model, error){
        	bootbox.alert(error);
        }             
    });
});