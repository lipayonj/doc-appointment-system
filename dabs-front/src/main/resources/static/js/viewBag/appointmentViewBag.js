
var app = app || {};

app.appoinmentViewBag = (function () {
	
	function getForm(){
		return $('#modal-form div div div#modal-body');
	}
	
    function loadForm(data) {
    	clearContent();
    	getForm().html(data);
    	show();
    }
    
    function show(){
    	$('#modal-form').modal('show');
    }

    function closeForm(){
    	$('#modal-form').modal('hide');
    }

    function clearContent() {
    	getForm().html('');
    }

    function getFormDom(){
    	return $('#modal-form div div form');
    }

    function reloadDataTable(){
    	$('#table').bootstrapTable('refresh');
    }
    

    function loadDataTable(){
        var table_params = $('#table').bootstrapTable({
            url: '/api/v1/appointment/',
            queryParams: function (p) {
                return {
                	//page: 0,
                    from: $('#filter_from').val(),
                    to:$('#filter_to').val()
                };
            },
            columns: [
            	{
	            		formatter : function(value,row,index) {
		                    return row.rowNo;
		                 }
		            }, {
		            	formatter : function(value,row,index) {
		                    return row.date;
		                 }
		            },{
		            	formatter : function(value,row,index) {
		                    return row.dateFrom;
		                 }
		            },{
		            	formatter : function(value,row,index) {
		                    return row.dateTo;
		                 }
		            }, {
		            	formatter : function(value,row,index) {
		                    return row.patientName;
		                 }
		            },
		                {
		                  field: 'operate',
		                  title: '<button type="button" class="btn btn-primary" id="add-new-btn">Add New</button>',
		                  align: 'center',
		                  valign: 'middle',
		                  clickToSelect: false,
		                  formatter : function(value,row,index) {
		                    return '<a class="btn btn-secondary appEdit" data-id="'+row.id+'">Edit</a>'
                	  				+'<a class="btn btn-info appView" data-id="'+row.id+'">View</a>'
                	  				+'<a class="btn btn-danger appDelete" data-id="'+row.id+'">Delete</a>';
		                  }
		                }
	              ],
	              responseHandler: function (res) {
	            	  console.log(res);
	                  return res;
	              }
        });
        $('#table').bootstrapTable('refresh');
    }

    return {
        load: function () {
            return {
            	show:show,
            	closeForm:closeForm,
            	getForm:getForm,
            	loadForm: loadForm,
            	clearContent: clearContent,
            	getFormDom:getFormDom,
            	loadDataTable:loadDataTable,
            	reloadDataTable:reloadDataTable
            }
        }
    }
}());