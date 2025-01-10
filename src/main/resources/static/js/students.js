function deleteStudent(id) {

	$.ajax({
		url: '/students/delete-ajax/',
		type: 'POST',
		data: {
			'id': id
		},
		success: function() {
			$("#" + id).remove();
		}
	});



}