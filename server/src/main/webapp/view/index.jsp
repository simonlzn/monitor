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
                        <div style="margin-left: 8px;"><button type="button" class="btn btn-info" id="logBtn">Show Log</button></div>
                        <div id="logInfo" style="display: none; border-top: 1px solid rgba(0,0,0,.25); margin-top: 10px; padding-top: 10px"></div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>

<script src="/jquery/jquery-2.1.4.min.js"></script>
<script src="/bootstrap/js/bootstrap.min.js"></script>
<script src="/js/overviewPanel.js"></script>
<script src="/js/machinePanel.js"></script>
<script src="/js/servicePanel.js"></script>
<script src="/js/util.js"></script>

<script>
    var serviceList = [];

    setInterval(function () {
        $.ajax({
            url: '/list/service',
            success: function (services) {
                services.forEach(function (service) {
                    service.id = service.ip + ':' + service.pid;
                });

                services.forEach(function (service) {
                    if (!serviceList.contains(service)) {
                        add(service);
                        serviceList.push(service);
                        $('#machinePanel').trigger('serviceListChange', serviceList);
                    }
                });

                serviceList.forEach(function (service) {
                    if (!services.contains(service)) {
                        remove(service);
                        var index = serviceList.indexOf(service)
                        var tempList = [];
                        tempList = tempList.concat(serviceList.slice(0, index-1));
                        tempList = tempList.concat(serviceList.slice(index, serviceList.length-1))
                        serviceList = tempList;
                        $('#machinePanel').trigger('serviceListChange', serviceList);
                    }
                });
            }
        })
    }, 2000);
</script>
</body>
</html>