
var list = new Ext.List({
    fullscreen: true,
    itemTpl : '{Name}: {Value}',
    grouped : false,
    indexBar: false,
    
    store: app.stores.SmartphoneInfos
});

app.views.SmartphoneDetails = Ext.extend(Ext.Panel, {
    title: "Gerät",
    items: [list],
    initComponent: function() {
        Ext.apply(this, {
            dockedItems: [{
                xtype: "toolbar",
                title: "Geräteinfos",
                items: [
                	new Ext.Button({
                		text:"Zurück",
                		ui:"back",
                		handler:function(btn, evt) {
                			Ext.getCmp('mainPanel').layout.setActiveItem('home');
                		}
                	}),
                ],
            }]
        });
        app.views.SmartphoneDetails.superclass.initComponent.apply(this, arguments);
    }
});

Ext.reg('smartphone_details', app.views.SmartphoneDetails);
