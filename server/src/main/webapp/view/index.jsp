<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; UTF-8">
    <title>Index</title>
    <link rel="stylesheet" href="/bootstrap/css/bootstrap.min.css"></link>
    <link rel="stylesheet" href="/css/index.css"></link>
</head>
<body>

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">

            </button>
            <a class="navbar-brand" href="#">Dashboard</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#">Help</a></li>
            </ul>
            <form class="navbar-form navbar-right">
                <input type="text" class="form-control" placeholder="Search...">
            </form>
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar" id="machineList">
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div id="machinePanel" style="display:none">
                <input type="hidden" id="machineIp">
                <div class="row">
                    <div class="col-md-6">
                        <div class="panel panel-default">
                            <div class="panel-heading">Services</div>
                            <div class="panel-body">
                                <table class="table table-hover">

                                    <tbody id="livingServiceList">

                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="panel panel-default">
                            <div class="panel-heading">Create</div>
                            <div class="panel-body">
                                <div class="col-md-6">
                                    <select class="form-control" id="templateSelect">

                                    </select>
                                </div>
                                <div class="col-md-6">
                                    <button type="submit" class="btn btn-default" id="createBtn">Create</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="panel panel-default">
                    <div class="panel-heading">Top</div>
                    <div class="panel-body" id="topInfo" style="white-space:pre">

                    </div>
                </div>

            </div>
            <div id="servicePanel" style="display:none">
                <input type="hidden" id="serviceId">
                <div class="panel panel-default">
                    <div class="panel-heading">Services</div>
                    <div class="panel-body">
                        <button type="button" class="btn btn-info" id="logBtn">Show Log</button>
                        <div id="logInfo"></div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>

<script src="/jquery/jquery-2.1.4.min.js"></script>
<script src="/bootstrap/js/bootstrap.min.js"></script>

<script>
    var serviceList = [];

    setInterval(function () {
        $.ajax({
            url: '/list/service',
            success: function (services) {
                function contains(serviceList, service) {
                    var exists = serviceList.filter(function (s) {
                        if (s.id === service.id)
                            return true;

                        return false;
                    });

                    if (exists.length != 0)
                        return true;

                    return false;
                }

                services.forEach(function (service) {
                    service.id = service.ip + ':' + service.pid;
                });

                function showMachinePanel(ip) {
                    $('#machineIp').val(ip);
                    $('#machinePanel').show();

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

                    var machineIp = $('#machineIp').val();
                    var serviceControlTemp = $('<tr style="display:none" id="serviceControlTemp">' +
                            '<th class="control-label"></th>' +
                            '<td><button type="button" class="pull-right btn btn-danger">Kill</button></td>' +
                            '</tr>');

                    $('#livingServiceList').html('');
                    serviceList.forEach(function (service) {
                        if (service.ip == machineIp) {
                            var serviceControl = serviceControlTemp.clone();
                            serviceControl.find('.control-label').html(service.name);
                            serviceControl.attr('data-id', service.id);
                            serviceControl.find('button').on('click', function () {
                                var id = $(this).parent().parent().data('id');
                                $.ajax({
                                    url : '/service/' + id + '/kill',
                                    type : 'post',
                                    success : function () {
                                        serviceControl.hide();
                                    }
                                })
                            });
                            serviceControl.show();
                            $('#livingServiceList').append(serviceControl)
                        }
                    });



                    var pids = [];
                    serviceList.forEach(function (service) {
                        if (service.ip == machineIp) {
                            pids.push(service.pid);
                        }
                    });

                    setInterval(function () {
                        $.ajax({
                            url : '/client/' + ip + '/top?pids=' + pids.join(','),
                            success : function (ret) {
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

                    $('#createBtn').on('click', function () {
                        var option = $('#templateSelect').val();
                        $.ajax({
                            url : '/service/' + ip + '/create',
                            type : 'post',
                            data : {'name' : option},
//                            contentType : 'application/json'
                        })
                    })

                }

                function showServicePanel() {
                    $('#servicePanel').show();
                    $('#logBtn').on('click', function () {
                        var serviceId = $('#serviceId').val();
                        $.ajax({
                            url : '/client/' + serviceId + '/log',
                            success : function (log) {
                                $('#logInfo').html(log);
                            }
                        })
                    })
                }

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

                        $('#machinePanel').hide();
                        showServicePanel();
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

                services.forEach(function (service) {
                    if (!contains(serviceList, service)) {
                        add(service);
                        serviceList.push(service);
                    }
                });

                serviceList.forEach(function (service) {
                    if (!contains(services, service)) {
                        remove(service);
                        var index = serviceList.indexOf(service)
                        var tempList = [];
                        tempList = tempList.concat(serviceList.slice(0, index-1));
                        tempList = tempList.concat(serviceList.slice(index, serviceList.length-1))
                        serviceList = tempList;
                    }
                });
            }
        })
    }, 2000);
</script>
</body>
</html>