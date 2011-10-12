app.models.SmartphoneInfo = Ext.regModel("SmartphoneInfo", 
{
	fields:
		[
		 "Name",
		 "Value"
		 ]
});

var UserProfile = {
	UserId: None,
	FirstName: None,
	LastName: None,
	Department: None,
	AccessToken: None,
	PushToken: None,
	
	load: function {
		window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, this._gotFileSystem, this._fileSystemFailure);
	},
	
	_fileSystemFailure: function {
		console.log("Error reading the user profile!");
	},
	
	_gotFileSystem: function(fileSystem) {
	    fileSystem.root.getFile("userprofile.txt", {create: true}, this._gotFileEntry, this._fileSystemFailure);
	},
	
	_gotFileEntry: function(fileEntry) {
		fileEntry.file(this._gotFile, this._fileSystemFailure);
	},
	
	_gotFile: function(file) {
	    var reader = new FileReader();
	    reader.onloadend = function(evt) {
	            console.log("Read as text");
	            console.log(evt.target.result);
	    };
	    reader.readAsText(file);
	},
	
	save: function {
		
	},
}
UserProfile.load()