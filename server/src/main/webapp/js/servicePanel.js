function showServicePanel(id) {
    $('#servicePanel').show();
    $('#serviceId').val(id);
    $('#logInfo').html('');
    $('#logInfo').hide();
    $('#logBtn').off('click').on('click', function () {
        var serviceId = $('#serviceId').val();
        $.ajax({
            url : '/service/' + serviceId + '/log',
            success : function (log) {
                $('#logInfo').html('');
                var lines = log.split('\n');
                lines.forEach(function (line, index) {
                    if (index == 0 || index == 1)
                        return;
                    var textLine = $('<pre></pre>');
                    textLine.html(line);
                    textLine.css('background-color', 'white');
                    textLine.css('border', 'none');
                    textLine.css('white-space', 'pre-wrap');
                    textLine.css('padding', '0 9.5px');
                    $('#logInfo').append(textLine);
                    $('#logInfo').show();
                });
            }
        })
    })
}
