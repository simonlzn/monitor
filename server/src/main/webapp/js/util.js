Array.prototype.contains = function contains(service) {
    var exists = this.filter(function (s) {
        if (s.id === service.id)
            return true;

        return false;
    });

    if (exists.length != 0)
        return true;

    return false;
};

