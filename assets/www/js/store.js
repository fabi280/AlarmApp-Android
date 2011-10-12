app.stores.SmartphoneInfos = new Ext.data.JsonStore({
    model  : 'SmartphoneInfo',

    data: [
        {Name: 'Name',   Value: 'device.name'},
        {Name: 'Phonegap',     Value: 'device.phonegap'},
        {Name: 'Platform',      Value: 'device.platform'},
        {Name: 'UUID',   Value: 'device.uuid'},
        {Name: 'Version',   Value: 'device.version'},
    ]
});

app.stores.UserInfos = new Ext.data.Store({
	model: "UserInfo",
});