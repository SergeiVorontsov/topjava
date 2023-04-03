const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl,
    updateTable: function () {
        $.get(userAjaxUrl, updateTableByData);
    }
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        }),
    );
});


$('#datatable').on('change', ':input[name="enabled"]', function () {
    let row = $(this).closest('tr');
    let id = $(row).attr("id");
    let checked = $(this).prop("checked");

    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl + "enable",
        data: {id: id, enabled: checked},
    }).done(function () {
        switch (checked) {
            case true:
                row.attr('data-user-enabled', "true");
                successNoty("Enabled successful");
                break;
            case false:
                row.attr('data-user-enabled', "false");
                successNoty("Disabled successful");
        }
    });
});