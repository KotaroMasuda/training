'use strict';

var rootUrl = "/java_s04/api/v1.1/expenses";
var getStatusUrl = "/java_s04/api/v1.1/statuses";
// var getPhotoUrl = "/java_s04/api/v1.1/photos";

initPage();

// ここはNewExpense新規申請画面用。
// $('#applyExpense').click(function() {
// $('.error').children().remove();
// if ($('#name').val() === '') {
// $('.error').append('<div>名前は必須入力です。</div>');
// }
// if ($('#empId').val() === '') {
// $('.error').append('<div>社員IDは必須入力です。</div>');
// }
// if ($('#empId').val() === '') {
// $('.error').append('<div>社員IDは必須入力です。</div>');
// }
// if ($('#age').val() === '') {
// $('.error').append('<div>年齢は必須入力です。</div>');
// }
// if ($('#gender').val() === '') {
// $('.error').append('<div>性別として男性、女性いずれかを選択してください。</div>');
// }
// if ($('#zip').val() === '') {
// $('.error').append('<div>郵便番号は必須入力です。</div>');
// }
// if ($('#pref').val() === '') {
// $('.error').append('<div>都道府県は必須入力です。</div>');
// }
// if ($('#address').val() === '') {
// $('.error').append('<div>住所は必須入力です。</div>');
// }
// if ($('#postId').val() === '') {
// $('.error').append('<div>いずれかの部署を選択してください。</div>');
// }
// if ($('.error').children().length != 0) {
// return false;
// }
//
// var id = $('#id').val()
// if (id === '')
// addEmployee();
// else
// updateEmployee(id);
// return false;
// })

$('#findExpense').click(function() {
	findByParam();
	return false;
})

//$('#newExpense').click(function() {
//	renderDetails({});
//});

function initPage() {
	var newOption = $('<option>').val(0).text('指定しない').prop('selected', true);
	$('#expStatusParam').append(newOption);
	makeStatusSelection('#expStatusParam');
	findAll();
	// makePostSelection('#postId');
	// 最後のこれは必要ない。statusは選択できるものでないから。
	// あとそもそもこのページで新規登録はしない。
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
//function findById(id) {
//	console.log('findByID start - id:' + id);
//	$.ajax({
//		type : "GET",
//		url : rootUrl + '/' + id,
//		dataType : "json",
//		success : function(data) {
//			console.log('findById success: ' + data.title);
//			renderDetails(data)
//		}
//	});
//}

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
//		+console.log('success')
	});
}

// function addEmployee() {
// console.log('addEmployee start');
//
// var fd = new FormData(document.getElementById("employeeForm"));
//
// $.ajax({
// url : rootUrl,
// type : "POST",
// data : fd,
// contentType : false,
// processData : false,
// dataType : "json",
// success : function(data, textStatus, jqXHR) {
// alert('社員データの追加に成功しました');
// findAll();
// renderDetails(data);
// },
// error : function(jqXHR, textStatus, errorThrown) {
// alert('社員データの追加に失敗しました');
// }
// })
// }
//
// function updateEmployee(id) {
// console.log('updateEmployee start');
//
// var fd = new FormData(document.getElementById("employeeForm"));
//
// $.ajax({
// url : rootUrl + '/' + id,
// type : "PUT",
// data : fd,
// contentType : false,
// processData : false,
// dataType : "json",
// success : function(data, textStatus, jqXHR) {
// alert('社員データの更新に成功しました');
// findAll();
// renderDetails(data);
// },
// error : function(jqXHR, textStatus, errorThrown) {
// alert('社員データの更新に失敗しました');
// }
// })
// }
//
//function deleteById(id) {
//	console.log('delete start - id:' + id);
//	$.ajax({
//		type : "DELETE",
//		url : rootUrl + '/' + id,
//		success : function() {
//			alert('経費データの削除に成功しました');
//			findAll();
//			renderDetails({});
//		},
//		error : function(jqXHR, textStatus, errorThrown) {
//			alert('経費データの削除に失敗しました');
//		}
//	});
//}

function renderTable(data) {
	var headerRow = '<tr><th>申請ID</th><th>申請日</th><th>更新日</th><th>申請者</th>' +
		'<th>タイトル</th><th>金額</th><th>ステータス</th></tr>';

	$('#expenses').children().remove();

	if (data.length === 0) {
		$('#expenses').append('<p>現在データが存在していません。</p>')
	} else {
		var table = $('<table>').attr('border', 1);
		table.append(headerRow);

		$.each(data, function(index, expense) {
			var row = $('<tr>');
			row.append($('<td>').text(expense.expenseId));
			row.append($('<td>').text(expense.applyDate));
			row.append($('<td>').text(expense.changeDate));
			row.append($('<td>').text(expense.applicant));
			row.append($('<td>').text(expense.title));
			row.append($('<td>').text(expense.price));
			row.append($('<td>').text(expense.status.type));
			row.append($('<td>').append(
					$('<button>').text("詳細").attr("type", "button").attr(
							"onclick", "location.href='./ExpenseDetail.html'")));
			table.append(row);
		});

		$('#expenses').append(table);
	}
}

//function renderDetails(employee) {
//	$('.error').text('');
//	$('#id').val(employee.id);
//	$('#empId').val(employee.empId);
//	$('#name').val(employee.name);
//	$('#age').val(employee.age);
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
//	$('#zip').val(employee.zip);
//	$('#pref').val(employee.pref);
//	$('#address').val(employee.address);
//	if (employee.post != null) {
//		$('#postId').val(employee.post.id);
//	}
//	$('#enterDate').val(employee.enterDate);
//	$('#retireDate').val(employee.retireDate);
//}

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
