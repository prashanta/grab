define([    
    'backbone',
    'marionette',
    'models/Model.Register',
    'templates',
    'bootbox'
], function(Backbone, Marionette, RegisterModel, templates, bootbox){    
    return Marionette.ItemView.extend({    
        template: templates.register,        
        tagName: 'div',
        model: new RegisterModel(),
        
        ui:{
        	form: 'form',
        	uname: '[name=uname]',   	
        	password: '[name=pass]',
        },
        
        events: {
            'click #submit': 'submitRegister',
        },
        
        modelEvents:{
        	'invalid' : 'dataInvalid'
        },
        
        submitRegister: function(){
        	var _this = this;
            Backbone.emulateJSON = true;            
            this.model.save({}, {
            	data: this.ui.form.serializeArray(),
            	success: function(model,response){            		
            		if(response.id == 0){
            			bootbox.alert("Registration successful");
            			_this.model.clear();
            			_this.ui.uname.val("");
            			_this.ui.password.val("");
            		}
            		else if(response.id == -1){
            			_this.model.clear();
            			bootbox.alert(response.message);
            		}
            		else
            			bootbox.alert("Something went wrong. Try again.");
            	}
            });
        },
        
        dataInvalid: function(model, error){
        	bootbox.alert(error);
        }
    });
});