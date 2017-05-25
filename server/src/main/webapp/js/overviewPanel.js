function addMachine(ip) {
    var container = $('<div style="cursor: pointer" name></div>')
    var iconElm = $('<span class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span>');
    var ipElm = $('<span class="glyphicon-class" style="margin-left: 4px; padding : 2px">' + ip + '</span>');
    var machineElm = $('<div name="' + ip + '">' +
        '</div>');
    container.append(iconElm);
    container.append(ipElm);
    machineElm.append(container);
    $('#machineList').append(machineElm);

    container.on('click', function () {
        $(this).toggleClass('selected');
        $('[name]').not($(this)).removeClass('selected');

        showMachinePanel(ip);
        $('#servicePanel').hide();
    });

    iconElm.on('click', function (e) {
        $(this).toggleClass('glyphicon-chevron-down');
        $(this).toggleClass('glyphicon-chevron-right');

        $(this).parent().parent().find('[name*=":"]').toggle();
        e.stopPropagation();
    })
    return machineElm;
}

function addService(machineElm, service) {
    var serviceElm = $('<div name="' + service.ip + ':' + service.pid + '" style="margin:2px 2px 2px 10px;cursor: pointer; padding : 2px">' +
        '<span class="glyphicon glyphicon-flash" aria-hidden="true"></span>' +
        '<span class="glyphicon-class" style="margin-left: 4px">' + service.name + '(' + service.port + ')' + '</span>' +
        '</div>');

    serviceElm.on('click', function () {
        $(this).toggleClass('selected');
        $('[name]').not($(this)).removeClass('selected');

        hideMachinePanel();
        showServicePanel(service.id);
    })

    machineElm.append(serviceElm);
}

function add(service) {
    var machineElm = $('[name = "' + service.ip + '"]');
    if (machineElm.length != 0) {
        if ($('[name="' + service.ip + ':' + service.pid + '"]').length == 0)
            addService(machineElm, service);
    } else {
        machineElm = addMachine(service.ip);
        addService(machineElm, service);
    }
}

function remove(service) {
    $('[name="' + service.ip + ':' + service.pid + '"]').remove();
}
