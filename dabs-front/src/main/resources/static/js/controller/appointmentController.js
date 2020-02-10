
var app = app || {};

app.appointmentController = function () {

    function AppointmentController(model, viewBag) {
        this._model = appointmentModel;
        this._viewBag = viewBag;
    }

    AppointmentController.prototype.getAddNewForm = function () {
    	$('#modal-form div div div#modal-body').html('');
    	var _this = this;
    	this._model.getAddNewForm().then(function (res) {
    		_this._viewBag.loadForm(res);
        }, function (error) {
        	console.log(error);
        });
    };
    
    AppointmentController.prototype.getEditForm = function (id) {
    	var _this = this;
    	this._model.getEditForm(id).then(function (res) {
    		_this._viewBag.loadForm(res);
        }, function (error) {
        	console.log(error);
        });
    };

    AppointmentController.prototype.update = function (id) {
    	
    	var _appData;
    	var dataAjax = this._model.getAppointmentDataById(id).then(function (res) {
    		_appData = res;
        }, function (error) {
        	console.log(error);
        });
    	
    	var _this = this;
    	var formAjax = this._model.getEditForm().then(function (res) {
    		_this._viewBag.loadForm(res);
    		_this._viewBag.closeForm();
    		_this._viewBag.reloadDataTable();
        }, function (error) {
        	console.log(error);
        });

    	$(document).on('shown.bs.modal', function (e) {
    		if(_appData == null) return;
    		_this._viewBag.getFormDom().find('#date').val(_appData['date'].substring(0, 10));
    		_this._viewBag.getFormDom().find('#dateFrom').val(_appData['dateFrom']);
    		_this._viewBag.getFormDom().find('#dateTo').val(_appData['dateTo']);
    		_this._viewBag.getFormDom().find('#description').text(_appData['description']);
    		_this._viewBag.getFormDom().find('#patientId').val(_appData['patientId']);
    		_this._viewBag.getFormDom().find('#id').val(_appData['id']);
    		_appData = null;
    	});
    };
    
    AppointmentController.prototype.view = function (id) {
    	var _this = this;
    	this._model.getAppointmentById(id).then(function (data) {
    		_this._viewBag.loadForm(data);
        }, function (error) {
            console.log(error);
        });
    };
    
    AppointmentController.prototype.submitNewAppointment = function () {
    	var _this = this;
    	
    	this._viewBag.getFormDom().find('div.has-danger').removeClass('has-danger');
    	this._viewBag.getFormDom().find('div.form-control-feedback').remove();

    	this._model.submitNewForm(this._viewBag.getFormDom().serialize())
	    	.then(function (res) {
	    		_this._viewBag.loadForm(res);
	    		_this._viewBag.closeForm();
	    		_this._viewBag.reloadDataTable();
	    		$('#alert-message').css('visibility','visible').text('New Appointment Submitted');
	        }, function (error) {
	        	if(error.status == 400){
		        	$.each( error.responseJSON, function( key, value ) {
		        		var $element = _this._viewBag.getFormDom().find('[name="' + key + '"]');
		        			$element.parent().addClass('has-danger');
		        			$('<div>').addClass('form-control-feedback')
		        				.text(value).appendTo($element.parent());
	        		});
	        	} else {
	        		$('#form-alert-message').text(error.responseJSON.message)
	        		$('#form-alert-message').css('visibility','visible');
	        	}
	        });
    };
    
    AppointmentController.prototype.remove = function (id) {
    	var _this = this;
    	this._model.remove(id)
	    	.then(function (res) {
	    		//_this._viewBag.loadForm(res);
	    		_this._viewBag.closeForm();
	    		_this._viewBag.reloadDataTable();
	    		$('#alert-message').css('visibility','visible').text('Appointment Delete');
	        }, function (error) {
	        	console.log(error);
	        	$('#form-alert-message').text(error.responseJSON.message)
        		$('#form-alert-message').css('visibility','visible');
	        });
    };
    
    return {
        load: function (model, viewBag) {
            return new AppointmentController(model, viewBag);
        }
    }
}();