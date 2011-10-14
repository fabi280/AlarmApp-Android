var AlarmService = {
		_base_url: "http://localhost:8000",
		
		_url: function(urlToken) {
			return this._base_url  + urlToken
		},
		
		_checkResult : function(okFunc, failureFunc ){
			return function(response) {
				if(response.result == "ok")
					okFunc(response)
				else
					failureFunc(response)
			}
		},
		
		_decodeResult : function(func) {
			return function(response, options) {
				var obj = Ext.decode(response.responseText)
				func(obj);
			}
		},
		
		_withLog: function(func) {
			return function(response, options) {
				console.log("Response is");
				console.log(Ext.encode(response));
				func(response);
			};
		},

		/**
		 * @param username: The name of the user to log in
		 * @param password: The password of the user to log in
		 * @param onSuccess: The callback method to call in case of success
		 * @param onFailure: The callback method to call in case of failure. 
		 * This callback should have one parameter for failure details
		 * 
		 * @returns void
		 */
		login: function(username, password, onSuccess, onFailure) {
			Ext.Ajax.request({
				url: this._url("/web_service/login/"),
				params: {
					username:username,
					password:password,
				},
				method:"POST",
				success: this._decodeResult(this._checkResult(onSuccess, onFailure)),
				failure: this._withLog(onFailure),
			});
		},
		
		/***
		 * This function requests an auth token. The auth token might not be authenticated yet.
		 * 
		 * @param purpose: A brief summary how the auth token will be used
		 * @param onSuccess: A callback function that is executed in case of success. 
		 * This callback expects one parameter for the returned auth token
		 * @param onFailure: A callback function that is executed in case of failure. This callback 
		 * expects one parameter for failure details
		 */
		requestAuthToken: function(purpose, onSuccess, onFailure) {
			
		},
		 
		
		/***
		 * @param authToken: The auth token to activate
		 * @param onSuccess: A callback function that is executed in case of success. 
		 * @param onFailure: A callback function that is executed in case of failure. This callback 
		 * expects one parameter for failure details
		 */
		activateAuthToken: function(authToken, onSuccess, onFailure) {
			
		},
		
		/**
		 * This function checks if an auth token is valid
		 */
		checkAuthToken: function(authToken, onSuccess, onFailure) {
			
		},
		
		createSmartphone: function(smartphone, onSuccess, onFailure ) {
			
		},
		
		getAlarmStatusList: function(operation, onSuccess, onFailure) {
			
		},
		
		setAlarmStatus: function(operation, status, onSuccess, onFailure) {
			
		},
}