app.views.Viewport = Ext.extend(Ext.TabPanel, {
    fullscreen: true,
	id: 'mainPanel',
    initComponent: function() {
        Ext.apply(this, {
            tabBar: {
                dock: 'bottom',
                layout: {
                    pack: 'center'
                }
            },
            items: [
                { xtype: 'home_view', id:"home" },
                { xtype: 'smartphone_details', id:"smartphone_details"},
                { xtype: 'authenticate', id:'authenticate'},
            ]
        });
        app.views.Viewport.superclass.initComponent.apply(this, arguments);
    }
});
