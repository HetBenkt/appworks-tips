$(function() {
    $("form[name='hosting']").validate({
        rules: {
            "kZYzubjXZ1": "required",
            "DU7uu8Hd1B": "required",
            "waY3MUqJDo": "required",
            "OTyYwKXWRE": {
                required: true,
                email: true
            }
        },
        messages: {
            "kZYzubjXZ1": "Please enter your first name",
            "DU7uu8Hd1B": "Please enter your last name",
            "waY3MUqJDo": "Please enter your wishes",
            "OTyYwKXWRE": "Please enter a valid email address"
        },
        submitHandler: function(form) {
            form.submit();
        }
    });

    $("form[name='download']").validate({
        rules: {
            "9aiQvAsyHa": "required",
            "pKdGB3hCf6": "required",
            "8azv3a7ZO9": {
                required: true,
                email: true
            }
        },
        messages: {
            "9aiQvAsyHa": "Please enter your first name",
            "pKdGB3hCf6": "Please enter your last name",
            "8azv3a7ZO9": "Please enter a valid email address"
        },
        submitHandler: function(form) {
            form.submit();
        }
    });

    $("form[name='download002']").validate({
        rules: {
            "UIMy41yrHk": "required",
            "frlQMmEVB9": "required",
            "p5xcHl0BfA": {
                required: true,
                email: true
            }
        },
        messages: {
            "UIMy41yrHk": "Please enter your first name",
            "frlQMmEVB9": "Please enter your last name",
            "p5xcHl0BfA": "Please enter a valid email address"
        },
        submitHandler: function(form) {
            form.submit();
        }
    });

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
            "09pKJqfKgH": "Please enter your feedback / question",
            "nr0rSpcGwX": "Please enter a valid email address"
        },
        submitHandler: function(form) {
            form.submit();
        }
    });
});