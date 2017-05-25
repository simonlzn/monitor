var machineTopTimer;

function showMachinePanel(ip) {
    $('#machineIp').val(ip);
    $('#machinePanel').show();

    initLivingServices();
    initCreatePanel();
    initTopInfo();


    function initCreatePanel() {
        $.ajax({
            url: '/list/serviceTemplate',
            success: function (templates) {
                var templateSelect = $('#templateSelect');
                templateSelect.html('<option>Select</option>');
                templates.forEach(function (template) {
                    var option = $('<option></option>');
                    option.val(template.name);
                    option.html(template.name)
                    templateSelect.append(option);
                })
            }
        });

        $('#createBtn').on('click', function () {
            var option = $('#templateSelect').val();
            $.ajax({
                url: '/service/' + ip + '/create',
                type: 'post',
                data: {'name': option},
            })
        })
    }

    function initLivingServices() {
        var serviceControlTemp = $('<tr style="display:none" id="serviceControlTemp">' +
            '<th class="control-label"></th>' +
            '<td><button type="button" class="pull-right btn btn-danger">Kill</button></td>' +
            '</tr>');

        $('#machinePanel').off('serviceListChange').on('serviceListChange', function (serviceList) {
            updateLivingServices(serviceList);
        });

        updateLivingServices(serviceList);

        function updateLivingServices(serviceList) {
            $('#livingServiceList').html('');
            serviceList.forEach(function (service) {
                if (service.ip == ip) {
                    var serviceControl = serviceControlTemp.clone();
                    serviceControl.find('.control-label').html(service.name);
                    serviceControl.attr('data-id', service.id);
                    serviceControl.find('button').on('click', function () {
                        var id = $(this).parent().parent().data('id');
                        $.ajax({
                            url: '/service/' + id + '/kill',
                            type: 'post',
                            success: function () {
                                serviceControl.hide();
                            }
                        })
                    });
                    serviceControl.show();
                    $('#livingServiceList').append(serviceControl)
                }
            });
        }
    }

    function initTopInfo() {
        $('#machinePanel').off('serviceListChange').on('serviceListChange', function (serviceList) {
            updateTopInfo(serviceList);
        });

        updateTopInfo(serviceList);

        function updateTopInfo(serviceList) {
            var pids = [];
            serviceList.forEach(function (service) {
                if (service.ip == ip) {
                    pids.push(service.pid);
                }
            });

            clearInterval(machineTopTimer);
            machineTopTimer = setInterval(function () {
                $.ajax({
                    url: '/client/' + ip + '/top?pids=' + pids.join(','),
                    success: function (ret) {
                        $('#topInfo').html('');
                        var lines = ret.split('\n');
                        lines.forEach(function (line, index) {
                            if (index == 0 || index == 1)
                                return;
                            var textLine = $('<pre></pre>');
                            textLine.html(line);
                            textLine.css('background-color', 'white');
                            textLine.css('border', 'none');
                            $('#topInfo').append(textLine);
                        })
                    }
                })
            }, 2000);
        }
    }
}

function hideMachinePanel() {
    $('#machinePanel').hide();
    clearInterval(machineTopTimer);
    $('#machinePanel').off('serviceListChange');
}