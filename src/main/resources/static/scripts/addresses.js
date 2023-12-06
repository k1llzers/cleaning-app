document.querySelectorAll('.editbtn').forEach(function(button) {
    button.addEventListener('click', function(event) {
        onEditBtnClick(event, button);
    });
});

    document.querySelectorAll('.deletebtn').forEach(function(button) {
    button.addEventListener('click', function(event) {
        onDeleteBtnClick(event, button);
    });
});

function onEditBtnClick(event, button) {
    var id = event.target.getAttribute('data-id');
    var row = document.getElementById('row-' + id);
    var hasOrders = button.getAttribute('hasOrders');
    if (hasOrders == 'true') {
        document.querySelector('#editNotice').innerHTML = 'This address has orders attached to it. <br>Editing it will <strong>NOT</strong> affect those orders.';
    } else {
        document.querySelector('#editNotice').textContent = '';
    }
    document.getElementById('editId').value = id;
    document.getElementById('editCity').value = row.querySelector('.city').textContent;
    document.getElementById('editStreet').value = row.querySelector('.street').textContent;
    document.getElementById('editHouseNumber').value = row.querySelector('.houseNumber').textContent;
    document.getElementById('editFlatNumber').value = row.querySelector('.flatNumber').textContent;
    document.getElementById('editAddressOverlay').style.display = 'block';
}

function onDeleteBtnClick(event, button) {
    var hasOrders = button.getAttribute('hasOrders');
    if (hasOrders == 'true') {
        document.querySelector('#deleteNotice').innerHTML = 'This address has orders attached to it. <br>Deleting it will <strong>NOT</strong> affect those orders.';
    } else {
        document.querySelector('#deleteNotice').textContent = '';
    }
    document.getElementById('deleteId').value = event.target.getAttribute('data-id');
    document.getElementById('deleteAddressOverlay').style.display = 'block';
}

function closeEditOverlay() {
    document.getElementById('editAddressOverlay').style.display = 'none';
}

function closeAddOverlay() {
    document.getElementById('addAddressOverlay').style.display = 'none';
}

function closeDeleteOverlay() {
    document.getElementById('deleteAddressOverlay').style.display = 'none';
}

document.getElementById('addAddressBtn').addEventListener('click', function() {
    document.getElementById('addAddressOverlay').style.display = 'block';
});


$(document).ready(function() {
    $('#deleteAddressForm').on('submit', function(e) {
        e.preventDefault();

        var form = $(this);
        var row = $('#row-' + form.find('input[name="id"]').val());
        var id = document.getElementById('deleteId').value;

        $.ajax({
            url: '/removeAddress?id=' + id,
            type: 'post',
            success: function() {
                row.remove();
                closeDeleteOverlay();
            },
            error: function() {
            }
        });
    });
});

$(document).ready(function() {
    $('#editAddressForm').on('submit', function(e) {
        e.preventDefault();

        var form = $(this);
        var row = $('#row-' + form.find('input[name="id"]').val());
        var data = {};
        form.serializeArray().forEach(function(item) {
            data[item.name] = item.value;
        });

        $.ajax({
            url: '/editAddress',
            type: 'post',
            datatype: 'json',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function() {
                row.find('.city').text(form.find('input[name="city"]').val());
                row.find('.street').text(form.find('input[name="street"]').val());
                row.find('.houseNumber').text(form.find('input[name="houseNumber"]').val());
                row.find('.flatNumber').text(form.find('input[name="flatNumber"]').val());
                closeEditOverlay();
            },
            error: function() {
            }
        });
    });
});

$(document).ready(function() {
    $('#addAddressForm').on('submit', function(e) {
        e.preventDefault();

        var form = $(this);
        var table = $('#addressesTable');
        var data = {};
        form.serializeArray().forEach(function(item) {
            data[item.name] = item.value;
        });

        $.ajax({
            url: '/addAddress',
            type: 'post',
            datatype: 'json',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function(id) {
                var row = '<tr id="row-' + id + '">';
                row += '<td class="city">' + form.find('input[name="city"]').val() + '</td>';
                row += '<td class="street">' + form.find('input[name="street"]').val() + '</td>';
                row += '<td class="houseNumber">' + form.find('input[name="houseNumber"]').val() + '</td>';
                row += '<td class="flatNumber">' + form.find('input[name="flatNumber"]').val() + '</td>';
                row += '<td style="display: flex;">';
                    row += '<button class="editbtn" style="margin-right:10px;" data-id="' + id + '" hasOrders="false">Edit</button>';
                    row += '<button class="deletebtn" style="margin-right:10px;"  data-id="' + id + '" hasOrders="false">Delete</button>';
                row += '</td>';
                row += '</tr>';
                table.append(row);

                var editbtn = document.querySelector('.editbtn[data-id="' + id + '"]');
                editbtn.addEventListener('click', function(event) {
                    onEditBtnClick(event, editbtn);
                });

                var deletebtn = document.querySelector('.deletebtn[data-id="' + id + '"]');
                deletebtn.addEventListener('click', function(event) {
                    onDeleteBtnClick(event, deletebtn);
                });

                closeAddOverlay();
                form.find('input[name="city"]').val('');
                form.find('input[name="street"]').val('');
                form.find('input[name="houseNumber"]').val('');
                form.find('input[name="flatNumber"]').val('');
            },
            error: function() {
            }
        });
    });
});


window.onload = function() {
    var addOverlay = document.getElementById('addAddressOverlay');
    var editFormOverlay = document.getElementById('editFormOverlay');
    var deleteOverlay = document.getElementById('deleteAddressOverlay');

    window.onclick = function(event) {
        if (event.target == addOverlay) {
            addOverlay.style.display = 'none';
        } else if (event.target == editFormOverlay) {
            editFormOverlay.style.display = 'none';
        } else if (event.target == deleteOverlay) {
            deleteOverlay.style.display = 'none';
        }
    }

    window.onkeydown = function(event) {
        if (event.key == 'Escape') {
            if (addOverlay.style.display == 'block') {
                addOverlay.style.display = 'none';
            }
            if (editFormOverlay.style.display == 'block') {
                editFormOverlay.style.display = 'none';
            } else if (deleteOverlay.style.display == 'block') {
                deleteOverlay.style.display = 'none';
            }
        }
    }
}