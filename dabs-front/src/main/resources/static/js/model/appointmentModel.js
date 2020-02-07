
var app = app || {};

app.appointmentModel = (function() {
    function AppointmentModel(requester) {
        this._requester = requester;
        this._serviceUrl = '/appointment/'
    }

    AppointmentModel.prototype.getAppointmentById = function (id) {
        var queryUrl = this._serviceUrl + id;
        return this._requester.get(queryUrl);
    };
    
    AppointmentModel.prototype.getAppointmentDataById = function (id) {
        var queryUrl = '/api/v1/appointment/'+ id;
        return this._requester.get(queryUrl);
    };
    
    AppointmentModel.prototype.getAddNewForm = function () {
        var queryUrl = this._serviceUrl + 'add';
        return this._requester.get(queryUrl);
    };
    
    AppointmentModel.prototype.getEditForm = function (id) {
        var queryUrl = this._serviceUrl + 'edit/';
        return this._requester.get(queryUrl);
    };

    AppointmentModel.prototype.submitNewForm = function (data) {
        var queryUrl = '/api/v1/appointment/save';
        return this._requester.post(queryUrl, data);
    };
    
    AppointmentModel.prototype.remove = function (id) {
        var queryUrl = '/api/v1/appointment/remove/'+id;
        return this._requester.remove(queryUrl);
    };
    
    AppointmentModel.prototype.updateForm = function (data) {
        var queryUrl = '/api/v1/appointment/update';
        return this._requester.put(queryUrl, data);
    };


    AppointmentModel.prototype.getAppointmentByDates = function (from, to) {
        var queryUrl = this._serviceUrl;
        return this._requester.get(queryUrl, {'dateFrom': from, 'dateTo': to});
    };
    
    AppointmentModel.prototype.postAppointment = function (data) {
        var queryUrl = this._serviceUrl + 'add';
        return this._requester.post(queryUrl, data);
    };
    
    AppointmentModel.prototype.deleteAppointment = function (id) {
        var queryUrl = this._serviceUrl + id;
        return this._requester.remove(queryUrl);
    };

    return {
        load: function(requester) {
            return new AppointmentModel(requester);
        }
    }
}());