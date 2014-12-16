define([    
    'backbone',
    'marionette',        
], function(Backbone, Marionette, templates){
	
	return  Marionette.Region.extend({
		onShow: function(view){
            /*this.listenTo(view, "dialog:close", this.closeDialog); 
            var self = this;*/
            //this.$el.modal({ backdrop: true, keyboard: true, show: true});
        },
 
        closeDialog: function(){
        	this.$el.modal('hide');
            this.reset();
        },
        
        showModal: function(){        	
        	//view.on("close", this.hideModal, this);
        	this.$el.modal('show');
        },

        hideModal: function(){
        	this.$el.modal('hide');
        }
    });
});