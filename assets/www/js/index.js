app = new Ext.Application({
    name: "app",

    launch: function() {
    	console.log("launch")
        this.views.viewport = new this.views.Viewport();
        this.views.smartphone_details = this.views.viewport.getComponent('home');
    }
});