var setup = function(targets) {
    targets.forEach(function(t) t.activate());
}

var isOver = function(targets) {
    return targets.stream().allMatch(function(t) !t.isActive());
}

var onHit = function(target) {
    target.deactivate();
}

