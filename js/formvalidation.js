$(function() {
    $("form[name='subscribe']").validate({
        rules: {
            "RuC1VnxFze": "required",
            "82ibVdHmnN": "required",
            "EOK697iero": {
                required: true,
                email: true
            }
        },
        messages: {
            "RuC1VnxFze": "Please enter your first name",
            "82ibVdHmnN": "Please enter your last name",
            "EOK697iero": "Please enter a valid email address"
        },
        submitHandler: function(form) {
            form.submit();
        }
    });

    $("form[name='quiz']").validate({
        rules: {
            "LwGuRGc9Yo": "required",
            "ePCIfeH8NN": "required",
            "PkFGrZcJyR": {
                required: true,
                email: true
            }
        },
        messages: {
            "LwGuRGc9Yo": "Please enter your first name",
            "ePCIfeH8NN": "Please enter your last name",
            "PkFGrZcJyR": "Please enter a valid email address"
        },
        submitHandler: function(form) {
            form.submit();
        }
    });

    $("form[name='feedback']").validate({
        rules: {
            "k8XsmX0JQC": "required",
            "5R9Tw5dSJv": "required",
            "09pKJqfKgH": "required",
            "nr0rSpcGwX": {
                required: true,
                email: true
            }
        },
        messages: {
            "k8XsmX0JQC": "Please enter your first name",
            "5R9Tw5dSJv": "Please enter your last name",
            "09pKJqfKgH": "Please enter your feedback",
            "nr0rSpcGwX": "Please enter a valid email address"
        },
        submitHandler: function(form) {
            form.submit();
        }
    });
});