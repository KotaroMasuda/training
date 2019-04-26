'use strict';


$('#applyExpense').click(function() {
	$('.error').children().remove();
	if ($('#name').val() === '') {
		$('.error').append('<div>名前は必須入力です。</div>');
	}
	if ($('#empId').val() === '') {
		$('.error').append('<div>社員IDは必須入力です。</div>');
	}
	if ($('#empId').val() === '') {
		$('.error').append('<div>社員IDは必須入力です。</div>');
	}
	if ($('#age').val() === '') {
		$('.error').append('<div>年齢は必須入力です。</div>');
	}
	if ($('#gender').val() === '') {
		$('.error').append('<div>性別として男性、女性いずれかを選択してください。</div>');
	}
	if ($('#zip').val() === '') {
		$('.error').append('<div>郵便番号は必須入力です。</div>');
	}
	if ($('#pref').val() === '') {
		$('.error').append('<div>都道府県は必須入力です。</div>');
	}
	if ($('#address').val() === '') {
		$('.error').append('<div>住所は必須入力です。</div>');
	}
	if ($('#postId').val() === '') {
		$('.error').append('<div>いずれかの部署を選択してください。</div>');
	}
	if ($('.error').children().length != 0) {
		return false;
	}

	var id = $('#id').val()
	if (id === '')
		addEmployee();
	else
		updateEmployee(id);
	return false;
})

