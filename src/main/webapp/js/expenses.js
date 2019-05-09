'use strict';

var rootUrl = "/java_s04/api/v1.1/expenses";
var getStatusUrl = "/java_s04/api/v1.1/statuses";
// var getPhotoUrl = "/java_s04/api/v1.1/photos";

initPage();

$('#applyExpense').click(function() {
	$('.error').children().remove();
	if ($('#expenseId').val() === '') {
		$('.error').append('<div>経費IDは必須入力です。</div>');
	}
	if ($('#applyDate').val() === '') {
		$('.error').append('<div>申請日は必須入力です。</div>');
	}
	if ($('#title').val() === '') {
		$('.error').append('<div>タイトルは必須入力です。</div>');
	}
	if ($('#payee').val() === '') {
		$('.error').append('<div>支払先は必須入力です。</div>');
	}
	if ($('#price').val() === '') {
		$('.error').append('<div>金額は必須入力です。</div>');
	}
	if ($('.error').children().length != 0) {
		return false;
	}

	var id = $('#id').val()
	if (id === '')
		addExpense();
	else
		updateExpense(id);
	return false;
})

$('#findExpense').click(function() {
	findByParam();
	return false;
})

$('#newExpense').click(function() {
	renderDetails({});
});

function initPage() {
	var newOption = $('<option>').val(0).text('指定しない').prop('selected', true);
	$('#expStatusParam').append(newOption);
	makeStatusSelection('#expStatusParam');
	findAll();
//	makePostSelection('#postId');
//	最後のこれは必要ない。statusは選択できるものでないから。
}

function findAll() {
	console.log('findAll start.')
	$.ajax({
		type : "GET",
		url : rootUrl,
		dataType : "json",
		success : renderTable
	});
}

// 編集削除ボタンで特定のIDに飛ぶため
function findById(id) {
	console.log('findByID start - id:' + id);
	$.ajax({
		type : "GET",
		url : rootUrl + '/' + id,
		dataType : "json",
		success : function(data) {
			console.log('findById success: ' + data.title);
			renderDetails(data)
		}
	});
}

function findByParam() {
	console.log('findByParam start.');

	var urlWithParam = rootUrl + '?statusId=' + $('#expStatusParam').val()
			+ '&applicantParam=' + $('#applicantParam').val() + '&titleParam='
			+ $('#titleParam').val();
	$.ajax({
		type : "GET",
		url : urlWithParam,
		dataType : "json",
		success : renderTable
	// +console.log('success')
	});
}

function addExpense() {
	console.log('addExpense start');

	var fd = new FormData(document.getElementById("expenseForm"));

	$.ajax({
		url : rootUrl,
		type : "POST",
		data : fd,
		contentType : false,
		processData : false,
		dataType : "json",
		success : function(data, textStatus, jqXHR) {
			alert('経費の申請に成功しました');
//			findAll();
//			renderDetails(data);
//			location.href='./ExpenseList.html';
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert('経費の申請に失敗しました');
			console.log(errorThrown);
		}
	})
}

function updateExpense(id) {
	console.log('updateExpense start');

	var fd = new FormData(document.getElementById("ExpenseForm"));

	$.ajax({
		url : rootUrl + '/' + id,
		type : "PUT",
		data : fd,
		contentType : false,
		processData : false,
		dataType : "json",
		success : function(data, textStatus, jqXHR) {
			alert('経費の更新申請に成功しました');
			findAll();
			renderDetails(data);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert('経費の更新申請に失敗しました');
		}
	})
}

//申請者本人のみ、かつ申請中のみ
function deleteById(id) {
	console.log('delete start - id:' + id);
	$.ajax({
		type : "DELETE",
		url : rootUrl + '/' + id,
		success : function() {
			alert('申請中経費の削除に成功しました');
			findAll();
			renderDetails({});
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert('申請中経費の削除に失敗しました');
		}
	});
}

function renderTable(data) {
	var headerRow = '<tr><th>申請ID</th><th>申請日</th><th>更新日</th><th>申請者</th>'
			+ '<th>タイトル</th><th>金額</th><th>ステータス</th></tr>';

	$('#expenses').children().remove();

	if (data.length === 0) {
		$('#expenses').append('<p>現在データが存在していません。</p>')
	} else {
		var table = $('<table>').attr('border', 1);
		table.append(headerRow);

		$
				.each(
						data,
						function(index, expense) {
							var row = $('<tr>');
							row.append($('<td>').text(expense.expenseId));
							row.append($('<td>').text(expense.applyDate));
							row.append($('<td>').text(expense.changeDate));
							row.append($('<td>').text(expense.applicant));
							row.append($('<td>').text(expense.title));
							row.append($('<td>').text(expense.price));
							row.append($('<td>').text(expense.status.type));
							row
									.append($('<td>')
											.append(
													$('<button>')
															.text("詳細")
															.attr("type",
																	"button")
															.attr("onclick",
																	"location.href='./ExpenseDetail.html'")));
							table.append(row);
						});

		$('#expenses').append(table);
	}
}

function renderDetails(expense) {
	$('.error').text('');
	$('#id').val(expense.id);
	$('#expenseId').val(expense.expenseId);
	$('#applyDate').val(expense.applyDate);
	$('#changeDate').val(expense.changeDate);
	$('#applicant').val(expense.applicant);
	$('#title').val(expense.title);
	$('#payee').val(expense.payee);
	$('#price').val(expense.price);
//	$('input[name="gender"]').val([ employee.gender ]);
//
//	$('#currentPhoto').children().remove();
//	$('#photoId').val(employee.photoId);
//	if (employee.photoId != null && employee.photoId != 0) {
//		// 末尾のDate.now()はキャッシュ対策
//		var currentPhoto = $('<img>').attr('src',
//				getPhotoUrl + '/' + employee.photoId + '?' + Date.now());
//		$('#currentPhoto').append(currentPhoto)
//	}
//	$('#address').val(employee.address);
//	if (employee.post != null) {
//		$('#postId').val(employee.post.id);
//	}
}

function makeStatusSelection(selectionId, expenses) {
	console.log('makeStatusSelection start.')
	$.ajax({
		type : "GET",
		url : getStatusUrl,
		dataType : "json",
		success : function(data, textStatus, jqXHR) {
			$.each(data, function(index, status) {
				var newOption = $('<option>').val(status.id).text(status.type);
				if (expenses != null && expenses.status.id == status.id) {
					newOption.prop('selected', isSelected);
				}
				$(selectionId).append(newOption);
			});
		}
	});
}
