$(document).ready(function() {
    $.urlParam = function(name) {
        var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
        if (results == null) {
            return null;
        } else {
            return results[1] || 0;
        }
    };

    $.makeid = function(length) {
        var result = '';
        var characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        var charactersLength = characters.length;
        for (var i = 0; i < length; i++) {
            result += characters.charAt(Math.floor(Math.random() * charactersLength));
        }
        return result;
    };

    $("#btnClickMe").click(function() {
        console.log('Hello world...');
        window.parent.publicAPIProvider.updateData($.urlParam('itemid'), 'prs_id', $.makeid(5));
        window.parent.publicAPIProvider.updateData($.urlParam('itemid'), 'prs_is_active', true);
        window.parent.publicAPIProvider.saveAllItems();
    });

    $("#btnDoAction").click(function() {
        let result = window.parent.publicAPIProvider.performAction($.urlParam('itemid'), 'a_set_prop_is_active');
        if (result) {
            console.log('Action done!');
        }
    });

    $("#btnNavigate").click(function() {
        let result = window.parent.publicAPIProvider.navigate($.urlParam('itemid'), {
            'clearBreadcrumb': true,
            'breadcrumbName': 'Person-1',
            'layoutId': $('#inputLayoutId').val()
        });
        console.log(result);
    });

    window.parent.publicAPIProvider.getItemData($.urlParam('itemid')).then((itemData) => {
        console.log(itemData);
        $('#inputName').attr('value', itemData.Properties.prs_name);
        $('#inputAge').attr('value', itemData.Properties.prs_age);
        $('#inputIdentity').attr('value', itemData.Properties.prs_id);
    });

});