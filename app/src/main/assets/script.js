var colCount, rowCount
var board
const maxTileVal = 8;
var game = {
    score: 0,
    difficulty: 0,
    waitTime: 3000,
    waitInc: 100,
    logClick: false
};

$.fn.animateRotate = function (angle, duration, easing, complete) {
    var args = $.speed(duration, easing, complete);
    var step = args.step;
    return this.each(function (i, e) {
        args.complete = $.proxy(args.complete, e);
        args.step = function (now) {
            $.style(e, 'transform', 'rotate(' + now + 'deg)');
            if (step) return step.apply(e, arguments);
        };

        $({
            deg: 0
        }).animate({
            deg: angle
        }, args);
    });
};

$(document).ready(function () {
    board = $('#board')
    colCount = 8 //Android.getColumnCount()
    rowCount = 12 //Android.getRowCount()
    for (var y = 0; y < rowCount; y++) {
        rowEl = '<div class="row">'
        for (var x = 0; x < colCount; x++) {
            rowEl += '<div class="tile" id="' + getId(x, y) + '"></div>'
        }
        rowEl += '</div>'
        board.append(rowEl)
    }
    setSizes()
    $(window).resize(setSizes)
    setupTiles();
    setTimeout(addLooper, game.waitTime)
})

function gameOver() {
    toast('Game Over')
}

function fillRandTile() {
    var xs = Math.floor(Math.random() * colCount),
        ys = Math.floor(Math.random() * rowCount);
    for (var x = xs; x < colCount; x++) {
        for (var y = ys; y < rowCount; y++) {
            if (getVal(x, y) === -1) {
                setFilled(x, y, true);
                return;
            }
        }
    }
    for (var x = 0; x < colCount; x++) {
        for (var y = 0; y < rowCount; y++) {
            if (getVal(x, y) === -1) {
                setFilled(x, y, true);
                return;
            }
        }
    }
    gameOver()
}

function addLooper() {
    fillRandTile();
    if (game.waitTime > game.waitInc) game.waitTime -= game.waitInc;
    setTimeout(addLooper, game.waitTime)
}

function tileClick(e) {
    try {
        var info = {
            clickId: $(e.target).attr('id'),
            clickCol: 0,
            clickRow: 0
        }
        info.clickCol = parseInt(info.clickId.split('_')[ 1 ]);
        info.clickRow = parseInt(info.clickId.split('_')[ 0 ].replace('tile', ''));
        info.up = findUp(info.clickCol, info.clickRow);
        info.left = findLeft(info.clickCol, info.clickRow)
        info.right = findRight(info.clickCol, info.clickRow)
        info.down = findDown(info.clickCol, info.clickRow)
        if (game.logClick) console.log(info)
        var scoreDelta = 0;
        if (info.left.val !== null && (info.left.val === info.up.val || info.left.val === info.down.val || info.left.val == info.right.val)) {
            scoreDelta += Math.abs(info.clickCol - info.left.x);
            animTileAway(info.left.x, info.left.y);
        }
        if (info.right.val !== null && (info.right.val == info.up.val || info.right.val == info.down.val || info.right.val == info.left.val)) {
            scoreDelta += Math.abs(info.clickCol - info.right.x);
            animTileAway(info.right.x, info.right.y);
        }
        if (info.up.val !== null && (info.up.val == info.left.val || info.up.val == info.down.val || info.up.val == info.right.val)) {
            scoreDelta += Math.abs(info.clickRow - info.up.y);
            animTileAway(info.up.x, info.up.y);
        }
        if (info.down.val !== null && (info.down.val == info.up.val || info.down.val == info.left.val || info.down.val == info.right.val)) {
            scoreDelta += Math.abs(info.clickRow - info.down.y);
            animTileAway(info.down.x, info.down.y);
        }
        updateScore(scoreDelta)
    } catch (err) {
        console.log('error  during click - ' + err.toString());
    }
}

function updateScore(inc) {
    game.score += inc;
    $('#scoreEl').html('Score - ' + game.score)
}

function findUp(x, y) {
    while (y >= 0) {
        var val = getVal(x, y);
        if (!isNaN(val) && val !== -1) {
            return {
                x: x,
                y: y,
                val: val
            }
        }
        y--;
    }
    return {
        val: null
    };
}

function findDown(x, y) {
    while (y < rowCount) {
        var val = getVal(x, y);
        if (!isNaN(val) && val !== -1) {
            return {
                x: x,
                y: y,
                val: val
            }
        }
        y++;
    }
    return {
        val: null
    };
}

function findRight(x, y) {
    while (x < colCount) {
        var val = getVal(x, y);
        if (!isNaN(val) && val !== -1) {
            return {
                x: x,
                y: y,
                val: val
            }
        }
        x++;
    }
    return {
        val: null
    };
}

function findLeft(x, y) {
    while (x >= 0) {
        var val = getVal(x, y);
        if (!isNaN(val) && val !== -1) {
            return {
                x: x,
                y: y,
                val: val
            }
        }
        x--;
    }
    return {
        val: null
    };
}

function setSizes() {
    var board = $('#board');
    var size = Math.min(board.width() / colCount, board.height() / rowCount);
    $.each(board.find('*.row'), function (n, o) {
        $(this).css('height', size - 6);
    });
    $.each(board.find('*.tile'), function (n, o) {
        var t = $(this);
        t.css('height', size - 6).css('width', (board.width() * .12) - 2);
        t.on('click', tileClick);
    });
}

function getId(x, y) {
    return 'tile' + y.toString() + '_' + x.toString()
}

function animTileAway(x, y) {
    var id = getId(x, y)
    var el = $('#' + id)
    var pos = el.offset();
    var animId = 'anim' + id + '_' + Math.floor(Math.random() * 100);
    $('body').append('<div class="animationTile" id="' + animId + '" style="top:' + pos.top + 'px;left:' + pos.left + 'px;height:' + el.height() + 'px;width:' + el.width() + 'px;"></div>')
    var animEl = $('#' + animId);
    animEl.addClass('filled-' + getVal(x, y).toString())
    var elClasses = el.attr('class').split(' ')
    for (var i = 0; i < elClasses.length; i++)
        if (elClasses[ i ].startsWith('filled')) el.removeClass(elClasses[ i ])
    el.attr('game-val', '');
    var w = $(window).width();
    var maxX = (pos.left > w / 2) ? w * 1.5 : w / 1.5;
    var minX = (pos.left > w / 2) ? w / 3 : (-1 / 3) * w;
    var newX = Math.random() * (maxX - minX) + minX;
    animEl.animate({
        top: $(window).height() * 1.5
    }, {
            duration: 2500,
            easing: 'swing',
            queue: false
        }).animate({
            left: newX
        }, {
            duration: 1500,
            easing: 'swing',
            queue: false
        })
    animEl.animateRotate(3000, 3000, 'linear', function () {
        animEl.remove();
    })
}

function getTile(x, y) {
    return $('#' + getId(x, y));
}

function setupTiles() {
    var numFilled = 0,
        maxVal = colCount * rowCount;
    for (var x = 0; x < colCount; x++) {
        for (var y = 0; y < rowCount; y++) {
            if (numFilled < maxVal && randBool()) {
                setFilled(x, y);
                numFilled++;
            }
        }
    }
}

function randBool() {
    return Math.random() > .5;
}

function setFilled(x, y, anim = false) {
    var tile = $('#' + getId(x, y));
    tile.addClass('filled');
    tile.css('display', 'none')
    var tileVal = Math.floor(Math.random() * (maxTileVal - 1)) + 1;
    tile.addClass('filled-' + tileVal.toString());
    tile.fadeIn(anim ? 1000 : 1, function () {
        tile.attr('game-val', tileVal.toString())
    })
}

function getVal(x, y) {
    var el = getTile(x, y);
    try {
        var va = el.attr('game-val');
        if (va && va !== '')
            return parseInt(va);
        else return -1
    } catch (err) {
        return -1;
    }
}
