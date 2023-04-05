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
        $('#datatable').on("click", ':input[name="enabled"]', function () {
            enableUser($(this));
        })
    );
});

function enableUser(checkbox) {
    let row = $(checkbox).closest('tr');
    let checked = $(checkbox).is(':checked');

    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl + "enable",
        data: {id: $(row).attr("id"), enabled: checked},
    }).done(function () {
        row.attr('data-user-enabled', checked);
        successNoty(checked? "Enabled": "Disabled");
    }).fail(function () {
        $(checkbox).prop("checked", !checked);
    });
}
